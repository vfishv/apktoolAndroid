package com.folderv.apktool.andadapter;

import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import brut.androlib.AndrolibException;

public class Logger {

    private static List<String> list = Collections.synchronizedList(new ArrayList<String>());;

    private static final String TAG_DEFAUKT = "LoggerAdapter";

    public String getTag() {
        return tag == null ? TAG_DEFAUKT : tag;
    }

    private String tag;

    public static Logger getLogger(String name) {
        return new Logger(name, null);
    }

    protected Logger(String name, String resourceBundleName) {
        this.tag = name;
    }

    private void addLog(String line) {
        if(list.size()>512){
            list.clear();
        }
        list.add(line);
    }

    public static void clearLog() {
        list.clear();
    }

    public static List<String> getList() {
        return list;
    }

    public void severe(String msg) {
        String m = "severe: " + msg;
        Log.i(getTag(), m);
        addLog(m);
    }

    public void warning(String msg) {
        String m = "warning: " + msg;
        Log.w(getTag(), m);
        addLog(m);
    }

    public void info(String msg) {
        String m = "info: " + msg;
        Log.i(getTag(), m);
        addLog(m);
    }

    public void fine(String msg) {
        String m = "fine: " + msg;
        Log.i(getTag(), m);
        //addLog(m);
    }

    public void log(Level warning, String msg, AndrolibException ex) {
        if(Level.WARNING.equals(warning)){
            String m = "warning: " + msg;
            Log.w(getTag(), m, ex);
            addLog(m + ex.getMessage());
        }
        else if(Level.SEVERE.equals(warning)){
            String m = "severe: " + msg;
            Log.w(getTag(), m, ex);
            addLog(m + ex.getMessage());
        }
    }
}
