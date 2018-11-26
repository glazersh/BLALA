import Term.ATerm;
import sun.awt.Mutex;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Posting {

    private int numberOfFile = 1;
    Queue<File> allfiles = new LinkedList<>();
    Queue<File> allfiles2 = new LinkedList<>();

    public Posting() {
    }

    public void createPostingFileFirstTime(String docName, Map<ATerm, Map<String,Integer>> words) {
        File file = new File("C:\\Users\\USER\\Desktop\\מערכות מידע דור\\סמסטר ד\\נושאים מתקדמים בתכנות\\SearchEngineJ\\src\\main\\java\\postings" + "\\postFile" + (numberOfFile++) + "");
        try {
            file.createNewFile();
            writeToFile(words, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readFile(File file) {
        List<String> lines = new ArrayList<>();

        try (GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(file));
             BufferedReader br = new BufferedReader(new InputStreamReader(gzip))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

            Collections.sort(lines);
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.err);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return lines;


    }

    private void writeToFile(Map<ATerm,Map<String,Integer>> wordsInDictionary, File file) {
        StringBuffer wordsInfo = new StringBuffer();
        StringBuffer docNumber ;
        for (ATerm term : wordsInDictionary.keySet()) {
            docNumber = new StringBuffer();
            for(String docName:wordsInDictionary.get(term).keySet()){
                docNumber.append("["+docName+"-"+wordsInDictionary.get(term).get(docName)+"]");
            }
            wordsInfo.append(term.finalName + docNumber+"\n"); // # number of [ is doc frequency (df), idf

        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            try {
                Writer writer = new OutputStreamWriter(new GZIPOutputStream(out), "UTF-8");
                try {
                    writer.write(wordsInfo.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    writer.close();
                }
            } finally {
                out.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void mergeFiles() {
        while(allfiles.size()!=2){
            File file1 = allfiles.poll();
            File file2 = allfiles.poll();
            mixFile(file1, file2);
        }

    }

    private void mixFile(File file1, File file2) {
        Stack<String> lineFile1 = getLines(file1);
        Stack<String> lineFile2 = getLines(file2);
        StringBuffer add = new StringBuffer("");
        String l1="";
        String l2="";

        if(!lineFile1.empty() && !lineFile2.empty()) {
            l1 = lineFile1.pop();
            l2 = lineFile2.pop();
        }


        String termL1 = l1.split(Pattern.quote("["))[0];
        String termL2 = l2.split(Pattern.quote("["))[0];
        while (!lineFile1.empty() && !lineFile2.empty()) {


            int check = termL1.compareTo(termL2);

            if(termL1.equals("01-4")) {
                int x = 3;
            }


            if (check == 0) {

                add.append(l1);
                add.append(l2.substring(termL2.length()) + "\n");

                // merge
                l1 = lineFile1.pop();
                l2 = lineFile2.pop();

                termL1 = l1.split(Pattern.quote("["))[0];
                termL2 = l2.split(Pattern.quote("["))[0];
                continue;
            }
            if (check > 0) {

                add.append(l2 + "\n");


                l2 = lineFile2.pop();
                termL2 = l2.split(Pattern.quote("["))[0];

            } else {

                add.append(l1 + "\n");

                l1 = lineFile1.pop();
                termL1 = l1.split(Pattern.quote("["))[0];

            }
        }
        while (!lineFile1.empty()) {
            add.append(lineFile1.pop() + "\n");
        }
        while (!lineFile2.empty()) {
            add.append(lineFile2.pop() + "\n");
        }

        File file = new File("C:\\Users\\USER\\Desktop\\מערכות מידע דור\\סמסטר ד\\נושאים מתקדמים בתכנות\\SearchEngineJ\\src\\main\\java\\postings" + "\\postFile" + (numberOfFile++) + "");
        try {
            file.createNewFile();
            allfiles.add(file);
            if(allfiles2.size()>=2){
                mixFile(allfiles2.poll(),allfiles2.poll());
            }
            file1.delete();
            file2.delete();
            FileWriter writer = new FileWriter(file);
            writer.write(add.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Stack<String> getLines(File file) {
        BufferedReader reader = null;

        //Create an ArrayList object to hold the lines of input file

        ArrayList<String> lines = new ArrayList<String>();
        Stack<String> sortFile = new Stack<>();

        try {
            //Creating BufferedReader object to read the input file

            reader = new BufferedReader(new FileReader(file.getPath()));

            //Reading all the lines of input file one by one and adding them into ArrayList

            String currentLine = reader.readLine();

            while (currentLine != null) {
                lines.add(currentLine);

                currentLine = reader.readLine();
            }

            //Sorting the ArrayList

            Collections.sort(lines, Collections.reverseOrder());
            for (String line : lines) {
                sortFile.push(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Closing the resources
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sortFile;
    }


}




