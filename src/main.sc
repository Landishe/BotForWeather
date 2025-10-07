require: api.js
    name = api

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
        a: ** От разработчика:Бот находиться в стадии разработки. На данный момент релизован поиск города, ответ на погоду на сегодня и на недел. Что одеть в процессе разработки. На запрос о городе лучше отвачать "в Москва". Город в Именительном падеже" **
            \n Добрый день! Я могу подсказать прогноз погоды а также подсказать погоду на всю неделю.
        go!: ./whereAreYou
        
        state: whereAreYou
            a: Уточните в каком городе посмотреть погоду?
            
        state: findCity
            q!: (в|нахожусь|буду|живу|пребываю) [$oneWord] $City * 
            script:
                
                $session.cityData = {
                    name: $parseTree._City.name,
                    lat: $parseTree._City.lat,
                    lon: $parseTree._City.lon,
                    current: 'temperature_2m',
                    daily: "temperature_2m_max",
                    date: $jsapi.dateForZone($parseTree._City.timezone, 'yyyy-MM-dd'),
                    };
                log($session.cityData);
            go!: ./question
        
            state: question
                a: Вы хотите узнать погоду на сегодня или на неделю?
        
        state: ask
            q!: (хочу|какая|сколько|на).*(сейчас|сегодня)
            go!: ../weatherOnDay
            
        state: ask2
            q!: (хочу|какая|сколько|узнать|на)*(этой|будет|ожидается|недел*)
            go!: ../weatherOnWeek
            
        state: weatherOnDay
            script:
                $temp.weatherResult = weatherApi($session.cityData);
                log($temp.weatherResult)
            if: $temp.weatherResult.isOk
                a: Сейчас в городе {{$session.cityData.name}} {{$temp.weatherResult.data.current.temperature_2m}} °C. Ожидается до {{$temp.weatherResult.data.daily.temperature_2m_max[0]}}°C.
            else:
                a: У меня не получилось узнать погоду. Попробуйте ещё раз.    
        
        state: weatherOnWeek
            script:
                $temp.weatherResultWeek = weatherApi($session.cityData);
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
                
                log(forecast.join("\n"))
                $temp.forecastText = forecast.join("\n");
                
            if: $temp.weatherResultWeek.isOk
                
                a: На этой неделе в городе {{$session.cityData.name}} ожидается на этой неделе:\n{{$temp.forecastText}}.
            else:
                a: У меня не получилось узнать погоду. Попробуйте ещё раз.
        
    state: NoMatch
        event!: noMatch
        a: Я не понял что вы сказали, повторите