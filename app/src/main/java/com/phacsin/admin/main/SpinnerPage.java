package com.phacsin.admin.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.phacsin.admin.DBHandler;
import com.phacsin.admin.R;
import com.phacsin.admin.scan.ScanRegisterEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Bineesh P Babu on 21-09-2016.
 */
public class SpinnerPage extends AppCompatActivity {
    Spinner spinnereventdetail;
    private String[] event_details = { "Rise Of The Phoenix", "Motivate", "Investing Wisely", "Entrepreneurship In IOT ","Prototype To Product","Inspire"
    ,"Bulls N Bears","Corporate Roadies","Idea To Product","Best E Cell","Clash Of Corporates","Kickstarter","B-plan","Best Critic"
    ,"On Your Marks","Hire The Best","Lean Startup","Binary Marketing","Creative Thinking","Entrepreneurship In Non-IT Sector","Pitch Perfect"};
    String event_id[] = new String[]{"phoenix","motivate","invest","iot","prototype","inspire","bnb","roadies","i2p","ecell","coc","kickstart","bplan","critic","marks","hire","lean","marketing","creative","en_in_no_it","pitch"};
    DBHandler dbHandler;
    SweetAlertDialog dialog;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spinner);
        spinnereventdetail = (Spinner) findViewById(R.id.event_detail);
        Button apply=(Button)findViewById(R.id.btn_apply) ;
        dbHandler = new DBHandler(getApplicationContext());
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("no_of_rows",String.valueOf(dbHandler.numberOfRows("user")));
                    if(!sharedPreferences.contains("event_id")) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("event_id", event_id[spinnereventdetail.getSelectedItemPosition()]);
                        editor.commit();
                    }
                    else {
                        if (!sharedPreferences.getString("event_id", "").equals(event_id[spinnereventdetail.getSelectedItemPosition()])) {
                            dbHandler.removeUser();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("event_id", event_id[spinnereventdetail.getSelectedItemPosition()]);
                            editor.commit();
                        }
                    }
                    if (dbHandler.numberOfRows("user") == 0) {
                        new SweetAlertDialog(SpinnerPage.this, SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText("Downloading  list")
                                .setContentText("This may take a while..")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                        dialog = new SweetAlertDialog(SpinnerPage.this, SweetAlertDialog.PROGRESS_TYPE)
                                                .setTitleText("Downloading  list")
                                                .setContentText("This may take a while..")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismissWithAnimation();
                                                    }
                                                });
                                        dialog.show();
                                        loadUsers();
                                    }
                                }).show();
                    } else {
                        Intent r = new Intent(getApplicationContext(), ScanRegisterEvent.class);
                        r.putExtra("event_id", event_id[spinnereventdetail.getSelectedItemPosition()]);
                        startActivity(r);
                    }
                }
            });



        ArrayAdapter<String> event_details_list = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, event_details);
        event_details_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnereventdetail.setAdapter(event_details_list);
    }

    private void loadUsers() {
        String URL = "http://entreprenia.org/app/get_registered_users.php?event_id="+event_id[spinnereventdetail.getSelectedItemPosition()];
        Log.d("URL",URL);
        JsonArrayRequest strReq = new JsonArrayRequest(Request.Method.GET,
                URL,null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.d("response", response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject json = response.getJSONObject(i);
                        dbHandler.insertUser(json.getString("user_id"));
                    }
                    dialog.dismissWithAnimation();
                    Intent r = new Intent(getApplicationContext(), ScanRegisterEvent.class);
                    r.putExtra("event_id",event_id[spinnereventdetail.getSelectedItemPosition()]);
                    startActivity(r);
                }catch (JSONException e)
                {
                    Log.d("json_error", response.toString());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("vError", "Error: " + error.getMessage());
                String errorMsg;
                if(error instanceof NoConnectionError)
                    errorMsg = "Network Error";
                else if(error instanceof TimeoutError)
                    errorMsg = "Timeout Error";
                else
                    errorMsg = "Unknown Error";
                Snackbar.make(findViewById(android.R.id.content), errorMsg, Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loadUsers();
                            }
                        }).show();
            }

        });

// Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }

}