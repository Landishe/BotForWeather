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
        
        state: sendCity
            q!: * сколько [сейчас] градус* [в] $City *
            script:
                $session = $request.query;
                log($session);
                weatherApi($session)
                
            if: $temp.responce.isOk
                a: сейчас в {{$parseTree._City.name}} {{$temp.responce.data.current.temperature_2m}} °C
                
            else:
                a: У меня не получилось узнать погоду. Попробуйте ещё раз.    
        
    state: NoMatch
        event!: noMatch
        a: Я не понял что вы сказали, повторите