package org.mbds.barcodebattler;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.mbds.barcodebattler.data.ICreature;
import org.mbds.barcodebattler.util.BarcodeToCreatureConverter;
import org.mbds.barcodebattler.util.BaseActivity;
import org.mbds.barcodebattler.util.Injector;

import javax.inject.Inject;

public class CreatureGeneratorActivity extends BaseActivity {

    @Inject
    ICreature creature;

    private EditText barcodeEntry;
    private String barcode;
    private Button generateButton;
    private TextView energyView;
    private TextView strikeView;
    private TextView defenceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.getInstance().getAppComponent().inject(this); // remplace le new Creature

        setContentView(R.layout.activity_creature_generator);

        if (this.getSupportActionBar() != null)
            this.getSupportActionBar().setTitle("Barcode Generator & Tester :");

        barcodeEntry = (EditText) findViewById(R.id.barcodeEntry);
        generateButton = (Button) findViewById(R.id.generate);
        energyView = (TextView) findViewById(R.id.energy);
        strikeView =  (TextView) findViewById(R.id.strike);
        defenceView = (TextView) findViewById(R.id.defense);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcode = barcodeEntry.getText().toString();

                BarcodeToCreatureConverter converter = new BarcodeToCreatureConverter(creature);
                creature = converter.convert(barcode);

                String energy = Integer.toString(creature.getEnergy());
                String strike = Integer.toString(creature.getStrike());
                String defence = Integer.toString(creature.getDefense());

                energyView.setText(energy);
                strikeView.setText(strike);
                defenceView.setText(defence);
            }
        });
    }
}
