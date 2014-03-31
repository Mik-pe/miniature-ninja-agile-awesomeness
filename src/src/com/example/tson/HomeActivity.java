package com.example.tson;

//IMPORT OUR OWN CLASSES
import tson_utilities.*;

//IMPORT ANDROID
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

//IMPORT OTHER

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
}
