package com.idit.gasomovil.BluetoothRegister;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BluetoothModel {

    private static final String NAME_DEFAULT = "OBD Go Movil";

    private String _id;
    private String _mac;
    private String _name = NAME_DEFAULT;

    private String _create;

    public BluetoothModel(String id, String mac, String name) {
        _id = id;
        _mac = mac;

        if (!name.isEmpty())
            _name = name;

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        _create = dateFormat.format(date);
    }

    public String get_id() {
        return _id;
    }

    public String get_mac() {
        return _mac;
    }

    public String get_name() {
        return _name;
    }

    public String get_create() {
        return _create;
    }

    Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id",_id);
        result.put("mac",_mac);
        result.put("name",_name);
        result.put("create", _create);
        return result;
    }
}
