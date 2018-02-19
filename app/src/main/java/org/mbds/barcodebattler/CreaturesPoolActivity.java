package org.mbds.barcodebattler;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.mbds.barcodebattler.data.Creature;
import org.mbds.barcodebattler.data.ICreature;
import org.mbds.barcodebattler.util.BarcodeBattlerDatabaseAdapter;
import org.mbds.barcodebattler.util.BarcodeToCreatureConverter;
import org.mbds.barcodebattler.util.BaseActivity;
import org.mbds.barcodebattler.util.CreaturesPoolAdapter;

import java.util.ArrayList;

public class CreaturesPoolActivity extends BaseActivity {
    private static final String TAG = CreaturesPoolActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
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
            customAdapter.clear();
//            data.setSuperheroes(databaseAdapter.getCreatures());

            data.setSuperheroes(new ArrayList<ICreature>());
            ArrayList<ICreature> savedCreatures = databaseAdapter.getCreatures();
            if (savedCreatures != null) {
                for (ICreature savedCreature : savedCreatures
                        ) {
                    if (savedCreature.getType().equals("SUPERHERO")) {
                        data.addSuperheroe(savedCreature);
                    }
                }
            }

            customAdapter.addAll(data.getSuperheroes());
            customAdapter.notifyDataSetChanged();
            creaturesPool.setAdapter(customAdapter);
        }

        if ( getIntent().getExtras() != null ) {
            Log.d(TAG, "[game] request to creature pool");
            String msg = getIntent().getExtras().getString("msg");
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
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
                // TODO: Ajouter un équippement en scannant un code barre #1
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            BarcodeDetector detector =
                    new BarcodeDetector.Builder(getApplicationContext())
//                                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                            .build();
//                if(!detector.isOperational()){
//                    txtView.setTe2xt("Could not set up the detector!");
//                    return;
//                }

            assert imageBitmap != null;
            Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
            SparseArray<Barcode> barcodes = detector.detect(frame);

            String thisCode = barcodes.valueAt(0).rawValue;
//          System.out.println( "--------------------------------------------------------------------------- barcode de l'item ajoute : "+ thisCode);
            ICreature creature = new Creature(thisCode, "**Custom**", 1, 1, 1, "blank", "SUPERHERO");
            BarcodeToCreatureConverter converter = new BarcodeToCreatureConverter(creature);
            creature = converter.convert(thisCode);

            if (databaseAdapter.getCreature(creature.getBarcode()).getBarcode() == null) { // Les créatures ne peuvent etre ajoutées qu'une seule fois
                databaseAdapter.insertCreature(
                        creature.getBarcode(),
                        creature.getName(),
                        creature.getEnergy(),
                        creature.getStrike(),
                        creature.getDefense(),
                        creature.getImageName(),
                        creature.getType()
                );
                ICreature addedCreature = databaseAdapter.getCreature(creature.getBarcode());
                databaseAdapter.updateCreature(addedCreature.getId(), "Barcode challenger " + addedCreature.getId());

                customAdapter.add(addedCreature);
                customAdapter.notifyDataSetChanged();
            }
        }
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
        data.setSuperheroes(databaseAdapter.getSuperheroes());
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
