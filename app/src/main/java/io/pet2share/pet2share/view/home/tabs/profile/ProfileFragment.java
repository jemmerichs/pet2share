package io.pet2share.pet2share.view.home.tabs.profile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.common.BasicActivity;
import io.pet2share.pet2share.common.DividerItemDecoration;
import io.pet2share.pet2share.data.profile.ProfileLoader;
import io.pet2share.pet2share.interfaces.loader.ProfileInformationLoadingInterface;
import io.pet2share.pet2share.interfaces.loader.ProfilePictureLoadingInterface;
import io.pet2share.pet2share.model.user.Profile;

/**
 * Created by Muki-Zenbook on 11.10.2016.
 */

public class ProfileFragment extends Fragment {

    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;

    @BindView(R.id.profile_iv)
    protected ImageView imageView;

    @BindView(R.id.name_tv)
    protected TextView name;

    @BindView(R.id.motto_tv)
    protected TextView motto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setAdapter(new ProfileItemsAdapter(getContext()));
        //setProfileName();
        // setProfilePicture();
        return view;
    }

    private void setProfileName() {
        ProfileLoader.getInstance().loadProfileInformation(((BasicActivity) getActivity()).getCurrentUid(), new ProfileInformationLoadingInterface() {
            @Override
            public void applyInformation(Profile profile) {
                name.setText(profile.getFirstname() + " " + profile.getLastname());
                motto.setText(profile.getMotto());
            }
        });
    }

    private void setProfilePicture() {
        ProfileLoader.getInstance().loadProfilePicture(((BasicActivity) getActivity()).getCurrentUid(), new ProfilePictureLoadingInterface() {
            @Override
            public void applyInformation(Bitmap profilePicture) {
                imageView.setImageBitmap(profilePicture);
            }
        });
    }
}