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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    startActivity(new Intent(SignUp.this, MapsActivity.class));
                } else{
                    pass1.setText("");
                    passTrue.setText("");
                }

            }
        });
    }
}
