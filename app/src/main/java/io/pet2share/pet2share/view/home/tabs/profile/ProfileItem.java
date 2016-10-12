package io.pet2share.pet2share.view.home.tabs.profile;

/**
 * Created by Muki-Zenbook on 12.10.2016.
 */

public class ProfileItem {

    private int image;
    private String name;

    public ProfileItem(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
