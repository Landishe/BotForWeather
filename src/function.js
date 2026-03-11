// определение ощущения температуры
function getTemperature(temperature){
    if (temperature >= 30) {
        return 'очень жарко';
    } else if (temperature >= 25){
        return 'жарко';
    } else if (temperature >= 20){
        return 'очень тепло';
    } else if (temperature >= 15){
        return 'комфортно';
    } else if (temperature >= 10){
        return 'прохладно';
    } else if (temperature >= 0){
        return 'холодно';
    } else {
        return 'очень холодно';
    }
}

// перевод скорости ветра из км\ч в м\с
function getWindSpeed (windSpeed) {
    return (windSpeed/3.6).toFixed(1)
}




function convertSunset(timeSunset){
    return timeSunset.split('T')[1]
};

function convertSunrise(timeSunrise){
    return timeSunrise.split('T')[1]
};

function convertToMinutes(timeStr) {
    var parts = timeStr.split(':');
    return parseInt(parts[0]) * 60 + parseInt(parts[1]);
};


// рекомендация какую одежду одеть сегодня
function getClothesWeatherOnDay(weatherCodeToday, temperatureToday){
    var recomendationClothes = [];
    if (temperatureToday >= 25){
        recomendationClothes.push('• 😎 Солнечные очки обязательны')
    } else if (temperatureToday >= 15) {
        recomendationClothes.push('• 🧥 Комфортная одежда могут пригодиться')
    } else if (temperatureToday >= 5){
        recomendationClothes.push("• 🍂 Дополнительный слой теплой одежды не помешает")
    } else ("• 🧤 Не забудьте перчатки и шарф")

    if (weatherCodeToday == 0 || weatherCodeToday == 1){
        recomendationClothes.push("• ☔ Возьмите зонт или дождевики")
    } else if ((weatherCodeToday >= 51 && weatherCodeToday <= 67) || (temperatureToday >= -5 && temperatureToday <= 5)){
        recomendationClothes.push('• ☔ Возьмите зонт или дождевик')
    } else if (weatherCodeToday >= 71 || weatherCodeToday <= 86){
        recomendationClothes.push('• 👢 теплпая обувь будет кстати')
    } else if (weatherCodeToday >= 95 || weatherCodeToday <= 99){
        recomendationClothes.push('• ⛈️ Избегайте открытых мест во время грозыза')
    }
    return recomendationClothes.join('\n')
}

// Определение какое погодное условие сейчас
function getWeatherCode(weatherCode){
    switch(weatherCode) {
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

// Определение условий погоды на весь день
function getWeatherCodeToday(weatherResult){
    switch(weatherResult) {
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

function getClothingRecomendation(temperature, weatherCode){
    var recommendation = []
    if (temperature >= 25) {
        recommendation.push('футболка👕', 'шорты', 'солнечные очки🕶')
    } else if (temperature >= 15) {
        recommendation.push('кофту или ветровку')
    } else if (temperature >= 5){
        recommendation.push("пальто или куртка", "кофта")
    } else ("теплое пальто", "шапка", "перчатки")

    if (weatherCode == 0 || weatherCode == 1){
        recommendation.push("солнечные очки")
    } else if ((weatherCode >= 57 || weatherCode <= 67) && (temperature >= 0 || temperature >= 30)){
        recommendation.push('зонт или дождевик')
    } else if ((weatherCode >= 71 || weatherCode <= 86) || (temperature <= 0 || temperature >= -30)){
        recommendation.push('теплая одежда и теплая обувь')
    } else if (weatherCode >= 95 || weatherCode <= 99){
        recommendation.push('Не выходить на улицу пока идет гроза')
    }
    return recommendation
}

function filterDays(weekTemperature, weekDays){
    var days = ['вс', 'пн', 'вт', 'ср', 'чт', 'пт' ,'сб']
    var firstDay = weekDays[0];
    var jsDate = new Date(firstDay);
    var dayOfWeek = jsDate.getDay();
    var forecast = []
    for (var i = 0; i < weekTemperature.length; i++){
        var currentDate = new Date(weekDays[i]);
        var dayName = days[currentDate.getDay()];
        var temperature = Math.round(weekTemperature[i]);
        forecast.push(dayName + ": " + temperature + "°C");
    }
    return forecast.join("\n");
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

