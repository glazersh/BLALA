import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class ParseUnit {

    Map<String,String> month= new HashMap<>();
    Map<String, Integer> wordsDict = new HashMap<>();
    Map<String, Map> fileDict = new HashMap<>();

    public ParseUnit(){
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

    // Hyphen
    // don't forget to remove  this "-"
    public void kindOfHyphen(String [] words){
        List<String> listOfWords = Arrays.asList(words);
        String [] wordsWithout = words[0].split("-");
        String firstWord = wordsWithout[0];
        String secondWord = wordsWithout[1];
        // Word - Word
        if (firstWord.matches("[a-zA-Z]+") && secondWord.matches("[a-zA-Z]+")){
            String word = firstWord+"-"+secondWord;
            if(wordsWithout.length == 3){
                word += "-"+wordsWithout[2];
            }
            System.out.println(word);
            return;
        }
        // Number - Number
        if (firstWord.matches("[0-9]+") && secondWord.matches("[0-9]+")){
            System.out.println(firstWord +" "+secondWord+" "+firstWord+"-"+secondWord);
            return;
        }

        // Number - Word  or  Word - Number
        if ((firstWord.matches("[a-zA-Z]+") && secondWord.matches("[0-9]+"))){
            System.out.println(secondWord+" "+firstWord+"-"+secondWord);
        }
        if(firstWord.matches("[0-9]+") && secondWord.matches("[a-zA-Z]+")){
            System.out.println(firstWord+" "+firstWord+"-"+secondWord);
        }

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
                String numberInString = number.toString();
                try
                {
                    double numInDouble;
                    int numInInt;
                    if(numberInString.contains(".")) {
                        numInDouble =Double.parseDouble(numberInString);
                        if(numInDouble < 1000) {
                            System.out.println(numInDouble);
                            return;
                        }
                        if(numInDouble>999 && numInDouble < 1000000) {
                            System.out.println(numInDouble / 1000 + "K");
                            return;
                        }
                        if(numInDouble>999999 && numInDouble < 1000000000) {
                            System.out.println(numInDouble / 1000000 + "M");
                            return;
                        }
                        if(numInDouble>999999999) {
                            System.out.println(numInDouble / 1000000000 + "B");
                            return;
                        }
                    }
                    else {
                        numInInt = Integer.parseInt(numberInString);

                        if (numInInt < 1000) {
                            System.out.println(numInInt);
                            return;
                        }
                        if (numInInt > 999 && numInInt < 1000000) {
                            if(numInInt % 1000 == 0)
                                System.out.println(numInInt / 1000 + "K");
                            else
                                System.out.println((double)numInInt / 1000 + "K");
                            return;
                        }
                        if (numInInt > 999999 && numInInt < 1000000000) {
                            if(numInInt % 1000000 == 0)
                                System.out.println(numInInt / 1000000 + "M");
                            else
                                System.out.println((double)numInInt / 1000000 + "M");
                            return;
                        }
                        if (numInInt > 999999999) {
                            if(numInInt % 1000000000 == 0)
                                System.out.println(numInInt / 1000000000 + "B");
                            else
                                System.out.println((double)numInInt / 1000000000 + "B");
                            return;
                        }
                    }
                }
                catch(NumberFormatException e){}

                if(numberInString.length()>=10){
                    int size = numberInString.length();
                    String bigNumber = numberInString.substring(0,size-9);
                    String after = numberInString.substring(size-9,size-1);
                    for(int i=after.length()-1;i>=0;i--){
                        if(after.charAt(i) != '0'){
                            after=after.substring(0,i+1);
                            break;
                        }
                    }
                    System.out.println(bigNumber+"."+after+"B");
                }
                else {
                    convertNumberToLetter(Integer.parseInt(numberInString), false, true);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        else {
            int number = Integer.parseInt(words[0]);
            if (words[1].equals("Thousand")){
                System.out.println(number+"K");
                return;
            }
            if (words[1].equals("Million")){
                System.out.println(number+"M");
                return;
            }
            if (words[1].equals("Billion")){
                System.out.println(number+"B");
                return;
            }
            if (words[1].equals("Trillion")){
                System.out.println(number*1000+"B");
                return;
            }
            System.out.println(words[0]+" "+words[1]);
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

            NumberFormat format = NumberFormat.getInstance(Locale.US);
            Number number = null;
            try {
                number = format.parse(words.get(0).substring(1));
                String numberInString = number.toString();
                if(Integer.parseInt(numberInString) < 1000000){
                    System.out.println(words.get(0).substring(1) + " Dollars");
                    return;
                }
                else {
                    System.out.println(Integer.parseInt(numberInString)/1000000 + " M Dollars");
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
        if(words.get(1).contains("/")){
            System.out.println(words.get(0) + " " +words.get(1) + " Dollars");
            return;
        }
        // just number
        if(!words.get(0).contains(".")){
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            Number number = null;
            try {
                number = format.parse(words.get(0));
                String numberInString = number.toString();
                int numberInInt = Integer.parseInt(numberInString);
                if(numberInInt<1000000)
                    System.out.println(words.get(0) +" Dollars");
                else{
                    System.out.println(numberInInt/1000000 + " M Dollars");
                }
            }catch (ParseException e) {
                    e.printStackTrace();
            }

        }
        else{
            double number = Double.parseDouble(words.get(0));
            if(number<1000000)
                System.out.println(words.get(0) +" Dollars");
            else{
                System.out.println(number/1000000+ " M Dollars");
            }
        }
    }
    // left-to scan the dict again with bigLetter
    public void mappingWords(String [] words){
        wordsDict.put("Got",2);
        for (String str: words) {
            char c = str.charAt(0);
            if(Character.isUpperCase(c)){
                if(wordsDict.containsKey(Character.toLowerCase(c) + str.substring(1,str.length()))){
                    Integer tmp = wordsDict.get((Character.toLowerCase(c) + str.substring(1,str.length())));
                    wordsDict.put(Character.toLowerCase(c) + str.substring(1,str.length()), tmp+1);
                }
                else if (wordsDict.containsKey(str))
                {
                    Integer tmp = wordsDict.get(str);
                    wordsDict.put(str, tmp + 1);
                }else
                    wordsDict.put(str,1);

            } else {
                if (wordsDict.containsKey(Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length()))) {
                    Integer tmp = wordsDict.get(Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length()));
                    wordsDict.remove(Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length()));
                    wordsDict.put(str, tmp + 1);
                } else if (wordsDict.containsKey(str)) {
                    Integer tmp = wordsDict.get(str);
                    wordsDict.put(str, tmp + 1);
                } else
                    wordsDict.put(str, 1);


            }
        }
        funcEnd();
        System.out.print(wordsDict);


    }

    public void funcEnd(){
        Iterator it = wordsDict.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            if(Character.isUpperCase(pair.getKey().toString().charAt(0))){
                Integer i = (Integer) pair.getValue();
                String s = (String) pair.getKey();
                wordsDict.remove(s);
                wordsDict.put(s.toUpperCase(),i);
            }
        }
    }

}

