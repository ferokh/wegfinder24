package de.info3.wegfinder24;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.info3.wegfinder24.newtwork.JSON_Anfrage.Anfrage;

public class NaviActivity extends AppCompatActivity {
    Integer i = 0;
    Double Strecke;
    Double Strecke_weiter;
    String Anweisung;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);
        ListView ListView;
        Button btnZuruck = this.findViewById(R.id.btnZurück);
        Button btnWeiter = this.findViewById(R.id.btnWeiter);

        // Abgreifen der Daten aus der vorherigen Activity
        Intent WAintent = this.getIntent();
        ArrayList<Anfrage> daten = (ArrayList<Anfrage>) WAintent.getSerializableExtra("daten");
        Integer var = (Integer) WAintent.getSerializableExtra("variante");
        Integer weg = (Integer) WAintent.getSerializableExtra("weg");

        // Anzeigen der ersten Richtungsanweisung
        TextView tvAnweisung = this.findViewById(R.id.tvAnweisung); //TextView für die Entfernung - Auto
        Anweisung = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(i).getInstruction();
        Strecke = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(i).getDistance();
        String strecke = Double.toString(Strecke);
        tvAnweisung.setText(Anweisung + "\nfür " + strecke + "m");


        // Mit dem Weiter-Button wird immer die nächste Anweisung mit der Strecke angezeigt
        btnWeiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(i).getWayPoints().get(1), daten.get(var).getFeatures().get(weg).getProperties().getWayPoints().get(1))) {
                    tvAnweisung.setText(Anweisung+"\nSie haben Iht Ziel erreicht.\nSänk ju for träveling wif Wegfinder24!");
                } else {
                    i++;
                    Strecke = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(i - 1).getDistance();
                    Anweisung = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(i).getInstruction();
                    Strecke_weiter = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(i).getDistance();
                    tvAnweisung.setText("nach " + round(Strecke, 0) + "m\n" + Anweisung + "\nfür " + round(Strecke_weiter, 0) + "m folgen");
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
        ListView = (ListView)findViewById(R.id.ListView);
        ArrayList<String> arrayList=new ArrayList<>();

        List wayPoints = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps();

        Integer wayPoint = wayPoints.size();


        for (int k = 0; k<wayPoint;k++) {
            Strecke = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(k).getDistance();
            Anweisung = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(k).getInstruction();
            Strecke = round(Strecke, 0);


            if (k == 0)
            {
                Anweisung = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(k).getInstruction();
                arrayList.add(Anweisung);
            }
            else if (k == wayPoint-1)
                {

                    arrayList.add(Anweisung);
                }
            else{
                Strecke = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(k-1).getDistance();
                Anweisung = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(k).getInstruction();
                Strecke = round(Strecke, 0);
                strecke = Double.toString(Strecke);
                arrayList.add( "nach " + strecke + "m\n" + Anweisung);
            }

            ArrayAdapter arrayAdapter1 = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, arrayList);
            ListView.setAdapter(arrayAdapter1);
        }
        //arrayList2.add(result.getDepartureList().get(i).getDateTime().getHour()+result.getDepartureList().get(i).getDateTime().getMinute());


    }

    private double round(double value, int decimalPoints) {
        double d = Math.pow(10, decimalPoints);
        return Math.round(value * d) / d;
    }
}