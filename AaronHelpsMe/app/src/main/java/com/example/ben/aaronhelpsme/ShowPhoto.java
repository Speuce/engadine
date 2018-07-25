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
    //Bitmap bitmap;
    Button back;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CodeStuff.setPhotoReference(this);
        System.out.println("creating..");
        setContentView(R.layout.activity_show_photo);
        Bundle extras = getIntent().getExtras();
        System.out.println("creating2");
        id = extras.getInt("ID");
        CodeStuff.requestImage(id);
        //bitmap = (Bitmap) extras.getParcelable("IMAGE");

        iv = (ImageView)findViewById(R.id.image);
        //iv.set

        back = (Button)findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(ShowPhoto.this, MapsActivity.class);
                startActivity(in);
            }
        });

    }
    public void setImage(final Bitmap b,final int i){
        Runnable r = new Runnable(){
            @Override
            public void run(){
                System.out.println("got here..");

                    System.out.println("got hereeee..");
                    if(b == null){
                        System.out.println("b is null.");
                    }
                    //iv.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false));
                    iv.setImageBitmap(b);
            }
        };
        this.runOnUiThread(r);

    }
}
