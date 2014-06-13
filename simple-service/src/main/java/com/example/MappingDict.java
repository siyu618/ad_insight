package com.example;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tianzy on 3/20/14.
 */
public class MappingDict {

    private static Map<String, String> dict;
    private static boolean loaded = false;
    private static String dictFile = MyResource.ROOT + "/" + "dict";
    private static Map<String, String> getDict() throws IOException {
        if (loaded) {
            return dict;
        }
        dict = new HashMap<String, String>();
        BufferedReader reader = new BufferedReader(new FileReader(new File(dictFile)));
        String line = null;
        int importedLines = 0;
        while ((line = reader.readLine()) != null) {
            String[] arr = line.split("\\|", -1);
            if (arr.length == 3) {
                String dimension = arr[0].trim();
                String value = arr[1].trim();
                String mappingValue = arr[2].trim();
                dict.put(dimension+"_" + value, mappingValue);
                importedLines ++;
            }
        }
        System.out.println("import # of lines : " + importedLines);
        loaded = true;
        return dict;
    }

    public static String getMapping(String dimension, String value) throws IOException {
        String key = dimension + "_" + value;
        return getDict().get(key);
    }
}



