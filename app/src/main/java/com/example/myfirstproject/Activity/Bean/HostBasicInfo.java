package com.example.myfirstproject.Activity.Bean;

import androidx.annotation.NonNull;

public class HostBasicInfo {
    private String hostID;
    private String password;
    public HostBasicInfo(String hostID, String password){
        this.hostID=hostID;
        this.password=password;
    }

    //get information
    public String getHostID(){return hostID;}
    public String getPassword(){return password;}
    //set information
    public void setHostID(String hostID){this.hostID=hostID;}
    public void setPassword(String password){this.password=password;}

    @NonNull
    @Override
    public String toString() {
        return "HostBasicInfo{" +
                "hostID=" + hostID +
                ", password='" + password + '\'' +
                '}';
    }
}
