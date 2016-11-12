package com.jordanleex13.hackprinceton.Helpers;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Jordan on 2016-11-11.
 */

public class JsonParser {

    String base = "https://api.seatgeek.com/2/events/3315199";

    public void httpGet(String searchTerm) throws IOException, JSONException {

       String in = "https://api.seatgeek.com/2/events/3315199";

       HttpClient httpclient = new DefaultHttpClient();
       HttpResponse response = httpclient.execute(new HttpGet(in));


       StatusLine statusLine = response.getStatusLine();

       if(statusLine.getStatusCode() == HttpStatus.SC_OK){
           ByteArrayOutputStream out = new ByteArrayOutputStream();
           response.getEntity().writeTo(out);


           String responseString = out.toString();
           //Log.d(TAG, responseString);



           JSONObject reader = new JSONObject(responseString);

           //Get the instance of JSONArray that contains JSONObjects
           JSONArray jsonArray = reader.optJSONArray("results");
           //Log.e(TAG, "Num of jobs is " + String.valueOf(jsonArray.length()));
           for(int i=0; i < jsonArray.length(); i++){
               JSONObject jsonObject = jsonArray.getJSONObject(i);

               String id = jsonObject.optString("id");
               String title = jsonObject.optString("title");
               String date = jsonObject.optString("datetime_local");
               String ticket_url = jsonObject.optString("url");
               String city = jsonObject.optString("city");
               String[] location = jsonObject.optString("location");
               System.out.println(location[0]);

           }



           out.close();
       } else{
           //Closes the connection.
           response.getEntity().getContent().close();

       }
    }
}
