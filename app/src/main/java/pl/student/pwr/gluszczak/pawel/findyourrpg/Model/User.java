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
    private float playerCreativity = 0;
    private float playerBehaviour = 0;
    private float playerGameFeel = 0;
    private float masterCreativity = 0;
    private float masterBehaviour = 0;
    private float masterGameFeel = 0;
    private String favouriteSystem;
    private String avatarUrl;

    public User(String email, String id, String username, int age, int gamesAsMaster, int gamesAsPlayer, float playerCreativity, float playerBehaviour, float playerGameFeel, float masterCreativity, float masterBehaviour, float masterGameFeel, String favouriteSystem, String avatarUrl) {
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
        this.favouriteSystem = favouriteSystem;
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
        playerCreativity = in.readFloat();
        playerBehaviour = in.readFloat();
        playerGameFeel = in.readFloat();
        masterCreativity = in.readFloat();
        masterBehaviour = in.readFloat();
        masterGameFeel = in.readFloat();
        favouriteSystem = in.readString();
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
        dest.writeFloat(playerCreativity);
        dest.writeFloat(playerBehaviour);
        dest.writeFloat(playerGameFeel);
        dest.writeFloat(masterCreativity);
        dest.writeFloat(masterBehaviour);
        dest.writeFloat(masterGameFeel);
        dest.writeString(favouriteSystem);
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public float getPlayerCreativity() {
        return playerCreativity;
    }

    public void setPlayerCreativity(float playerCreativity) {
        this.playerCreativity = playerCreativity;
    }

    public float getPlayerBehaviour() {
        return playerBehaviour;
    }

    public void setPlayerBehaviour(float playerBehaviour) {
        this.playerBehaviour = playerBehaviour;
    }

    public float getPlayerGameFeel() {
        return playerGameFeel;
    }

    public void setPlayerGameFeel(float playerGameFeel) {
        this.playerGameFeel = playerGameFeel;
    }

    public float getMasterCreativity() {
        return masterCreativity;
    }

    public void setMasterCreativity(float masterCreativity) {
        this.masterCreativity = masterCreativity;
    }

    public float getMasterBehaviour() {
        return masterBehaviour;
    }

    public void setMasterBehaviour(float masterBehaviour) {
        this.masterBehaviour = masterBehaviour;
    }

    public float getMasterGameFeel() {
        return masterGameFeel;
    }

    public void setMasterGameFeel(float masterGameFeel) {
        this.masterGameFeel = masterGameFeel;
    }

    public String getFavouriteSystem() {
        return favouriteSystem;
    }

    public void setFavouriteSystem(String favouriteSystem) {
        this.favouriteSystem = favouriteSystem;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
