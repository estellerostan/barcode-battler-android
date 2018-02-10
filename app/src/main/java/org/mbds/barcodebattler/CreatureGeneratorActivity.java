package org.mbds.barcodebattler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

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

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView myImageView = (ImageView) findViewById(R.id.imgview);
                Bitmap myBitmap = BitmapFactory.decodeResource(
                        getApplicationContext().getResources(),
                        R.drawable.beast_feast__flip_side);
                myImageView.setImageBitmap(myBitmap);

                BarcodeDetector detector =
                        new BarcodeDetector.Builder(getApplicationContext())
//                                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                                .build();
//                if(!detector.isOperational()){
//                    txtView.setTe2xt("Could not set up the detector!");
//                    return;
//                }

                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Barcode> barcodes = detector.detect(frame);

                Barcode thisCode = barcodes.valueAt(0);
                TextView txtView = (TextView) findViewById(R.id.txtContent);
                txtView.setText(thisCode.rawValue);
            }
        });
    }
}
