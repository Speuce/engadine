package com.example.ben.aaronhelpsme;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Button;

import java.io.ByteArrayOutputStream;


public class MarkerAdd extends AppCompatActivity {

    Button map, camera;
    EditText comment;
    boolean done = false;
    String strName = null;
    String disaster = null;
    String commentS = null;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    Bitmap imageBitmap;
    Intent i;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_add);
        addListenerOnButton();

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.fire:
                if (checked)
                    disaster = "fire";
                break;
            case R.id.flood:
                if (checked)
                    disaster = "flood";
                    break;
            case R.id.icestorm:
                if (checked)
                    disaster = "icestorm";
                    break;
            case R.id.windstorm:
                if (checked)
                    disaster = "windstorm";
                    break;
            case R.id.riot:
                if (checked)
                    disaster = "riot";
                    break;
            case R.id.accident:
                if (checked)
                    disaster = "accident";
                    break;
            case R.id.tornado:
                if (checked)
                    disaster = "tornado";
                    break;
            case R.id.hurricane:
                if (checked)
                    disaster = "hurricane";
                    break;
            case R.id.earthquake:
                if (checked)
                    disaster = "earthquake";
                    break;
            case R.id.tsunami:
                if (checked)
                    disaster = "tsunami";
                    break;
            case R.id.volcano:
                if (checked)
                    disaster = "volcano";
                    break;
            case R.id.avalanche:
                if (checked)
                    disaster = "avalanche";
                    break;
        }
    }

    public void addListenerOnButton() {

        map = (Button) findViewById(R.id.add);

        comment = (EditText) findViewById(R.id.comment);
        camera = (Button) findViewById(R.id.picture);


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i = new Intent(MarkerAdd.this, MapsActivity.class);

                //NEW FLAGGG
                if(imageBitmap != null) {
                    Bundle extras = getIntent().getExtras();
                    commentS = comment.getText().toString();
                    CodeStuff.getCon().newFlag(ConnectionManager.user, disaster + " " + commentS,
                            extras.getDouble("lat"), extras.getDouble("lon"), Flag.getByteArray(imageBitmap));
                    commentS = comment.getText().toString();
                    strName = "yes";
                }
//                i.putExtra("STRING_I_NEED", strName);
//                i.putExtra("DISASTER", disaster);
//                i.putExtra("COMMENT", commentS);
//
//                i.putExtra("IMAGE",imageBitmap);
                if (disaster != null&&imageBitmap != null) {
                    startActivity(i);
                } else {

                }

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                }
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");


        }
    }



}
