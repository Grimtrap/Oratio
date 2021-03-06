import com.google.gson.stream.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * OratioTreeGenerator.java
 * Creates OratioTree objects from a custom JSON file which contains the data and image url for each mouth shape
 * @author Joey Chik
 * created 2018-11-15
 * last modified 2018-11-25
 */

public class OratioTreeGenerator {
    private static final String[] ARPABET_SYMBOLS =
            {
                    "AA", "AE", "AH", "AO", "AW",
                    "AY", "B", "CH", "D", "DH",
                    "EH", "ER", "EY", "F", "G",
                    "HH", "IH", "IY", "JH", "K",
                    "L", "M", "N", "NG", "OW",
                    "OY", "P", "R", "S", "SH",
                    "T", "TH", "UH", "UW", "V",
                    "W", "Y", "Z", "ZH"
            };
    private MouthShape avatar;

    /**
     * generates a tree from a JSON file containing an array of MouthShapes
     * @param jsonURL pathname for the json file
     * @return
     * @throws IOException
     */
    public OratioTree<MouthShape> generateTreeFromJson(String jsonURL) throws IOException{
        FileReader fileReader = new FileReader(new File(jsonURL));
        JsonReader jsonReader = new JsonReader(fileReader);

        OratioTree<MouthShape> tree = new OratioTree<>();

        OratioLinkedList<MouthShape> mList = new OratioLinkedList<>();

        // start reading json
        jsonReader.beginArray();

        // read default avatar image
        jsonReader.beginObject();
        String avatarFilePath;
        if (jsonReader.nextName().equals("avatar")) {
            avatarFilePath = jsonReader.nextString();
            avatar = new MouthShape(avatarFilePath, ARPABET_SYMBOLS);
        }
        jsonReader.endObject();

        while (jsonReader.hasNext()) {
            mList.add(readMouthShape(jsonReader));
        }

        jsonReader.endArray();
        jsonReader.close();
        // finish reading json

        for (String symbol : ARPABET_SYMBOLS) {
            MouthShape m = getMouthShapesWithPhoneticSpelling(mList, symbol);
            if (m == null) {
                m = avatar;
            }
            tree.add(m, symbol);
        }

        return tree;
    }

    /**
     * Returns the default avatar image
     * @return The default avatar image
     */
    public MouthShape getAvatar() {
        return avatar;
    }

    private MouthShape readMouthShape(JsonReader jsonReader) throws IOException{
        String fileName = null;
        String[] spelling = null;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();

            if (name.equals("fileName")) {
                fileName = jsonReader.nextString();
            } else if (name.equals("spelling")) {
                spelling = readJsonStringArray(jsonReader);
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();

        return new MouthShape(fileName, spelling);
    }

    private String[] readJsonStringArray(JsonReader jsonReader) throws IOException{
        OratioLinkedList<String> strings = new OratioLinkedList<>();

        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            strings.add(jsonReader.nextString());
        }
        jsonReader.endArray();

        return strings.toArray();
    }

    private MouthShape getMouthShapesWithPhoneticSpelling
            (OratioLinkedList<MouthShape> mList, String spelling){
        for (int i = 0; i < mList.size(); i++) {
            MouthShape m = mList.get(i);
            for (String s : m.getSpelling()){
                if (s.trim().equals(spelling.trim())) {
                    return m;
                }
            }
        }
        System.out.println("null: " + spelling);
        return null;
    }
}
