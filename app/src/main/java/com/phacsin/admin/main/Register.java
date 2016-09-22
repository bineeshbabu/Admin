package com.phacsin.admin.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.phacsin.admin.R;
import com.phacsin.admin.scan.ScanRegisterEvent;

/**
 * Created by Bineesh P Babu on 21-09-2016.
 */
public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinnervenue,spinnerevent,spinnereventdetail;
    private String[] venues = { "Venue 1", "Venue 2", "Venue 3", "Venue 4",
            "Venue 5", "Venue 6", "Venue 7", "Venue 8"};
    private String[] events = { "Workshops", "Panel Discussions", "Competitions", "Talks",};
    private String[] event_details = { "i2p", "bplan", "bullsnabears", "craetive ",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        spinnervenue = (Spinner) findViewById(R.id.venue);
        spinnerevent = (Spinner) findViewById(R.id.events);
        spinnereventdetail = (Spinner) findViewById(R.id.event_detail);
        Button apply=(Button)findViewById(R.id.btn_apply) ;
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r = new Intent(getApplicationContext(), ScanRegisterEvent.class);
                startActivity(r);
            }
        });

        ArrayAdapter<String> venue_list = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, venues);
        venue_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> event_list = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, events);
        event_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> event_details_list = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, event_details);
        event_details_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnervenue.setAdapter(venue_list);
        spinnervenue.setOnItemSelectedListener(this);

        spinnerevent.setAdapter(event_list);
        spinnerevent.setOnItemSelectedListener(this);

        spinnereventdetail.setAdapter(event_details_list);
        spinnereventdetail.setOnItemSelectedListener(this);
    }
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnervenue.setSelection(position);
        String selState = (String) spinnervenue.getSelectedItem();

        spinnerevent.setSelection(position);
        String sel1 = (String) spinnerevent.getSelectedItem();

        spinnereventdetail.setSelection(position);
        String sel2 = (String) spinnereventdetail.getSelectedItem();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}