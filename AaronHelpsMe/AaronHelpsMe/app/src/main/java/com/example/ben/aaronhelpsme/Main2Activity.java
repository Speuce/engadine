package com.example.ben.aaronhelpsme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    private TextView Textv;
    private Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Textv = (TextView)findViewById(R.id.tvd);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();


        if(b!=null)
        {
            String j =(String) b.get("username");
            Textv.setText("Welcome " + j);
        }

//        addListenerOnButton();

    }

    public void sendMessage(View view){

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
//    public void addListenerOnButton() {
//
//        next = (Button) findViewById(R.id.nextbtn);
//
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                    startActivity(new Intent(Main2Activity.this, MapsActivity.class));
//
//
//
//
//
//            }
//        });
//
//    }

}
