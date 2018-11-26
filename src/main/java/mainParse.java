import Term.ATerm;
import Term.Percent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class mainParse {



    public static void main(String [] args){

        ParseUnit p = new ParseUnit();
    if(true) {

        String[]text = {"Between", "6-8"};
        p.parse(text,"dd");
        String[]text1 = {"bla", "dor", "Shula"};
        p.parse(text1,"cc");

        String[] price1 = {"1.7320", "Dollars"};
        String[] price2 = {"22", "3/4", "Dollars"};
        String[] price3 = {"$450,000"};
        String[] price4 = {"1,000,000", "Dollars"};
        String[] price5 = {"$450,000,000"};
        String[] price6 = {"$100", "million"};
        String[] price7 = {"20.6", "m", "Dollars"};
        String[] price8 = {"$100", "billion"};
        String[] price9 = {"100", "bn", "Dollars"};
        String[] price10 = {"100", "billion", "U.S.", "dollars"};
        String[] price11 = {"320", "million", "U.S.", "dollars"};
        String[] price12 = {"1", "trillion", "U.S.", "dollars"};

        String[][] allPrice = {price1, price2, price3, price4, price5, price6, price7, price8, price9, price10, price11, price12};
        System.out.println("~~~~~~ Price ~~~~~~");
        for (int i = 0; i < allPrice.length && true; i++) {
            p.parse(allPrice[i], "DD");
        }
        System.out.println();
    }


        String [] number1 = {"10,123"};
        String [] number2 = {"123","Thousand"};
        String [] number3 = {"1010.56"};
        String [] number4 = {"10,123,000"};
        String [] number5 = {"55","Million"};
        String [] number6 = {"10,123,000,000"};
        String [] number7 = {"55","Billion"};
        String [] number8 = {"7","Trillion"};
        String [] number9 = {"204"};
        String [] number10 = {"204","4/5"};

        String [][] allNumbers = {number1,number2,number3,number4,number5,number6,number7,number8, number9,number10};
        System.out.println("~~~~~~ Number ~~~~~~");
        for(int i=0;i<allNumbers.length && true;i++){
            p.parse(allNumbers[i], "DD");
        }


        String [] perc1 = {"6%"};
        String [] perc2 = {"10.6","percent"};
        String [] perc3 = {"10.6","percentage"};
        String [] perc12 = {"204"};
        String [] perc13 = {"204","4/5"};

        String [][] allPerc = {perc1,perc2,perc3,perc12,perc13};
        System.out.println("~~~~~~ Percent ~~~~~~");
        for(int i=0;i<allPerc.length && true;i++){
            p.parse(allPerc[i],"FF");
        }
        System.out.println();


        String [] price1 = {"1.7320", "Dollars"};
        String [] price2 = {"22", "3/4", "Dollars"};
        String [] price3 = {"$450,000"};
        String [] price4 = {"1,000,000", "Dollars"};
        String [] price5 = {"$450,000,000"};
        String [] price6 = {"$100","million"};
        String [] price7 = {"20.6","m","Dollars"};
        String [] price8 = {"$100", "billion"};
        String [] price9 = {"100", "bn" ,"Dollars"};
        String [] price10 = {"100", "billion", "U.S.", "dollars"};
        String [] price11 = {"320", "million", "U.S.", "dollars"};
        String [] price12 = {"1", "trillion", "U.S.", "dollars"};

        String [][] allPrice = {price1,price2,price3,price4,price5,price6,price7,price8,price9,price10,price11,price12};
        System.out.println("~~~~~~ Price ~~~~~~");

        System.out.println();

        String [] date1 = {"14","MAY"};
        String [] date2 = {"14","May"};
        String [] date3 = {"June","4"};
        String [] date4 = {"JUNE","4"};
        String [] date5 = {"May","1994"};
        String [] date6 = {"MAY","1994"};

        String [][] allDate = {date1,date2,date3,date4,date5,date6};
        System.out.println("~~~~~~ Date ~~~~~~");
        for(int i=0;i<allDate.length && true;i++){
            p.parse(allDate[i], "DD");
        }
        System.out.println();

        String [] hyphen1 = {"Value-added"};
        String [] hyphen2 = {"step-by-step"};


        //String [] words = {"1,400"};
        //p.parse(words);
        System.out.println("~~~~~~~~");

        System.out.println();
    }




}