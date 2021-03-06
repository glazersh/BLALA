import IO.CountryInMemoryDB;
import IO.CountryInfo;
import Term.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ParseUnit {

    Posting post = new Posting();
    Stemmer stem = new Stemmer();


    Map<String,String> month= new HashMap<>();
    HashSet<String> afterNumber = new HashSet<>();

    Set<String> stopWords = new HashSet<>();
    Set<String>signs = new HashSet<>();

    Map<ATerm,Map<String,Integer>> allWordsDic = new HashMap<>();

    Map<String,Integer> termMap;

    Map<String,String> docInfo = new HashMap<>();

    ATerm term;
    StringBuffer termBeforeChanged;

    Map<ATerm,Integer>wordsInDoc = new HashMap<>();

    CountryInMemoryDB countryInMemory;
    Map<CountryInfo,String> capitalTerms = new HashMap<>();

    HashSet<String> allTerm = new HashSet<>();
    int maxTermCounter;
    int minTermCounter;
    int counterMinTerm;
    String commonTerm = "";


    boolean isTNumber = false;
    boolean isInteger = true;
    boolean isTermPrice = false;
    boolean isTermNumber = false;
    boolean isTermPercent = false;
    boolean isTermDate = false;
    boolean found = false;




    public ParseUnit(){
        insertMonth(); // init all months
        insertAfterWords(); // init special words for our parse
        StopWords(); // init all stopWords from stopWords.txt
        insertSigns(); // init all the signs
        /*
        try {
            countryInMemory = new CountryInMemoryDB("https://restcountries.eu/rest/v2/all?fields=name;capital;population;currencies");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
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
        afterNumber.add("m");
        afterNumber.add("bn");
    }
    private void insertSigns(){
        signs.add(".");
        signs.add(",");
        signs.add(";");
        signs.add("(");
        signs.add("{");
        signs.add("[");
        signs.add(")");
        signs.add("}");
        signs.add("]");
        signs.add(":");
        signs.add("!");
        signs.add("?");
        signs.add("`");
        signs.add("|");
        signs.add("+");
        signs.add("'");
        signs.add("*");
        signs.add(" ");
        signs.add("#");
        signs.add("=");//
        signs.add("/");
        signs.add("@");
        signs.add("--");
        signs.add(""+'"');

    }


    /**
     * Check if the string is number
     * @param str
     * @return true / false
     */
    private boolean isNumber(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        int length = str.length();
        int i = 0;

        // set the length and value for highest positive int or lowest negative int
        int maxlength = 12;
        String maxnum = String.valueOf(Integer.MAX_VALUE);
        if (str.length()>1 && str.charAt(0) == '-' ) {
            maxlength = 13;
            i = 1;
            maxnum = String.valueOf(Integer.MIN_VALUE);
        }

        // verify digit length does not exceed int range
        if (length > maxlength) {
            return false;
        }

        // verify that all characters are numbers
        if (maxlength == 11 && length == 1) {
            return false;
        }
        int counter = 0;
        for (int num = i; num < length; num++) {
            char c = str.charAt(num);
            if (c < '0' || c > '9') {
                if(counter==0 && ((c=='.' || c=='/') && length!=1))
                    counter++;
                else
                    return false;

            }
        }

        // verify that number value is within int range
        if (length == maxlength) {
            for (; i < length; i++) {
                if (str.charAt(i) < maxnum.charAt(i)) {
                    return true;
                }
                else if (str.charAt(i) > maxnum.charAt(i)) {
                    return false;
                }
            }
        }
        isTNumber = true;
        return true;
    }

    /**
     * Remove the comma from the string
     * @param str
     * @return string without comma
     */
    private String removeComma(String str){
        StringBuffer newStr = new StringBuffer();
        for(int i=0;i<str.length();i++){
            if(str.charAt(i) != ',')
                newStr.append(str.charAt(i));
        }
        return newStr.toString();
    }

    /**
     * Check if the string need Special case
     * @param word
     * @param second
     * @return true / false
     */
    private boolean isNormalWord(String word, String second){
        if(isNumber(removeComma(word)))
            return false;
        if(word.charAt(0) == '$')
            return false;
        if(word.charAt(word.length()-1) == '%')
            return false;
        if(month.containsKey(word) && isNumber(second))
            return false;
        if(word.equalsIgnoreCase("Between"))
            return false;
        if(word.contains("-"))
            return false;
        if(word.contains("/"))
            return false;

//        if(word.contains("-"))
//            return false;
        return true;
    }

    /**
     * Term of Price
     * @param word
     * @param allTerm
     * @param isInteger
     */
    private void termPrice(String word, String [] allTerm, boolean isInteger){
        boolean aboveM = true;
        termBeforeChanged = new StringBuffer(word);
        switch (allTerm[1]) {
            case "m":
            case "million":
                termBeforeChanged.append(" M Dollars");
                break;
            case "bn":
            case "billion":
                if (isInteger) {
                    termBeforeChanged.append("000 M Dollars");
                }
                else {
                    int numD = (int)(Double.parseDouble(word)*1000);
                    termBeforeChanged.replace(0,allTerm[0].length(),Integer.toString(numD));
                    termBeforeChanged.append(" M Dollars");
                }
                break;
            case "trillion":
                if (isInteger) {
                    termBeforeChanged.append("000000 M Dollars");
                } else {
                    int numD = (int)(Double.parseDouble(word)*1000000);
                    termBeforeChanged.replace(0,allTerm[0].length(),Integer.toString(numD));
                    termBeforeChanged.append(" M Dollars");
                }
                break;
            case "Dollars" :
                if(word.contains("/") || Double.parseDouble(word)/1000000<1) {
                    aboveM = false;
                    termBeforeChanged.append(" Dollars");
                }
                else{
                    double numD = (Double.parseDouble(word)/1000000);
                    termBeforeChanged.replace(0,allTerm[0].length(),Double.toString(numD));
                    termBeforeChanged.append(" M Dollars");
                }
                break;

            default :
                aboveM = false;
                termBeforeChanged.append(" "+allTerm[1]+" Dollars");
                break;
        }

        if(aboveM)
            term = new PriceM(termBeforeChanged.toString());
        else
            term = new Price(termBeforeChanged.toString());
        increaseCounter(term);

    }
    private void oneTermPrice(String word, String oneTerm){
        boolean aboveM = true;
        termBeforeChanged = new StringBuffer(oneTerm);
        if(Double.parseDouble(word)/1000000<=1) {
            aboveM = false;
            termBeforeChanged.append(" Dollars");
        }
        else{
            double numD = (Double.parseDouble(word)/1000000);
            termBeforeChanged.replace(0,word.length(),Double.toString(numD));
            termBeforeChanged.append(" M Dollars");
        }
        if(aboveM)
            term = new PriceM(termBeforeChanged.toString());
        else
            term = new Price(termBeforeChanged.toString());
        increaseCounter(term);
    }

    /**
     * Term of Number
     * @param realword
     * @param termWords
     */
    private void termNumber(String realword, String[] termWords) {

        if(realword.contains("/"))
            term = new NumberK(realword);
        else {
            termBeforeChanged = new StringBuffer(realword);
            switch (termWords[1]) {
                case "thousand":
                case "Thousand": {
                    termBeforeChanged.append("K");
                    term = new NumberK(termBeforeChanged.toString());
                    break;
                }
                case "million":
                case "Million": {
                    termBeforeChanged.append("M");
                    term = new NumberM(termBeforeChanged.toString());
                    break;
                }
                case "billion":
                case "Billion": {
                    termBeforeChanged.append("B");
                    term = new NumberB(termBeforeChanged.toString());
                    break;
                }
                case "trillion":
                case "Trillion": {
                    termBeforeChanged.append("000B");
                    term = new NumberB(termBeforeChanged.toString());
                    break;
                }
                default:
                    termBeforeChanged.append(" " + termWords[1]);
                    term = new NumberU(termBeforeChanged.toString());
                    break;
            }
        }
        increaseCounter(term);
    }
    private void oneTermNumber(String word) {
        if(word.contains("/"))
            term = new NumberK(word);
        else {
            double numberWord = Double.parseDouble(word);
            // under 1K
            if (numberWord < 1000) {
                term = new NumberU(word);
            } else {
                // 1k - 1M
                if (numberWord < 1000000) {
                    numberWord = numberWord / 1000;
                    termBeforeChanged = new StringBuffer(cutDot0(numberWord) + "K");
                    term = new NumberK(termBeforeChanged.toString());

                } else {
                    // 1M - 1B
                    if (numberWord < 1000000000) {
                        numberWord = numberWord / 1000000;
                        termBeforeChanged = new StringBuffer(cutDot0(numberWord) + "M");
                        term = new NumberM(termBeforeChanged.toString());
                    }
                    // over 1B
                    else {
                        numberWord = numberWord / 1000000000;
                        termBeforeChanged = new StringBuffer(cutDot0(numberWord) + "B");
                        term = new NumberB(termBeforeChanged.toString());
                    }
                }
            }
        }
        increaseCounter(term);

    }

    /**
     * Term of Date
     * @param wordsTerm
     */
    private void termDate(String [] wordsTerm) {
        if(month.containsKey(wordsTerm[0])){ // first is month
            if(wordsTerm[1].length() > 2) { // year
                termBeforeChanged = new StringBuffer(wordsTerm[1]+"-"+month.get(wordsTerm[0]));
                term = new DateYear(termBeforeChanged.toString());
            }
            else{ // day
                if(wordsTerm[1].length()!= 1)
                    termBeforeChanged = new StringBuffer(month.get(wordsTerm[0])+"-"+wordsTerm[1]);
                else
                    termBeforeChanged = new StringBuffer(month.get(wordsTerm[0])+"-0"+wordsTerm[1]);
                term = new DateDay(termBeforeChanged.toString());
            }
        }
        else{ // day
            if(wordsTerm[1].length() == 1) {
                termBeforeChanged = new StringBuffer(month.get(wordsTerm[1])+"-0"+wordsTerm[0]);
            }else{
                termBeforeChanged = new StringBuffer(month.get(wordsTerm[1])+"-"+wordsTerm[0]);
            }
            term = new DateDay(termBeforeChanged.toString());
        }
        increaseCounter(term);
    }

    private boolean isTermNumber(String word){
        if (isNumber(word)) {
            if (word.contains(".")) {
                isInteger = false;
            }
            return true;
        }
        return false;
    }

    private void init(){
        isTNumber = false;
        isInteger = true;
        isTermPrice = false;
        isTermNumber = false;
        isTermPercent = false;
        isTermDate = false;
        found = false;
    }


    private void typeTerm(String [] termWords, String word){
        // if the term is date
        if (isTermDate) {
            termDate(termWords);
            return;
        }

        // if the term is price
        if (isTermPrice) {
            termPrice(word, termWords, isInteger);
            return;
        }

        // if the term is percent
        if (isTermPercent) {
            term = new Percent(termBeforeChanged.toString());
            increaseCounter(term);
            return;
        }
        // the term is number
        else
            termNumber(word, termWords);
    }
    private void oneWordTypeTerm(String word, String real) {

        // if the term is price
        if (isTermPrice) {
            oneTermPrice(word, real.substring(1));
            return;
        }

        // if the term is percent
        if (isTermPercent) {
            term = new Percent(real);
            increaseCounter(term);
            return;
        }
        //  the term is number
        if(isTNumber) {
            oneTermNumber(word);
            return;
        }
        // is normal string
        else // trash here
            term = new Word(real);
    }

    private void termFullDate(String word) {
        String[]fullDate = word.split("/");
        String day = fullDate[0];
        String month = fullDate[1];
        String year = fullDate[2];
        if(year.length()==2){
            year = "19"+year;
        }
        try{
            if(Integer.parseInt(month) > 12 && Integer.parseInt(month) <= 31 && Integer.parseInt(day) < 13){
                String tmp = month;
                month =day;
                day= tmp;
            }

            if(Integer.parseInt(month) < 13 && Integer.parseInt(day) <= 31){
                term = new DateDay(month+"-"+day);
                increaseCounter(term);
                term = new DateYear(year+"-"+month);
                increaseCounter(term);
                return;
            }


            else{
                term = new Word(word);
                increaseCounter(term);
            }

        }catch (Exception e){
            term = new Word(word);
            increaseCounter(term);
        }
    }

    private boolean isFullDate(String word) {
        if(word.length()<8)
            return false;
        int counter =0;
        for(int i=0;i<word.length();i++){
            if(counter<6 &&counter%3==2){
                if(word.charAt(i) != '/'){
                    return false;
                }
                counter++;
            }else{
                if(!Character.isDigit(word.charAt(i))){
                    return false;
                }
                counter++;
            }
        }
        return true;
    }


    private boolean betweenTerm(String num1String, String andString, String num2String){
        String num1 = cutSigns(num1String);
        String and = cutSigns(andString);
        String num2 = cutSigns(num2String);
        if(and.equals("and") && isNumber(removeComma(num1)) && isNumber(removeComma(num2))) {
            num1 = TermNumber(removeComma(num1));
            num2 = TermNumber(removeComma(num2));
            term = new Range(num1+"-"+num2);
            increaseCounter(term);
            return true;
        }
        return false;
    }


    public String transferNumberInRange(String number, String kind){
        boolean isTrillion = false;
        double firstNumber = Double.parseDouble(number);
        if(kind.equalsIgnoreCase("Thousand")){
            firstNumber*=1000;
        }
        if(kind.equalsIgnoreCase("Million")){
            firstNumber*=1000000;
        }
        if(kind.equalsIgnoreCase("Billion")){
            firstNumber*=1000000000;
        }
        if(kind.equalsIgnoreCase("Trillion")){
            firstNumber*=1000000000;
            isTrillion = true;
        }
        String finalRange = TermNumber(firstNumber+"");
        if(isTrillion){
            finalRange = finalRange.replace("B","000B");
            isTrillion = false;
        }
        return finalRange;
    }


    private int RangeTerm(String word, String word2, String minusWord){
        int addIndex = 0;

        String afterWord = cutSigns(word2);
        String beforeWord = cutSigns(minusWord);
        String beforeHypen = word.split("-")[0];
        String afterHypen = word.split("-")[1];

        // 1-2 Month -> 1 month, 2 month
        if (month.containsKey(afterWord) && isNumber(beforeHypen) && isNumber(afterHypen) ) {
            String[] first = {beforeHypen, afterWord};
            String[] second = {afterHypen, afterWord};
            termDate(first);
            termDate(second);
            term = new NumberU(beforeHypen);
            increaseCounter(term);
            term = new NumberU(afterHypen);
            increaseCounter(term);
            return 1+addIndex;
        }

        // equalIgnore
        if(isNumber(afterHypen) && (afterWord.equalsIgnoreCase("Thousand") || afterWord.equalsIgnoreCase("Million") || afterWord.equalsIgnoreCase("Billion") || afterWord.equalsIgnoreCase("Trillion"))){
            String numberRange = transferNumberInRange(afterHypen, afterWord);
            term=new Range(beforeHypen+"-"+numberRange);
            increaseCounter(term);
            return 1+addIndex;

        }
        if (isNumber(beforeHypen) && isNumber(afterHypen) && month.containsKey(beforeWord)) {
            String[] first = {beforeHypen, beforeWord};
            String[] second = {afterHypen, beforeWord};
            termDate(first);
            termDate(second);
            return addIndex;
        }
        if((beforeHypen.equalsIgnoreCase("Thousand") || beforeHypen.equalsIgnoreCase("Million") || beforeHypen.equalsIgnoreCase("Billion") || beforeHypen.equalsIgnoreCase("Trillion"))) {
            if(isNumber(beforeWord) && isNumber(afterHypen)){
                String[]numbers = {beforeWord,afterHypen};
                String[]kinds = {beforeHypen,afterWord};
                String[]finalRange = new String[2];
                for(int a=0;a<2;a++){
                    finalRange[a] = transferNumberInRange(numbers[a],kinds[a]);
                }
                term=new Range(finalRange[0]+"-"+finalRange[1]);
                increaseCounter(term);
                return 1+addIndex;
            }
            if(isNumber(removeComma(beforeWord))){
                oneTermNumber(removeComma(beforeWord));
                String finalRange = transferNumberInRange(removeComma(beforeWord),beforeHypen);
                term=new Range(finalRange+"-"+afterHypen);
                increaseCounter(term);
                return addIndex;
            }
            if(isNumber(removeComma(afterHypen))){
                oneTermNumber(removeComma(afterHypen));
                String finalRange = transferNumberInRange(removeComma(afterHypen),afterWord);
                term=new Range(finalRange+"-"+afterHypen);
                increaseCounter(term);
                return addIndex;
            }
            else{
                term = new Range(word);
                increaseCounter(term);
                return addIndex;
            }
        }
        if(word.startsWith("-") && !isNumber(removeComma(word.substring(1)))){
            if(!stopWords.contains(word.substring(1))){// trash here !
                stem.add(word.toCharArray(), word.length());
                stem.stem();
                term=new Word(word.substring(1));
                increaseCounter(term);
            }
            return addIndex;
        }
        /// here
        if(isNumber(removeComma(beforeHypen)) && isNumber(removeComma(afterHypen))) {
            beforeHypen = removeComma(beforeHypen);
            afterHypen = removeComma(afterHypen);
            oneTermNumber(beforeHypen);
            oneTermNumber(afterHypen);
            beforeHypen = TermNumber(beforeHypen);
            afterHypen = TermNumber(afterHypen);
            word = beforeHypen + "-" + afterHypen;
        }
        term = new Range(word);
        increaseCounter(term);
        return addIndex;
    }

    /**
     * The parser
     * @param allText
     * @param docName
     */
    public void parse(String [] allText, String docName,String cityName) {
        maxTermCounter = 0;
        counterMinTerm = 0;
        minTermCounter = 100;
        String commonTerm = "";
        wordsInDoc = new HashMap<>();


        /**
         * Check every words :
         *           1. cut all the signs in the first and the last character
         *           2. check if stopWords List contain the word
         *               2.1 if true, continue
         *               2.2 else, send to stemmer
         */
        for (int i = 0; i < allText.length; i++) {

            init(); // init all boolean variable

            String word = cutSigns(allText[i]); // cut the signs
            //String secondWord = cutSigns(allText[i+1]);

            if ((word.length() == 1 && !isNumber(word)))
                continue;


            if (!word.equals("") && (word.equals("Between") || !stopWords.contains(word.toLowerCase()))) {



                // regular text
                if (i + 1 < allText.length && isNormalWord(word, cutSigns(allText[i + 1]))) {

                    // stemmer
                    stem.add(word.toCharArray(), word.length());
                    stem.stem();
                    if(stem.toString().endsWith("'") || stem.toString().endsWith("`")) {
                        word = cutSigns(stem.toString());
                    }else{
                        word=stem.toString();
                    }
                    term = new Word(word);
                    increaseCounter(term);
                } else {
                    if (i == allText.length - 1) {
                        if (isNormalWord(word, "no")) {
                            stem.add(word.toCharArray(), word.length());
                            stem.stem();
                            term = new Word(word);
                            increaseCounter(term);
                            continue;
                        }
                    }

                    if ((i + 3 < allText.length && word.equals("Between"))) {
                        if(betweenTerm(allText[i+1],allText[i+2],allText[i+3]))
                            i=i+3;
                        continue;

                    }
                    if(((word.contains("-") && !word.endsWith("-") && i - 1 >= 0 && i+1 <allText.length))) {
                        int index = RangeTerm(word,allText[i+1],allText[i-1]);
                        i += index;
                        continue;
                    }


                    if (!isTermNumber(word)) {
                        word = removeComma(word);
                        isTermNumber(word);
                    }

                    // if word's first character is with "$"
                    // and then check if word is Number.
                    if (!isTNumber && word.charAt(0) == '$') {

                        allText[i] = allText[i].substring(1); // cut $
                        word = word.substring(1);
                        if (isTermNumber(word)) {
                            isTermPrice = true;
                        }
                        // the number is fraction
                        if (isTNumber && word.contains("/")) {
                            term = new Price(allText[i] + "Dollars");
                            increaseCounter(term);
                            continue;
                        }
                        // string that first character is "$"
                        if (!isTNumber) {
                            boolean flag = false;
                            for(int charW = 0; charW<word.length()-1;charW++){
                                if(signs.contains(word.charAt(charW)+"")&& !Character.isDigit(word.charAt(charW+1))){
                                    flag = true;
                                    break;
                                }
                            }
                            if(flag)
                                continue;


                            term = new Word('$' + word);
                            increaseCounter(term);
                            continue;
                        }
                    }

                    // if word's last character is "%"
                    // and then check if is number
                    if (!isTNumber && word.charAt(word.length() - 1) == '%') {
                        word = word.substring(0, word.length() - 1);
                        if (isTermNumber(word)) {
                            isTermPercent = true;
                        }
                        // if number is fraction
                        if (isTNumber && word.contains("/")) {
                            term = new Percent(word);
                            increaseCounter(term);
                            continue;
                        }
                        if (!isTNumber) {

                            boolean flag = false;
                            for(int charW = 0; charW<word.length()-1;charW++){
                                if(signs.contains(word.charAt(charW)+"")&& !Character.isDigit(word.charAt(charW+1))){
                                    flag = true;
                                    break;
                                }
                            }
                            if(flag)
                                continue;

                            term = new Word(word);
                            increaseCounter(term);
                            continue;
                        }
                    }
                    if(isFullDate(word)){
                        termFullDate(word);
                        continue;
                    }

                    int next = 0;
                    String nextWord = "";
                    if (i + 1 < allText.length) {
                        nextWord = cutSigns(allText[i + 1]);
                    }
                    while (i + 1 < allText.length && (afterNumber.contains(nextWord)) || (month.containsKey(word) && isNumber(nextWord)) || (isTNumber && month.containsKey(nextWord))) {
                        if (next == 2) {
                            if (!nextWord.equals("U.S.") && !nextWord.contains("/"))
                                break;
                        }
                        next++;
                        i = i + 1;
                        if (i + 1 < allText.length) {
                            nextWord = cutSigns(allText[i + 1]);
                        } else
                            break;
                    }
                    i = i - next;
                    if (next > 0 && i + next < allText.length) {
                        String[] termWords = new String[next + 1];
                        for (int j = 0; j < next + 1; j++) {
                            String wordTmp = cutSigns(allText[i + j]);
                            if ((wordTmp.equalsIgnoreCase("Dollars"))) {
                                isTermPrice = true;
                                found = true;
                            }
                            if ((wordTmp.equalsIgnoreCase("Thousand") || wordTmp.equalsIgnoreCase("Million") || wordTmp.equalsIgnoreCase("Billion") || wordTmp.equalsIgnoreCase("Trillion"))) {
                                isTermNumber = true;
                                found = true;
                            }
                            if (month.containsKey(wordTmp)) {
                                isTermDate = true;
                                found = true;
                            }
                            if (wordTmp.equals("percent") || wordTmp.equals("percentage")) {
                                isTermPercent = true;
                                found = true;
                                termBeforeChanged = new StringBuffer(word + "%");
                            }

                            termWords[j] = wordTmp;
                        }
                        typeTerm(termWords, word);
                    }
                    // the term is one word
                    else {
                        String another = cutSigns(allText[i]);
                        oneWordTypeTerm(word, another);
                    }
                    i = i + next;
                }
            }
        }

        for(ATerm term:wordsInDoc.keySet()){

            if(term instanceof Word ) {
                char c = term.finalName.charAt(0);
                int counterWord = 0;
                if (Character.isUpperCase(c)) {
                    String tp = Character.toLowerCase(c) + term.finalName.substring(1); // the term with lowerCase
                    ATerm a = new Word(tp);
                    if (allWordsDic.containsKey(a)) {
                        if (allWordsDic.get(a).get(docName) != null) {
                            //check hoe many times appeared in this doc
                            counterWord = wordsInDoc.get(term) + allWordsDic.get(a).get(docName);
                        } else
                            counterWord = wordsInDoc.get(term);
                        // max
                        if(maxTermCounter<counterWord){
                            maxTermCounter = counterWord;
                            commonTerm = term.finalName;
                        }
                        // min
                        if(minTermCounter>counterWord){
                            minTermCounter = counterWord;
                            counterMinTerm=0;
                            counterMinTerm++;
                        }

                        allWordsDic.get(a).put(docName, counterWord);
                    } else
                        checkIfExistsUpper(docName, term);
                } else {
                    String tp = term.finalName.toUpperCase();
                    ATerm a = new Word(tp);
                    if (allWordsDic.containsKey(a)) {
                        Map<String, Integer> p;
                        p = allWordsDic.get(a);
                        //maybe no need to remove just put
                        allWordsDic.remove(a);
                        //checkCapital(term.finalName);
                        allWordsDic.put(term, p);
                        //may not be needed if checking earlier
                        if (allWordsDic.get(term).get(docName) != null)
                            counterWord = wordsInDoc.get(term) + allWordsDic.get(term).get(docName);
                        else
                            counterWord = wordsInDoc.get(term);
                        // max
                        if(maxTermCounter<counterWord){
                            maxTermCounter = counterWord;
                            commonTerm = term.finalName;
                        }
                        // min
                        if(minTermCounter>counterWord){
                            minTermCounter = counterWord;
                            counterMinTerm=0;
                            counterMinTerm++;
                        }

                        allWordsDic.get(term).put(docName, counterWord);

                    } else {
                        checkIfExistsLower(docName, term);
                    }
                }
            }
            else{
                termMap = new HashMap<>();
                // max
                if(maxTermCounter<wordsInDoc.get(term)){
                    maxTermCounter = wordsInDoc.get(term);
                    commonTerm = term.finalName;
                }
                if(minTermCounter==wordsInDoc.get(term)){
                    counterMinTerm++;
                }
                // min
                if(minTermCounter>wordsInDoc.get(term)){
                    minTermCounter = wordsInDoc.get(term);
                    counterMinTerm=0;
                    counterMinTerm++;
                }

                //if do not exist
                termMap.put(docName, wordsInDoc.get(term));
                //checkCapital(term.finalName);
                allWordsDic.put(term, termMap);
            }
        }


        //// Finish
        docInfo.put(docName,maxTermCounter+","+wordsInDoc.size()+","+counterMinTerm+","+cityName);
        //post.writePerDoc(docName,cityName,wordsInDoc.size(),maxTermCounter,counterMinTerm);




    }

    private void checkIfExistsLower(String docName, ATerm termOld) {
        if (allWordsDic.containsKey(termOld)) {
            int counterWord = wordsInDoc.get(termOld);
            if(allWordsDic.get(termOld).get(docName)==null){
                // max
                if(maxTermCounter<counterWord){
                    maxTermCounter = counterWord;
                    commonTerm = term.finalName;
                }
                //min
                if(minTermCounter==counterWord){
                    counterMinTerm++;
                }
                if(minTermCounter>counterWord){
                    minTermCounter = counterWord;
                    counterMinTerm = 0;
                    counterMinTerm++;
                }

                allWordsDic.get(termOld).put(docName, counterWord);
            }else{
                counterWord = wordsInDoc.get(termOld)+allWordsDic.get(termOld).get(docName);

                if(maxTermCounter<counterWord){
                    maxTermCounter = counterWord;
                    commonTerm = term.finalName;
                }
                if(minTermCounter==counterWord){
                    counterMinTerm++;
                }
                if(minTermCounter>counterWord){
                    minTermCounter = counterWord;
                    counterMinTerm=0;
                    counterMinTerm++;
                }

                allWordsDic.get(termOld).put(docName, counterWord);
            }
        } else {
            termMap = new HashMap<>();

            if(maxTermCounter<wordsInDoc.get(termOld)){
                maxTermCounter = wordsInDoc.get(termOld);
                commonTerm = term.finalName;
            }
            if(minTermCounter==wordsInDoc.get(termOld)){
                counterMinTerm++;
            }
            if(minTermCounter>wordsInDoc.get(termOld)){
                minTermCounter = wordsInDoc.get(termOld);
                counterMinTerm=0;
                counterMinTerm++;
            }

            //if do not exist
            termMap.put(docName, wordsInDoc.get(termOld));
            //checkCapital(termOld.finalName);
            allWordsDic.put(termOld, termMap);
        }
    }


    private void checkIfExistsUpper(String docName, ATerm termOld) {
        ATerm termUp = new Word(termOld.finalName.toUpperCase());
        if (allWordsDic.containsKey(termUp)) {
            int counterWord = wordsInDoc.get(termOld);

            if(allWordsDic.get(termUp).get(docName)==null){

                if(maxTermCounter<wordsInDoc.get(termOld)){
                    maxTermCounter = wordsInDoc.get(termOld);
                    commonTerm = term.finalName;
                }
                if(minTermCounter==wordsInDoc.get(termOld)){
                    counterMinTerm++;
                }
                if(minTermCounter>wordsInDoc.get(termOld)){
                    minTermCounter = wordsInDoc.get(termOld);
                    counterMinTerm=0;
                    counterMinTerm++;
                }


                allWordsDic.get(termUp).put(docName, counterWord);
            }
            else {
                counterWord = wordsInDoc.get(termOld) + allWordsDic.get(termUp).get(docName);

                if(maxTermCounter<counterWord){
                    maxTermCounter = wordsInDoc.get(termOld);
                    commonTerm = term.finalName;
                }
                if(minTermCounter==counterWord){
                    counterMinTerm++;
                }
                if(minTermCounter>counterWord){
                    minTermCounter = wordsInDoc.get(termOld);
                    counterMinTerm=0;
                    counterMinTerm++;
                }

                allWordsDic.get(termUp).put(docName, counterWord);
            }
        } else {
            termMap = new HashMap<>();
            //if do not exist

            if(maxTermCounter<wordsInDoc.get(termOld)){
                maxTermCounter = wordsInDoc.get(termOld);
                commonTerm = term.finalName;
            }
            if(minTermCounter==wordsInDoc.get(termOld)){
                counterMinTerm++;
            }
            if(minTermCounter>wordsInDoc.get(termOld)){
                minTermCounter = wordsInDoc.get(termOld);
                counterMinTerm=0;
                counterMinTerm++;
            }

            termMap.put(docName, wordsInDoc.get(termOld));
            //checkCapital(termUp.finalName);
            allWordsDic.put(termUp, termMap);
        }
    }



    /**
     * Function which updates the counter of the term in the doc
     * in case the term doesn't exist, insert.
     * @param term
     */
    private void increaseCounter(ATerm term){

        if(term.finalName.equalsIgnoreCase("B")){
            int x=4;
        }

        if(wordsInDoc.containsKey(term)) {
            Integer tmp = wordsInDoc.get(term);
            wordsInDoc.put(term, tmp + 1);
        }else{
            wordsInDoc.put(term,1);
        }
        if(!allTerm.contains(term.finalName.toLowerCase())){
            allTerm.add(term.finalName.toLowerCase());
        }
    }

    private String cutSigns(String beforeCut) {
        int lengthBeforeWord = beforeCut.length();
        int startCharacter=0;
        int endCharacter=beforeCut.length();

        // -,$,%,a-z,A-Z,0-9
        for(int i=0;i<beforeCut.length()-1;i++){
            if(Character.isLetterOrDigit(beforeCut.charAt(i)) || ((beforeCut.charAt(i) == '-' || beforeCut.charAt(i) == '$') && Character.isDigit(beforeCut.charAt(i+1))) ){
                break;
            }
            else
                startCharacter++;
        }
        if(startCharacter!=0) {
            //System.out.println(beforeCut);
            beforeCut = beforeCut.substring(startCharacter);
            lengthBeforeWord = beforeCut.length();
            endCharacter=beforeCut.length();

        }//remove " 's "
        int startFromEnd = lengthBeforeWord;
        if(beforeCut.length()-2>=0 && ((beforeCut.charAt(beforeCut.length()-2) == '`' || (int)beforeCut.charAt(beforeCut.length()-2) == 39) && beforeCut.endsWith("s"))){
            endCharacter=beforeCut.length()-2;
            startFromEnd-=2;
        }
        for(int i=startFromEnd-1;i>0;i--){
            if(Character.isLetterOrDigit(beforeCut.charAt(i)) || (beforeCut.charAt(i) == '%'  && Character.isDigit(beforeCut.charAt(i-1))) || (beforeCut.charAt(i) == '.' && beforeCut.charAt(i-1) == 'S')){
                break;
            }
            else
                endCharacter--;
        }
        if(endCharacter!=lengthBeforeWord) {
            beforeCut = beforeCut.substring(0, endCharacter);
        }
        if(beforeCut.length()==1 && !Character.isLetterOrDigit(beforeCut.charAt(0))){
            beforeCut = "";
        }

        return beforeCut;
    }

    private String TermNumber(String word) {
        if(word.contains("/"))
            return word;
        double numberWord = Double.parseDouble(word);
        String numberInString;
        // under 1K
        if (numberWord < 1000) {
            return word;
        } else {
            // 1k - 1M
            if (numberWord < 1000000) {
                numberWord = numberWord / 1000;
                numberInString = cutDot0(numberWord);
                termBeforeChanged = new StringBuffer(numberInString + "K");
                return termBeforeChanged.toString();

            } else {
                // 1M - 1B
                if (numberWord < 1000000000) {
                    numberWord = numberWord / 1000000;
                    numberInString = cutDot0(numberWord);
                    termBeforeChanged = new StringBuffer(numberInString + "M");
                    return termBeforeChanged.toString();
                }
                // over 1B
                else {
                    numberWord = numberWord / 1000000000;
                    numberInString = cutDot0(numberWord);
                    termBeforeChanged = new StringBuffer(numberInString + "B");
                    return termBeforeChanged.toString();
                }
            }
        }
    }

    private String cutDot0(double number){
        String numberInString = number+"";
        String dot0 = numberInString.substring(numberInString.length()-2);
        if(dot0.equals(".0")){
            return numberInString.substring(0,numberInString.length()-2);
        }
        return String.valueOf(number);
    }

    private void checkCapital(String str){
        CountryInfo capitalTerm = countryInMemory.getCountryByCapital(str.toUpperCase());
        if(capitalTerm!=null){
            String pop =capitalTerm.getPopulation();
            String rightWord = TermNumber(pop);
            capitalTerms.put(capitalTerm, capitalTerm.getCountryName()+":"+capitalTerm.getCurrency()+":" + rightWord);
        }
    }


}

