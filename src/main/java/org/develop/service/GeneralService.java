package org.develop.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GeneralService {
    public static final String DESTINATION_URL = "http://127.0.0.1:9321/machine-service";

    public static void post(String jsonBody, String fileName,String endpoint) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(DESTINATION_URL).path(endpoint);


        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .header("client", "angelo")
                .post(Entity.entity(jsonBody, MediaType.APPLICATION_JSON));

        if (response.getStatus() == 200) {
            //String responseBody = response.readEntity(String.class);

            String filePath = "/home/angelo/Documentos/Work/PLCBridge/src/main/datalogs/" + fileName;

            try {
                Files.deleteIfExists(Paths.get(filePath));
                System.out.println("Archivo eliminado correctamente: " + filePath);
            } catch (IOException e) {
                System.err.println("Error al eliminar el archivo: " + e.getMessage());
            }
        } else {
            System.out.println("Error: " + response.getStatus());
        }

        response.close();
        client.close();
    }
}
