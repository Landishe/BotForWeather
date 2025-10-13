function getTemperature(weatherResult){
    if (weatherResult.data.current.temperature_2m >= 30) {
        return 'очень жарко';
    } else if (weatherResult.data.current.temperature_2m >= 25){
        return 'жарко';
    } else if (weatherResult.data.current.temperature_2m >= 20){
        return 'очень тепло';
    } else if (weatherResult.data.current.temperature_2m >= 15){
        return 'комфортно';
    } else if (weatherResult.data.current.temperature_2m >= 10){
        return 'прохладно';
    } else if (weatherResult.data.current.temperature_2m >= 0){
        return 'холодно';
    } else {
        return 'очень холодно';
    }
}

function getWeatherCode(weatherResult){
    switch(weatherResult.data.current.weather_code) {
        case 0: return 'ясно☀️';
        case 1: return 'преимущественно ясно🌤';
        case 2: return 'переменная облачность⛅️';
        case 3: return 'пасмурно🌧';
        case 45: case 48: return 'туман🌫';
        case 51: case 53: case 55: return 'морось☔️';
        case 56: case 57: return 'ледяная морось💦';
        case 61: case 63: case 65: return 'дождь🌧';
        case 66: case 67: return 'ледяной дождь🌧';
        case 71: case 73: case 75: return 'снег🌧';
        case 77: return 'снежные зерна🌧';
        case 80: case 81: case 82: return 'ливневый дождь⛈';
        case 85: case 86: return "ливневый снег🌧";
        case 95: case 96: case 99: return "гроза⚡️";
        default: return "неизвестно";
    }
}

function getClothingRecomendation(weatherResult){
    var recommendation = []
    if (weatherResult.data.current.temperature_2m >= 25) {
        recommendation.push('футболку👕', 'шорты', 'солнечные очки🕶')
    } else if (weatherResult.data.current.temperature_2m >= 15) {
        recommendation.push('кофту или ветровку')
    } else if (weatherResult.data.current.temperature_2m >= 5){
        recommendation.push("пальто или куртку", "кофту")
    } else ("теплое пальто", "шапку", "перчатки")

    if (weatherResult.data.current.weather_code == 0 || weatherResult.data.current.weather_code == 1){
        recommendation.push("солнечные очки")
    } else if (weatherResult.data.current.weather_code >= 57 || weatherResult.data.current.weather_code <= 67){
        recommendation.push('зонт или дождевик')
    } else if (weatherResult.data.current.weather_code >= 71 || weatherResult.data.current.weather_code <= 86){
        recommendation.push('зонт и теплую обувь')
    } else if (weatherResult.data.current.weather_code >= 95 || weatherResult.data.current.weather_code <= 99){
        recommendation.push('Не выходить на улицу пока идет гроза')
    }
    return recommendation
}

function getWeeklyAverage(weatherResult) {
    var sum = weatherResult.data.daily.temperature_2m_max.reduce(function(a, b){
        return a + b;
    }, 0);
    return (sum / weatherResult.data.daily.temperature_2m_max.length).toFixed(1);
}

function getWeeklyGeneralAdvice(weatherResult){
    var avgTemp = getWeeklyAverage(weatherResult)
    
    if (avgTemp >= 20) {
        return {
            mood: "тепло 🌤️",
            advice: "На этой неделе будет тепло - отличное время для прогулок на свежем воздухе",
            emoji: "😊"
        };
    } else if (avgTemp >= 15) {
        return {
            mood: "комфортно 🌼", 
            advice: "На неделе комфортная температура - можно планировать активность на улице",
            emoji: "👍"
        };
    } else if (avgTemp >= 10) {
        return {
            mood: "прохладно 🍂",
            advice: "На неделе будет прохладно - если будете гулять, возьмите кофту или ветровку",
            emoji: "🧥"
        };
    } else if (avgTemp >= 5) {
        return {
            mood: "холодно ❄️",
            advice: "На неделе ожидается холодно - для прогулок потребуется теплая одежда",
            emoji: "🧤"
        };
    } else if (avgTemp >= 0) {
        return {
            mood: "очень холодно 🥶",
            advice: "На неделе будет очень холодно - одевайтесь теплее, если планируете выходить",
            emoji: "🧣"
        };
    } else {
        return {
            mood: "морозно ⛄",
            advice: "На неделе морозно - лучше оставайтесь в тепле, ограничьте время на улице",
            emoji: "🏠"
        };
    }
}

function getTemperatureRange(weatherResult) {
    var temperatures = weatherResult.data.daily.temperature_2m_max;
    log(temperatures)
    
    var min = temperatures[0];
    for (var i = 1; i < temperatures.length; i++) {
        if (temperatures[i] < min) {
            min = temperatures[i];
        }
    }
    
    var max = temperatures[0];
    for (var i = 1; i < temperatures.length; i++) {
        if (temperatures[i] > max) {
            max = temperatures[i];
        }
    }
    
    
    var range = max - min;
    
    return {
        min: min,
        max: max,
        range: range.toFixed(1),
        hasBigChanges: range > 10,
    };
}
function getRangeAdvice(weatherResult) {
    var tempRange = getTemperatureRange(weatherResult);
    if(tempRange.hasBigChanges){
        return "⚠️ Обратите внимание: на неделе большой перепад температур (" + tempRange.range + "°C) - готовьте разную одежду";
    } else {
        return "📊 Температура будет стабильной в течение недели";
    }
}