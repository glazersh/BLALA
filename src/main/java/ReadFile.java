import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ReadFile {
    ParseUnit p = new ParseUnit();

    public ReadFile(String path) {
        List<File> allFiles = null;
        int counter =0;
        try {
            allFiles = Files.walk(Paths.get(path)).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
            for (File file : allFiles) {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    Document doc = Jsoup.parse(new String(Files.readAllBytes(file.toPath())));
                    Elements elements = doc.getElementsByTag("DOC");
                    for (Element element : elements) {
                        if(counter== 0 ) {
                            String docText = element.getElementsByTag("TEXT").text();
                            String docName = element.getElementsByTag("DOCNO").text();
                            String[] withoutSpace = docText.split(" ");
                            // remove stop words
                            // stemming
                            System.out.println("~~~~~" + docName + "~~~~~~");
                            p.parse(withoutSpace);
                            counter++;
                        }
                        else{
                            break;
                            //counter++;
                        }
                    }
                    p.printDic();
                    break;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) { }
    }



    public static void main(String[]args) {
        //ReadFile rf = new ReadFile("C:\\Users\\USER\\Desktop\\search2018\\corpus\\FB396065");
        long start = System.nanoTime();
        ReadFile rf = new ReadFile("C:\\Users\\USER\\Desktop\\search2018\\corpus");
        //rf.p.printDic();
        long finish = System.nanoTime();
        System.out.println(finish-start);
    }


}
