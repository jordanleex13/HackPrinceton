package com.jordanleex13.hackprinceton.Helpers;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Jordan on 2016-11-11.
 */

public class JsonParser {

    String base = "https://api.seatgeek.com/2";

    public void httpGet(String searchTerm) throws IOException, JSONException {

//        JobManager.setSearchTerm(searchTerm);
//
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpResponse response = httpclient.execute(new HttpGet(in));
//
//
//        StatusLine statusLine = response.getStatusLine();
//
//        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            response.getEntity().writeTo(out);
//
//
//            String responseString = out.toString();
//            Log.d(TAG, responseString);
//
//
//
//            JSONObject reader = new JSONObject(responseString);
//
//            //Get the instance of JSONArray that contains JSONObjects
//            JSONArray jsonArray = reader.optJSONArray("results");
//            Log.e(TAG, "Num of jobs is " + String.valueOf(jsonArray.length()));
//            for(int i=0; i < jsonArray.length(); i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                String jobTitle = jsonObject.optString("jobtitle");
//                String company = jsonObject.optString("company");
//                String city = jsonObject.optString("city");
//                String state = jsonObject.optString("state");
//                String country = jsonObject.optString("country");
//                String description = jsonObject.optString("snippet");
//                String url = jsonObject.optString("url");
//                float latitude = Float.parseFloat(jsonObject.optString("latitude"));
//                float longitude = Float.parseFloat(jsonObject.optString("longitude"));
//
//
//                Job curr = new Job(jobTitle, company, city, state, country, description, url, latitude, longitude);
//                JobManager.addJob(curr);
//
//                publishProgress();
//
//            }
//
//
//
//            out.close();
//        } else{
//            //Closes the connection.
//            response.getEntity().getContent().close();
//
//        }
    }
}
