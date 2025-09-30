
function weatherApi($session) {
    $temp.responce = $http.get('https://api.open-meteo.com/v1/forecast?latitude=${lat}&longitude=${lon}&current=${current}',{
        query: {
            lat: $parseTree._City.lat,
            lon: $parseTree._City.lon,
            current: 'temperature_2m',
            }    
        });
}


