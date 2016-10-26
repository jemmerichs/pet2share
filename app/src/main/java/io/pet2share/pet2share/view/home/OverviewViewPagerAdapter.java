package io.pet2share.pet2share.view.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.pet2share.pet2share.R;
import io.pet2share.pet2share.common.Pet2ShareApplication;
import io.pet2share.pet2share.view.home.tabs.MessagesFragment;
import io.pet2share.pet2share.view.home.tabs.offers.OffersFragment;
import io.pet2share.pet2share.view.home.tabs.profile.ProfileFragment;

/**
 * Created by Muki-Zenbook on 11.10.2016.
 */

public class OverviewViewPagerAdapter extends FragmentPagerAdapter {

    public final static List<Drawable> fragmentIcons = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();
    private Context context = Pet2ShareApplication.get();
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();


    public OverviewViewPagerAdapter(FragmentManager fm) {
        super(fm);
        initIconsAndTitles();

    }

    private void initIconsAndTitles() {
        fragmentTitleList.add(context.getString(R.string.messages_fragment_title));
        fragmentIcons.add(context.getDrawable(R.drawable.vector_message));

        fragmentTitleList.add(context.getString(R.string.offers_fragment_title));
        fragmentIcons.add(context.getDrawable(R.drawable.image_paw));

        fragmentTitleList.add(context.getString(R.string.profile_fragment_title));
        fragmentIcons.add(context.getDrawable(R.drawable.vector_profile));
    }


    @Override
    public Fragment getItem(int position) {
        // Do NOT try to save references to the Fragments in getItem(),
        // because getItem() is not always called. If the Fragment
        // was already created then it will be retrieved from the FragmentManger
        // and not here (i.e. getItem() won't be called again).
        switch (position) {
            case 0:
                return new MessagesFragment();
            case 1:
                return new OffersFragment();
            case 2:
                return new ProfileFragment();
            default:
                // This should never happen. Always account for each position above
                return null;
        }
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // save the appropriate reference depending on position
        switch (position) {
            case 0:
                MessagesFragment messagesFragment = (MessagesFragment) super.instantiateItem(container, position);
                registeredFragments.put(position, messagesFragment);
                return messagesFragment;
            case 1:
                OffersFragment offersFragment = (OffersFragment) super.instantiateItem(container, position);
                registeredFragments.put(position, offersFragment);
                return offersFragment;

            case 2:
                ProfileFragment profileFragment = (ProfileFragment) super.instantiateItem(container, position);
                registeredFragments.put(position, profileFragment);
                return profileFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }


}
