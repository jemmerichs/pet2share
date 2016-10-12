package io.pet2share.pet2share.view.home.tabs.profile;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.pet2share.pet2share.R;

/**
 * Created by Muki-Zenbook on 12.10.2016.
 */

public class ProfileItemsAdapter extends RecyclerView.Adapter<ProfileItemsAdapter.ProfileItemViewholder> {

    private List<ProfileItem> profileItems = new ArrayList<>();
    private Context context;

    public ProfileItemsAdapter(Context context) {
        this.context = context;
        initProfileItems();
    }

    private void initProfileItems() {
        ProfileItem profileItem = new ProfileItem(R.drawable.vector_key, "Security");
        ProfileItem profileItem2 = new ProfileItem(R.drawable.vector_settings, "Settings");
        ProfileItem profileItem3 = new ProfileItem(R.drawable.vector_contact, "Contact us");
        ProfileItem profileItem4 = new ProfileItem(R.drawable.vector_info, "About us");
        ProfileItem profileItem5 = new ProfileItem(R.drawable.vector_pets, "My pets");
        ProfileItem profileItem6 = new ProfileItem(R.drawable.vector_ratings, "My ratings");


        profileItems.add(profileItem5);
        profileItems.add(profileItem6);
        profileItems.add(profileItem);
        profileItems.add(profileItem2);
        profileItems.add(profileItem3);
        profileItems.add(profileItem4);
    }


    @Override
    public ProfileItemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile, parent, false);

        return new ProfileItemViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(ProfileItemViewholder holder, int position) {
        ProfileItem profileItem = profileItems.get(position);
        holder.image.setImageDrawable(context.getDrawable(profileItem.getImage()));
        holder.image.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        holder.name.setText(profileItem.getName());

    }

    @Override
    public int getItemCount() {
        return profileItems.size();
    }

    public static class ProfileItemViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_iv)
        protected ImageView image;

        @BindView(R.id.item_name)
        protected TextView name;

        public ProfileItemViewholder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }
}
