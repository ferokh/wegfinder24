package de.info3.wegfinder24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import de.info3.wegfinder24.newtwork.JSON_Anfrage.Anfrage;

public class NaviActivity extends AppCompatActivity {
    Integer i = 0;
    Double Strecke;
    String Anweisung;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        Button btnZuruck = this.findViewById(R.id.btnZurück);
        Button btnWeiter = this.findViewById(R.id.btnWeiter);

        Intent WAintent = this.getIntent();
        ArrayList<Anfrage> daten = (ArrayList<Anfrage>) WAintent.getSerializableExtra("daten");
        Integer var = (Integer) WAintent.getSerializableExtra("variante");
        Integer weg = (Integer) WAintent.getSerializableExtra("weg");


        TextView tvAnweisung = this.findViewById(R.id.tvAnweisung); //TextView für die Entfernung - Auto
        Strecke = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(i).getDistance();
        Anweisung = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(i).getInstruction();
        tvAnweisung.setText(Anweisung);


        btnWeiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(i).getWayPoints().get(1) !=
                        daten.get(var).getFeatures().get(weg).getProperties().getWayPoints().get(1) ) {
                    i++;
                    Strecke = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(i-1).getDistance();
                    Anweisung = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(i).getInstruction();
                    tvAnweisung.setText("Nach " + round(Strecke, 0) + "m\n" + Anweisung);
                }
                else {
                    tvAnweisung.setText("Sänk ju for träveling wif Wegfinder24!");
                }
            }
        });
        btnZuruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NaviActivity.this, WegActivity.class);
                intent.putExtra("daten", daten);
                intent.putExtra("variante", var);
                startActivity(intent);
            }
        });
    }
    private double round (double value, int decimalPoints)
    {
        double d = Math.pow(10,decimalPoints);
        return Math.round(value * d)/d;
    }
}