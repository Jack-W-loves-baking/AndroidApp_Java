package com.example.dmsassignment4;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**This class is to use the GET method to fetch data from URL asynchronously.
 */
public class Info extends AsyncTask<String, Void, String> {

    private TextView tView;

    public Info(TextView tView) {
        this.tView = tView;
    }

    /**
     *
     * @param strings arguments
     * @return
     */
    @Override
    protected String doInBackground(String... strings) {
        int ID = 0;
        String title = "";
        Double longitude = 0d;
        Double latitude = 0d;
        String output = "";
        try {
            URL bookingUrl = new URL("http://10.0.2.2:8080/AssignmentFourProject/webresources/locationdata");
            HttpURLConnection conn = (HttpURLConnection) bookingUrl.openConnection();

            //HTTPURL connection set up
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

            //if connected
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    jsonResponse.append(line);
                    line = br.readLine();
                }
                br.close();
                conn.disconnect();

                //convert plain text json to valid info.
                String finalJson = jsonResponse.toString();
                JSONArray object = new JSONArray(finalJson);
                for (int i = 0; i < object.length(); i++) {
                    JSONObject desc = object.getJSONObject(i);
                    latitude = desc.getDouble("latitude");
                    longitude = desc.getDouble("longitude");
                    ID = desc.getInt("id");
                    title = desc.getString("title");
                    User user = new User(ID, latitude, longitude, title);
                    MapsActivity.wholeList.add(user);

                    //output will be the text on the board.
                    output += "ID: " + ID + " ,Relationship:" + title + " ,Latitude:"
                            + latitude + " ,Longitude:" + longitude + "\n";
                }

                return output;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     *
     * @param workerResult to setText
     */
    protected void onPostExecute(String workerResult) {
        tView.setText(workerResult);
    }

}
