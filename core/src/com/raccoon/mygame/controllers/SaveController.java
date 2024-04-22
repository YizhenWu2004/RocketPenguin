package com.raccoon.mygame.controllers;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class SaveController {
    Map<Integer, Integer> keyvaluepairs;
    LevelLoader loader;
    File file;

    public SaveController(LevelLoader loader){
        this.loader = loader;
        readFile();
    }
    //creates empty save file.
    private void createDefaultKeyValuePairs(){
        Map<Integer, Integer> pairs = new HashMap<>(this.loader.getLevels().size);
        for(int i = 0; i < this.loader.getLevels().size; i++){
            pairs.put(i, 0);
        }
        this.keyvaluepairs = pairs;
    }


    private void readFile() {
        keyvaluepairs = new HashMap<>();
        try {
            file = new File("save.txt");
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (!data.isEmpty()) {
                    String[] parts = data.split("=");
                    int key = Integer.parseInt(parts[0]);
                    int value = Integer.parseInt(parts[1]);
                    keyvaluepairs.put(key, value);
                }
            }
            myReader.close();

            if (keyvaluepairs.isEmpty()) {
                createDefaultKeyValuePairs();
                writeToFile();
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            createDefaultKeyValuePairs();
            writeToFile();
            e.printStackTrace();
        }
    }


    private void writeToFile() {
        try {
            FileWriter writer = new FileWriter("save.txt");
            for (Map.Entry<Integer, Integer> entry : keyvaluepairs.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
    public void editKeyValuePair(int level, int stars){
        this.keyvaluepairs.replace(level, stars);
        //clear the current save and override it
        writeToFile();
    }
    public Map<Integer, Integer> getKeyvaluepairs(){
        return this.keyvaluepairs;
    }
}
