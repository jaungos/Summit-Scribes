package summitscribes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.*;
import org.json.simple.parser.*;

public class JSONReader {
    private JSONParser parser;
    private ArrayList<String> wordslist;

    public JSONReader() {
        this.parser = new JSONParser();
        this.wordslist = new ArrayList<String>();
    }

    public void readFile(String fileToRead) {
        // Reference: https://www.tutorialspoint.com/how-can-we-read-a-json-file-in-java
        // https://howtodoinjava.com/java/library/json-simple-read-write-json-examples/
        try (FileReader reader = new FileReader(fileToRead)) {
            // Read JSON file
            Object obj = this.parser.parse(reader);
            JSONObject jsonObject = (JSONObject)obj;

            // Get the words JSON Object
            JSONArray words = (JSONArray)jsonObject.get("words");

            Iterator<String> iterator = words.iterator();
            while (iterator.hasNext()) {
                String wordToAdd = iterator.next();
                this.wordslist.add(wordToAdd);
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getWordList() {
        return this.wordslist;
    }
}
