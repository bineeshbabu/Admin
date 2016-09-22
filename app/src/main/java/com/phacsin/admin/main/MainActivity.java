package com.phacsin.admin.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.phacsin.admin.scan.ScanParticipation;
import com.phacsin.admin.scan.ScanPayment;
import com.phacsin.admin.scan.ScanPaymentView;
import com.phacsin.admin.R;

public class MainActivity extends AppCompatActivity {

    public Button pay,reg,pay_view,participate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         pay = (Button) findViewById(R.id.payment);
         reg = (Button) findViewById(R.id.btn_register);
        pay_view = (Button) findViewById(R.id.view_payment);
        participate = (Button) findViewById(R.id.participation);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r = new Intent(getApplicationContext(), ScanPayment.class);
                startActivity(r);
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r = new Intent(getApplicationContext(), SpinnerPage.class);
                startActivity(r);
            }
        });
        pay_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r = new Intent(getApplicationContext(), ScanPaymentView.class);
                startActivity(r);
            }
        });
        participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r = new Intent(getApplicationContext(), ScanParticipation.class);
                startActivity(r);
            }
        });
    }



}