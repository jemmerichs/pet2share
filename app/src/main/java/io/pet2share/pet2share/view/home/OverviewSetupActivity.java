package io.pet2share.pet2share.view.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.vstechlab.easyfonts.EasyFonts;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.common.BasicActivity;

import static io.pet2share.pet2share.view.home.OverviewViewPagerAdapter.fragmentIcons;

/**
 * Created by mgerc on 25.10.2016.
 */

public abstract class OverviewSetupActivity extends BasicActivity implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    protected TextView title;


    @BindView(R.id.viewpager)
    protected ViewPager viewPager;

    @BindView(R.id.tab_layout)
    protected TabLayout tabLayout;

    @BindView(R.id.offer_btn)
    protected FloatingActionButton offerButton;

    protected GoogleApiClient googleApiClient;
    protected Location latestLocation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setupToolbar();
        initTabLayout();
        initGooglePlayAPI();

    }

    private void initGooglePlayAPI() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(
                    LocationServices.API).build();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        title.setTypeface(EasyFonts.captureIt(this));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);*/

    }

    private void initTabLayout() {
        viewPager.setAdapter(new OverviewViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);         /* limit is a fixed integer*/
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(fragmentIcons.get(i));
        }
        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

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

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overvi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_offers:
                handleRefreshMenuItemPressed();
                return true;
            case R.id.filter_list:
                showFilterDialog();
                return true;
            case android.R.id.home:
                Toast.makeText(this, "HOME", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    abstract void showFilterDialog();

    abstract void handleRefreshMenuItemPressed();
}
