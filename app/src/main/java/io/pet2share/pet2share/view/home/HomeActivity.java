package io.pet2share.pet2share.view.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.common.BasicActivity;
import io.pet2share.pet2share.data.profile.ProfileLoader;
import io.pet2share.pet2share.interfaces.loader.ProfileInformationLoadingInterface;
import io.pet2share.pet2share.interfaces.loader.ProfilePictureLoadingInterface;
import io.pet2share.pet2share.model.user.Profile;

/**
 * Created by Muki-Zenbook on 07.10.2016.
 */

public class HomeActivity extends BasicActivity {

    @BindView(R.id.profile_image)
    protected ImageView profileImageView;

    @BindView(R.id.profileNameTextView)
    protected TextView profileName;

    @BindView(R.id.offer_btn)
    protected Button offer;

    @BindView(R.id.search_btn)
    protected Button search;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getFirebaseAuth().getCurrentUser()!=null) {
            ProfileLoader.getInstance().loadProfileInformation(getCurrentUid(), new ProfileInformationLoadingInterface() {
                @Override
                public void applyInformation(Profile profile) {
                    HomeActivity.this.profileName.setText(profile.getFirstname() + " " + profile.getLastname());
                }
            });
            ProfileLoader.getInstance().loadProfilePicture(getCurrentUid(), new ProfilePictureLoadingInterface() {
                @Override
                public void applyInformation(Bitmap profilePicture) {
                    HomeActivity.this.profileImageView.setImageBitmap(profilePicture);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}
