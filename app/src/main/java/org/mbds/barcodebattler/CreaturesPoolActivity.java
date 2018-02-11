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

import org.mbds.barcodebattler.util.BarcodeBattlerDatabaseAdapter;
import org.mbds.barcodebattler.util.BaseActivity;
import org.mbds.barcodebattler.util.CreaturesPoolAdapter;

public class CreaturesPoolActivity extends BaseActivity {
    //This flag is required to avoid first time onResume refreshing
    static boolean loaded = false;
    private ListView creaturesPool;
    private Data data;
    private BarcodeBattlerDatabaseAdapter databaseAdapter;
    private CreaturesPoolAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_creatures_pool);

        Button addCreatureButton = (Button) findViewById(R.id.addCreature);

        databaseAdapter = new BarcodeBattlerDatabaseAdapter(getApplicationContext());
        databaseAdapter.open();

        data = new Data();

        creaturesPool = (ListView) findViewById(R.id.creaturesPool);

        if (!data.isEmpty()) {
            customAdapter = new CreaturesPoolAdapter(this, R.layout.activity_creatures_pool, data.getSuperheroes());
            creaturesPool.setAdapter(customAdapter);
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

                                final int id = databaseAdapter.getCreature(data.getSuperheroes().get(position).getBarcode()).getId();
                                customAdapter.remove(customAdapter.getItem(position));
                                customAdapter.notifyDataSetInvalidated();
                                int res = databaseAdapter.deleteCreature(id);
//                                System.out.println( "--------------------------------------------------------------------------- id de l'item a supprimer : "+ id); // debug
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

    @Override
    public void onResume() {
        super.onResume();

        if (!loaded) {
            //First time just set the loaded flag true
            loaded = true;
        } else {

            //Reload data
            refresh();
        }
    }

    private void refresh() {
        customAdapter.clear();
        data.setSuperheroes(databaseAdapter.getCreatures());
        customAdapter.addAll(data.getSuperheroes());
        customAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        int selection = creaturesPool.getSelectedItemPosition();
        customAdapter.notifyDataSetChanged();
        creaturesPool.setSelection(selection);
    }
}
