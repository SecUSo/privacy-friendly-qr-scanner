package com.secuso.privacyfriendlycodescanner.qrscanner.database;

@Deprecated
public class ScannedData {

    private int _id;
    private String _content;

    public ScannedData(){
    }

    public ScannedData(String name){
        this._content = name;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._content = _name;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _content;
    }

}

