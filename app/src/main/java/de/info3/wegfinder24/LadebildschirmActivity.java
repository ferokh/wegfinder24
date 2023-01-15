package de.info3.wegfinder24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;



public class LadebildschirmActivity extends AppCompatActivity {
    public final int LOAD_TIME = 1800; //wie lange soll der Startbildschirm angezeigt werden
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ladebildschirm);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LadebildschirmActivity.this, EingabeActivity.class); //nach der oben angegebenen Zeit wird die Activity EingabeActivity gestartet
                startActivity(intent);
                finish();
            }
        }, LOAD_TIME);
    }
}
