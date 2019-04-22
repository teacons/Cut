package cut;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Cut {
    private String inFile;
    private String outFile;
    private boolean symOrWord;
    private String range;


    Cut(String inFile, String outFile, boolean symOrWord, String range) {
        this.inFile = inFile;
        this.outFile = outFile;
        this.symOrWord = symOrWord;
        this.range = range;
    }

     private List<Integer> rangePrep(String range) {
        if (!range.matches("([0-9\\-])+")) throw new IllegalArgumentException();
        int indOfSep = range.indexOf("-");
        List<Integer> list = new ArrayList<>();
        if (indOfSep == 0) list.add(0);
        else list.add(Integer.parseInt(range.substring(0, indOfSep)));
        if (indOfSep == range.length() - 1) list.add(range.length() - 1);
        else list.add(Integer.parseInt(range.substring(indOfSep + 1)));
        if (list.get(1) < list.get(0)) throw new IllegalArgumentException("Второе значение не может быть меньше первого");
        return list;
    }

    void cutPrep() {
        List<String> list = new ArrayList<>();
        if (inFile != null)
            try{
                FileInputStream file = new FileInputStream(inFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(file));
                String strLine;
                while ((strLine = br.readLine()) != null){
                    list.add(strLine);
                }
            }catch (IOException e){
                System.out.println("Ошибка с чтением файла");
            }
        else {
            Scanner in = new Scanner(new InputStreamReader(System.in));
            System.out.print("Введите текст: ");
            String temp;
            while (in.hasNext()) {
                temp = in.nextLine();
                if (temp.equals("end")) break;
                list.add(temp);
            }
            in.close();
        }
        List<Integer> rangeInt = rangePrep(range);
        StringBuilder builder = new StringBuilder();
        String sep = "";
        for (String s : list) {
            builder.append(sep);
            sep = "\n";
            builder.append(cut(s, rangeInt, symOrWord));
        }
        if (outFile == null) System.out.println(builder.toString());
        else writer(builder.toString());
    }

    private String cut(String line, List<Integer> rangeInt, boolean symOrWord) {
        String result;
        String linePrep = line.trim();
        if (!symOrWord) {
            if (rangeInt.get(1) < linePrep.length()) result = linePrep.substring(rangeInt.get(0), rangeInt.get(1));
            else result = linePrep.substring(rangeInt.get(0));
        }
        else {
            StringBuilder builder = new StringBuilder();
            List<String> wordList = new ArrayList<>(Arrays.asList(linePrep.split(" ")));
            for (int i = rangeInt.get(0); (i < rangeInt.get(1) && i < wordList.size()); i++) {
                builder.append(wordList.get(i));
                builder.append(' ');
            }
            result = builder.toString().trim();
        }
        return result;
    }
    void writer(String result) {
        try(FileWriter writer = new FileWriter(outFile, false))
        {
            writer.write(result);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}

