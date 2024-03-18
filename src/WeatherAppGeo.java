import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherAppGeo {

    //Getting Weather
    public static JSONObject getWeatherData(String locationName){

        JSONArray locationInfo = getLocationData(locationName);

        JSONObject loactionLongLat = (JSONObject) locationInfo.get(0);
        double longitude = (double) loactionLongLat.get("longitude");
                double latitude = (double) loactionLongLat.get("latitude");

                String url = "https://api.open-meteo.com/v1/forecast?" + "latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&temperature_unit=fahrenheit&wind_speed_unit=mph&precipitation_unit=inch&timezone=America%2FNew_York";

                try {
            HttpURLConnection conn = getAPIResponse(url);

            if(conn.getResponseCode() != 200){
                System.out.println("Error: Could not find connection");
                return null;
            }else{
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());

                while(scanner.hasNext()){
                    resultJson.append(scanner.nextLine());
                }
                scanner.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resutJsonObt = (JSONObject) parser.parse(String.valueOf(resultJson));

                JSONObject hourly = (JSONObject) resutJsonObt.get("hourly");
                JSONArray time = (JSONArray) hourly.get("time");

                int index = findCurrentTime(time);

                JSONArray tempData = (JSONArray) hourly.get("temperature_2m");
                double temp = (double) tempData.get(index);

                JSONArray weatherCode =(JSONArray) hourly.get("weather_code");
                String weatherCondition = convertWeatherCode((long)weatherCode.get(index));

                JSONArray relhumidity = (JSONArray) hourly.get("relative_humidity_2m");
                long humidity = (long) relhumidity.get(index);

                JSONArray windSpeed = (JSONArray) hourly.get("wind_speed_10m");
                double windspeed = (double) windSpeed.get(index);

                JSONObject weatherData = new JSONObject();
                weatherData.put("temperature", temp);
                weatherData.put("weatercondition",weatherCondition);
                weatherData.put("humidity",humidity);
                weatherData.put("windspeed",windspeed);

                return weatherData;

            }
                }catch(Exception e){
                e.printStackTrace();
                }



        return null;
    }

    public static JSONArray getLocationData(String locationName){

        locationName = locationName.replaceAll(" ","+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";

        try{

    HttpURLConnection conn = getAPIResponse(urlString);

    if(conn.getResponseCode() != 200){
        System.out.println("Error: Did not connect");
        return null;
    }
    else{
        StringBuilder jsonResult = new StringBuilder();
        Scanner scanner = new Scanner(conn.getInputStream());
        while(scanner.hasNext()){
            jsonResult.append(scanner.nextLine());
        }
        scanner.close();
        conn.disconnect();

        JSONParser parser = new JSONParser();
        JSONObject resultJSONObj = (JSONObject) parser.parse(String.valueOf(jsonResult));

        JSONArray locationData = (JSONArray) resultJSONObj.get("results");
        return locationData;
    }
    }catch(Exception e){
e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection getAPIResponse(String urlString){
      try{
          URL url = new URL(urlString);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();

          conn.setRequestMethod("GET");
          conn.connect();
          return conn;
      }catch(IOException e){
          e.printStackTrace();
      }
        return null;
    }

    private static int findCurrentTime(JSONArray timelist){
        String currentTime = getCurrentTime();

        for(int i = 0; i < timelist.size();i++){
            String time = (String) timelist.get(i);
            if(time.equalsIgnoreCase(currentTime)){
                return i;
            }
        }
return 0;
    }
    private static String getCurrentTime(){
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        String formattedTime = currentDateTime.format(formatter);

        return formattedTime;
    }

    private static String convertWeatherCode(long weathercode){
         String weatherConditon = "";

         if(weathercode == 0L){
             weatherConditon = "Clear";
         }else if(weathercode > 0L && weathercode <= 3L ){
             weatherConditon = "Cloudy";
         }
         else if((weathercode >= 51L && weathercode <=67L) || (weathercode >= 80L && weathercode <= 99L)){
             weatherConditon = "Rain";
         }else if(weathercode >= 71L && weathercode <77L){
             weatherConditon= "Snow";
         }
         return weatherConditon;
    }


}

