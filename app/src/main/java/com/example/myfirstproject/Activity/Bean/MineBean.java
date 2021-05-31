package com.example.myfirstproject.Activity.Bean;

import androidx.annotation.NonNull;

public class MineBean {
    private String hostId;
    private String hostNick;
    private String hostIcon;
    private String hostAvatar;

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getHostNick() {
        return hostNick;
    }

    public void setHostNick(String hostNick) {
        this.hostNick = hostNick;
    }

    public String getHostIcon() {
        return hostIcon;
    }

    public void setHostIcon(String hostIcon) {
        this.hostIcon = hostIcon;
    }

    public String getHostAvatar() {
        return hostAvatar;
    }

    public void setHostAvatar(String hostAvatar) {
        this.hostAvatar = hostAvatar;
    }

    @NonNull
    @Override
    public String toString() {
        return "MineBean{" +
                "hostID=" + hostId +
                ", hostNick='" + hostNick +
                ", hostIcon="  + hostIcon  +
                ", hostAvatar="+ hostAvatar +'\''+
                '}';
    }
}
