package com.example.ben.aaronhelpsme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {

    private Button next;
    private EditText username,email,pass1,passTrue;
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CodeStuff.setSignupReference(this);
        setContentView(R.layout.activity_sign_up);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        username = (EditText) findViewById(R.id.editText1);
        email = (EditText) findViewById(R.id.editText2);
        pass1 = (EditText) findViewById(R.id.editText3);
        passTrue = (EditText) findViewById(R.id.editText4);

        next = (Button) findViewById(R.id.Sign);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String passO=pass1.getText().toString();
                String passT=passTrue.getText().toString();

                if (passO.equals(passT)) {
                    user= username.getText().toString();
                    CodeStuff.registerUser(user, email.getText().toString(), passO);

                } else{
                    pass1.setText("");
                    passTrue.setText("");
                }

            }
        });
    }
    public void userRegistered(){
        ConnectionManager.user = user;
        System.out.println("User registered.");
        startActivity(new Intent(SignUp.this, MapsActivity.class));
    }
    public void userFailedRegistration(String error){
        username.setText("");
        pass1.setText("");
        passTrue.setText("");
        System.out.println("User failed registration: " + error);
    }
}
