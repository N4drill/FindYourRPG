package pl.student.pwr.gluszczak.pawel.findyourrpg.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.ClusterMarker;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class MyClusterManagerRenderer extends DefaultClusterRenderer<ClusterMarker> {

    private final IconGenerator mIconGenerator;
    private final ImageView mImageView;
    private final int mMarkerWidth;
    private final int mMarkerHeight;

    public MyClusterManagerRenderer(Context context, GoogleMap map, ClusterManager<ClusterMarker> clusterManager) {
        super(context, map, clusterManager);


        mIconGenerator = new IconGenerator(context.getApplicationContext());
        mImageView = new ImageView(context.getApplicationContext());
        mMarkerHeight = (int) context.getResources().getDimension(R.dimen.cluster_default_size);
        mMarkerWidth = (int) context.getResources().getDimension(R.dimen.cluster_default_size);
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(mMarkerWidth, mMarkerHeight));
        int padding = (int) context.getResources().getDimension(R.dimen.cluster_default_padding);
        mImageView.setPadding(padding, padding, padding, padding);
        mIconGenerator.setContentView(mImageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(ClusterMarker item, MarkerOptions markerOptions) {

        mImageView.setImageResource(item.getIconPicture());
        Bitmap icon = mIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.getTitle());
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<ClusterMarker> cluster) {
        //TODO: Consider if use clusers ?
        return false;
    }
}
