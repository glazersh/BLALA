import Term.ATerm;
import sun.awt.Mutex;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Posting {

    private int numberOfFile = 1;
    Queue<File>theFiles;
    Queue<File> merge1;

    public Posting() {
        theFiles = new LinkedList<>();
        merge1 = new LinkedList<>();
    }

    public void createPostingFileFirstTime(Map<ATerm, Map<String,Integer>> words) {
        if(true) {
            SortedMap<ATerm,Map<String,Integer>> treeDict = new TreeMap(new Comparator<ATerm>() {
                @Override
                public int compare(ATerm o1, ATerm o2) {
                    return compare(o1.finalName,o2.finalName);
                }

                private int compare(String finalName, String finalName1) {
                    return finalName.compareTo(finalName1);
                }
            });
            for(ATerm term:words.keySet()){
                treeDict.put(term,words.get(term));
            }
            File file = new File("D:\\documents\\users\\dorlev\\Downloads\\SearchEngineJ\\src\\main\\java\\posting\\" + (numberOfFile++) + "");
            try {
                file.createNewFile();
                writeToFile(treeDict, file);
                theFiles.add(file);
                if(theFiles.size()==8){
                    while(theFiles.size()!=0) {
                        //Thread t = new Thread(()->mergeFiles(theFiles.poll(),theFiles.poll()));
                        //t.start();
                        mergeFiles(theFiles.poll(), theFiles.poll());
                    }
                    while(merge1.size()!=1){
                        //Thread t = new Thread(()->mergeFiles(merge1.poll(),merge1.poll()));
                        //t.start();
                        mergeFiles(merge1.poll(),merge1.poll());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Queue<String> readFile(File file) {
        List<String> lines = new ArrayList<>();
        Queue<String> lineInQueue = new LinkedList<>();

        try (//GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(file));
             FileInputStream out = new FileInputStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(out))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            //Collections.sort(lines, Collections.reverseOrder());

            for(String lineInFile:lines){
                lineInQueue.add(lineInFile);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace(System.err);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return lineInQueue;


    }

    private void writeToFile(Map<ATerm,Map<String,Integer>> wordsInDictionary, File file) {
        StringBuffer wordsInfo = new StringBuffer();
        StringBuffer docNumber ;
        for (ATerm term : wordsInDictionary.keySet()) {
            docNumber = new StringBuffer();
            for(String docName:wordsInDictionary.get(term).keySet()){
                docNumber.append("["+docName+":"+wordsInDictionary.get(term).get(docName)+"]");
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

    private void mergeFiles(File file1, File file2) {
        Queue<String> lineFile1 = readFile(file1);
        Queue<String> lineFile2 = readFile(file2);

        StringBuffer add = new StringBuffer("");
        String l1="";
        String l2="";

        if(lineFile1.size()!=0 && lineFile2.size()!=0) {
            l1 = lineFile1.poll();
            l2 = lineFile2.poll();
        }

        String termL1 = l1.split(Pattern.quote("["))[0];
        String termL2 = l2.split(Pattern.quote("["))[0];

        while (lineFile1.size()!=0 && lineFile2.size()!=0) {
            int check = termL1.compareTo(termL2);
            if(termL1.equals("special") || termL2.equals("special")){
                int x=4;
            }
            if (check == 0) {
                add.append(l1);
                add.append(l2.substring(termL2.length()) + "\n");

                // merge
                l1 = lineFile1.poll();
                l2 = lineFile2.poll();

                termL1 = l1.split(Pattern.quote("["))[0];
                termL2 = l2.split(Pattern.quote("["))[0];
                continue;
            }
            if (check > 0) {
                add.append(l2 + "\n");
                l2 = lineFile2.poll();
                termL2 = l2.split(Pattern.quote("["))[0];
            } else {
                add.append(l1 + "\n");
                l1 = lineFile1.poll();
                termL1 = l1.split(Pattern.quote("["))[0];

            }
        }
        while (lineFile1.size()!=0) {
            add.append(lineFile1.poll() + "\n");
        }
        while (lineFile2.size()!=0) {
            add.append(lineFile2.poll() + "\n");
        }
        File file = new File( "D:\\documents\\users\\dorlev\\Downloads\\SearchEngineJ\\src\\main\\java\\posting\\" + (numberOfFile++) + "");
        try {
            file.createNewFile();
            file1.delete();
            file2.delete();
            merge1.add(file);
            FileOutputStream out = new FileOutputStream(file);
            try {
                Writer writer = new OutputStreamWriter(out);
                //Writer writer = new OutputStreamWriter(new GZIPOutputStream(out), "UTF-8");
                //Writer writer = new OutputStreamWriter(out);
                try {
                    writer.write(add.toString());
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


    }


}




