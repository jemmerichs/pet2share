package io.pet2share.pet2share.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.controller.EventBusMessage;
import io.pet2share.pet2share.services.CreateOfferService;
import io.pet2share.pet2share.view.create.CreateOfferActivity;

/**
 * Created by Muki-Zenbook on 11.10.2016.
 */

public class OverviewActivity extends OverviewSetupActivity {

    private OverviewViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_overview);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

    }

    @Override
    void showFilterDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.filter_title)
                .customView(R.layout.dialog_filter, true)
                .positiveText("Ok")
                .show();

        View view = dialog.getView();
    }

    @Override
    void handleRefreshMenuItemPressed() {
        EventBus.getDefault().post(new EventBusMessage());

    }

    @OnClick(R.id.offer_btn)
    public void createOffer() {
        Intent intent = new Intent(this, CreateOfferActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        adapter = new OverviewViewPagerAdapter(getSupportFragmentManager());
        viewPager.addOnPageChangeListener(this);
        tabLayout.getTabAt(1).select();
        tabLayout.addOnTabSelectedListener(this);
    }

}
