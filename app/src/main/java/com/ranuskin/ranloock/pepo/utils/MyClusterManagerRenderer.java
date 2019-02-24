package com.ranuskin.ranloock.pepo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.ranuskin.ranloock.pepo.Objects.ClusterMarker;

public class MyClusterManagerRenderer extends DefaultClusterRenderer<ClusterMarker> {
    private final IconGenerator iconGenerator;
    private final ImageView imageView;
    private int markerWidth;
    private int markerHeight;

    public MyClusterManagerRenderer(Context context, GoogleMap map, ClusterManager<ClusterMarker> clusterManager) {
        super(context, map, clusterManager);

        iconGenerator = new IconGenerator(context.getApplicationContext());
        imageView = new ImageView(context.getApplicationContext());
        markerWidth = 150;
        markerHeight = 150;
        imageView.setLayoutParams(new ViewGroup.LayoutParams(markerWidth,markerHeight));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iconGenerator.setContentView(imageView);

    }

    @Override
    protected void onBeforeClusterItemRendered(ClusterMarker item, MarkerOptions markerOptions) {
        imageView.setImageBitmap(item.getIconPicture());
        Bitmap icon = iconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.getTitle());



    }
}
