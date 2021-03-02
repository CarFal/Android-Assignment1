package com.gmail.carlos.map524_assignment1_carlosfalconett;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorageManager {
    private static int STORAGE_PERMISSION_CODE = 23;

    private void writeData(File myFile, String data){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(myFile);
            fileOutputStream.write(data.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveInternalFile(Activity activity, String toStore){
        FileOutputStream fileOutputStream = null;
        try{
            File file = activity.getFilesDir();
            fileOutputStream = activity.openFileOutput("userData3.txt", Context.MODE_APPEND);
            fileOutputStream.write(toStore.getBytes());
            Toast.makeText(activity, "Saved \n" + "Path: "+ file + "\tserData3.txt", Toast.LENGTH_SHORT).show();

            return;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String loadInternalFile(Activity activity){
        String loadedData = "";
        try {
            FileInputStream fileInputStream = activity.openFileInput("userData3.txt");
            int read = -1;
            StringBuffer buffer = new StringBuffer();
            while((read = fileInputStream.read())!= -1){
                buffer.append((char)read);
            }
            Log.d("Code", buffer.toString());
            loadedData = buffer.toString();
        } catch (Exception e){
            e.printStackTrace();
        }

        return loadedData;
    }

    public void ResetFile(Activity activity) {
        FileOutputStream fileOutputStream = null;
        try{
            File file = activity.getFilesDir();
            fileOutputStream = activity.openFileOutput("userData3.txt", Context.MODE_PRIVATE);
            //fileOutputStream.write(toStore.getBytes());
            Toast.makeText(activity, "The File: " + file + "userData3.txt has been reset.", Toast.LENGTH_SHORT).show();
            //Toast.makeText(activity, "Reseted \n" + "Path: "+ file + "\tserData3.txt", Toast.LENGTH_SHORT).show();

            return;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
