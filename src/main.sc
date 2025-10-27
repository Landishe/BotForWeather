require: api.js
    name = api
    
require: function.js
    name = getTemp

require: city/city.sc
    module = sys.zb-common
    
require: dateTime/moment.min.js
    module = sys.zb-common

theme: /

    state: start
        q!: $regex<start>
        script:
            $session = {}
            $temp = {}
        a: ** От разработчика: Бот находится в стадии разработки и могут быть ошбки. Бот разбирает сокращенные название городов например: СПБ, МСК Екб и тп. Добавлено обновление вывода погоды на "сейчас"**
            \n Добрый день! Я могу подсказать прогноз погоды в вашем городе.
        go!: ./whereAreYou
        
        state: whereAreYou
            a: Уточните в каком городе посмотреть погоду?
            
        state: findCity
            q!: [$oneWord] $City * 
            script:
                $session.cityData = {
                    name: capitalize($caila.inflect($parseTree._City.name, ["loct"])),
                    lat: $parseTree._City.lat,
                    lon: $parseTree._City.lon,
                    current: 'temperature_2m',
                    daily: "temperature_2m_max",
                    date: $jsapi.dateForZone($parseTree._City.timezone, "HH:mm"),
                    };
                log($session.cityData);
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
                $temp.weatherResult = weatherApi($session.cityData);
                $temp.tempData = getTemperature($temp.weatherResult);
                $temp.WeatherCode = getWeatherCode($temp.weatherResult);
                $temp.clothesRecomendation = getClothingRecomendation($temp.weatherResult);
                
                function windSpeed (weatherResult) {
                    return ($temp.weatherResult.data.current.wind_speed_10m / 3.6).toFixed(1)
                }
                $temp.windSpeed = windSpeed($temp.weatherResult)
                
                function convertToLocalTime(weatherResult){
                    var timeSunsetSunshine = []
                    timeSunsetSunshine.push($temp.weatherResult.data.daily.sunrise[0].split('T')[1])
                    timeSunsetSunshine.push($temp.weatherResult.data.daily.sunset[0].split('T')[1])
                    return timeSunsetSunshine
                }
                $temp.timeSunset = convertToLocalTime($temp.weatherResult);
                log($temp.timeSunset)
                log($temp.weatherResult)
                
            
            if: $temp.weatherResult.isOk
                if: (($session.cityData.date <= $temp.timeSunset[0]) || ($session.cityData.date >= $temp.timeSunset[1]))
                    a: Сейчас в городе {{$session.cityData.name}} {{$temp.weatherResult.data.current.temperature_2m}} °C. Ожидается до {{$temp.weatherResult.data.daily.temperature_2m_max[0]}}°C. Ощущается как: {{$temp.tempData}}"
                    
                else:
                    a: Сейчас в городе {{$session.cityData.name}} {{$temp.weatherResult.data.current.temperature_2m}} °C. Ожидается до {{$temp.weatherResult.data.daily.temperature_2m_max[0]}}°C. Сейчас в городе {{$temp.tempData}} и {{$temp.WeatherCode}} Сила ветра {{$temp.windSpeed + 'м/с'}}.
                    a: Вот что может пригодиться сейчас: {{$temp.clothesRecomendation.join(', ')}}
            else:
                a: У меня не получилось узнать погоду. Попробуйте ещё раз.    
        
        state: weatherOnDay
            script:
                $temp.weatherResult = weatherApi($session.cityData);
                $temp.tempData = getTemperature($temp.weatherResult);
                $temp.getWeatherCodeToday = getWeatherCodeToday($temp.weatherResult);
                $temp.clothesRecomendation = getClothesWeatherOnDay($temp.weatherResult);
                log($temp.weatherResult)
                
                function windSpeed (weatherResult) {
                    return ($temp.weatherResult.data.daily.wind_speed_10m_max[0] / 3.6).toFixed(1)
                }
                $temp.windSpeed = windSpeed($temp.weatherResult);
                
            if: $temp.weatherResult.isOk
                a: Сегодня в городе ожидается {{$session.cityData.name}} {{$temp.weatherResult.data.daily.temperature_2m_max[0]}}°C.
                   {{$temp.tempData}} и {{$temp.getWeatherCodeToday}}, сила ветра {{$temp.windSpeed + 'м/с'}}.
                a: Рекомендация:\n {{$temp.clothesRecomendation}}
            else:
                a: У меня не получилось узнать погоду. Попробуйте ещё раз.  
        
        state: weatherOnWeek
            script:
                $temp.weatherResultWeek = weatherApi($session.cityData);
                $temp.getWeeklyAverage = getWeeklyAverage($temp.weatherResultWeek);
                $temp.getWeeklyGeneralAdvice = getWeeklyGeneralAdvice($temp.weatherResultWeek);
                $temp.getTemperatureRange = getTemperatureRange($temp.weatherResultWeek);
                $temp.getRangeAdvice=getRangeAdvice($temp.weatherResultWeek);
                
                var weatherWeek = $temp.weatherResultWeek.data.daily.temperature_2m_max;
                var days = ['вс', 'пн', 'вт', 'ср', 'чт', 'пт' ,'сб']
                var day = $temp.weatherResultWeek.data.daily.time;
                var firstDay = day[0];
                var jsDate = new Date(firstDay);
                var dayOfWeek = jsDate.getDay();
                
                var forecast = []
                for (var i = 0; i < weatherWeek.length; i++){
                    var currentDate = new Date(day[i]);
                    var dayName = days[currentDate.getDay()];
                    var temperature = Math.round(weatherWeek[i]);
                    forecast.push(dayName + ": " + temperature + "°C");
                }
                $temp.forecastText = forecast.join("\n");
                
            if: $temp.weatherResultWeek.isOk
                a: На этой неделе в городе {{$session.cityData.name}} ожидается:\n{{$temp.forecastText}}
                a: Диапозон от {{$temp.getTemperatureRange.min + "°C"}} до {{$temp.getTemperatureRange.max + "°C"}}
                   Средняя температура: {{$temp.getWeeklyAverage + "°C"}}
                   {{$temp.getRangeAdvice}}
                a: Настроение недели: {{$temp.getWeeklyGeneralAdvice.mood}} \n
                   {{$temp.getWeeklyGeneralAdvice.advice}} {{$temp.getWeeklyGeneralAdvice.emoji}}
            else:
                a: У меня не получилось узнать погоду. Попробуйте ещё раз.
        
    state: NoMatch
        event!: noMatch
        a: Я не понял что вы сказали, повторите