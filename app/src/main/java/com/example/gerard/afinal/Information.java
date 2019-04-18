package com.example.gerard.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_test_ocr);

        Intent myIntent = getIntent();
        String result = myIntent.getStringExtra("result");

        TextView txt = (TextView) findViewById(R.id.textView2);
        txt.setText(result);

    }
}
