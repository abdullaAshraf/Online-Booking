package com.example.aabdu.booking;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DataHandler {

    private String TAG = MainActivity.class.getSimpleName();

    private static String url = "https://murmuring-citadel-12769.herokuapp.com/";

    private ProgressDialog pDialog;

    ArrayList<Booking> list = new ArrayList<>();

    HashMap<String,String> data;
    String addToUrl = "";

    Context context;

    public DataHandler(Context con){
        context = con;
    }

    public void getReservations(){
        data = new HashMap<>();
        addToUrl = "getreservations";
        new GetReservations().execute();
    }

    public void getReservations(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        data = new HashMap<>();
        data.put("day",Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
        data.put("month",Integer.toString(cal.get(Calendar.MONTH)));
        data.put("year",Integer.toString(cal.get(Calendar.YEAR)));
        addToUrl = "getreservationbyday";
        new GetReservations().execute();
    }

    public void getReservations(String email){
        data = new HashMap<>();
        data.put("email",email);
        addToUrl = "getreservationbyperson";
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
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        HttpHandler httpHandler = new HttpHandler();
        data = new HashMap<>();
        data.put("day",Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
        data.put("month",Integer.toString(cal.get(Calendar.MONTH)));
        data.put("year",Integer.toString(cal.get(Calendar.YEAR)));
        data.put("starth",Integer.toString(cal.get(Calendar.HOUR)));
        data.put("startm",Integer.toString(cal.get(Calendar.MINUTE)));
        String dataString = "";
        for (String item: data.values())
            dataString += "/" + item;
        httpHandler.sendGet(url + "deletereservation" + dataString);
        return true;
    }

    public boolean addReservation(String email , Date date , int duration){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        HttpHandler httpHandler = new HttpHandler();
        data = new HashMap<>();
        data.put("email",email);
        data.put("day",Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
        data.put("month",Integer.toString(cal.get(Calendar.MONTH)));
        data.put("year",Integer.toString(cal.get(Calendar.YEAR)));
        data.put("starth",Integer.toString(cal.get(Calendar.HOUR)));
        data.put("startm",Integer.toString(cal.get(Calendar.MINUTE)));
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
            String jsonStr = sh.makeServiceCall(url + addToUrl + dataString);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    JSONArray reservations = new JSONArray(jsonStr);

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
            //show data
            CheckFragment.bookingsList = list;
            CheckFragment.mAdapter.setItems(list);
            CheckFragment.mAdapter.notifyDataSetChanged();
            for (Booking b: list) {
                Log.e(TAG,b.getDate() + b.getTime(true));
            }

        }

    }
}
