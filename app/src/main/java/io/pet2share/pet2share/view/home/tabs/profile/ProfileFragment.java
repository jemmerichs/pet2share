package io.pet2share.pet2share.view.home.tabs.profile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.common.BasicActivity;
import io.pet2share.pet2share.common.BasicOverviewActivityFragment;
import io.pet2share.pet2share.common.DividerItemDecoration;
import io.pet2share.pet2share.common.Receiver;
import io.pet2share.pet2share.common.RecyclerItemClickListener;
import io.pet2share.pet2share.common.ServiceResultReceiver;
import io.pet2share.pet2share.data.profile.ProfileLoader;
import io.pet2share.pet2share.interfaces.loader.ProfileInformationLoadingInterface;
import io.pet2share.pet2share.interfaces.loader.ProfilePictureLoadingInterface;
import io.pet2share.pet2share.model.user.Profile;
import io.pet2share.pet2share.services.DeleteAllOffersService;

/**
 * Created by Muki-Zenbook on 11.10.2016.
 */

public class ProfileFragment extends BasicOverviewActivityFragment implements RecyclerItemClickListener, Receiver {

    private ServiceResultReceiver serviceResultReceiver;
    private ProgressDialog        progressDialog;

    @BindView(R.id.recycler_view) protected RecyclerView recyclerView;

    @BindView(R.id.profile_iv) protected ImageView imageView;

    @BindView(R.id.name_tv) protected TextView name;

    @BindView(R.id.motto_tv) protected TextView motto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setAdapter(new ProfileItemsAdapter(getContext(), this));
        setProfileName();
        setProfilePicture();
        serviceResultReceiver = new ServiceResultReceiver(new Handler());
        serviceResultReceiver.setReceiver(this);
        return view;
    }

    @Override
    public void onItemClicked(int itemPosition) {
        switch (itemPosition) {
            case 6:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    progressDialog = new ProgressDialog(getActivity());
                    DeleteAllOffersService.startDeleteOffersService(getActivity(), progressDialog, serviceResultReceiver);
                });
                builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
                builder.setTitle("Delete all offers ?");
                builder.setMessage("This will remove all current showing offers permanently");
                builder.create().show();

        }
        Toast.makeText(getActivity(), "Clicked: " + itemPosition, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case DeleteAllOffersService.DELETE_SERVICE_CODE:
                progressDialog.dismiss();
                break;
        }

    }

    private void setProfileName() {
        ProfileLoader.getInstance().loadProfileInformation(((BasicActivity) getActivity()).getCurrentUid(),
                                                           new ProfileInformationLoadingInterface() {
                                                               @Override
                                                               public void applyInformation(Profile profile) {
                                                                   name.setText(profile.getFirstname() + " " + profile.getLastname());
                                                                   motto.setText(profile.getMotto());
                                                               }
                                                           });
    }

    private void setProfilePicture() {
        ProfileLoader.getInstance().loadProfilePicture(getOverviewActivity().getCurrentUid(), new ProfilePictureLoadingInterface() {
            @Override
            public void applyInformation(String profilePictureURL) {
                Picasso.with(getContext()).load(profilePictureURL).into(imageView);
            }
        });
    }
}