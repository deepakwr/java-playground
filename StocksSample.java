import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
import java.net.*;
import com.google.gson.*;
    


public class StocksSample{

    final static ArrayList<String> strDays = new ArrayList<String>(Arrays.asList( "sunday", "monday", "tuesday", "wednesday", "thursday","friday", "saturday" ));

    static void openAndClosePrices(String firstDate, String lastDate, String weekDay) {
        
        int pageNumber = 1;
        int totalPages = 0;
        int totalResponse = 0;

        Date startDate = convertStringToDate(firstDate);
        Date endDate = convertStringToDate(lastDate);

        String url ="https://jsonmock.hackerrank.com/api/stocks/?page=";

        //First request to get page number one to get the count of total pages available.
        JsonObject jsonObject = readJsonObjectFromURL(url,pageNumber);
        totalPages = jsonObject.get("total_pages").getAsInt();
        totalResponse = jsonObject.get("total").getAsInt();

        for(int i=1;i<=totalPages;i++)
        {   
            //Already loaded the first page.
            if(i!=1)
                jsonObject = readJsonObjectFromURL(url, i);
            
            if (jsonObject != null) {
                
                JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
                Date arrayFirstDate = convertStringToDate(jsonArray.get(0).getAsJsonObject().get("date").getAsString());
                Date arrayLastDate = convertStringToDate(jsonArray.get(jsonArray.size()-1).getAsJsonObject().get("date").getAsString());

                // Efficiently checking whether the date is available on the page response loaded.
                if((startDate.compareTo(arrayFirstDate) >= 0 && startDate.compareTo(arrayLastDate) <= 0 )
                    || (endDate.compareTo(arrayFirstDate) >= 0 && endDate.compareTo(arrayLastDate) <= 0)){
                       
                    //Loop only if the date is within the json on the page response loaded.
                    for (JsonElement ja : jsonArray) {
                        JsonObject stockJsonObject = ja.getAsJsonObject();
                        String dateString = getString(stockJsonObject.get("date"));
                        Date date = convertStringToDate(dateString);

                        //Checking if the stock date is within the specified start and end date.
                        if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {

                            //checking for the specified day of the week with the stock.
                            if (checkSpecifiedDay(date, weekDay)) {
                                String openPrice = getString(stockJsonObject.get("open"));
                                String closePrice = getString(stockJsonObject.get("close"));
                                System.out.println(dateString + " " + openPrice + " " + closePrice);
                            }
                        }
                    }
                }
            } else {
                System.out.println("Failed to get response from API:" + url);
            }
        }
    }

    static String getString(JsonElement object){
        if(object==null)
        {
            return null;
        }
        return object.getAsString();
    }

    static Date convertStringToDate(String date){
        Date simpleDate = null;
        try{
            simpleDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).parse(date);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return simpleDate;
    }

    static boolean checkSpecifiedDay(Date date,String day){
    try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            boolean check = cal.get(Calendar.DAY_OF_WEEK) == strDays.indexOf(day.toLowerCase()) + 1;
            return check;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    static JsonObject readJsonObjectFromURL(String urlString,int pageNumber){
        try {
            URL url = new URL(urlString+pageNumber);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        
            String str = org.apache.commons.io.IOUtils.toString(br);
            JsonElement jsonElement = new JsonParser().parse(str);
            if (jsonElement.isJsonObject()){
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                return jsonObject;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String _firstDate;
        try {
            _firstDate = in.nextLine();
        } catch (Exception e) {
            _firstDate = null;
        }
        
        String _lastDate;
        try {
            _lastDate = in.nextLine();
        } catch (Exception e) {
            _lastDate = null;
        }
        
        String _weekDay;
        try {
            _weekDay = in.nextLine();
        } catch (Exception e) {
            _weekDay = null;
        }
        
        openAndClosePrices(_firstDate, _lastDate, _weekDay);
        
    }
}
