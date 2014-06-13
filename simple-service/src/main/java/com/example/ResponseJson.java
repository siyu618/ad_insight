package com.example;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by shiyao on 3/20/14.
 */
public class ResponseJson {
    HashMap<String, HashMap<String, HashMap<String, String>>> json;
    private static Gson gson = new Gson();
    ResponseJson() {
        json = new HashMap<String, HashMap<String, HashMap<String, String>>>();
    }
    public void add(String income, String ca, String count, String type) {
        if(json == null) {
            json = new HashMap<String, HashMap<String, HashMap<String, String>>>();
        }
        HashMap<String, HashMap<String, String>> income_value;
        if(json.get(income) == null) {
            income_value = new HashMap<String, HashMap<String, String>>();
        }
        else {
           income_value = json.get(income);
        }
        HashMap<String, String> ca_value;
        if(income_value.get(ca) == null) {
            ca_value = new HashMap<String, String>();
        }
        else ca_value = income_value.get(ca);
        String mappingType = null;
        try {
            mappingType = MappingDict.getMapping(income, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (null != mappingType) {
            type = mappingType;
        }
        int num = Integer.parseInt(count);
        if(type.equals("Unknown")) {

            if (ca_value != null && ca_value.get(type) != null) {
                num += Integer.parseInt(ca_value.get(type));
            }
        }
        ca_value.put(type, num+"");
        income_value.put(ca, ca_value);
        json.put(income, income_value);
    }
    public String toJson() {
        return gson.toJson(json);
    }
}
