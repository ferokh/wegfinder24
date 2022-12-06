package de.info3.wegfinder24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;



public class Ladebildschirm extends AppCompatActivity {
    public final int LOAD_TIME = 1500; //wie lange soll der Ladescreen angezeigt werden
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ladebildschirm);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Ladebildschirm.this, Eingabe.class); //der Ladebildschirm
                startActivity(intent);
            }
        }, LOAD_TIME);
    }
}
