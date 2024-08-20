package org.develop.helpers;

import java.io.FileWriter;
import java.io.Writer;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.procimg.Register;
import org.develop.config.machine.MachineJson;
import org.json.simple.JSONObject;

public class ModBus {
    private String ipAddress;
    private int port;
   // private int minRefRegister;
    //private int registersToRead;

    public ModBus(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        //this.minRefRegister = minRef;
        //this.registersToRead = count;
    }

    public ModBus(){}

    public TCPMasterConnection getConnected() {
        try {
            InetAddress address = InetAddress.getByName(this.ipAddress);
            TCPMasterConnection connection = new TCPMasterConnection(address);
            connection.setPort(this.port);

            connection.connect();

            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Register> getResponse(TCPMasterConnection connection, ArrayList<Integer> registersToRead) {
        try {
            ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);

            ArrayList<Register> allRegisters = new ArrayList<>();

            for (int r : registersToRead){
                ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(r, 1);

                transaction.setRequest(request);
                transaction.execute();

                ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();
                if (response != null) {
                    Register[] registers = response.getRegisters();
                    allRegisters.addAll(Arrays.asList(registers));
                } else {
                    System.out.println("No se recibió respuesta del servidor Modbus para el registro " + r);
                    return null;
                }
            }

            return allRegisters;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeConnection(TCPMasterConnection connection){
        connection.close();
    }

    public void saveRegistersToFile(Register[] registers, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Register register : registers) {
                short value = (short) register.getValue();
                writer.write(value + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String saveRegistersToJson(ArrayList<Register> registers, String fileName, ArrayList<Integer> readRegisters, JSONObject machine) {

        Path filePath = Paths.get("/home/angelo/Documentos/Work/PLCBridge/src/main/datalogs/", fileName);

        try (Writer writer = new FileWriter(filePath.toFile())) {
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[\n");
            for (int i = 0; i < registers.size(); i++) {
                jsonBuilder.append("  {\n");
                jsonBuilder.append("    \"machineId\": ").append((String) machine.get("machineId")).append(",\n");
                jsonBuilder.append("    \"reg\": ").append(readRegisters.get(i)).append(",\n");
                jsonBuilder.append("    \"value\": \"").append(registers.get(i).getValue()).append("\",\n");
                jsonBuilder.append("    \"insertAt\": \"").append(extractTimestamp(fileName)).append("\"\n");
                jsonBuilder.append("  }");
                if (i < registers.size() - 1) {
                    jsonBuilder.append(",");
                }
                jsonBuilder.append("\n");
            }
            jsonBuilder.append("]\n");
            writer.write(jsonBuilder.toString());

            return String.valueOf(jsonBuilder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void setIpAddress(String val){
        this.ipAddress = val;
    }

    public void setPort(int val){
        this.port = val;
    }

    public int getPort() {
        return port;
    }

    public String getIPAddress() {
        return ipAddress;
    }


    private static String extractTimestamp(String fileName) {
        String[] parts = fileName.split("[-.]");

        if (parts.length == 3 && parts[0].equals("datalog")) {
            try {
                return parts[1];
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Devolver -1 o lanzar una excepción si el formato no es correcto
        throw new IllegalArgumentException("Invalid file name format");
    }



}

 /*ReadInputDiscretesRequest request = new ReadInputDiscretesRequest(0, 10);

            // Ejecutar la transacción
            transaction.setRequest(request);
            transaction.execute();

            // Obtener la respuesta
            ReadInputDiscretesResponse response = (ReadInputDiscretesResponse) transaction.getResponse();
            if (response != null) {
                BitVector discretes = response.getDiscretes();
                System.out.println("Valores de las entradas discretas: ");
                for (int i = 0; i < discretes.size(); i++) {
                    boolean discrete = discretes.getBit(i); // Obtiene el valor booleano en la posición i
                    System.out.println(discrete);
                }
            } else {
                System.out.println("No se recibió respuesta del PLC");
            }*/
