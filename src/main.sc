require: api.js
    name = api

require: city/city.sc
    module = sys.zb-common

theme: /

    state: start
        q!: $regex<start>
        script:
            $session = {}
            $temp = {}
        a: Добрый день! Я могу подсказать прогноз погоды а также подсказать погоду на  всю неделю и если хотите могу подсказать какую одежду надеть сегодня. 
        
        state: weatherOnDay
            
            q!: *[какая] сколько [сейчас] градус* [в] $City *
            script:
                var cityData = {
                    lat: $parseTree._City.lat,
                    lon: $parseTree._City.lon,
                    current: 'temperature_2m',
                    daily: "temperature_2m_max",
                    };
               
                $temp.weatherResult = weatherApi(cityData);
               
                log($temp.weatherResult);
            if: $temp.weatherResult.isOk
                a: Сейчас в {{$parseTree._City.name}} {{$temp.weatherResult.data.current.temperature_2m}} °C. Ожидается максимальная температура {{$temp.weatherResult.data.daily.temperature_2m_max[0]}}
                
            else:
                a: У меня не получилось узнать погоду. Попробуйте ещё раз.    
        
        state: weatherOnWeek
            q!:* какая погода [будет] на этой неделе* [в] $City
            script:
                var cityData = {
                    lat: $parseTree._City.lat,
                    lon: $parseTree._City.lon,
                    current: 'temperature_2m',
                    daily: "temperature_2m_max",
                    };
                $temp.weatherResultWeek = weatherApi(cityData);
               
                log($temp.weatherResultWeek);
            if: $temp.weatherResultWeek.isOk
                a: На этой неделе в {{$parseTree._City.name}} Ожидается {{$temp.weatherResultWeek.data.daily.temperature_2m_max}}
                
            else:
                a: У меня не получилось узнать погоду. Попробуйте ещё раз.
        
    state: NoMatch
        event!: noMatch
        a: Я не понял что вы сказали, повторите