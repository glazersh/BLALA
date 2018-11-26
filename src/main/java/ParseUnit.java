import Term.*;
import com.sun.xml.internal.ws.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

import static jdk.nashorn.internal.runtime.JSType.isNumber;

public class ParseUnit {

    Posting post = new Posting();
    Stemmer stem = new Stemmer();


    Map<String,String> month= new HashMap<>();
    HashSet<String> afterNumber = new HashSet<>();

    Set<String> stopWords = new HashSet<>();
    Set<Character>signs = new HashSet<>();

    Map<ATerm,Map<String,Integer>> allWordsDic = new HashMap<>();


    ATerm term;
    StringBuffer termBeforeChanged;

    Map<ATerm,Integer>wordsInDoc;


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


    /**
     * Check if the string is number
     * @param str
     * @return true / false
     */
    private boolean isNumber(String str) {
        if (str == null) {
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
                if(counter==0 &&(c=='.' || c=='/'))
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
        if(month.containsKey(word) && isNumber(second) )
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
                case "Thousand": {
                    termBeforeChanged.append("K");
                    term = new NumberK(termBeforeChanged.toString());
                    break;
                }
                case "Million": {
                    termBeforeChanged.append("M");
                    term = new NumberM(termBeforeChanged.toString());
                    break;
                }
                case "Billion": {
                    termBeforeChanged.append("B");
                    term = new NumberB(termBeforeChanged.toString());
                    break;
                }
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
                    termBeforeChanged = new StringBuffer(numberWord + "K");
                    term = new NumberK(termBeforeChanged.toString());

                } else {
                    // 1M - 1B
                    if (numberWord < 1000000000) {
                        numberWord = numberWord / 1000000;
                        termBeforeChanged = new StringBuffer(numberWord + "M");
                        term = new NumberM(termBeforeChanged.toString());
                    }
                    // over 1B
                    else {
                        numberWord = numberWord / 1000000000;
                        termBeforeChanged = new StringBuffer(numberWord + "B");
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
        else
            term = new Word(real);
    }

    /**
     * The parser
     * @param allText
     * @param docName
     */
    public void parse(String [] allText, String docName) {

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

            if ((i + 3 < allText.length && word.equals("Between")) || ((word.contains("-") && !word.endsWith("-") && i - 1 >= 0 && i+1 <allText.length))) {
                if(word.equals("Between")){
                        String num1 = cutSigns(allText[i+1]);
                        String and = cutSigns(allText[i+2]);
                        String num2 = cutSigns(allText[i+3]);
                    if(isNumber(num1) && isNumber(num2) && and.equals("and")) {
                        term = new Range(num1+"-"+num2);
                        i=i+3;
                        increaseCounter(term);
                        continue;
                    }
                    continue;
                }
                word = word;
                String secondWord = cutSigns(allText[i + 1]);
                String beforeWord = cutSigns(allText[i - 1]);
                if (month.containsKey(secondWord)) {
                    String[] first = {word.split("-")[0], secondWord};
                    String[] second = {word.split("-")[1], secondWord};
                    termDate(first);
                    termDate(second);
                    i = i + 1;
                    continue;

                }
                if (month.containsKey(beforeWord)) {
                    boolean ismonth = true;
                    continue;
                } else {
                    term = new Range(word);
                    increaseCounter(term);
                    continue;
                }

            }

            if ((word.length() == 1 && !isNumber(word)))
                continue;

            if (!word.equals("") && (word.equals("Between") || !stopWords.contains(word.toLowerCase()))) {

                // regular text
                if (i + 1 < allText.length && isNormalWord(word, cutSigns(allText[i + 1]))) {
                    // stemmer
                    stem.add(word.toCharArray(), word.length());
                    stem.stem();
                    term = new Word(stem.toString());
                    increaseCounter(term);
                } else {
                    if (i == allText.length - 1) {
                        if (isNormalWord(word, "no")) {
                            term = new Word(word);
                            increaseCounter(term);
                            continue;
                        }
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
                            term = new Word('$' + allText[i]);
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
                            term = new Price(allText[i]);
                            increaseCounter(term);
                            continue;
                        }
                        if (!isTNumber) {
                            term = new Word(allText[i]);
                            increaseCounter(term);
                            continue;
                        }
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
                            if (!found) {

                                if ((wordTmp.equals("Dollars") || wordTmp.equals("dollars"))) {
                                    isTermPrice = true;
                                    found = true;
                                }
                                if ((wordTmp.equals("Thousand") || wordTmp.equals("Million") || wordTmp.equals("Billion") || wordTmp.equals("Trillion"))) {
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
                            }
                            termWords[j] = wordTmp;
                        }
                        typeTerm(termWords, word);
                    }
                    // the term is one word
                    else {
                        oneWordTypeTerm(word, cutSigns(allText[i]));
                    }
                    i = i + next;
                }
            }
        }
        Map<String,Integer> termMap = new HashMap<>();
        for(ATerm term:wordsInDoc.keySet()){
            int counter = wordsInDoc.get(term);
            if(allWordsDic.containsKey(term)) {
                allWordsDic.get(term).put(docName, counter);
            }
            else{
                termMap.put(docName,wordsInDoc.get(term));
                allWordsDic.put(term,termMap);
            }
        }
        //Thread t = new Thread(()->post.createPostingFileFirstTime(docName,wordsInDoc));
        //t.start();

        //post.createPostingFileFirstTime(docName, wordsInDoc);


    }

/*
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
                        allWordsDic.put(term, p);
                        //may not be needed if checking earlier
                        if (allWordsDic.get(term).get(docName) != null)
                            counterWord = wordsInDoc.get(term) + allWordsDic.get(term).get(docName);
                        else
                            counterWord = wordsInDoc.get(term);
                        allWordsDic.get(term).put(docName, counterWord);

                    } else {
                        checkIfExistsLower(docName, term);
                    }
                }
            }
            else{
                termMap = new HashMap<>();
                //if do not exist
                termMap.put(docName, wordsInDoc.get(term));
                allWordsDic.put(term, termMap);
            }
        }


    }

    private void checkIfExistsLower(String docName, ATerm termOld) {
        if (allWordsDic.containsKey(termOld)) {
            int counterWord = wordsInDoc.get(termOld);
            allWordsDic.get(termOld).put(docName, counterWord);
        } else {
            termMap = new HashMap<>();
            //if do not exist
            termMap.put(docName, wordsInDoc.get(termOld));
            allWordsDic.put(termOld, termMap);
        }
    }


    private void checkIfExistsUpper(String docName, ATerm termOld) {
        ATerm termUp = new Word(termOld.finalName.toUpperCase());
        if (allWordsDic.containsKey(termOld)) {
            int counterWord = wordsInDoc.get(termOld);
            allWordsDic.get(termOld).put(docName, counterWord);
        } else {
            termMap = new HashMap<>();
            //if do not exist
            termMap.put(docName, wordsInDoc.get(termOld));
            allWordsDic.put(termUp, termMap);
        }
    }
*/


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

    private String cutSigns(String beforeCut) {
        while(!beforeCut.equals("") && signs.contains(beforeCut.charAt(0))) {
            beforeCut = beforeCut.substring(1);
        }
        while(!beforeCut.equals("") && signs.contains(beforeCut.charAt(beforeCut.length()-1))) {
            beforeCut = beforeCut.substring(0,beforeCut.length()-1);
        }
        return beforeCut;
    }


}

