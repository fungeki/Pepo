package com.ranuskin.ranloock.pepo.Objects;

import android.graphics.Bitmap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {

    private LatLng position;
    private String title;
    private String snippet;
    private Bitmap iconPicture;
    private int serial;

    public ClusterMarker(LatLng position, String title, String snippet, Bitmap iconPicture, int serial) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.iconPicture = iconPicture;
        this.serial = serial;
    }
    public ClusterMarker() {

    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public Bitmap getIconPicture() {
        return iconPicture;
    }

    public void setIconPicture(Bitmap iconPicture) {
        this.iconPicture = iconPicture;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}
