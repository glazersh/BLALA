import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class ParseUnit {

    Map<String,String> month= new HashMap<>();


    private String [] monthList = {"January", "JANUARY", "Jan",
                                   "February", "FEBRUARY", "Feb",
                                   "March", "MARCH", "Mar",
                                   "April", "APRIL", "Apr",
                                   "May", "MAY", "MEy",
                                   "June", "JUNE", "Jun",
                                   "July", "JULY", "Jul",
                                   "August", "AUGUST", "Aug",
                                   "September", "SEPTEMBER", "Sep",
                                   "October", "OCTOBER", "Oct",
                                   "November", "NOVEMBER", "Nov",
                                   "December", "DECEMBER", "Dec"};
    List<String> listOfMonth;

    public ParseUnit(){
        listOfMonth = Arrays.asList(monthList);
        month.put("January","01");
        month.put("JANUARY","01");
        month.put("Jan","01");
        month.put("February","02");
        month.put("FEBRUARY","02");
        month.put("Feb","02");
        month.put("March","03");
        month.put("MARCH","03");
        month.put("Mar","03");
        month.put("April","04");
        month.put("APRIL","04");
        month.put("Apr","04");
        month.put("May","05");
        month.put("MAY","05");
        month.put("June","06");
        month.put("JUNE","06");
        month.put("Jun","06");
        month.put("July","07");
        month.put("JULY","07");
        month.put("Jul","07");
        month.put("August","08");
        month.put("AUGUST","08");
        month.put("Aug","08");
        month.put("September","09");
        month.put("SEPTEMBER","09");
        month.put("Sep","09");
        month.put("October","10");
        month.put("OCTOBER","10");
        month.put("Oct","10");
        month.put("November","11");
        month.put("NOVEMBER","11");
        month.put("Nov","11");
        month.put("December","12");
        month.put("DECEMBER","12");
        month.put("Dec","12");
    }


    public void kindOfNumber(String [] words){
        List<String> listOfWords = Arrays.asList(words);
        // check if the number is a price expression
        if (listOfWords.contains("Dollars") || listOfWords.contains("U.S.") || listOfWords.contains("dollars") ||
                words[0].contains("$")){
            numberOfPrice(listOfWords);
            return;
        }
        // check if the number is a Date
        if ((words.length == 2) && (month.containsKey(words[0]) || month.containsKey(words[1]))){
            numberOfDate(words);
            return;
        }

        // check if this a percent number
        if (words[0].contains("%") || listOfWords.contains("percent") || listOfWords.contains("percentage")){
            numberOfPercent(words);
            return;
        }
        // regular expression
        if (words.length == 1){
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            try {
                Number number = format.parse(words[0]);
                String numberInteger = number.toString();
                convertNumberToLetter(Integer.parseInt(numberInteger), false, true);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        else {
            int number = Integer.parseInt(words[0]);
            if (words[1].equals("Thousand")){
                number = number*1000;
            }
            if (words[1].equals("Million")){
                number = number*1000000;
            }

            if (words[1].equals("Billion")){
                number = number*1000000000;
            }

            if (words[1].equals("Trillion")){
                number = number*1000000000*1000;
            }
            convertNumberToLetter(number, false, true);
        }
    }

    private String convertNumberToLetter(int number, boolean ifPrice, boolean divide) {
        if (ifPrice){
            if (number >= 1000000 && divide){
                if (number % 1000000 != 0){
                    return (double)number/1000000 +" M";
                }
                else{
                    return number/1000000 +" M";
                }
            }
            else{
                return number+"" ;
            }
        }
        else{
            if(number > 999 && number < 1000000){
                if ( number %1000 !=0 ){
                    System.out.println(((double)number/1000) + "K");
                }
                else{
                    System.out.println((number/1000) + "K");
                }
            }
            if(number > 999999 && number < 1000000000){
                if ( number %1000000 !=0 ){
                    System.out.println(((double)number/1000000) + "M");
                }
                else{
                    System.out.println((number/1000000) + "M");
                }
            }
            if(number > 999999999){
                if ( number %1000000000 !=0 ){
                    System.out.println(((double)number/1000000000) + "B");
                }
                else{
                    System.out.println((number/1000000000) + "B");
                }
            }
        }
        return "";
    }

    private void numberOfPercent(String[] words) {
        String wordPercent = words[0];
        if (!words[0].contains("%")){
            wordPercent = wordPercent+"%";
        }
        System.out.println(wordPercent);
    }

    private void numberOfDate(String[] words) {
        String monthInString = "";
        String secondString = "";
        if (month.containsKey(words[0])){
            monthInString = month.get(words[0]);
            secondString = words[1];
        }
        if (month.containsKey(words[1])){
            monthInString = month.get(words[1]);
            secondString = words[0];
        }
        if(Integer.parseInt(secondString) < 31){
            if(Integer.parseInt(secondString) < 10) {
                if(!secondString.startsWith("0"))
                secondString = "0"+secondString;
            }
            System.out.println(monthInString+"-"+secondString);
        }
        else {
            System.out.println(secondString+"-"+monthInString);
        }

    }

    private void numberOfPrice(List<String> words) {
        int numberInt = 0;
        if (words.contains("million") || words.contains("billion") || words.contains("trillion")){
            String numberString = words.get(0);
            if (numberString.contains("$")) {
                numberString = numberString.substring(1);
            }
            numberInt = Integer.parseInt(numberString);
            if(words.contains("billion")){
                numberInt = numberInt*1000;
            }
            if(words.contains("trillion")){
                numberInt*=1000000;
            }
            String finalRecord = convertNumberToLetter(numberInt, true, false) + " M Dollars";
            System.out.println(finalRecord);
            return;
        }
        if(words.get(0).contains("$")){
            String finalRecord = convertNumberToLetter(Integer.parseInt(words.get(0).substring(1)), true, true) + " Dollars";
            System.out.println(finalRecord);
            return;
        }
        if(words.get(1).equals("m") || words.get(1).equals("bn")){
            try{
                int numberInTmp = Integer.parseInt(words.get(0));
                if(words.get(1).equals("bn")){
                    numberInTmp = numberInTmp*1000;
                    String finalRecord = convertNumberToLetter(numberInTmp,true, true) + " M Dollars";
                    System.out.println(finalRecord);
                    return;
                }
            }catch(NumberFormatException e){}
            try{
                double numberInTmp = Double.parseDouble(words.get(0)) * 1000000;
                if(words.get(1).equals("bn")){
                    numberInTmp = numberInTmp*1000;
                }
                String finalRecord = convertNumberToLetter((int)numberInTmp,true, true) + " Dollars";
                System.out.println(finalRecord);
                return;
            }catch(NumberFormatException e){}
        }
        String finalRecord = convertNumberToLetter(Integer.parseInt(words.get(0)),true,true) + " Dollars";
        System.out.println(finalRecord);
    }


}

