package pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons;

import android.app.Application;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;

public class UserClient extends Application {

    private User mUser = null;

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

}
