package com.example.ben.aaronhelpsme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import java.util.Map;

public class LogIn extends AppCompatActivity {

    private TextView Textv;
    private Button next;
    private EditText user,pass;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        CodeStuff.setLoginReference(this);
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
                    username = user.getText().toString();
                    CodeStuff.verifyUser(username, pass.getText().toString());
                    //startActivity(new Intent(LogIn.this, MapsActivity.class));
            }
        });


    }
    public void userVerified(){
        ConnectionManager.user = this.username;
        startActivity(new Intent(LogIn.this, MapsActivity.class));
    }
    public void userNotVerified(){
        System.out.println("User not verified.");
        pass.setText("");
    }

}
