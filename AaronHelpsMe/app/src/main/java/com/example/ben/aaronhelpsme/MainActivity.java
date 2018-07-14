package com.example.ben.aaronhelpsme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    private EditText edittext1, edittext2,edittext3;
    private Button buttonLog, buttonSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();
    }



    public void addListenerOnButton() {
        /*
        edittext1 = (EditText) findViewById(R.id.editText1);
        edittext2 = (EditText) findViewById(R.id.editText2);
        edittext3 = (EditText) findViewById(R.id.editText3);
        */
        buttonLog = (Button) findViewById(R.id.Login);
        buttonSign = (Button) findViewById(R.id.Sign);

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                String value1=edittext1.getText().toString();
                String value2=edittext2.getText().toString();
                String value3=edittext3.getText().toString();
*/

                    Intent in = new Intent(MainActivity.this, LogIn.class);

                    startActivity(in);




            }
        });

        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent in2 = new Intent(MainActivity.this, SignUp.class);

                startActivity(in2);




            }
        });
    }
}
