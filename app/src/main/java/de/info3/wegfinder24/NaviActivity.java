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
import java.util.Objects;

import de.info3.wegfinder24.newtwork.JSON_Anfrage.Anfrage;

public class NaviActivity extends AppCompatActivity {
    Integer i = 0;
    Double Strecke;
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
        tvAnweisung.setText(Anweisung);

        // Mit dem Weiter-Button wird immer die nächste Anweisung mit der Strecke angezeigt
        btnWeiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(i).getWayPoints().get(1), daten.get(var).getFeatures().get(weg).getProperties().getWayPoints().get(1))) {
                    tvAnweisung.setText("Sänk ju for träveling wif Wegfinder24!");
                } else {
                    i++;
                    Strecke = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(i - 1).getDistance();
                    Anweisung = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(i).getInstruction();
                    tvAnweisung.setText("Nach " + round(Strecke, 0) + "m\n" + Anweisung);
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
        //ListView = (ListView)findViewById(R.id.ListView);
        //ArrayList<String> arrayList=new ArrayList<>();

        /*for (int k = 0; k<11;k++) {
            Integer j = 0;
            Strecke = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(j - 1).getDistance();
            Anweisung = daten.get(var).getFeatures().get(weg).getProperties().getSegments().get(0).getSteps().get(j).getInstruction();
            Strecke = round(Strecke, 0);
            String strecke = Double.toString(Strecke);
            arrayList.add("Nach " + strecke + "m\n" + Anweisung);
            j++;

            ArrayAdapter arrayAdapter1 = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, arrayList);
            ListView.setAdapter(arrayAdapter1);
        }*/
        //arrayList2.add(result.getDepartureList().get(i).getDateTime().getHour()+result.getDepartureList().get(i).getDateTime().getMinute());


    }

    private double round(double value, int decimalPoints) {
        double d = Math.pow(10, decimalPoints);
        return Math.round(value * d) / d;
    }
}