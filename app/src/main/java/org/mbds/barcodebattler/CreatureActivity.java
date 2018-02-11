package org.mbds.barcodebattler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.mbds.barcodebattler.battle.BattleActivity;
import org.mbds.barcodebattler.data.ICreature;
import org.mbds.barcodebattler.util.BaseActivity;

public class CreatureActivity extends BaseActivity {
    private ICreature creature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superhero);

        TextView name = (TextView) findViewById(R.id.name);
        TextView energy = (TextView) findViewById(R.id.energy);
        TextView strike = (TextView) findViewById(R.id.strike);
        TextView defense = (TextView) findViewById(R.id.defense);
        ImageView icon = (ImageView) findViewById(R.id.icon);
        Button addToBattle = (Button) findViewById(R.id.addToBattle);

        Bundle extras = getIntent().getExtras();
        creature = extras.getParcelable("creature");

        assert creature != null;
        name.setText(String.valueOf(creature.getName()));
        energy.setText(String.valueOf(creature.getEnergy()));
        strike.setText(String.valueOf(creature.getStrike()));
        defense.setText(String.valueOf(creature.getDefense()));

        String mDrawableName = creature.getImageName();
        int resID = getResources().getIdentifier(mDrawableName, "drawable", getApplicationContext().getPackageName());

        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                resID);

        icon.setImageBitmap(bitmap);

        addToBattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatureActivity.this, BattleActivity.class);

                intent.putExtra("creatureP1", creature);
//                setResult(RESULT_OK, intent);
                CreatureActivity.this.startActivity(intent);
//                finish();
            }
        });
    }
}
