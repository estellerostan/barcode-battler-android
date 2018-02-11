package org.mbds.barcodebattler.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                          int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
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
                decodeSampledBitmapFromResource(returnView.getContext().getResources(), resID, 100, 100));

        return returnView;
    }
}