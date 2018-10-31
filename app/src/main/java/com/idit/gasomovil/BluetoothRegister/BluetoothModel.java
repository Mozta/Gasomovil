package com.idit.gasomovil.BluetoothRegister;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BluetoothModel {

    private static final String NAME_DEFAULT = "OBD Go Movil";
    public  static final int MAX_SIZE = 3;

    private String m;
    private String i;
    private String n = NAME_DEFAULT;

    private String o;
    private Map<String, Boolean> s;
    private String c;

    public BluetoothModel() {
    }

    public BluetoothModel(String id, String mac, String name, String owner) {
        i = id;
        m = mac;
        if (!name.isEmpty())
            n = name;
        c = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        o = owner;
        s = new HashMap<>();
    }


    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public Map<String, Boolean> getS() {
        return s;
    }

    public void setS(Map<String, Boolean> s) {
        this.s = s;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("i",i);
        result.put("m",m);
        result.put("n",n);
        result.put("c",c);
        result.put("o",o);
        result.put("s",s);
        return result;
    }
}
