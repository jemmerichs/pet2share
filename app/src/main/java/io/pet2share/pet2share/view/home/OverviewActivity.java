package io.pet2share.pet2share.view.home;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.common.BasicActivity;
import io.pet2share.pet2share.data.offer.OfferLoader;
import io.pet2share.pet2share.interfaces.loader.OfferLoadingInterface;
import io.pet2share.pet2share.model.offer.Offer;

import static io.pet2share.pet2share.view.home.OverviewActivityViewPagerAdapter.fragmentIcons;

/**
 * Created by Muki-Zenbook on 11.10.2016.
 */

public class OverviewActivity extends BasicActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.tab_layout)
    protected TabLayout tabLayout;

    @BindView(R.id.viewpager)
    protected ViewPager viewPager;

    @BindView(R.id.offer_btn)
    protected FloatingActionButton offerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        ButterKnife.bind(this);
        viewPager.setAdapter(new OverviewActivityViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(this);
        initTabLayout();
        tabLayout.getTabAt(1).select();

    }

    private void initTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(fragmentIcons.get(i));
        }
        tabLayout.addOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tab.getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        tab.getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 1) {
            offerButton.show();
            OfferLoader.getInstance().loadOffersByUID(getCurrentUid(), new OfferLoadingInterface() {
                @Override
                public void loadOffers(ArrayList<Offer> offers) {
                    for(Offer offer : offers) {
                        System.out.println("Offer:" +offer.getName());
                    }
                }
            });
        } else {
            offerButton.hide();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * TODO:
     * Show UI for creating real data stuff
     */
    @OnClick(R.id.offer_btn)
    public void createOffer() {
        Date soon = new Date();
        soon.setTime(new Date().getTime() + 1000000);
        Date sooner = new Date();
        sooner.setTime(new Date().getTime() + 10000);
        ArrayList<Long> times = new ArrayList<>();
        times.add(sooner.getTime());
        times.add(soon.getTime());
        Offer offer = new Offer("Spitz sucht einen temporären Hüter", new Date().getTime(),soon.getTime() , 49.4844656, 8.4678411, times, "Hallo, sein Name ist Spitz und er ist süß.");

        OfferLoader.getInstance().createOffer(getCurrentUid(),offer);

    }
}
