package pl.student.pwr.gluszczak.pawel.findyourrpg.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

public class ParcableUserPosition implements Parcelable {

    private GeoPoint mGeoPoint;

    public ParcableUserPosition(GeoPoint geoPoint) {
        mGeoPoint = geoPoint;
    }

    public ParcableUserPosition() {
    }

    protected ParcableUserPosition(Parcel in) {
    }

    public static final Creator<ParcableUserPosition> CREATOR = new Creator<ParcableUserPosition>() {
        @Override
        public ParcableUserPosition createFromParcel(Parcel in) {
            return new ParcableUserPosition(in);
        }

        @Override
        public ParcableUserPosition[] newArray(int size) {
            return new ParcableUserPosition[size];
        }
    };

    public GeoPoint getGeoPoint() {
        return mGeoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        mGeoPoint = geoPoint;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
