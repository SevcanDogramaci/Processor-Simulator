package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Parser {

    private File file;
    private ArrayList<String> inputLines;
    private ArrayList<String> clearedLines;
    private Map<String, Integer> labelAddressesMap;
    private ArrayList<Instruction> instructions;

    public Parser(File file) {
        this.file = file;
        readFile();
        clearComments(inputLines);
    }

    public Parser(String text) {
        inputLines = new ArrayList<>(Arrays.asList(text.split("\n")));
        clearComments(inputLines); // eliminate undesired parts, try to get instructions into required format
    }

    public void createInstructions() throws Exception {
        instructions = new ArrayList<>();
        labelAddressesMap = new HashMap<>();

        extractLabels(); // Extracts labels and stores them into a map with their index.

        for (int i = 0; i < clearedLines.size(); i++) {
            try {
                Instruction in = Instruction.createInstruction(clearedLines.get(i), i, this);
                if(in != null)
                    instructions.add(in);
                else
                    throw new Exception("Instruction not supported: " + clearedLines.get(i));
            }catch (Exception e){
                if (e.getMessage() == null)
                    throw new Exception("Instruction not supported: " + clearedLines.get(i));
                else
                    throw new Exception(e.getMessage() + "\nInstruction not supported: " + clearedLines.get(i));
            }
        }

    }

    private void extractLabels(){

        int i = 0;
        String line;

        while (i < this.clearedLines.size()){

            line = this.clearedLines.get(i);

            if(line.contains(":")){
                String labelName = line.substring(0, line.indexOf(":"));
                String instruction;

                this.labelAddressesMap.put(labelName, i);

                if(line.indexOf(":") < line.length() - 1){
                    instruction = line.substring(line.indexOf(":")+1);
                    this.clearedLines.set(i, instruction.trim());
                    i++;
                }
                else {
                    this.clearedLines.remove(i);
                }
            }
            else{
                i++;
            }
        }
    }

    public int getLabelAddress(String labelName) throws Exception {
        if (labelAddressesMap.get(labelName) != null)
            return labelAddressesMap.get(labelName);
        else
            throw new Exception(labelName + " is not defined!");
    }

    private void readFile(){
        // read this file and save into a string.
        try (FileReader reader = new FileReader(this.file);
             BufferedReader br = new BufferedReader(reader)) {

            // read line by line
            String line;
            this.inputLines = new ArrayList<>(); //keeps lines
            while ((line = br.readLine()) != null) {
                this.inputLines.add(line);
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private void clearComments(ArrayList<String> lines){

       // boolean commentFlag = false; // for block comments, true when a block comment opened.
        clearedLines = new ArrayList<>();

        for (String line : lines){

            line = line.replace("\t", " ")
                    .replace(", ", ",").replace(",", ", ")
                    .trim().replaceAll(" +", " "); // transform into required format.

            // eliminate comment part of the line
            if (line.contains("#")){
                line = line.substring(0, line.indexOf("#")).trim();
            }
            line = line.trim();

            if (line.length()>=1 && !line.contains(".")){
                clearedLines.add(line);
            }
        }
    }

    public String getLines() {
        StringBuilder sb = new StringBuilder();

        if (instructions == null) {
            for (String s : clearedLines) {
                sb.append(s);
                if (s.contains(":") && s.trim().lastIndexOf(":") == s.trim().length() - 1)
                    sb.append(" ");
                else
                    sb.append("\n");
            }

        } else {
            for (int i = 0; i < instructions.size(); i++) {
                String s = instructions.get(i).getLine();
                if (labelAddressesMap.containsValue(i)){
                    sb.append(getLabelName(i)).append(": ");
                }
                sb.append(s).append("\n");
            }

            if (labelAddressesMap.containsValue(instructions.size())){
                sb.append(getLabelName(instructions.size())).append(": ");
            }

        }

        return sb.toString();
    }

    private String getLabelName(int i){
        final String[] name = new String[1];
        labelAddressesMap.forEach((k,v) -> {
            if (v == i)
                name[0] = k;
        });
        return name[0];
    }


    public List<Instruction> getInstructions() {
        return this.instructions;
    }
}
