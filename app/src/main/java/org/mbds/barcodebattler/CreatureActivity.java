package org.mbds.barcodebattler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.mbds.barcodebattler.data.ICreature;
import org.mbds.barcodebattler.util.BaseActivity;

public class CreatureActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creature);

        TextView energy = (TextView) findViewById(R.id.energy);
        TextView strike = (TextView) findViewById(R.id.strike);
        TextView defense = (TextView) findViewById(R.id.defense);
        ImageView icon = (ImageView) findViewById(R.id.icon);

        Bundle extras = getIntent().getExtras();
        ICreature creature = extras.getParcelable("creature");

        assert creature != null;
        energy.setText(String.valueOf(creature.getEnergy()));
        strike.setText(String.valueOf(creature.getStrike()));
        defense.setText(String.valueOf(creature.getDefense()));

        String mDrawableName = creature.getImageName();
        int resID = getResources().getIdentifier(mDrawableName, "drawable", getApplicationContext().getPackageName());

        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                resID);

        icon.setImageBitmap(bitmap);
    }
}
