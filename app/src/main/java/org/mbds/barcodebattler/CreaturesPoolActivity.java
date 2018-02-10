package org.mbds.barcodebattler;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.mbds.barcodebattler.data.ICreature;
import org.mbds.barcodebattler.util.BarcodeBattlerDatabaseAdapter;
import org.mbds.barcodebattler.util.BaseActivity;

import java.util.ArrayList;

public class CreaturesPoolActivity extends BaseActivity {
    //This flag is required to avoid first time onResume refreshing
    static boolean loaded = false;
    private ListView creaturesPool;
    private Data data;
    private BarcodeBattlerDatabaseAdapter databaseAdapter;
    private ArrayList<ICreature> superheroes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_creatures_pool);

        Button addCreatureButton = (Button) findViewById(R.id.addCreature);

        databaseAdapter = new BarcodeBattlerDatabaseAdapter(getApplicationContext());
        databaseAdapter.open();

        data = new Data();

//        getApplicationContext().deleteDatabase("database.db"); // DEBUG

        superheroes = data.getSuperheroes();
        databaseAdapter = new BarcodeBattlerDatabaseAdapter(getApplicationContext());
        databaseAdapter.open();

        for (ICreature superheroe : superheroes
                ) {
            databaseAdapter.insertCreature(superheroe.getName(),
                    superheroe.getEnergy(),
                    superheroe.getStrike(),
                    superheroe.getDefense(),
                    superheroe.getImageName()
            );
        }

        ArrayList<ICreature> savedCreatures = databaseAdapter.getCreatures();

        creaturesPool = (ListView) findViewById(R.id.creaturesPool);
        creaturesPool.invalidateViews();

        if (savedCreatures != null) {
            data.setSuperheroes(savedCreatures);
        }

        if (!data.isEmpty()) {
            creaturesPool.setAdapter(data);
        } else { // TODO: ?
        }

        creaturesPool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CreaturesPoolActivity.this, CreatureActivity.class);
                intent.putExtra("creature", data.getItem(position));
                CreaturesPoolActivity.this.startActivity(intent);
            }
        });

        creaturesPool.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(CreaturesPoolActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(CreaturesPoolActivity.this);
                }
                builder.setTitle("Supprimer une créature")
                        .setMessage("Êtes-vous sûr de vouloir supprimer " + data.getSuperheroes().get(position).getName() + " ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                data.getSuperheroes().remove(position);
                                creaturesPool.invalidateViews();
                                int res = databaseAdapter.deleteCreature(position);
                                Toast.makeText(getApplicationContext(), "Suppression de " + res + " créature", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                Toast.makeText(getApplicationContext(), "Annulé", Toast.LENGTH_SHORT).show();
//                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });

        addCreatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Ajouter une créature ou un équippement en scannant un code barre #1
            }
        });
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (!loaded) {
//            //First time just set the loaded flag true
//            loaded = true;
//        } else {
//
//            //Reload data
//            data.setSuperheroes(databaseAdapter.getCreatures());
//
//            // TODO:
//            adapter.notifyDatasetChanged();
//        }
//    }

}
