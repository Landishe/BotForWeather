require: city/city.sc
    module = sys.zb-common

theme: /

    state: start
        q!: $regex<start>
        script:
            $session = {}
            $temp = {}
        a: Добрый день! Я могу подсказать прогноз погоды а также подсказать погоду на  всю неделю и если хотите могу подсказать какую одежду одеть сегодня. 
        
        state: sendCity
            q!: * сколько [сейчас] градус* [в] $City *
            script:
                log($Cities);
                $temp.responce = $http.get('https://api.open-meteo.com/v1/forecast?latitude=55.7522,59.9386,56.75,45.0625,57.625&longitude=37.6156,30.3141,60.8125,39,39.875&current=temperature_2m,relative_humidity_2m,apparent_temperature,rain,showers,snowfall,weather_code,cloud_cover,wind_speed_10m,wind_direction_10m&daily=weather_code,temperature_2m_max,rain_sum,showers_sum,snowfall_sum&wind_speed_unit=ms&timezone=auto')
                
                
                $session.guestAnswer = $request.query;
                log($temp.responce.data)
                
            if: $temp.responce.isOk
                a: сейчас в {{$parseTree._City.name}} {{$temp.responce.data[0].current.temperature_2m}} °C
                
            else:
                a: У меня не получилось узнать погоду. Попробуйте ещё раз.    
        
    state: NoMatch
        event!: noMatch
        a: Я не понял что вы сказали, повторите