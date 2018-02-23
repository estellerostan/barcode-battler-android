package org.mbds.barcodebattler;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.mbds.barcodebattler.data.Creature;
import org.mbds.barcodebattler.data.ICreature;
import org.mbds.barcodebattler.util.BarcodeBattlerDatabaseAdapter;
import org.mbds.barcodebattler.util.BarcodeToCreatureConverter;
import org.mbds.barcodebattler.util.ImageHelper;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

class Data implements ListAdapter {
    private ArrayList<ICreature> superheroes;
    private ArrayList<ICreature> enemies;
    private BarcodeToCreatureConverter converter;

    private Context context;

    Data(Context context) {
        this.context = context;
        superheroes = new ArrayList<>();
        addSuperheroes();
        enemies = new ArrayList<>();
        addEnemies();
    }

    private void addSuperheroes() {
        final String superheroType = "SUPERHERO";

        String barcode1 = "340912373503";
        String barcode2 = "331010383501";
        String barcode3 = "9691260136502";
        String barcode4 = "401207336501";
        String barcode5 = "391110346509";
        String barcode6 = "320813183500";

        ICreature s1 = new Creature(barcode1, "Beast Feast", "beast_feast", superheroType);
        ICreature s2 = new Creature(barcode2, "Cool Candy", "cool_candy", superheroType);
        ICreature s3 = new Creature(barcode3, "Jam Bam", "jam_bam", superheroType);
        ICreature s4 = new Creature(barcode4, "Jaw Breaker", "jaw_breaker", superheroType);
        ICreature s5 = new Creature(barcode5, "Mega Blaster", "mega_blaster", superheroType);
        ICreature s6 = new Creature(barcode6, "Razor Fist", "razor_fist", superheroType);

        converter = new BarcodeToCreatureConverter(s1);
        s1 = converter.convert(barcode1);

        converter = new BarcodeToCreatureConverter(s2);
        s2 = converter.convert(barcode2);

        converter = new BarcodeToCreatureConverter(s3);
        s3 = converter.convert(barcode3);

        converter = new BarcodeToCreatureConverter(s4);
        s4 = converter.convert(barcode4);

        converter = new BarcodeToCreatureConverter(s5);
        s5 = converter.convert(barcode5);

        converter = new BarcodeToCreatureConverter(s6);
        s6 = converter.convert(barcode6);

        superheroes.add(s1);
        superheroes.add(s2);
        superheroes.add(s3);
        superheroes.add(s4);
        superheroes.add(s5);
        superheroes.add(s6);
    }

    private void addEnemies() {
        String[] enemiesCardsNames = {"air_dragon",
                                        "baguza",
                                        "darman",
                                        "dolcoon",
                                        "droome",
                                        "gilger",
                                        "pandoral",
                                        "rezadon",
                                        "zanbee"
                                        };

        String[] enemiesNames = {"Air Dragon",
                                "Baguza",
                                "Darman",
                                "Dolcoon",
                                "Droome",
                                "Gilger",
                                "Pandoral",
                                "Rezadon",
                                "Zanbee"
                                };

        for (int i = 0; i < enemiesCardsNames.length; i++
             ) {
            String mDrawableName = enemiesCardsNames[i] + "_flip_side";
            int resID = context.getResources().getIdentifier(mDrawableName, "drawable", context.getPackageName());

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID);

            BarcodeDetector detector =
                    new BarcodeDetector.Builder(context).build();

            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<Barcode> barcodes = detector.detect(frame);

            if (barcodes.size() != 0) {
                String thisCode = barcodes.valueAt(0).rawValue;

                ICreature enemy = new Creature(thisCode, enemiesNames[i], enemiesCardsNames[i], "ENEMY");

                converter = new BarcodeToCreatureConverter(enemy);
                enemy = converter.convert(thisCode);

                enemies.add(enemy);
            }
        }


        BarcodeBattlerDatabaseAdapter databaseAdapter = new BarcodeBattlerDatabaseAdapter(context.getApplicationContext());
        databaseAdapter.open();
        if (databaseAdapter.getEnemies().size() == 0) { // if the scanner did not work then we create some creatures ourselves
            for (int i = 0; i < 5; i++) {
                // nextInt is normally exclusive of the top value,
                // so add 1 to make it inclusive
                int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);
                String barcode = String.valueOf(randomNum) + String.valueOf(i) + "3";

                ICreature enemy = new Creature(barcode, "Enemy " + i, "blank", "ENEMY");
            }
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return superheroes.size();
    }

    @Override
    public ICreature getItem(int position) {
        return superheroes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View returnView;

        if (convertView == null) {
            Context context = parent.getContext();
            returnView = View.inflate(context, R.layout.activity_superhero, null);
        } else {
            returnView = convertView;
        }
        TextView name = returnView.findViewById(R.id.name);
        name.setText(String.valueOf(superheroes.get(position).getName()));
        TextView energy = returnView.findViewById(R.id.energy);
        energy.setText(String.valueOf(superheroes.get(position).getEnergy()));
        TextView strike = returnView.findViewById(R.id.strike);
        strike.setText(String.valueOf(superheroes.get(position).getStrike()));
        TextView defense = returnView.findViewById(R.id.defense);
        defense.setText(String.valueOf(superheroes.get(position).getDefense()));

        ImageView image = returnView.findViewById(R.id.icon);

        String mDrawableName = superheroes.get(position).getImageName();
        int resID = returnView.getResources().getIdentifier(mDrawableName, "drawable", returnView.getContext().getPackageName());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(returnView.getContext().getResources(), resID, options);

        image.setImageBitmap(
                ImageHelper.decodeSampledBitmapFromResource(returnView.getContext().getResources(), resID, 100, 100));

        return returnView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return superheroes.isEmpty();
    }

    ArrayList<ICreature> getSuperheroes() {
        return superheroes;
    }

    void setSuperheroes(ArrayList<ICreature> superheroes) {
        this.superheroes = superheroes;
    }

    public void addSuperheroe(ICreature superheroe) {
        superheroes.add(superheroe);
    }

    ArrayList<ICreature> getEnemies() {
        return enemies;
    }

    void setEnemies(ArrayList<ICreature> enemies) {
        this.enemies = enemies;
    }
}