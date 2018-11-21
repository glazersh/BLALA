import Term.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

import static jdk.nashorn.internal.runtime.JSType.isNumber;

public class ParseUnit {

    Map<String,String> month= new HashMap<>();
    Map<String, Map> fileDict = new HashMap<>();
    HashSet<String> afterNumber = new HashSet<>();

    Set<String> stopWords = new HashSet<>();
    HashSet<Character>signs = new HashSet<>();



    
    Map<ATerm,Integer>TMP1 = new HashMap<>();
    Map<String, ATerm> wordsDict = new HashMap<>();
    Map<String,Integer>termMap = new HashMap<>();
    Map<ATerm,Map<String,Integer>> allWordsDic = new HashMap<>();
    Stemmer stem = new Stemmer();

    ATerm term;
    //Map<String,Map>
    Map<ATerm,Integer>wordsInDoc;


    public ParseUnit(){
        insertMonth(); // init all months
        insertAfterWords(); // init special words for our parse
        StopWords(); // init all stopWords from stopWords.txt
        insertSigns(); // init all the signs
    }

    private void StopWords(){
        Scanner file = null;
        try {
            //don't forget to change the path !!!!
            file = new Scanner(new File("C:\\Users\\USER\\Desktop\\מערכות מידע דור\\סמסטר ד\\נושאים מתקדמים בתכנות\\SearchEngineJ\\src\\main\\resources\\stopWords.txt"));
            // For each word in the input
            while (file.hasNext()) {
                // Convert the word to lower case, trim it and insert into the set
                // In this step, you will probably want to remove punctuation marks
                stopWords.add(file.next());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void insertMonth() {
        month.put("January", "01");
        month.put("JANUARY", "01");
        month.put("Jan", "01");
        month.put("February", "02");
        month.put("FEBRUARY", "02");
        month.put("Feb", "02");
        month.put("March", "03");
        month.put("MARCH", "03");
        month.put("Mar", "03");
        month.put("April", "04");
        month.put("APRIL", "04");
        month.put("Apr", "04");
        month.put("May", "05");
        month.put("MAY", "05");
        month.put("June", "06");
        month.put("JUNE", "06");
        month.put("Jun", "06");
        month.put("July", "07");
        month.put("JULY", "07");
        month.put("Jul", "07");
        month.put("August", "08");
        month.put("AUGUST", "08");
        month.put("Aug", "08");
        month.put("September", "09");
        month.put("SEPTEMBER", "09");
        month.put("Sep", "09");
        month.put("October", "10");
        month.put("OCTOBER", "10");
        month.put("Oct", "10");
        month.put("November", "11");
        month.put("NOVEMBER", "11");
        month.put("Nov", "11");
        month.put("December", "12");
        month.put("DECEMBER", "12");
        month.put("Dec", "12");
    }
    private void insertAfterWords(){
        afterNumber.add("Thousand");
        afterNumber.add("Million");
        afterNumber.add("Billion");
        afterNumber.add("Trillion");
        afterNumber.add("percent");
        afterNumber.add("percentage");
        afterNumber.add("Dollars");
        afterNumber.add("million");
        afterNumber.add("billion");
        afterNumber.add("trillion");
        afterNumber.add("U.S.");
        afterNumber.add("dollars");
    }
    private void insertSigns(){
        signs.add('.');
        signs.add(',');
        signs.add(';');
        signs.add('(');
        signs.add('{');
        signs.add('[');
        signs.add(')');
        signs.add('}');
        signs.add(']');
        signs.add(':');
        signs.add('!');
        signs.add('?');
        signs.add('`');
        signs.add('|');
        signs.add('+');
        signs.add('"');
        signs.add('*');
    }


    public void parse(String [] allText, String docName){
        wordsInDoc = new HashMap<>();
        List <String>expression = new ArrayList();

        //Check every words :
        //  1. cut all the signs in the first and the last character
        //  2. check if stopWords List contain the word
        //      2.1 if true, continue
        //      2.2 else, send to stemmer
        //
        for(int i=0;i<allText.length;i++){

            String word = cutSigns(allText[i]); // cut the signs

            if(!word.equals("") && (word.equals("Between") || !stopWords.contains(word.toLowerCase()))) {

                // stemmer
                stem.add(word.toCharArray(),word.length());
                stem.stem();
                word = stem.toString(); // get the word after the stem
/*
                // check if word is number
                // if true, check if Integer or Double
                boolean isNumber = false;
                boolean isInteger = true;
                if(isWordIsNumber(word)){
                    isNumber = true;
                    if(word.contains(".")){
                        isInteger = false;
                    }
                }

                // if word's first character is with ($)Word or last character is Word(%)
                // and then check if word is Number.
                if(i+1<allText.length && (word.charAt(0) == '$' || allText[i+1].equals("Dollars") || allText[i+2].equals("Dollars") || allText[i+2].equals("U.S."))) {
                    if (word.charAt(0) == '$') {
                        if (isWordIsNumber(word.substring(1))) {
                            if (word.contains(".")) {
                                isInteger = false;
                            }
                        }
                    }

                }
                if(word.charAt(word.length()-1) == '%') {
                    if (isWordIsNumber(word.substring(0, word.length() - 1))) {
                        if (word.contains(".")) {
                            isInteger = false;
                        }
                    }
                }
*/



                try {

                    if(word.charAt(0) == '$' || word.charAt(0) == '%')
                        Integer.parseInt(word.substring(1));
                    else
                        Integer.parseInt(word);
                    expression.add(word);
                    while (i+1<allText.length && (afterNumber.contains(allText[i + 1]) || month.containsKey(allText[i + 1]))) {
                        String nextWord = cutSigns(allText[++i]);
                        expression.add(nextWord);
                    }
                    String[] tmp2 = new String[expression.size()];
                    expression.toArray(tmp2);
                    kindOfNumber(tmp2);
                    expression.clear();
                    continue;
                } catch (NumberFormatException e) {
                    try {
                        if(!word.equals("") && (word.charAt(0) == '$' || word.charAt(word.length()-1) == '%'))
                            Double.parseDouble(word.substring(1));
                        else
                            Double.parseDouble(word);
                        expression.add(word);
                        while (i+1<allText.length && (afterNumber.contains(allText[i + 1]) || month.containsKey(allText[i + 1]))) {
                            String nextWord = cutSigns(allText[++i]);
                            expression.add(nextWord);
                            i = i + 1;
                        }
                        String[] tmp2 = new String[expression.size()];
                        expression.toArray(tmp2);
                        kindOfNumber(tmp2);
                        expression.clear();
                        continue;
                    } catch (NumberFormatException e2) {}
                    if(i+3 < allText.length && allText[i].equals("Between")){
                        String[] tmp = new String[4];
                        tmp[0] = allText[i];
                        tmp[1] = allText[i+1];
                        tmp[2] = allText[i+2];
                        tmp[3] = allText[i+3];
                        kindOfHyphen(tmp);
                        i=i+3;
                    }
                    else {
                        term = new Word(word);
                        increaseCounter(term);
                        TMP1.put(term, 1);
                    }

                }
            }
        }
        for(ATerm term:wordsInDoc.keySet()){
            int counterWord = wordsInDoc.get(term);// how many time the term exist
            if(allWordsDic.containsKey(term)) {
                allWordsDic.get(term).put(docName, counterWord);
            }
            else{
                termMap.put(docName,wordsInDoc.get(term));
                allWordsDic.put(term,termMap);
            }
        }
    }

    /**
     * Function which updates the counter of the term in the doc
     * in case the term doesn't exist, insert.
     * @param term
     */
    private void increaseCounter(ATerm term){
        if(wordsInDoc.containsKey(term)) {
            Integer tmp = wordsInDoc.get(term);
            wordsInDoc.put(term, tmp + 1);
        }else{
            wordsInDoc.put(term,1);
        }
    }

    private boolean isWordIsNumber(String word) {
        if(word.matches("[0-9.]+")){
            return true;
        }
        return false;
    }

    private void checkIfNumIsInteger(String wordNumber, List exp){

    }

    private String cutSigns(String beforeCut) {
        while(!beforeCut.equals("") && signs.contains(beforeCut.charAt(0))) {
            beforeCut = beforeCut.substring(1);
        }
        while(!beforeCut.equals("") && signs.contains(beforeCut.charAt(beforeCut.length()-1))) {
            beforeCut = beforeCut.substring(0,beforeCut.length()-1);
        }
        return beforeCut;
    }

    // Hyphen
    // don't forget to remove  this "-"
    public void kindOfHyphen(String [] words){

        String firstWord = words[0];
        String secondWord = words[1];
        String thirdWord = words[2];
        String fourthWord = words[3];

        if(secondWord.matches("[0-9.]+") && thirdWord.equals("and") && fourthWord.matches("[0-9.]+")){
            term = new Range(firstWord+"-"+secondWord+"-"+thirdWord+"-"+fourthWord);
            TMP1.put(term,1);
            increaseCounter(term);
        }
    }

    public void kindOfNumber(String [] words) {
        List<String> listOfWords = Arrays.asList(words);
        // check if the number is a price expression
        if (listOfWords.contains("Dollars") || listOfWords.contains("U.S.") || listOfWords.contains("dollars") ||
                words[0].contains("$")) {
            numberOfPrice(listOfWords);
            return;
        }
        // check if the number is a Date
        if ((words.length == 2) && (month.containsKey(words[0]) || month.containsKey(words[1]))) {
            numberOfDate(words);
            return;
        }
        // check if this a percent number
        if (words[0].contains("%") || listOfWords.contains("percent") || listOfWords.contains("percentage")) {
            numberOfPercent(words);
            return;
        }
        numOfRegularExpression(words);
    }

        private void numOfRegularExpression(String [] words){
        // regular expression
        if (words.length == 1){
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            try {
                // remove all ","
                String numberInString = format.parse(words[0]).toString();
                try
                {
                    double numInDouble= -1;
                    int numInInt = -1;
                    boolean isDouble = false;

                    if(numberInString.contains(".")){
                        numInDouble =Double.parseDouble(numberInString);
                        isDouble = true;
                    }
                    else{
                        numInInt = Integer.parseInt(numberInString);
                    }
                    if((numInInt == -1 && numInDouble < 1000) || (numInInt < 1000 && numInDouble ==-1) ){
                        term = new NumberU(words[0]);
                        increaseCounter(term);
                        TMP1.put(term,1);
                        //.out.println(words[0]);
                        return;
                    }
                    if((numInDouble>999 && numInDouble < 1000000 && numInInt == -1) ||
                        numInInt >999 && numInInt < 1000000 && numInDouble == -1) {
                        if (isDouble) {
                            term = new NumberK(numInDouble / 1000 + "K");
                            //System.out.println(numInDouble / 1000 + "K");
                        }
                        else {
                            if(numInInt % 1000 == 0) {
                                term = new NumberK(numInInt / 1000 + "K");
                                ///System.out.println(numInInt / 1000 + "K");
                            }else{
                                term = new NumberK((double)numInInt / 1000 + "K");
                                //System.out.println((double)numInInt / 1000 + "K");
                            }
                        }
                        increaseCounter(term);
                        TMP1.put(term,1);
                        return;
                    }
                    if((numInDouble>999999 && numInDouble < 1000000000 && numInInt == -1) ||
                            numInInt >999999 && numInInt < 1000000000 && numInDouble == -1) {
                        if (isDouble) {
                            term = new NumberK(numInDouble / 1000000 + "M");
                            //System.out.println(numInDouble / 1000000 + "M");
                        }
                        else {
                            if(numInInt % 1000000 == 0) {
                                term = new NumberK(numInInt / 1000000 + "M");
                                //System.out.println(numInInt / 1000000 + "M");
                            }else{
                                term = new NumberK((double)numInInt / 1000000 + "M");
                                //System.out.println((double)numInInt / 1000000 + "M");
                            }
                        }
                        increaseCounter(term);
                        TMP1.put(term,1);
                        return;
                    }
                    if((numInDouble>999999999  && numInInt == -1) ||
                            numInInt >999999999 && numInDouble == -1) {
                        if (isDouble) {
                            term = new NumberK(numInDouble / 1000000000 + "B");
                            //System.out.println(numInDouble / 1000000000 + "B");
                        }
                        else {
                            if(numInInt % 1000000000 == 0) {
                                term = new NumberK(numInInt / 1000000000 + "B");
                                //System.out.println(numInInt / 1000000000 + "B");
                            }else{
                                term = new NumberK((double)numInInt / 1000000000 + "B");
                                //System.out.println((double)numInInt / 1000000000 + "B");
                            }
                        }
                        increaseCounter(term);
                        TMP1.put(term,1);
                        return;
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
                    term = new NumberB(bigNumber+"."+after+"B");
                    //System.out.println(bigNumber+"."+after+"B");
                    increaseCounter(term);
                    TMP1.put(term,1);
                }
                else {
                    convertNumberToLetter(Integer.parseInt(numberInString), false, true);
                    increaseCounter(term);
                    TMP1.put(term,1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            boolean isInt = true;
            int number =0;
            double numberDouble = 0 ;
            try{
                Integer.parseInt(words[0]);
                number = Integer.parseInt(words[0]);
            }catch (NumberFormatException e){
                numberDouble = Double.parseDouble(words[0]);
                isInt = false;
            }

            if (words[1].equals("Thousand")){
                if (isInt)
                    term = new NumberK(number+"K");
                else
                    term = new NumberK(numberDouble+"K");
                //System.out.println(number+"K");
                increaseCounter(term);
                TMP1.put(term,1);
                return;
            }
            if (words[1].equals("Million")){
                if(isInt)
                    term = new NumberM(number+"M");
                else
                    term = new NumberM(numberDouble+"M");
                //System.out.println(number+"M");
                increaseCounter(term);
                TMP1.put(term,1);
                return;
            }
            if (words[1].equals("Billion")){
                if(isInt)
                    term = new NumberB(number+"B");
                else
                    term = new NumberB(numberDouble+"B");
                //System.out.println(number+"B");
                increaseCounter(term);
                TMP1.put(term,1);
                return;
            }
            if (words[1].equals("Trillion")){
                if(isInt)
                    term = new NumberB(number*1000+"B");
                else
                    term = new NumberB(numberDouble*1000+"B");
                //System.out.println(number*1000+"B");
                increaseCounter(term);
                TMP1.put(term,1);
                return;
            }
            // fraction - check if K M B?
            term = new NumberU(words[0]+" "+words[1]);
            //System.out.println(words[0]+" "+words[1]);
            increaseCounter(term);
            TMP1.put(term,1);
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
                    term = new NumberK(((double)number/1000) + "K");
                    //System.out.println(((double)number/1000) + "K");
                }
                else{
                    term = new NumberK((number/1000) + "K");
                    //System.out.println((number/1000) + "K");
                }
                increaseCounter(term);
                TMP1.put(term,1);
            }
            if(number > 999999 && number < 1000000000){
                if ( number %1000000 !=0 ){
                    term = new NumberM(((double)number/1000000) + "M");
                    //System.out.println(((double)number/1000000) + "M");
                }
                else{
                    term = new NumberM((number/1000000) + "M");
                    //System.out.println((number/1000000) + "M");
                }
                increaseCounter(term);
                TMP1.put(term,1);
            }
            if(number > 999999999){
                if ( number %1000000000 !=0 ){
                    term = new NumberB(((double)number/1000000000) + "B");
                    //System.out.println(((double)number/1000000000) + "B");
                }
                else{
                    term = new NumberB((number/1000000000) + "B");
                    //System.out.println((number/1000000000) + "B");
                }
                increaseCounter(term);
                TMP1.put(term,1);
            }
        }
        return "";
    }

    private void numberOfPercent(String[] words) {
        String wordPercent = words[0];
        if (!words[0].contains("%")){
            wordPercent = wordPercent+"%";
        }
        term = new Percent(wordPercent);
        increaseCounter(term);
        TMP1.put(term,1);
        //System.out.println(wordPercent);
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
            term = new DateDay(monthInString+"-"+secondString);
            //System.out.println(monthInString+"-"+secondString);
        }
        else {
            term = new DateYear(secondString+"-"+monthInString);
            //System.out.println(secondString+"-"+monthInString);
        }
        increaseCounter(term);
        TMP1.put(term,1);

    }

    private void numberOfPrice(List<String> words) {
        int numberInt;
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
            String finalRecord = numberInt + " M Dollars";
            term = new PriceM(finalRecord);
            increaseCounter(term);
            TMP1.put(term,1);
            //System.out.println(finalRecord);
            return;
        }
        if(words.get(0).contains("$")){

            NumberFormat format = NumberFormat.getInstance(Locale.US);
            try {
                // check double !
                String numberInString = format.parse(words.get(0).substring(1)).toString();
                if(!numberInString.contains(".")){
                    if(numberInString.length() < 7){
                        term = new Price(words.get(0).substring(1) + " Dollars");
                        increaseCounter(term);
                        TMP1.put(term,1);
                        return;
                    }else{
                        term = new PriceM(Integer.parseInt(numberInString)/1000000 + " M Dollars");
                        increaseCounter(term);
                        TMP1.put(term,1);
                        return;
                    }
                }else{
                    if(numberInString.length() < 8){
                        term = new Price(words.get(0).substring(1) + " Dollars");
                        increaseCounter(term);
                        TMP1.put(term,1);
                        return;
                    }else {
                        term = new PriceM(Double.parseDouble(numberInString) / 1000000 + " M Dollars");
                        increaseCounter(term);
                        TMP1.put(term,1);
                        return;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(words.size() > 1 && (words.get(1).equals("m") || words.get(1).equals("bn"))){
            double numberInDouble;
            int numberInInt;
            String finalRecord = words.get(0) + " M Dollars";
            if(words.get(1).equals("bn")){
                if(words.get(0).contains(".")){
                    numberInDouble = Double.parseDouble(words.get(0)) * 1000;
                    finalRecord = numberInDouble + " M Dollars";
                }
                else{
                    numberInInt = Integer.parseInt(words.get(0)) * 1000;
                    finalRecord = numberInInt + " M Dollars";
                }
            }
            term = new PriceM(finalRecord);
            increaseCounter(term);
            TMP1.put(term,1);
            //System.out.println(finalRecord);
            return;

        }
        if(words.size()>1 && words.get(1).contains("/")){
            //System.out.println(words.get(0) + " " +words.get(1) + " Dollars");
            term = new Price(words.get(0) + " " +words.get(1) + " Dollars");
            increaseCounter(term);
            TMP1.put(term,1);
            return;
        }
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        String numberInString = null;
        double numberDouble2 = -1.0;
        int numberInt2 = -1;
        boolean isDouble = false;
        try {
            numberInString = format.parse(words.get(0)).toString();
            if (numberInString.contains(".")){
                numberDouble2 = Double.parseDouble(numberInString);
                isDouble = true;
            }
            else{
                numberInt2 = Integer.parseInt(numberInString);
            }
            if((numberDouble2 == -1 && numberInt2 < 1000000 && numberInt2 >= 0) || (numberInt2 == -1 && numberDouble2 < 1000000 && numberDouble2 >= 0)){
                term = new Price(words.get(0) + " Dollars");
                increaseCounter(term);
                TMP1.put(term,1);
                //System.out.println(words.get(0) + " Dollars");
            }
            else{
                if(isDouble){
                    term = new PriceM(numberDouble2/1000000 + " M Dollars");
                    //System.out.println(numberDouble2/1000000 + " M Dollars");
                }else {
                    term = new PriceM(numberInt2/1000000 + " M Dollars");
                    //System.out.println(numberInt2/1000000 + " M Dollars");
                }
                increaseCounter(term);
                TMP1.put(term,1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return;
    }
    // left-to scan the dict again with bigLetter
    public void mappingWords(String [] words){

        for (String str: words) {
            char c = str.charAt(0);
            if(Character.isUpperCase(c)){
                if(wordsDict.containsKey(Character.toLowerCase(c) + str.substring(1,str.length()))){
                    //Integer tmp = wordsDict.get((Character.toLowerCase(c) + str.substring(1,str.length())));
                   // wordsDict.put(Character.toLowerCase(c) + str.substring(1,str.length()), tmp+1);
                }
                else if (wordsDict.containsKey(str))
                {
                    //Integer tmp = wordsDict.get(str);
                    //wordsDict.put(str, tmp + 1);
                }//else
                    //wordsDict.put(str,1);

            } else {
                if (wordsDict.containsKey(Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length()))) {
                    //Integer tmp = wordsDict.get(Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length()));
                    wordsDict.remove(Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length()));
                    //wordsDict.put(str, tmp + 1);
                } else if (wordsDict.containsKey(str)) {
                    //Integer tmp = wordsDict.get(str);
                    //wordsDict.put(str, tmp + 1);
                } //else
                    //wordsDict.put(str, 1);


            }
        }
        funcEnd();
        System.out.print(wordsDict);


    }

    private void funcEnd(){
        Iterator it = wordsDict.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            if(Character.isUpperCase(pair.getKey().toString().charAt(0))){
                Integer i = (Integer) pair.getValue();
                String s = (String) pair.getKey();
                wordsDict.remove(s);
                //wordsDict.put(s.toUpperCase(),i);
            }
        }
    }

    // for us
    public void printDic(){
        int i=0;
/*
        for (String word:wordsDict.keySet()) {
            System.out.println(word);
            i++;
        }
*/
        System.out.println("size of dictionary - "+wordsDict.size());

        System.out.println("size of ATerm dictionary - "+TMP1.size());
    }

}

