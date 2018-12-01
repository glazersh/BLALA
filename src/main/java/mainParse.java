import Term.ATerm;
import Term.Percent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class mainParse {



    public static void main(String [] args) {

        ParseUnit p = new ParseUnit();
        String[] words = {"d","d,","01/02/1994","d","d"};
        p.parse(words,"dd","d");
        String word = "speakerss";
        String wordUp = "SPEAKERS";
        int compre = word.compareToIgnoreCase(wordUp);
        char d = word.charAt(1);
        System.out.println((int)d);
    }


}