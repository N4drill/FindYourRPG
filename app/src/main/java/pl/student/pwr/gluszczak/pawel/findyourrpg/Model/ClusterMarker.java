package pl.student.pwr.gluszczak.pawel.findyourrpg.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {

    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private int iconPicture;
    private Event mEvent;

    public ClusterMarker(LatLng position, String title, String snippet, int iconPicture, Event event) {
        mPosition = position;
        mTitle = title;
        mSnippet = snippet;
        this.iconPicture = iconPicture;
        mEvent = event;
    }

    public ClusterMarker() {
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public void setPosition(LatLng position) {
        mPosition = position;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public void setSnippet(String snippet) {
        mSnippet = snippet;
    }

    public int getIconPicture() {
        return iconPicture;
    }

    public void setIconPicture(int iconPicture) {
        this.iconPicture = iconPicture;
    }

    public Event getEvent() {
        return mEvent;
    }

    public void setEvent(Event event) {
        mEvent = event;
    }
}
