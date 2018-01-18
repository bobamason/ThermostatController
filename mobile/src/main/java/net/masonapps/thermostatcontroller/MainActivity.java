package net.masonapps.thermostatcontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSetButtonClicked(View view) {
        if(view.getId() == R.id.buttonStart){
            
        } else if(view.getId() == R.id.buttonEnd){

        }
    }
}
