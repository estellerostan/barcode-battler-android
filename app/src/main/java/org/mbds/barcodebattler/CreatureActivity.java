package org.mbds.barcodebattler;

import android.os.Bundle;
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

        Bundle extras = getIntent().getExtras();
        ICreature creature = extras.getParcelable("creature");

        assert creature != null;
        energy.setText(String.valueOf(creature.getEnergy()));
        strike.setText(String.valueOf(creature.getStrike()));
        defense.setText(String.valueOf(creature.getDefense()));
    }
}
