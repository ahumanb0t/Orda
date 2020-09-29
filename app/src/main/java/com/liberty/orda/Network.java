package com.liberty.orda;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Network {
    public static final Network Instance = new Network();

    public void init() {
        MainThread.Instance.onMessage("Some text in chat!");
        MainThread.Instance.onUnitUpdatePosition();
    }

    public void setUnitPosition() {

    }

    public List<LatLng> getUnitsPosition() {
        return null;
    }

    public void sendMessage(float ch, String msg) {

    }

    public List<String> getMessages() {
        return null;
    }
}
