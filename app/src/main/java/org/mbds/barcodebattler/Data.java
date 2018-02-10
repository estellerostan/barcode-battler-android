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
    private ArrayList<ICreature> creatures;

    Data() {
        creatures = new ArrayList<>();
        // dummy data

        ICreature c1 = new Creature("Beast Feast", 1, 1, 1, "beast_feast");
        ICreature c2 = new Creature("Cool Candy", 1, 2, 2, "cool_candy");
        ICreature c3 = new Creature("Jam Bam", 1, 2, 3, "jam_bam");

        String barcode1 = "340912373503";
        BarcodeToCreatureConverter converter = new BarcodeToCreatureConverter(c1);
        c1 = converter.convert(barcode1);

        String barcode2 = "331010383501";
        converter = new BarcodeToCreatureConverter(c2);
        c2 = converter.convert(barcode2);

        String barcode3 = "9691260136502";
        converter = new BarcodeToCreatureConverter(c3);
        c3 = converter.convert(barcode3);

        creatures.add(c1);
        creatures.add(c2);
        creatures.add(c3);
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
        return creatures.size();
    }

    @Override
    public ICreature getItem(int position) {
        return creatures.get(position);
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
            returnView = View.inflate(context, R.layout.activity_creature, null);
        } else {
            returnView = convertView;
        }
        TextView name = returnView.findViewById(R.id.name);
        name.setText(String.valueOf(creatures.get(position).getName()));
        TextView energy = returnView.findViewById(R.id.energy);
        energy.setText(String.valueOf(creatures.get(position).getEnergy()));
        TextView strike = returnView.findViewById(R.id.strike);
        strike.setText(String.valueOf(creatures.get(position).getStrike()));
        TextView defense = returnView.findViewById(R.id.defense);
        defense.setText(String.valueOf(creatures.get(position).getDefense()));

        // TODO:
        ImageView image = returnView.findViewById(R.id.icon);

        String mDrawableName = creatures.get(position).getImageName();
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
        return creatures.isEmpty();
    }

    ArrayList<ICreature> getCreatures() {
        return creatures;
    }

    public void setCreatures(ArrayList<ICreature> creatures) {
        this.creatures = creatures;
    }

    public void addCreature(ICreature creature) {
        creatures.add(creature);
    }
}