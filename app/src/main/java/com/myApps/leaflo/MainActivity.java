package com.myApps.leaflo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public  static final int RequestPermissionCode  = 1 ;
    private Spinner mSpinner;
    private TextView mTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinner = findViewById(R.id.progress_spin);
        mTv = findViewById(R.id.percentage_txt);
        EnableRuntimePermission();
        Button btnCamera = findViewById(R.id.capture_btn);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mTv.setVisibility(View.INVISIBLE);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(takePictureIntent, 7);
            }
        });

        Button btnGallery = findViewById(R.id.gallery_btn);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        Button btnAnalyse = findViewById(R.id.analyse_btn);
        btnAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // mSpinner.setVisibility(View.VISIBLE);
             //   mSpinner.animate();
               int percentage = AverageImg();
               String str = String.valueOf(percentage);
                mSpinner.setVisibility(View.GONE);

                mTv.setVisibility(View.VISIBLE);
                mTv.setText(str + "%");
               // Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        //    Log.e("picked", data.getData().toString());
        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (requestCode == 7 && resultCode == RESULT_OK) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            ImageView iv = findViewById(R.id.image);
            iv.setImageBitmap(bitmap);
        }
        if (requestCode == 0 && resultCode == RESULT_OK) {


            ImageView iv = findViewById(R.id.image);
            iv.setImageURI(data.getData());
        }
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CAMERA))
        {

            Toast.makeText(MainActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    private int AverageImg()
    {
        ImageView iv = findViewById(R.id.image);
        BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int pixelCount = 0;

        for (int y = 0; y < bitmap.getHeight(); y++)
        {
            for (int x = 0; x < bitmap.getWidth(); x++)
            {
                int c = bitmap.getPixel(x, y);

                pixelCount++;
                redBucket += Color.red(c);
                greenBucket += Color.green(c);
                blueBucket += Color.blue(c);
                // does alpha matter?
            }
        }
        int averageColor = Color.rgb(redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);

        // int red   = (averageColor & 0x00ff0000) >> 16;
         int green = (averageColor & 0x0000ff00) >> 8;
       //  int blue  = (averageColor & 0x000000ff);
        int greenPercentage = (int) ((green / 255.0) * 100);
        return greenPercentage;
    }
}