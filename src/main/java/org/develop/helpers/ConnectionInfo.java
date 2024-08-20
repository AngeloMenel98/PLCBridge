package org.develop.helpers;

import org.json.simple.JSONObject;

public class ConnectionInfo {
    private ModBus modBus;
    private JSONObject jsonObject;

    public ConnectionInfo(ModBus modBus, JSONObject jsonObject) {
        this.modBus = modBus;
        this.jsonObject = jsonObject;
    }

    public ModBus getModBus() {
        return modBus;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
