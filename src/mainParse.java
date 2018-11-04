public class mainParse {



    public static void main(String [] args){

        ParseUnit p = new ParseUnit();

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

        for(int i=0;i<allNumbers.length && true;i++){
            p.kindOfNumber(allNumbers[i]);
        }

        String [] perc1 = {"6%"};
        String [] perc2 = {"10.6","percent"};
        String [] perc3 = {"10.6","percentage"};

        String [][] allPerc = {perc1,perc2,perc3};

        for(int i=0;i<allPerc.length && true;i++){
            p.kindOfNumber(allPerc[i]);
        }

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

        for(int i=0;i<allPrice.length && true;i++){
            p.kindOfNumber(allPrice[i]);
        }

    }




}