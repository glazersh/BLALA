import Term.ATerm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Posting {


    public void createPostingFile(){
        File file = new File("C:\\Users\\USER\\Desktop\\מערכות מידע דור\\סמסטר ד\\נושאים מתקדמים בתכנות\\SearchEngineJ\\src\\main\\java\\postings"+"\\postFile"+1+"");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(Map<ATerm,Integer>wordsInDictionary, String docName, File file){
        StringBuffer wordsInfo = new StringBuffer();
        for(ATerm term:wordsInDictionary.keySet()){
            wordsInfo.append(term.finalName+"["+docName+"-"+wordsInDictionary.get(term)+"]\n"); // # number of [ is doc frequency (df), idf
        }
        try {
            FileWriter writer = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
