package pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class FacePickerAdapter extends BaseAdapter {

    private Context mContext;
    private boolean mFlag = false;

    public FacePickerAdapter(Context context) {
        mContext = context;
    }

    public int getCount() {
        return mImages.length;
    }

    public Integer getItem(int position) {
        return mImages[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(400, 400));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mImages[position]);
        return imageView;
    }

    private Integer[] mImages = {
            R.drawable.face_placeholder,
            R.drawable.face_1,
            R.drawable.face_2,
            R.drawable.face_3,
            R.drawable.face_4,
            R.drawable.face_5,
            R.drawable.face_6,
            R.drawable.face_7,
            R.drawable.face_8,
            R.drawable.face_9,
            R.drawable.face_10,
            R.drawable.face_11,
    };
}
