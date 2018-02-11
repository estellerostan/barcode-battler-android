package org.mbds.barcodebattler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.mbds.barcodebattler.battle.BattleActivity;
import org.mbds.barcodebattler.data.ICreature;
import org.mbds.barcodebattler.util.BarcodeBattlerDatabaseAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button generateButton = (Button) findViewById(R.id.generate);
        Button toPoolButton = (Button) findViewById(R.id.toPool);
        Button toBattleButton = (Button) findViewById(R.id.toBattle);

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

        Data data = new Data();

//        getApplicationContext().deleteDatabase("database.db"); // DEBUG

        BarcodeBattlerDatabaseAdapter databaseAdapter = new BarcodeBattlerDatabaseAdapter(getApplicationContext());
        databaseAdapter.open();

        ArrayList<ICreature> superheroes = data.getSuperheroes();

        for (ICreature superhero : superheroes
                ) {

            if (databaseAdapter.getCreature(superhero.getBarcode()).getBarcode() == null) { // Les créatures ne peuvent etre ajoutées qu'une seule fois
                databaseAdapter.insertCreature(
                        superhero.getBarcode(),
                        superhero.getName(),
                        superhero.getEnergy(),
                        superhero.getStrike(),
                        superhero.getDefense(),
                        superhero.getImageName(),
                        superhero.getType()
                );
//                System.out.println( "---------------------------------------------------------------------------" + databaseAdapter.getCreature(superhero.getBarcode()).getId()); // debug
//                System.out.println( "---------------------------------------------------------------------------" + superhero.getName()); // debug
            }
        }
//            }

        // on supprime les anciens ennemis...
        for (ICreature enemy : databaseAdapter.getEnemies()) {
            databaseAdapter.deleteEnemy(enemy.getId());
        }

        //  pour les remplacer par des nouveaux
        ArrayList<ICreature> enemies = data.getEnemies();

        for (ICreature enemy : enemies
                ) {

            if (databaseAdapter.getCreature(enemy.getBarcode()).getBarcode() == null) { // Les créatures ne peuvent etre ajoutées qu'une seule fois
                databaseAdapter.insertCreature(
                        enemy.getBarcode(),
                        enemy.getName(),
                        enemy.getEnergy(),
                        enemy.getStrike(),
                        enemy.getDefense(),
                        enemy.getImageName(),
                        enemy.getType()
                );
//                System.out.println( "---------------------------------------------------------------------------" + databaseAdapter.getCreature(superhero.getBarcode()).getId()); // debug
//                System.out.println( "---------------------------------------------------------------------------" + superhero.getName()); // debug
            }
        }

        ArrayList<ICreature> savedCreatures = databaseAdapter.getCreatures();
        if (savedCreatures != null) {
            for (ICreature savedCreature : savedCreatures
                    ) {
                if (savedCreature.getType().equals("SUPERHERO")) {
                    data.setSuperheroes(savedCreatures);
                }
            }
        }
    }
}
