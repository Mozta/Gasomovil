package com.idit.gasomovil.BluetoothRegister;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BluetoothModel {

    private static final String NAME_DEFAULT = "OBD Go Movil";
    public  static final int MAX_SIZE = 3;

    private String _mac;
    private String _id;
    private String _name = NAME_DEFAULT;



    private String _owner;
    private Map<String, Boolean> _share;
    private String _create;

    public BluetoothModel(String id, String mac, String name, String owner) {
        _id = id;
        _mac = mac;

        if (!name.isEmpty())
            _name = name;

        _create = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

        _owner = owner;

        _share = new HashMap<>();

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

    public String get_owner() {
        return _owner;
    }

    public Map<String, Boolean> get_share() {
        return _share;
    }

    public void set_share(Map<String, Boolean> _share) {
        this._share = _share;
    }

    Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("i",_id);
        result.put("m",_mac);
        result.put("n",_name);
        result.put("c", _create);
        result.put("o", _owner);
        result.put("s", _share);
        return result;
    }
}
