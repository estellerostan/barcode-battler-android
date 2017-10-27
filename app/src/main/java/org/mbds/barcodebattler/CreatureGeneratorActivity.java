package org.mbds.barcodebattler;

import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.mbds.barcodebattler.data.Creature;
import org.mbds.barcodebattler.util.BarcodeToCreatureConverter;

public class CreatureGeneratorActivity extends AppCompatActivity {

    private EditText barcodeEntry;
    private String barcode;
    private Button generateButton;
    private TextView energyView;
    private TextView strikeView;
    private TextView defenceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creature_generator);

        if (this.getSupportActionBar() != null)
            this.getSupportActionBar().setTitle("Barcode Generator & Tester :");

        barcodeEntry = (EditText) findViewById(R.id.barcodeEntry);
        generateButton = (Button) findViewById(R.id.generate);
        energyView = (TextView) findViewById(R.id.energy);
        strikeView =  (TextView) findViewById(R.id.strike);
        defenceView = (TextView) findViewById(R.id.defence);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcode = barcodeEntry.getText().toString();
                Creature creature = BarcodeToCreatureConverter.convert(barcode);

                String energy = Integer.toString(creature.getEnergy());
                String strike = Integer.toString(creature.getStrike());
                String defence = Integer.toString(creature.getDefence());

                energyView.setText(energy);
                strikeView.setText(strike);
                defenceView.setText(defence);
            }
        });
    }
}
