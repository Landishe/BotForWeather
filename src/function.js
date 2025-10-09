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
        case 0: return 'ясно';
        case 1: return 'преимущественно ясно';
        case 2: return 'переменная облачность';
        case 3: return 'пасмурно';
        case 45: case 48: return 'туман';
        case 51: case 53: case 55: return 'морось';
        case 56: case 57: return 'ледяная морось';
        case 61: case 63: case 65: return 'дождь';
        case 66: case 67: return 'ледяной дождь';
        case 71: case 73: case 75: return 'снег';
        case 77: return 'снежные зерна';
        case 80: case 81: case 82: return 'ливневый дождь';
        case 85: case 86: return "ливневый снег";
        case 95: case 96: case 99: return "гроза";
        default: return "неизвестно";
    }
}

function getClothingRecomendation(weatherResult){
    var recommendation = []
    if (weatherResult.data.current.temperature_2m >= 25) {
        recommendation.push('футболку', 'шорты', 'солнечные очки')
    } else if (weatherResult.data.current.temperature_2m >= 15) {
        recommendation.push('кофту или ветровку')
    } else if (weatherResult.data.current.temperature_2m >= 5){
        recommendation.push('пальто', 'куртку')
    } else ('теплое пальто', 'теплую куртку')

    if (weatherResult.data.current.weather_code == 0 || weatherResult.data.current.weather_code == 1){
        recommendation.push("солнечные очки")
    } else if (weatherResult.data.current.weather_code >= 57 || weatherResult.data.current.weather_code <= 67){
        recommendation.push("зонт", 'дождевик')
    } else if (weatherResult.data.current.weather_code >= 71 || weatherResult.data.current.weather_code <= 86){
        recommendation.push("зонт", 'теплая обувь')
    } else if (weatherResult.data.current.weather_code >= 95 || weatherResult.data.current.weather_code <= 99){
        recommendation.push('Не выходить на улицу пока идет гроза')
    }
    log(recommendation)
    return recommendation
    
}

