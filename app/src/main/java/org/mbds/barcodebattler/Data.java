package org.mbds.barcodebattler;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.mbds.barcodebattler.data.Creature;
import org.mbds.barcodebattler.data.ICreature;

import java.util.ArrayList;

class Data implements ListAdapter {
    private ArrayList<ICreature> creatures;

    Data() {
        creatures = new ArrayList<>();
        // dummy data
        Creature c1 = new Creature(1, 1, 1);
        Creature c2 = new Creature(1, 2, 2);
        Creature c3 = new Creature(1, 2, 3);

//        Creature c1 = new Creature();
//        c1.setEnergy(1);
//        c1.setStrike(1);
//        c1.setDefense(1);
//        Creature c2 = new Creature();
//        c2.setEnergy(1);
//        c2.setStrike(1);
//        c2.setDefense(1);
//        Creature c3 = new Creature();
//        c3.setEnergy(1);
//        c3.setStrike(1);
//        c3.setDefense(1);

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
        TextView energy = returnView.findViewById(R.id.energy);
        energy.setText(String.valueOf(creatures.get(position).getEnergy()));
        TextView strike = returnView.findViewById(R.id.strike);
        strike.setText(String.valueOf(creatures.get(position).getStrike()));
        TextView defense = returnView.findViewById(R.id.defense);
        defense.setText(String.valueOf(creatures.get(position).getDefense()));

        // TODO:
//        ImageView image = returnView.findViewById(R.id.icon);

//        if (creatures.get(position).image != null) {
//            image.setImageBitmap(creatures.get(position).image);
//        }
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