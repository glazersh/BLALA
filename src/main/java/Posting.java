import Term.ATerm;
import sun.awt.Mutex;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Posting {

    private int numberOfFile = 1;
    Stack<File>theFiles;

    public Posting() {
        theFiles = new Stack<>();
    }

    public void createPostingFileFirstTime(Map<ATerm, Map<String,Integer>> words) {
        if(true) {
            File file = new File("C:\\Users\\glazersh\\IdeaProjects\\SearchEngineJ\\src\\main\\java\\postings\\post" + (numberOfFile++) + "");
            try {
                file.createNewFile();
                writeToFile(words, file);
                theFiles.add(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            File file2 = new File("C:\\Users\\glazersh\\IdeaProjects\\SearchEngineJ\\src\\main\\java\\postings\\post" + (numberOfFile++) + "");
            try {
                file2.createNewFile();
                writeToFile(words, file2);
                theFiles.add(file2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mergeFiles();
        }
    }

    private Stack<String> readFile(File file) {
        List<String> lines = new ArrayList<>();
        Stack<String> lineInStack = new Stack<>();

        try (GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(file));
             BufferedReader br = new BufferedReader(new InputStreamReader(gzip))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            Collections.sort(lines, Collections.reverseOrder());

            for(String lineInFile:lines){
                lineInStack.push(lineInFile);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace(System.err);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return lineInStack;


    }

    private void writeToFile(Map<ATerm,Map<String,Integer>> wordsInDictionary, File file) {
        StringBuffer wordsInfo = new StringBuffer();
        StringBuffer docNumber ;
        String docNameConst;
        for (ATerm term : wordsInDictionary.keySet()) {
            docNumber = new StringBuffer();
            docNameConst = wordsInDictionary.get(term).keySet().toString().split("-")[0].substring(1);
            for(String docName:wordsInDictionary.get(term).keySet()){
                String[]splitDoc = docName.split("-");
                if(splitDoc[0].equals(docNameConst)){
                    docNumber.append("["+splitDoc[1]+":"+wordsInDictionary.get(term).get(docName)+"]");
                }
                else {
                    docNumber.append("[" + docName + ":" + wordsInDictionary.get(term).get(docName) + "]");
                    docNameConst = wordsInDictionary.get(term).keySet().toString().split("-")[0].substring(1);
                }
            }
            wordsInfo.append(term.finalName + docNumber+"\n"); // # number of [ is doc frequency (df), idf

        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            try {
                //Writer writer = new OutputStreamWriter(new GZIPOutputStream(out), "UTF-8");
                Writer writer = new OutputStreamWriter(out);
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
        mixFile(theFiles.pop(), theFiles.pop());
    }

    private void mixFile(File file1, File file2) {
        Stack<String> lineFile1 = readFile(file1);
        Stack<String> lineFile2 = readFile(file2);

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
        File file = new File( "C:\\Users\\glazersh\\IdeaProjects\\SearchEngineJ\\src\\main\\java\\postings\\post" + (numberOfFile++) + "");
        try {
            file.createNewFile();
            file1.delete();
            file2.delete();
            FileOutputStream out = new FileOutputStream(file);
            try {
                Writer writer = new OutputStreamWriter(new GZIPOutputStream(out), "UTF-8");
                //Writer writer = new OutputStreamWriter(out);
                try {
                    writer.write(add.toString());
                    theFiles.add(file);
                }catch (IOException e) {
                    e.printStackTrace();
                }finally {
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


//            try {
//                out = new FileOutputStream(file);
//                Writer writer = new OutputStreamWriter(new GZIPOutputStream(out), "UTF-8");
//                writer.write(add.toString());
//                theFiles.add(file);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

    }


}




