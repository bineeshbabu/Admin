package com.phacsin.admin.scan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;
import com.phacsin.admin.DBHandler;
import com.phacsin.admin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Bineesh P Babu on 22-09-2016.
 */
public class ScanRegisterEvent extends AppCompatActivity  implements ZXingScannerView.ResultHandler{
    ZXingScannerView scannerView;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_register);
        scannerView = (ZXingScannerView) findViewById(R.id.scanner_view);
        scannerView.setResultHandler(this);
        scannerView.startCamera();
        dbHandler = new DBHandler(getApplicationContext());
    }


    @Override
    public void handleResult(Result result) {
        scannerView.stopCamera();
        scannerView.setResultHandler(this);
        final String contents = result.getText();
        final String event_id = getIntent().getStringExtra("event_id");
        if (!dbHandler.eventExists(contents, event_id)) {
            new SweetAlertDialog(ScanRegisterEvent.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(contents)
                    .setContentText("Register for event")
                    .setCancelText("Cancel")
                    .setConfirmText("Register")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            dbHandler.insertEvent(contents, event_id);
                            scannerView.startCamera();
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            scannerView.startCamera();
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        } else {
            new SweetAlertDialog(ScanRegisterEvent.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(contents)
                    .setContentText("Already Registered for this event")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            scannerView.startCamera();
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })

                    .show();
        }
    }
}
