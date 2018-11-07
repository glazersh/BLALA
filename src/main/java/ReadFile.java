import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ReadFile {
    ParseUnit p = new ParseUnit();

    public void separateDoc(){
    File file = new File("C:\\Users\\dorlev\\IdeaProjects\\SearchEngineJ\\src\\main\\resources\\FB396001");
        try {
        FileInputStream fis = new FileInputStream(file);
        Document doc = Jsoup.parse(new String(Files.readAllBytes(file.toPath())));
        Elements elements = doc.getElementsByTag("DOC");
        for (Element element : elements){
            String docText = element.getElementsByTag("TEXT").text();
            String docName = element.getElementsByTag("DOCNO").text();
            String [] withoutSpace = docText.split(" ");
            // remove stop words
            // stemming
            System.out.println("~~~~~"+docName+"~~~~~~");
            p.parse(withoutSpace);

        }

    }
      catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

}


    public static void main(String[]args) {
        ReadFile rf = new ReadFile();
        rf.separateDoc();
    }


}
