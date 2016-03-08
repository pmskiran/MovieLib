package com.grab.movielib.com.grab.movielib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.grab.movielib.com.grab.movielib.home.MovieInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class FileOperations {

    public static boolean writeToFile(Context context, String fileName, Object data) {
        boolean result =true;

        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            result = false;
            Log.e("MovieLib", "writeToFile() Exception."+e.toString());
        } catch (IOException e) {
            result = false;
            Log.e("MovieLib", "writeToFile() Exception."+e.toString());
        }

        return result;
    }

    public static Object readFromFile(Context context, String fileName) {
        Object obj = null;
//        if(! new File(fileName).exists())
//            return obj;

        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            obj = objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            Log.e("MovieLib", "readFromFile() Exception."+e.toString());
        } catch (IOException e) {
            Log.e("MovieLib", "readFromFile() Exception."+e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("MovieLib", "readFromFile() Exception."+e.toString());
        }

        return obj;
    }

    public static void appendObject(Context context, String fileName, MovieInfo movieInfo){
        try {
            Object obj = readFromFile(context, fileName);
            HashMap<String, MovieInfo> hashMap = null;
            if(null != obj)
                hashMap = (HashMap<String, MovieInfo>) obj;
            else
                hashMap = new HashMap<String, MovieInfo>();
            hashMap.put(movieInfo.id, movieInfo);
            writeToFile(context, fileName, hashMap);
        }catch (ClassCastException e) {
            Log.e("MovieLib", "appendObject() Exception."+e.toString());
        }
    }

    public static void deleteObject(Context context, String fileName, MovieInfo movieInfo) {
        try{
            Object obj = readFromFile(context, fileName);
            HashMap<String, MovieInfo> hashMap = null;
            if(null != obj)
                hashMap = (HashMap<String, MovieInfo>) obj;
            if(null != hashMap && hashMap.containsKey(movieInfo.id)) {
                hashMap.remove(movieInfo.id);
                writeToFile(context, fileName, hashMap);
            }
        }catch (ClassCastException e) {
            Log.e("MovieLib", "deleteObject() Exception."+e.toString());
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
