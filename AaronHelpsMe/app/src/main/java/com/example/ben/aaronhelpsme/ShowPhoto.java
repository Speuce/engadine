package com.example.ben.aaronhelpsme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ShowPhoto extends AppCompatActivity {

    ImageView iv;
    Bitmap bitmap;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        Bundle extras = getIntent().getExtras();
        bitmap = (Bitmap) extras.getParcelable("IMAGE");

        iv = (ImageView)findViewById(R.id.image);
        iv.setImageBitmap(bitmap);

        back = (Button)findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(ShowPhoto.this, MapsActivity.class);
                startActivity(in);
            }
        });




    }
}
