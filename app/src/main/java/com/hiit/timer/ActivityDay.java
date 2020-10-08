package com.hiit.timer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityDay extends android.app.Activity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        String extra = getIntent().getStringExtra("nbeEvent");
        TextView tv = findViewById(R.id.eventsTextView);
        tv.setText(extra);

        Button buttonRetour = (Button)findViewById(R.id.buttonRetour);

        buttonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                //intent.putExtra("nbeEvent", String.valueOf(currentDay)); //Optional parameters
                v.getContext().startActivity(intent);
                //Toast.makeText(getContext(), "CA MARCHE", Toast.LENGTH_LONG).show();
            }
        });



    }

}


