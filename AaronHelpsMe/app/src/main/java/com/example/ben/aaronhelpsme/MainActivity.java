package com.example.ben.aaronhelpsme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    private EditText edittext1, edittext2,edittext3;
    private Button buttonSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();
    }



    public void addListenerOnButton() {
        edittext1 = (EditText) findViewById(R.id.editText1);
        edittext2 = (EditText) findViewById(R.id.editText2);
        edittext3 = (EditText) findViewById(R.id.editText3);
        buttonSum = (Button) findViewById(R.id.Button);

        buttonSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value1=edittext1.getText().toString();
                String value2=edittext2.getText().toString();
                String value3=edittext3.getText().toString();

                if (value2.equals(value3)){
                    Intent in = new Intent(MainActivity.this, Main2Activity.class);
                    in.putExtra("username", value1);
                    startActivity(in);

                } else {
                    Toast.makeText(getApplicationContext(),"Passwords are different", Toast.LENGTH_LONG).show();
                    edittext2.setText("");
                    edittext3.setText("");

                }



            }
        });
    }
}
