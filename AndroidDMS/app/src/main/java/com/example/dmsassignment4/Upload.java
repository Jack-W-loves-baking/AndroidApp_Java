package com.example.dmsassignment4;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * This class is to upload the user info to the database by POST method.
 */
public class Upload extends AsyncTask<String, Void, Void> {
    JSONObject data;

    public Upload(Map<String, Object> data){
        Log.v("testing:",data.toString());

        if(this.data == null){
            this.data = new JSONObject(data);
        }
    }

    @Override
    protected Void doInBackground(String... strings) {
        try{
            URL bookingUrl = new URL("http://10.0.2.2:8080/AssignmentFourProject/webresources/locationdata");
            HttpURLConnection conn = (HttpURLConnection) bookingUrl.openConnection();

            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(true);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("POST");

            if(this.data!=null){
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(this.data.toString());
                writer.flush();
                writer.close();
            }

            int statusCode = conn.getResponseCode();

            if(statusCode==200){
                InputStream inputStream = new BufferedInputStream(conn.getInputStream());
                String response = inputStream.toString();
                Log.v("test",response);
            }
            else{
                Log.v("error", String.valueOf(statusCode));
            }

            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
