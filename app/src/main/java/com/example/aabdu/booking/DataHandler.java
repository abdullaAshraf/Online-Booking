package com.example.aabdu.booking;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DataHandler {

    private String TAG = MainActivity.class.getSimpleName();

    private static String url = "https://murmuring-citadel-12769.herokuapp.com/";

    private ProgressDialog pDialog;

    ArrayList<Booking> list = new ArrayList<>();

    HashMap<String,String> data;

    Context context;

    public DataHandler(Context con){
        context = con;
    }

    public void getReservations(){
        data = new HashMap<>();
        new GetReservations().execute();
    }

    public void getReservations(Date date){
        data = new HashMap<>();
        data.put("day",Integer.toString(date.getDate()));
        data.put("month",Integer.toString(date.getMonth()));
        data.put("year",Integer.toString(date.getYear()));
        new GetReservations().execute();
    }

    public void getReservations(String email){
        data = new HashMap<>();
        data.put("email",email);
        new GetReservations().execute();
    }

    public boolean addUser(String id , String email , String firstName , String lastName){
        HttpHandler httpHandler = new HttpHandler();
        data = new HashMap<>();
        data.put("accesstoken",id);
        data.put("name",firstName + " " + lastName);
        data.put("email",email);
        httpHandler.sendPost(url + "adduser" , data);
        return true;
    }

    public boolean blockUser(String email){
        HttpHandler httpHandler = new HttpHandler();
        httpHandler.sendGet(url + "blockuser/" + email);
        return true;
    }

    public boolean deleteReservation(Date date){
        HttpHandler httpHandler = new HttpHandler();
        data = new HashMap<>();
        data.put("day",Integer.toString(date.getDate()));
        data.put("month",Integer.toString(date.getMonth()));
        data.put("year",Integer.toString(date.getYear()));
        data.put("starth",Integer.toString(date.getHours()));
        data.put("startm",Integer.toString(date.getMinutes()));
        String dataString = "";
        for (String item: data.values())
            dataString += "/" + item;
        httpHandler.sendGet(url + "deletereservation" + dataString);
        return true;
    }

    public boolean addReservation(String email , Date date , int duration){
        HttpHandler httpHandler = new HttpHandler();
        data = new HashMap<>();
        data.put("email",email);
        data.put("day",Integer.toString(date.getDate()));
        data.put("month",Integer.toString(date.getMonth()));
        data.put("year",Integer.toString(date.getYear()));
        data.put("starth",Integer.toString(date.getHours()));
        data.put("startm",Integer.toString(date.getMinutes()));
        data.put("durationinm",Integer.toString(duration));
        httpHandler.sendPost(url + "addreservation" , data);
        return true;
    }

    private class GetReservations extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String dataString = "";
            for (String item: data.values())
                dataString += "/" + item;
            String jsonStr = sh.makeServiceCall(url + "getreservations" + dataString);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray reservations = jsonObj.getJSONArray("reservations");

                    // looping through All Reservations
                    for (int i = 0; i < reservations.length(); i++) {
                        JSONObject c = reservations.getJSONObject(i);

                        // tmp Booking for single reservation
                       Booking booking = new Booking(c.getString("_id"),c.getString("email"),c.getInt("day"),c.getInt("month"),c.getInt("year"),c.getInt("starth"),c.getInt("startm"),c.getInt("durationinm"),c.getInt("confirmed"));


                        // adding reservation to reservation list
                        list.add(booking);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            for (Booking b: list) {
                Log.e(TAG,b.getDate() + b.getTime(true));
            }
        }

    }
}
