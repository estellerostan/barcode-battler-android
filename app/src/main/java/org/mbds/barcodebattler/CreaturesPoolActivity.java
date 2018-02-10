package org.mbds.barcodebattler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.mbds.barcodebattler.util.BaseActivity;

public class CreaturesPoolActivity extends BaseActivity {
    private ListView creaturesPool;
    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_creatures_pool);

        data = new Data();

        creaturesPool = (ListView) findViewById(R.id.creaturesPool);
        creaturesPool.invalidateViews();

        if (!data.isEmpty()) {
            data.getCreatures();
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

        creaturesPool.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // TODO:
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                return false;
            }
        });
    }
}
