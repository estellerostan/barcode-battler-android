package org.mbds.barcodebattler;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.mbds.barcodebattler.data.Creature;
import org.mbds.barcodebattler.data.ICreature;
import org.mbds.barcodebattler.util.BarcodeToCreatureConverter;

import java.util.ArrayList;

class Data implements ListAdapter {
    private ArrayList<ICreature> superheroes;

    Data() {
        superheroes = new ArrayList<>();
        ICreature s1 = new Creature("Beast Feast", 1, 1, 1, "beast_feast");
        ICreature s2 = new Creature("Cool Candy", 1, 2, 2, "cool_candy");
        ICreature s3 = new Creature("Jam Bam", 1, 2, 3, "jam_bam");
        ICreature s4 = new Creature("Jaw Breaker", 1, 2, 3, "jaw_breaker");
        ICreature s5 = new Creature("Mega Blaster", 1, 2, 3, "mega_blaster");
        ICreature s6 = new Creature("Razor Fist", 1, 2, 3, "razor_fist");

        String barcode1 = "340912373503";
        BarcodeToCreatureConverter converter = new BarcodeToCreatureConverter(s1);
        s1 = converter.convert(barcode1);

        String barcode2 = "331010383501";
        converter = new BarcodeToCreatureConverter(s2);
        s2 = converter.convert(barcode2);

        String barcode3 = "9691260136502";
        converter = new BarcodeToCreatureConverter(s3);
        s3 = converter.convert(barcode3);

        String barcode4 = "401207336501";
        converter = new BarcodeToCreatureConverter(s4);
        s4 = converter.convert(barcode4);

        String barcode5 = "391110346509";
        converter = new BarcodeToCreatureConverter(s5);
        s5 = converter.convert(barcode5);

        String barcode6 = "320813183500";
        converter = new BarcodeToCreatureConverter(s6);
        s6 = converter.convert(barcode6);

        superheroes.add(s1);
        superheroes.add(s2);
        superheroes.add(s3);
        superheroes.add(s4);
        superheroes.add(s5);
        superheroes.add(s6);
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
            returnView = View.inflate(context, R.layout.activity_superheroe, null);
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

        // TODO:
        ImageView image = returnView.findViewById(R.id.icon);

        String mDrawableName = superheroes.get(position).getImageName();
        int resID = returnView.getResources().getIdentifier(mDrawableName, "drawable", returnView.getContext().getPackageName());

        Bitmap icon = BitmapFactory.decodeResource(returnView.getContext().getResources(),
                resID);

        image.setImageBitmap(icon);

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

    public void setSuperheroes(ArrayList<ICreature> superheroes) {
        this.superheroes = superheroes;
    }

    public void addSuperheroe(ICreature superheroe) {
        superheroes.add(superheroe);
    }
}