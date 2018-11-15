package pl.student.pwr.gluszczak.pawel.findyourrpg.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String email;
    private String id;
    private String username;
    private int age;
    private int gamesAsMaster = 0;
    private int gamesAsPlayer = 0;
    private int playerCreativity = 0;
    private int playerBehaviour = 0;
    private int playerGameFeel = 0;
    private int masterCreativity = 0;
    private int masterBehaviour = 0;
    private int masterGameFeel = 0;
    private String avatarUrl;

    public User(String email, String id, String username, int age, int gamesAsMaster, int gamesAsPlayer, int playerCreativity, int playerBehaviour, int playerGameFeel, int masterCreativity, int masterBehaviour, int masterGameFeel, String avatarUrl) {
        this.email = email;
        this.id = id;
        this.username = username;
        this.age = age;
        this.gamesAsMaster = gamesAsMaster;
        this.gamesAsPlayer = gamesAsPlayer;
        this.playerCreativity = playerCreativity;
        this.playerBehaviour = playerBehaviour;
        this.playerGameFeel = playerGameFeel;
        this.masterCreativity = masterCreativity;
        this.masterBehaviour = masterBehaviour;
        this.masterGameFeel = masterGameFeel;
        this.avatarUrl = avatarUrl;
    }

    public User() {
    }

    protected User(Parcel in) {
        email = in.readString();
        id = in.readString();
        username = in.readString();
        age = in.readInt();
        gamesAsMaster = in.readInt();
        gamesAsPlayer = in.readInt();
        playerCreativity = in.readInt();
        playerBehaviour = in.readInt();
        playerGameFeel = in.readInt();
        masterCreativity = in.readInt();
        masterBehaviour = in.readInt();
        masterGameFeel = in.readInt();
        avatarUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(id);
        dest.writeString(username);
        dest.writeInt(age);
        dest.writeInt(gamesAsMaster);
        dest.writeInt(gamesAsPlayer);
        dest.writeInt(playerCreativity);
        dest.writeInt(playerBehaviour);
        dest.writeInt(playerGameFeel);
        dest.writeInt(masterCreativity);
        dest.writeInt(masterBehaviour);
        dest.writeInt(masterGameFeel);
        dest.writeString(avatarUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGamesAsMaster() {
        return gamesAsMaster;
    }

    public void setGamesAsMaster(int gamesAsMaster) {
        this.gamesAsMaster = gamesAsMaster;
    }

    public int getGamesAsPlayer() {
        return gamesAsPlayer;
    }

    public void setGamesAsPlayer(int gamesAsPlayer) {
        this.gamesAsPlayer = gamesAsPlayer;
    }

    public int getPlayerCreativity() {
        return playerCreativity;
    }

    public void setPlayerCreativity(int playerCreativity) {
        this.playerCreativity = playerCreativity;
    }

    public int getPlayerBehaviour() {
        return playerBehaviour;
    }

    public void setPlayerBehaviour(int playerBehaviour) {
        this.playerBehaviour = playerBehaviour;
    }

    public int getPlayerGameFeel() {
        return playerGameFeel;
    }

    public void setPlayerGameFeel(int playerGameFeel) {
        this.playerGameFeel = playerGameFeel;
    }

    public int getMasterCreativity() {
        return masterCreativity;
    }

    public void setMasterCreativity(int masterCreativity) {
        this.masterCreativity = masterCreativity;
    }


    public int getMasterBehaviour() {
        return masterBehaviour;
    }

    public void setMasterBehaviour(int masterBehaviour) {
        this.masterBehaviour = masterBehaviour;
    }

    public int getMasterGameFeel() {
        return masterGameFeel;
    }

    public void setMasterGameFeel(int masterGameFeel) {
        this.masterGameFeel = masterGameFeel;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
