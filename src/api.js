

function weatherApi(cityData) {
   
    var responce = $http.get('https://api.open-meteo.com/v1/forecast?latitude=${lat}&longitude=${lon}&current=${current}&daily=${daily}',{
        query: {
            lat: cityData.lat,
            lon: cityData.lon,
            current: cityData.current,
            daily: cityData.daily
            }
        });
    return responce
     
}



