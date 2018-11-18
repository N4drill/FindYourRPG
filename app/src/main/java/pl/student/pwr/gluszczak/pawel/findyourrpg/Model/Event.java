package pl.student.pwr.gluszczak.pawel.findyourrpg.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;

public class Event implements Parcelable {

    private String title;
    private String min_exp;
    private String rec_exp;
    private int needed_players;
    private GeoPoint localization;
    private String description;
    private ArrayList<User> participants;
    private Date date;
    private User game_maser;

    public Event(String title, String min_exp, String rec_exp, int needed_players, GeoPoint localization, String description, ArrayList<User> participants, Date date, User game_maser) {
        this.title = title;
        this.min_exp = min_exp;
        this.rec_exp = rec_exp;
        this.needed_players = needed_players;
        this.localization = localization;
        this.description = description;
        this.participants = participants;
        this.date = date;
        this.game_maser = game_maser;
    }

    public Event() {
    }

    protected Event(Parcel in) {
        title = in.readString();
        min_exp = in.readString();
        rec_exp = in.readString();
        needed_players = in.readInt();
        description = in.readString();
        participants = in.createTypedArrayList(User.CREATOR);
        game_maser = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(min_exp);
        dest.writeString(rec_exp);
        dest.writeInt(needed_players);
        dest.writeString(description);
        dest.writeTypedList(participants);
        dest.writeParcelable(game_maser, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public User getGame_maser() {
        return game_maser;
    }

    public void setGame_maser(User game_maser) {
        this.game_maser = game_maser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMin_exp() {
        return min_exp;
    }

    public void setMin_exp(String min_exp) {
        this.min_exp = min_exp;
    }

    public String getRec_exp() {
        return rec_exp;
    }

    public void setRec_exp(String rec_exp) {
        this.rec_exp = rec_exp;
    }

    public int getNeeded_players() {
        return needed_players;
    }

    public void setNeeded_players(int needed_players) {
        this.needed_players = needed_players;
    }

    public GeoPoint getLocalization() {
        return localization;
    }

    public void setLocalization(GeoPoint localization) {
        this.localization = localization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
