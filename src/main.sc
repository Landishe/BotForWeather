require: api.js
    type = scriptEs6
    name = api

theme: /

    state: start
        q!: $regex<start>
        script:
            $session = {}
            
        a: Добрый день! Я могу подсказать прогноз погоды а также подсказать погоду на  всю неделю и если хотите могу подсказать какую одежду одеть сегодня. 
        
        state: sendCity
            q: *
            scriptEs6:
                $session.guestAnswer = $request.query;
                log($session.guestAnswer)
                api.weatherApi()
                
        
    state: NoMatch
        event!: noMatch
        a: Я не понял что вы сказали, повторите