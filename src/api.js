function weatherApi(cityData) {
    
    var response = $http.get('https://api.open-meteo.com/v1/forecast?latitude=${latitude}&longitude=${longitude}&timezone=${timezone}&current=${current}&daily=${daily}',{
        query: {
            latitude: cityData.lat,
            longitude: cityData.lon,
            timezone: 'auto',
            current: 'temperature_2m' + ',weather_code' + ',wind_speed_10m',
            daily: 'temperature_2m_max' + ',weather_code' + ',wind_speed_10m_max' + ',sunset' + ',sunrise',
            }
            
        });
    log("WEATHER API RESPONSE: " + JSON.stringify(response, null, 2));
    
    return response
    
}



