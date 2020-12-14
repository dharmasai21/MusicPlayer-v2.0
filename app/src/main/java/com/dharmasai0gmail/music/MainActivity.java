package com.dharmasai0gmail.music;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
{
    public static final int REQUEST_CODE = 1;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void getPermission()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }
        else
        {
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "permission granted !", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();
    }

    public void songs(android.view.View view)
    {
        android.content.Intent i = new android.content.Intent(this, Main2Activity.class);
        startActivity(i);
    }
    public void videos(android.view.View view)
    {
        android.content.Intent i = new android.content.Intent(this, Main6Activity.class);
        startActivity(i);
//        String path;
//        Intent myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        myFileIntent.setType("*/*");
//        startActivityForResult(myFileIntent,10);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case(10):

                if(resultCode == RESULT_OK){
                    String path = data.getData().getPath();

                    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    Toast.makeText(this, "path = " + path, Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
