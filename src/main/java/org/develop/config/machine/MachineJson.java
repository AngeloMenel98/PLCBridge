package org.develop.config.machine;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MachineJson {

    public  ArrayList<JSONObject> loadMachines(String path) {
        ArrayList<JSONObject> maquinas = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();

            Object obj = parser.parse(new FileReader(path));

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray machines = (JSONArray) jsonObject.get("machines");

            for (Object machine : machines) {
                maquinas.add((JSONObject) machine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return maquinas;
    }

}
