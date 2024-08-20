package org.develop.config.products;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;

public class ProductJson {

    /*public ArrayList<JSONObject> loadProducts(String path){
        ArrayList<JSONObject> productList = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();

            Object obj = parser.parse(new FileReader(path));

            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;

            JSONArray products = (JSONArray) jsonObject.get("products");
            for (Object product : products){
                productList.add((JSONObject) product);
            }

            return productList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productList;
    }*/
}
