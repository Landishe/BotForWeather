async function weatherApi() {
    try {
        const url = 'https://api.open-meteo.com/v1/forecast?latitude=55.7522,59.9386,56.75,45.0625,57.625&longitude=37.6156,30.3141,60.8125,39,39.875&current=temperature_2m,relative_humidity_2m,apparent_temperature,rain,showers,snowfall,weather_code,cloud_cover,wind_speed_10m,wind_direction_10m&daily=weather_code,temperature_2m_max,rain_sum,showers_sum,snowfall_sum&wind_speed_unit=ms&timezone=auto';
        
        const response = await fetch(url);
        
        if (!response.ok) {
            throw new Error(`Ошибка HTTP: ${response.status}`);
        }
        
        const jsonData = await response.json();
        log('✅ Данные успешно получены:');
        log(JSON.stringify(jsonData, null, 2));
        
        return jsonData;
        
    } catch (error) {
        error('❌ Ошибка при выполнении запроса:', error);
    }
}

export default {weatherApi};

require: api.js
    type = scriptEs6
    name = api