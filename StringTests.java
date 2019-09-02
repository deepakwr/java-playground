import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;



class Result {

    // List of months in 3 letter as specified for returning the number as per their index.
    final static ArrayList<String> strMonths = new ArrayList<String>(
        Arrays.asList("jan", "feb", "mar", "apr","may", "jun", "jul", "aug", "sep","oct","nov","dec"));

    //Returns List of all reformatted dates as per the output specified. eg: 20th Oct 2052 → 2052-10-20
    public static List<String> reformatDate(List<String> dates) {
        List<String> reformattedDates = new ArrayList<String>();
        for(String date: dates){
            String reformattedDate = reformatDate(date,"-");
            if(reformattedDate!=null)
            {
                reformattedDates.add(reformattedDate);
            }
        }
        return reformattedDates;
    }

   //This function converts the date into the specified order. "Year-Month-Day" and with the specified character (Dividing character. this can be set as per requirements). 
    public static String reformatDate(String date, String dividingCharacter){
        String reformattedDate = null;
        String[] dates =  splitAndFormatDate(date," ");
        if(dates.length==3)
        {
            // The final string is reorderd as per the output mentioned. The ordering can also be moved to a separate function to make it more reusable.
            reformattedDate = dates[2] + dividingCharacter + dates[1] + dividingCharacter + dates[0];
        }    
        return reformattedDate;
    }


    //The string date is assumed to be day/month/year. The splitting character can be specified as per requirements and the string provided. eg. 1. " " 2. "/" 3. "-"
    public static String[] splitAndFormatDate(String date,String splitCharacter){
        String[] dates = date.split(splitCharacter);
        //Considering the date is split into 3, day, month and year.
        if(dates.length==3)
        {
            // removing the last two letter at the end of the day. "eg: 4th, 1st, 2nd -> 4,1,2".
            dates[0] = convertSingleDigit(dates[0].substring(0,dates[0].length()-2));
            
            // Month information converting the 3 letter text (checking with lower case) to number.
            dates[1] = convertSingleDigit(Integer.valueOf(strMonths.indexOf(dates[1].toLowerCase())+1).toString());

            //Year information does not require any modification.
        }
        return dates;
    }

    // This function add "0" in front of single digit number. Used for number in day or month.
    public static String convertSingleDigit(String text){
        if(text.length()==1)
        {
            text = "0" + text;
        }
        return text;
    }

}

public class StringTests {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int datesCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> dates = new ArrayList<>();

        for (int i = 0; i < datesCount; i++) {
            String datesItem = bufferedReader.readLine();
            dates.add(datesItem);
        }

        List<String> result = Result.reformatDate(dates);

        for (int i = 0; i < result.size(); i++) {
            bufferedWriter.write(result.get(i));

            if (i != result.size() - 1) {
                bufferedWriter.write("\n");
            }
        }

        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
