package io.pet2share.pet2share.view.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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

class OverviewActivityViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();
    public final static List<Drawable> fragmentIcons = new ArrayList<>();

    private Context context = Pet2ShareApplication.get();


    public OverviewActivityViewPagerAdapter(FragmentManager fm) {
        super(fm);
        initFragments();
    }

    private void initFragments() {
        fragmentList.add(new MessagesFragment());
        fragmentTitleList.add(context.getString(R.string.messages_fragment_title));
        fragmentIcons.add(context.getDrawable(R.drawable.vector_message));
        fragmentList.add(new OffersFragment());
        fragmentTitleList.add(context.getString(R.string.offers_fragment_title));
        fragmentIcons.add(context.getDrawable(R.drawable.image_paw));
        fragmentList.add(new ProfileFragment());
        fragmentTitleList.add(context.getString(R.string.profile_fragment_title));
        fragmentIcons.add(context.getDrawable(R.drawable.vector_profile));

    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
