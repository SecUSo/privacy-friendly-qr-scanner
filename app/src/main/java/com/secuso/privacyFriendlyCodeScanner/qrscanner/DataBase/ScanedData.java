package com.secuso.privacyFriendlyCodeScanner.qrscanner.DataBase;


public class ScanedData {

    private int _id;
    private String _content;

    public ScanedData(){
    }

    public ScanedData(String name){
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

