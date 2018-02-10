package org.mbds.barcodebattler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.mbds.barcodebattler.battle.BattleActivity;

public class MainActivity extends AppCompatActivity {

    private Button generateButton;
    private Button toPoolButton;
    private Button toBattleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateButton = (Button) findViewById(R.id.generate);
        toPoolButton = (Button) findViewById(R.id.toPool);
        toBattleButton = (Button) findViewById(R.id.toBattle);

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
    }
}
