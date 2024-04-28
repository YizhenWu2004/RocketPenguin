package com.raccoon.mygame.controllers;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class SaveController {
    private Map<Integer, Integer> keyvaluepairs = new HashMap<>();
    LevelLoader loader;
    File file;

    public SaveController(LevelLoader loader){
        this.loader = loader;
        readFile();
        addToSaveIfMoreLevels();
        writeToFile();
        //no idea if this works
    }
    //creates empty save file.
    private void createDefaultKeyValuePairs(){
        for(int i = 0; i < this.loader.getLevels().size; i++){
            this.keyvaluepairs.put(i, 0);
        }
    }


    private void readFile() {
        try {
            //create a new file if it doesn't already exist
            //otherwise read save.txt
            file = new File("save.txt");
            //create a file scanner
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                //read each line
                String data = myReader.nextLine();
                if (!data.isEmpty()) {
                    //if the current line isn't empty
                    String[] parts = data.split("=");
                    int key = Integer.parseInt(parts[0]);
                    int value = Integer.parseInt(parts[1]);
                    //parse and place into the key value pairs
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
        try (FileWriter writer = new FileWriter(file)){
            for (Map.Entry<Integer, Integer> entry : keyvaluepairs.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
    public void editKeyValuePair(int level, int score){
        if(score > this.keyvaluepairs.get(level)) {
            this.keyvaluepairs.replace(level, score);
            //clear the current save and override it
            writeToFile();
        }
    }
    public Map<Integer, Integer> getKeyvaluepairs(){
        return this.keyvaluepairs;
    }

    //add to save if more levels
    private void addToSaveIfMoreLevels() {
        int currentLevelCount = keyvaluepairs.size();
        int availableLevels = loader.getLevels().size;
        if (currentLevelCount < availableLevels) {
            for (int i = currentLevelCount; i < availableLevels; i++) {
                keyvaluepairs.putIfAbsent(i, 0);
            }
            writeToFile();
        }
    }

    public void deleteSaveFile() {
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Save file deleted successfully.");
                createDefaultKeyValuePairs();
            } else {
                System.out.println("Failed to delete the save file.");
            }
        } else {
            System.out.println("No save file to delete.");
        }
    }
}
