package com.codreamstudio.rest_azure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    String facebookID;
    String url = "http://dzikiekuny.azurewebsites.net/tables/users?ZUMO-API-VERSION=2.0.0";

    JsonArrayRequest getUser = new JsonArrayRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    try {
                        UserModel myUser =  jsonUsersParser(response, facebookID);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DiskBasedCache cache = new DiskBasedCache(getApplicationContext().getCacheDir(), 1024 * 1024); // 1MB cap   //TODO: zapytac igora
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        //mRequestQueue.add(insertUser(new UserModel("Wodzu", "1234", "joined")));
        mRequestQueue.add(updateUser(new UserModel("Wodzu", "1234", "joined1"), "479cc74d-0612-4859-bd8c-34ed2196ffaf"));
        mRequestQueue.add(getUser);

    }
    public UserModel jsonUsersParser(JSONArray users, String fbID) throws JSONException {
        for(int i = 0; i<users.length(); ++i){
            if(users.getJSONObject(i).getString("fbid").equals(fbID)) {
                JSONObject userObject = users.getJSONObject(i);
                return new UserModel(userObject.getString("name"), userObject.getString("fbid"), userObject.getString("joined"), userObject.getString("id"));
            }
        }
        return null;

    }
    public UserModel jsonUsersParser(JSONArray users) throws JSONException {
        for(int i = 0; i<users.length(); ++i){
                JSONObject userObject = users.getJSONObject(i);
                return new UserModel(userObject.getString("name"), userObject.getString("fbid"), userObject.getString("joined"), userObject.getString("id"));
        }
        return null;

    }
    private JsonObjectRequest insertUser(UserModel user){
        JSONObject params = new JSONObject();
        try {
            params.put("name", user.getName());
            params.put("fbid", user.getFbid());
            params.put("joined", user.getJoined());
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

    private JsonObjectRequest updateUser(UserModel user, String userID){
        JSONObject params = new JSONObject();
        try {
            params.put("name", user.getName());
            params.put("fbid", user.getFbid());
            params.put("joined", user.getJoined());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(Request.Method.PATCH,  "http://dzikiekuny.azurewebsites.net/tables/users/"+userID+"?ZUMO-API-VERSION=2.0.0", params,  new Response.Listener<JSONObject>() {

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
    private JsonArrayRequest getUser(String userID){
        return new JsonArrayRequest
                (Request.Method.GET, "http://dzikiekuny.azurewebsites.net/tables/users/"+userID+"?ZUMO-API-VERSION=2.0.0", null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            UserModel myUser =  jsonUsersParser(response);
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
