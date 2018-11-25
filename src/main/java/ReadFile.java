import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

    ParseUnit Parse = new ParseUnit();

    public ReadFile(String path) {
        List<File> allFiles = null;
        //int counter =0;
        try {
            // Read all files from path
            allFiles = Files.walk(Paths.get(path)).
                    filter(Files::isRegularFile).
                    map(Path::toFile).
                    collect(Collectors.toList());

            for (File file : allFiles) {
                //System.out.println(file.getName());
                try {
                    FileInputStream fis = new FileInputStream(file);
                    Document doc = Jsoup.parse(new String(Files.readAllBytes(file.toPath())));
                    Elements elements = doc.getElementsByTag("DOC");

                    // For every doc in the file
                    // Cut all the string from <TEXT> until </TEXT>
                    // Send it to ParseUnit
                    for (Element element : elements) {
                        if(true) {
                            String docText = element.getElementsByTag("TEXT").text();
                            String docName = element.getElementsByTag("DOCNO").text();
                            String[] withoutSpaceText = docText.split(" "); // split the text by " "(space) into array
                            //System.out.println("~~~~~" + docName + "~~~~~~");
                            Parse.parse(withoutSpaceText, docName);

                        }
                        else{ // for debug
                            int doNothing;
                        }
                    }
                    //break;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            int x= 4;
        } catch (IOException e) { }
    }



    public static void main(String[]args) {
        long start = System.nanoTime();
        //ReadFile rf = new ReadFile("C:\\Users\\USER\\Desktop\\search2018\\corpus\\FB496139");

        ReadFile rf = new ReadFile("C:\\Users\\USER\\Desktop\\search2018\\corpus");
        //rf.p.printDic();
        long finish = System.nanoTime();
        System.out.println(finish-start);
    }


}
