package de.info3.wegfinder24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import de.info3.wegfinder24.newtwork.JSON_Anfrage.Anfrage;

public class NaviActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        Button btnZuruck =this.findViewById(R.id.btnZurück);
        Button btnWeiter =this.findViewById(R.id.btnWeiter);

        Intent WAintent = this.getIntent();
        Anfrage daten = (Anfrage) WAintent.getSerializableExtra("daten");


        TextView tvAnweisung = this.findViewById(R.id.tvAnweisung); //TextView für die Entfernung - Auto
        Double Strecke = daten.getFeatures().get(0).getProperties().getSegments().get(0).getSteps().get(0).getDistance();
        String Anweisung = daten.getFeatures().get(0).getProperties().getSegments().get(0).getSteps().get(0).getInstruction();
        tvAnweisung.setText("Nach " + round(Strecke, 0) + "m\n" + Anweisung);


    }
    private double round (double value, int decimalPoints)
    {
        double d = Math.pow(10,decimalPoints);
        return Math.round(value * d)/d;
    }
}