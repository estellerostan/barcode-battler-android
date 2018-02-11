package org.mbds.barcodebattler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.mbds.barcodebattler.battle.BattleActivity;
import org.mbds.barcodebattler.data.ICreature;
import org.mbds.barcodebattler.util.BarcodeBattlerDatabaseAdapter;
import org.mbds.barcodebattler.util.CreaturesPoolAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button generateButton;
    private Button toPoolButton;
    private Button toBattleButton;

    private Data data;
    private BarcodeBattlerDatabaseAdapter databaseAdapter;
    private ArrayList<ICreature> superheroes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateButton = (Button) findViewById(R.id.generate);
        toPoolButton = (Button) findViewById(R.id.toPool);
        toBattleButton = (Button) findViewById(R.id.toBattle);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreatureGeneratorActivity.class);
                startActivity(intent);
            }
        });

        toPoolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreaturesPoolActivity.class);
                startActivity(intent);
            }
        });

        toBattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BattleActivity.class);
                startActivity(intent);
            }
        });

        databaseAdapter = new BarcodeBattlerDatabaseAdapter(getApplicationContext());
        databaseAdapter.open();

        data = new Data();

//        getApplicationContext().deleteDatabase("database.db"); // DEBUG

        superheroes = data.getSuperheroes();
        databaseAdapter = new BarcodeBattlerDatabaseAdapter(getApplicationContext());
        databaseAdapter.open();

        int i = 0;
        for (ICreature superhero : superheroes
                ) {

            if (databaseAdapter.getCreature(superhero.getBarcode()).getBarcode() == null) { // Les créatures ne peuvent etre ajoutées qu'une seule fois
                databaseAdapter.insertCreature(
                        superhero.getBarcode(),
                        superhero.getName(),
                        superhero.getEnergy(),
                        superhero.getStrike(),
                        superhero.getDefense(),
                        superhero.getImageName()
                );
//                System.out.println( "---------------------------------------------------------------------------" + databaseAdapter.getCreature(superhero.getBarcode()).getId()); // debug
//                System.out.println( "---------------------------------------------------------------------------" + superhero.getName()); // debug
            }
//            }
        }

        ArrayList<ICreature> savedCreatures = databaseAdapter.getCreatures();


        if (savedCreatures != null) {
            data.setSuperheroes(savedCreatures);
        }

        if (!data.isEmpty()) {
            CreaturesPoolAdapter customAdapter = new CreaturesPoolAdapter(this, R.layout.activity_creatures_pool, data.getSuperheroes());
        } else { // TODO: ?
        }
    }
}
