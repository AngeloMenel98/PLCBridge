package org.develop.service;

public class MachineService extends GeneralService {
    private String url;
    private String service;

    public MachineService() {
        this.url = GeneralService.DESTINATION_URL;
        this.service = "/";
    }

    public void sendMachines(String jsonBody, String fileName){
        post(jsonBody, fileName, this.service);
    }
}
