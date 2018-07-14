package com.example.ben.aaronhelpsme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

public class LogIn extends AppCompatActivity {

    private TextView Textv;
    private Button next;
    private EditText user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);










        addListenerOnButton();

    }
    public void addListenerOnButton() {

        user = (EditText) findViewById(R.id.enterUsername);
        pass = (EditText) findViewById(R.id.enterPassword);

        next = (Button) findViewById(R.id.log);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    startActivity(new Intent(LogIn.this, MapsActivity.class));





            }
        });

    }

}
