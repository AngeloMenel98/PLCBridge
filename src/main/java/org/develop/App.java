package org.develop;

import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.Register;
import org.develop.config.machine.MachineJson;
import org.develop.helpers.ConnectionInfo;
import org.develop.helpers.ModBus;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import org.develop.service.MachineService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class App {
    public static void main(String[] args) {
        String pathMachine = "/home/angelo/Documentos/Work/PLCBridge/src/main/java/org/develop/config/machine/machineConfig.json";

        Map<TCPMasterConnection, ConnectionInfo> connectionMap = new HashMap<>();

        ArrayList<TCPMasterConnection> connections = new ArrayList<>();

        MachineJson machineJson = new MachineJson();
        ArrayList<JSONObject> machines = machineJson.loadMachines(pathMachine);
        ArrayList<Integer> registersToRead = new ArrayList<>();

        MachineService machineService = new MachineService();

        for (JSONObject machine : machines) {
            String ipAddress = (String) machine.get("ipAddress");
            int port = Integer.parseInt((String) machine.get("port"));

            JSONArray readableRegistersArray = (JSONArray) machine.get("readableRegisters");

            for (Object registerObj : readableRegistersArray) {
                registersToRead.add(Integer.parseInt((String) registerObj));
            }

            try {
                ModBus modBus = new ModBus(ipAddress, port);

                TCPMasterConnection connection = modBus.getConnected();

                if(!connection.isConnected()){
                    throw new RuntimeException("Connection to modBus failed");
                }

                connections.add(connection);

                ConnectionInfo connInfo = new ConnectionInfo(modBus, machine);
                connectionMap.put(connection, connInfo);
            } catch (Exception e) {
                // Manejar la excepción adecuadamente (log, notificación, etc.)
                e.printStackTrace();
            }
        }

        while(true){
            for (TCPMasterConnection conn : connections){
                try {
                    ConnectionInfo connInfo = connectionMap.get(conn);

                    ArrayList<Register> allRegisters = connInfo.getModBus().getResponse(conn, registersToRead);


                    String fileName = generateFileName();
                    String json = connInfo.getModBus().saveRegistersToJson(allRegisters, fileName, registersToRead, connInfo.getJsonObject());

                    machineService.sendMachines(json, fileName);

                    Thread.sleep(5000);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private static String generateFileName() {
        long timestampInSeconds = System.currentTimeMillis() / 1000L;
        return "datalog-" + timestampInSeconds + ".log";
    }
}
