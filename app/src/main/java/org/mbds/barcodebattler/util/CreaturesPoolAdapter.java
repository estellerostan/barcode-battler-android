package org.mbds.barcodebattler.util;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.mbds.barcodebattler.R;
import org.mbds.barcodebattler.data.ICreature;

import java.util.List;

public class CreaturesPoolAdapter extends ArrayAdapter<ICreature> {
    private int resource;
    private LayoutInflater inflater;

    public CreaturesPoolAdapter(Context ctx, int resourceId, List<ICreature> objects) {
        super(ctx, resourceId, objects);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        //context = ctx;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        View returnView = convertView;

        if (returnView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            returnView = vi.inflate(R.layout.activity_superhero, parent, false);
        }

        ICreature creature = getItem(position);

        TextView name = returnView.findViewById(R.id.name);
        assert creature != null;
        name.setText(String.valueOf(creature.getName()));
        TextView energy = returnView.findViewById(R.id.energy);
        energy.setText(String.valueOf(creature.getEnergy()));
        TextView strike = returnView.findViewById(R.id.strike);
        strike.setText(String.valueOf(creature.getStrike()));
        TextView defense = returnView.findViewById(R.id.defense);
        defense.setText(String.valueOf(creature.getDefense()));

        // TODO:
        ImageView image = returnView.findViewById(R.id.icon);

        String mDrawableName = creature.getImageName();
        int resID = returnView.getResources().getIdentifier(mDrawableName, "drawable", returnView.getContext().getPackageName());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(returnView.getContext().getResources(), resID, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;

        image.setImageBitmap(
                ImageHelper.decodeSampledBitmapFromResource(returnView.getContext().getResources(), resID, 100, 100));

        Button addToBattle = returnView.findViewById(R.id.addToBattle);
        addToBattle.setVisibility(View.GONE);

        return returnView;
    }
}