package com.idit.gasomovil.BluetoothRegister;

import java.util.HashMap;
import java.util.Map;

public class BluetoothModel {

    private static final String NAME_DEFAULT = "OBD Go Movil";

    private static String _id;
    private static String _mac;
    private static String _name = NAME_DEFAULT;

    BluetoothModel(String id, String mac, String name) {
        _id = id;
        _mac = mac;
        if (!name.isEmpty())
            _name = name;
    }

    public static String get_id() {
        return _id;
    }

    public static void set_id(String _id) {
        BluetoothModel._id = _id;
    }

    public static String get_mac() {
        return _mac;
    }

    public static void set_mac(String _mac) {
        BluetoothModel._mac = _mac;
    }

    public static String get_name() {
        return _name;
    }

    public static void set_name(String _name) {
        BluetoothModel._name = _name;
    }

    Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id",_id);
        result.put("mac",_mac);
        result.put("name",_name);
        return result;
    }
}
