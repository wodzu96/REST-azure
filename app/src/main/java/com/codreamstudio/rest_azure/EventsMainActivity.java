package com.codreamstudio.rest_azure;

/**
 * Created by wodzu on 03.06.17.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventsMainActivity extends AppCompatActivity {
    String facebookID;
    String url = "http://dzikiekuny.azurewebsites.net/tables/events?ZUMO-API-VERSION=2.0.0";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DiskBasedCache cache = new DiskBasedCache(getApplicationContext().getCacheDir(), 1024 * 1024); // 1MB cap   //TODO: zapytac igora
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        mRequestQueue.add(updateEvent(new EventModel("Wodzu1", "11 wrzesnia 2001", "123", "Super", "Chujowi", "123", "123", "123"), "59a026bb-bafe-4a00-a711-a509405b326a"));
       // mRequestQueue.add(updateUser(new UserModel("Wodzu", "1234", "joined1"), "479cc74d-0612-4859-bd8c-34ed2196ffaf"));
        mRequestQueue.add(getEvents());

    }
    public ArrayList<EventModel> jsonEventsParser(JSONArray eventsArray) throws JSONException {
        ArrayList<EventModel>events = new ArrayList<>();
        for(int i = 0; i<eventsArray.length(); ++i){
            JSONObject eventObject = eventsArray.getJSONObject(i);
            events.add(new EventModel(eventObject.getString("name"), eventObject.getString("deadline_date"), eventObject.getString("user_id"), eventObject.getString("description"), eventObject.getString("members"), eventObject.getString("lat"), eventObject.getString("lng"), eventObject.getString("sport_id"), eventObject.getString("id")));
        }
        return events;

    }
    public EventModel jsonEventParser(JSONArray eventsArray) throws JSONException {
        for(int i = 0; i<eventsArray.length(); ++i){
            JSONObject eventObject = eventsArray.getJSONObject(i);
            return new EventModel(eventObject.getString("name"), eventObject.getString("deadline_date"), eventObject.getString("user_id"), eventObject.getString("description"), eventObject.getString("members"), eventObject.getString("lat"), eventObject.getString("lng"), eventObject.getString("sport_id"), eventObject.getString("id"));
        }
        return null;

    }
    private JsonArrayRequest getEvents(){
        return new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<EventModel> events =  jsonEventsParser(response);
                            Log.i("Events size", String.valueOf(events.size()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
    }
    private JsonObjectRequest insertEvent(EventModel event){
        JSONObject params = new JSONObject();
        try {
            params.put("name", event.getName());
            params.put("deadline_date", event.getDeadlineDate());
            params.put("user_id", event.getUserID());
            params.put("description", event.getDescription());
            params.put("members", event.getMembers());
            params.put("lat", event.getLat());
            params.put("lng", event.getLng());
            params.put("sport_id", event.getSportID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(Request.Method.POST,
                url, params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Działa", response.toString());
                        // pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error volley", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });
    }

    private JsonObjectRequest updateEvent(EventModel event, String eventID){
        JSONObject params = new JSONObject();
        try {
            params.put("name", event.getName());
            params.put("deadline_date", event.getDeadlineDate());
            params.put("user_id", event.getUserID());
            params.put("description", event.getDescription());
            params.put("members", event.getMembers());
            params.put("lat", event.getLat());
            params.put("lng", event.getLng());
            params.put("sport_id", event.getSportID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(Request.Method.PATCH,  "http://dzikiekuny.azurewebsites.net/tables/events/"+eventID+"?ZUMO-API-VERSION=2.0.0", params,  new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Działa", response.toString());
                // pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error volley", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });
    }
    private JsonArrayRequest getEvent(String eventID){
        return new JsonArrayRequest
                (Request.Method.GET, "http://dzikiekuny.azurewebsites.net/tables/events/"+eventID+"?ZUMO-API-VERSION=2.0.0", null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            EventModel myUser =  jsonEventParser(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
    }

}