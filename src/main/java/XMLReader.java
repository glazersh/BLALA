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


public class XMLReader {
    public static void main(String[]args){

        File file = new File("C:\\Users\\USER\\Desktop\\מערכות מידע דור\\סמסטר ד\\נושאים מתקדמים בתכנות\\SearchEngineJ\\src\\main\\resources\\FB396001");
        try {
            FileInputStream fis = new FileInputStream(file);
            Document doc = Jsoup.parse(new String(Files.readAllBytes(file.toPath())));
            Elements elements = doc.getElementsByTag("DOC");
            for (Element element : elements){
                String name = element.getElementsByTag("TEXT").text();
                String name2 = element.getElementsByTag("DOCNO").text();
                System.out.println(name);
                System.out.println(name2);
            }

            /*
            long start = System.currentTimeMillis();


            for(Element e: doc.select("TEXT")){
                String tmp = e.childNode(0).toString();
                String [] tmp2 = tmp.split(" ");
                for(int i=0;i<tmp2.length;i++) {

                }
            }
            System.out.println();
            long elapsedTimeMillis = System.currentTimeMillis()-start;
            float elapsedTimeSec = elapsedTimeMillis/1000F;
            System.out.println(elapsedTimeSec);
            */
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
