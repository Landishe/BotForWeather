require: api.js
    name = api
    
require: function.js
    name = getTemp

require: city/city.sc
    module = sys.zb-common
    
require: dateTime/moment.min.js
    module = sys.zb-common

theme: /
# стейты называются с заглавной буквы
    state: start
        q!: $regex<start>
        script: 
        a: ** От разработчика: Бот находится в стадии разработки и могут быть ошбки. Бот разбирает сокращенные название городов например: СПБ, МСК Екб и тп. Добавлено обновление вывода погоды на "сейчас"**
            \n Добрый день! Я могу подсказать прогноз погоды в вашем городе.
        go!: ./whereAreYou
        
        state: whereAreYou
            a: Уточните в каком городе посмотреть погоду?
            
        state: findCity
            q!: [$oneWord] $City * 
            script:
                function sendTelegramLocation(telegaData){
                    var dataLocation = telegaData
                    log('тут в функции данные из Эвента' + dataLocation)
                    }
                $session.cityData = {
                    name: capitalize($caila.inflect($parseTree._City.name, ["loct"])),
                    lat: $parseTree._City.lat,
                    lon: $parseTree._City.lon,
                    date: $jsapi.dateForZone($parseTree._City.timezone, "HH:mm"),
                    };
                log($session.cityData)
            go!: ./question
        
            state: question
                a: Вы хотите узнать погоду на сейчас, сегодня или на неделю?
        
        state: ask
            q!: $regexp_i<(?:на\s+)?(сейчас)>
            go!: ../weatherCurrent
        
        state: ask1    
            q!: $regexp_i<(?:на\s+)?(сегодня)>
            go!: ../weatherOnDay
            
        state: ask2
            q!: $regexp_i<(?:на\s+)?(недел[яею])>
            go!: ../weatherOnWeek
            
        state: weatherCurrent
            script:
                
                # JSON для работы с данными на сейчас
                $session.weatherResult = weatherApi($session.cityData);
                # переменная для температуры сейчас;
                $session.temperature = $session.weatherResult.data.current.temperature_2m;
                # переменная для кода погоды
                $session.weatherCode = $session.weatherResult.data.current.weather_code;
                # переменная для скорости погоды
                $session.windSpeed = $session.weatherResult.data.current.wind_speed_10m;
                # Погода в течении всего дня
                $session.temperatureDay = $session.weatherResult.data.daily.temperature_2m_max[0];
                # переменная для восхода и заката
                $session.timeSunrise = $session.weatherResult.data.daily.sunrise[0];
                $session.timeSunset = $session.weatherResult.data.daily.sunset[0];
                # переменные для функций : температура сейчас, код погоды и рекомендация по одежде, скорость ветра, восхода и заката солнца
                $temp.tempData = getTemperature($session.temperature); //вот тут и ниже правильно
                $temp.weatherCode = getWeatherCode($session.weatherCode);
                $temp.clothesRecomendation = getClothingRecomendation($session.temperature, $session.weatherCode);
                $temp.windSpeed = getWindSpeed($session.windSpeed);
                $temp.timeSunsetOrSunrise = convertToLocalTime($session.timeSunrise, $session.timeSunset)
                
                
            if: $session.weatherResult.isOk
                if: (($session.cityData.date <= $temp.timeSunsetOrSunrise[0]) || ($session.cityData.date >= $temp.timeSunsetOrSunrise[1]))
                    a: Сейчас в городе {{$session.cityData.name}} {{$session.temperature}} °C. Ожидается до {{$session.temperatureDay}}°C. Ощущается как: "{{$temp.tempData}}"
                else:
                    a: Сейчас в городе {{$session.cityData.name}} {{$session.temperature}} °C. Ожидается до {{$session.temperatureDay}}°C. Сейчас в городе {{$temp.tempData}} и {{$temp.weatherCode}} Сила ветра {{$temp.windSpeed + 'м/с'}}.
                    a: Вот что может пригодиться сейчас: {{$temp.clothesRecomendation.join(', ')}}
            else:
                a: У меня не получилось узнать погоду. Попробуйте ещё раз.    
        
        state: weatherOnDay
            script:
                log("Здесь переменная из weatherCurrent " + $session.weatherResult)
                # переменная для json для работы с данными на сегодня
                $temp.weatherResult = weatherApi($session.cityData);
                # Переменная для скорости ветра
                $session.windSpeedToday = $temp.weatherResult.data.daily.wind_speed_10m_max[0];
                # Переменная для кода погоды сегодня
                $session.weatherCodeToday = $temp.weatherResult.data.daily.weather_code[0]
                # Переменная для температуры сегодня
                $session.temperatureDaily = $temp.weatherResult.data.daily.temperature_2m_max[0];
                # Определение погодных условий
                $temp.getWeatherCodeToday = getWeatherCodeToday($session.weatherCodeToday);
                # Рекомендация по одежде
                $temp.clothesRecomendation = getClothesWeatherOnDay($session.weatherCodeToday, $session.temperatureDaily);
                # определение скорости ветра
                $temp.windSpeed = getWindSpeed($session.windSpeedToday)
                
                
            if: $temp.weatherResult.isOk
                a: Сегодня в городе ожидается {{$session.cityData.name}} {{$session.temperatureDaily}}°C.
                   {{$temp.tempData}} и {{$temp.getWeatherCodeToday}}, сила ветра {{$temp.windSpeed + 'м/с'}}.
                a: Рекомендация:\n {{$temp.clothesRecomendation}}
            else:
                a: У меня не получилось узнать погоду. Попробуйте ещё раз.  
        
        state: weatherOnWeek
            script:
                $temp.weatherResultWeek = weatherApi($session.cityData);
                # Переменная для сводки погоды на неделю
                $temp.weekTemperature = $temp.weatherResultWeek.data.daily.temperature_2m_max;
                # Переменная для массива дат
                $temp.weekDays = $temp.weatherResultWeek.data.daily.time;
                
                $temp.getWeeklyAverage = getWeeklyAverage($temp.weatherResultWeek);
                $temp.getWeeklyGeneralAdvice = getWeeklyGeneralAdvice($temp.weatherResultWeek);
                $temp.getTemperatureRange = getTemperatureRange($temp.weatherResultWeek);
                $temp.getRangeAdvice=getRangeAdvice($temp.weatherResultWeek);
                # Функция которая синхронизирует дни неделе к погоде по дням
                $temp.filterDays = filterDays($temp.weekTemperature, $temp.weekDays)
                
               
            if: $temp.weatherResultWeek.isOk
                a: На этой неделе в городе {{$session.cityData.name}} ожидается:\n{{$temp.filterDays}}
                a: Диапозон от {{$temp.getTemperatureRange.min + "°C"}} до {{$temp.getTemperatureRange.max + "°C"}}
                   Средняя температура: {{$temp.getWeeklyAverage + "°C"}}
                   {{$temp.getRangeAdvice}}
                a: Настроение недели: {{$temp.getWeeklyGeneralAdvice.mood}} \n
                   {{$temp.getWeeklyGeneralAdvice.advice}} {{$temp.getWeeklyGeneralAdvice.emoji}}
            else:
                a: У меня не получилось узнать погоду. Попробуйте ещё раз.
        
    state: geolocation
        event: telegramSendLocation
        script:
            log('пришли данны из Телеграмма');
            var $context = $jsapi.context(); 
            var telegaData = $context.request.data
            log(telegaData)
            
            
                
    state: NoMatch
        event!: noMatch
        a: Я не понял что вы сказали, повторите