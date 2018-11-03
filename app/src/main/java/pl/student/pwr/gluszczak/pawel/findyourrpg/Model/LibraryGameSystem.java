package pl.student.pwr.gluszczak.pawel.findyourrpg.Model;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class LibraryGameSystem {
    private String mName;
    private int mImageResource;
    private String mDescription;


    public LibraryGameSystem(String name, String description) {
        mImageResource = R.drawable.ic_placeholder;
        mName = name;
        mDescription = description;
    }

    public LibraryGameSystem(String name, int imageResource, String description) {
        mName = name;
        mImageResource = imageResource;
        mDescription = description;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public void setImageResource(int imageResource) {
        mImageResource = imageResource;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
