package Term;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public abstract class  ANumber extends ATerm {

    public ANumber(String words){
        super(words);
        /*
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
                            finalName = words[0];
                            System.out.println(numInDouble);
                            return;
                        }
                        if(numInDouble>999 && numInDouble < 1000000) {
                            finalName = numInDouble / 1000 + "K";
                            System.out.println(numInDouble / 1000 + "K");
                            return;
                        }
                        if(numInDouble>999999 && numInDouble < 1000000000) {
                            finalName = numInDouble / 1000000 + "M";
                            System.out.println(numInDouble / 1000000 + "M");
                            return;
                        }
                        if(numInDouble>999999999) {
                            finalName = numInDouble / 1000000000 + "B";
                            System.out.println(numInDouble / 1000000000 + "B");
                            return;
                        }
                    }
                    else {
                        numInInt = Integer.parseInt(numberInString);

                        if (numInInt < 1000) {
                            finalName = words[0];
                            System.out.println(numInInt);
                            return;
                        }
                        if (numInInt > 999 && numInInt < 1000000) {
                            if(numInInt % 1000 == 0) {
                                finalName = numInInt / 1000 + "K";
                                System.out.println(numInInt / 1000 + "K");
                            }
                            else {
                                System.out.println((double) numInInt / 1000 + "K");
                                finalName = (double)numInInt / 1000 + "K";
                            }
                            return;
                        }
                        if (numInInt > 999999 && numInInt < 1000000000) {
                            if(numInInt % 1000000 == 0) {
                                finalName = numInInt / 1000000 + "M";
                                System.out.println(numInInt / 1000000 + "M");
                            }
                            else {
                                finalName = (double)numInInt / 1000000 + "M";
                                System.out.println((double) numInInt / 1000000 + "M");
                            }
                            return;
                        }
                        if (numInInt > 999999999) {
                            if(numInInt % 1000000000 == 0) {
                                finalName = numInInt / 1000000000 + "B";
                                System.out.println(numInInt / 1000000000 + "B");
                            }
                            else {
                                finalName = (double)numInInt / 1000000000 + "B";
                                System.out.println((double) numInInt / 1000000000 + "B");
                            }
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
                    finalName = bigNumber+"."+after+"B";
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
            if (words[1].equals("Thousand")) {
                finalName = number + "K";
                System.out.println(number + "K");
                return;
            }
            if (words[1].equals("Million")) {
                finalName = number + "M";
                System.out.println(number + "M");
                return;
            }
            if (words[1].equals("Billion")) {
                finalName = number + "B";
                System.out.println(number + "B");
                return;
            }
            if (words[1].equals("Trillion")) {
                finalName =number * 1000 + "B";
                System.out.println(number * 1000 + "B");
                return;
            }
            // fraction
            finalName = words[0] + " " + words[1];
            System.out.println(words[0] + " " + words[1]);
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
                    finalName = (double)number/1000 + "K";
                    System.out.println(((double)number/1000) + "K");
                }
                else{
                    finalName = number/1000 + "K";
                    System.out.println((number/1000) + "K");
                }
            }
            if(number > 999999 && number < 1000000000){
                if ( number %1000000 !=0 ){
                    finalName = ((double)number/1000000) + "M";
                    System.out.println(((double)number/1000000) + "M");
                }
                else{
                    finalName = (number/1000000) + "M";
                    System.out.println((number/1000000) + "M");
                }
            }
            if(number > 999999999){
                if ( number %1000000000 !=0 ){
                    finalName = ((double)number/1000000000) + "B";
                    System.out.println(((double)number/1000000000) + "B");
                }
                else{
                    finalName = (number/1000000000) + "B";
                    System.out.println((number/1000000000) + "B");
                }
            }
        }
        return "";
        */
    }
}
