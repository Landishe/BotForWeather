function weatherApi(cityData) {
    
    var responce = $http.get('https://api.open-meteo.com/v1/forecast?latitude=${lat}&longitude=${lon}&timezone=auto&current=${current}&daily=${daily}',{
        query: {
            lat: cityData.lat,
            lon: cityData.lon,
            timezone: 'auto',
            current: 'temperature_2m' + ',weather_code' + ',wind_speed_10m',
            daily: 'temperature_2m_max' + ',weather_code' + ',wind_speed_10m_max' + ',sunset' + ',sunrise',
            }
        });
    return responce
     
}



