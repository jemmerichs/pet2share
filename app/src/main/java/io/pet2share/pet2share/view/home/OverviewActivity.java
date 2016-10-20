package io.pet2share.pet2share.view.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.view.ActionMode;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.common.BasicActivity;
import io.pet2share.pet2share.data.PictureLoader;
import io.pet2share.pet2share.data.offer.OfferLoader;
import io.pet2share.pet2share.interfaces.loader.OfferLoadingInterface;
import io.pet2share.pet2share.model.offer.Offer;

import static io.pet2share.pet2share.view.home.OverviewActivityViewPagerAdapter.fragmentIcons;

/**
 * Created by Muki-Zenbook on 11.10.2016.
 */

public class OverviewActivity extends BasicActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.tab_layout) protected TabLayout tabLayout;

    @BindView(R.id.viewpager) protected ViewPager viewPager;

    @BindView(R.id.offer_btn) protected FloatingActionButton offerButton;

    protected GoogleApiClient googleApiClient;
    protected Location        latestLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        ButterKnife.bind(this);
        viewPager.setAdapter(new OverviewActivityViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(this);
        initTabLayout();
        tabLayout.getTabAt(1).select();
        initGooglePlayAPI();
    }

    private void initGooglePlayAPI() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(
                    LocationServices.API).build();
        }
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
        OfferLoader.getInstance().loadAllOffers(offers -> {

            if (offers != null) {
                delete(offers);
            }
        }, null);

        /*Date soon = new Date();
        soon.setTime(new Date().getTime() + 1000000);
        Date sooner = new Date();
        sooner.setTime(new Date().getTime() + 10000);
        ArrayList<Long> times = new ArrayList<>();
        times.add(sooner.getTime());
        times.add(soon.getTime());
        Offer offer = new Offer("Spitz sucht einen temporären Hüter", new Date().getTime(), soon.getTime(), 49.4844656, 8.4678411, times,
                                "Hallo, sein Name ist Spitz und er ist süß.");

        OfferLoader.getInstance().createOffer(getCurrentUid(), offer);
        OfferLoader.getInstance().uploadPictureForOffer(offer,  BitmapFactory.decodeResource(this.getResources(), R.drawable.dog));
        Offer offer2 = new Offer("Süße Katze brauch für 2 Wochen ein neues Zuhause", new Date().getTime(), soon.getTime(), 49.4844656, 8.4678411, times,
                                "Achtung SÜSS!");

        OfferLoader.getInstance().createOffer(getCurrentUid(), offer2);
        OfferLoader.getInstance().uploadPictureForOffer(offer2,  BitmapFactory.decodeResource(this.getResources(), R.drawable.cat));
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                                               Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        latestLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    private void delete(List<Offer> offerList) {
        for (Offer offer : offerList) {
            OfferLoader.getInstance().deleteOffer(offer);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
