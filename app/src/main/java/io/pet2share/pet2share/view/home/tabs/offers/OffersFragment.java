package io.pet2share.pet2share.view.home.tabs.offers;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.common.BasicOverviewActivityFragment;
import io.pet2share.pet2share.common.Receiver;
import io.pet2share.pet2share.common.ServiceResultReceiver;
import io.pet2share.pet2share.controller.EventBusMessage;
import io.pet2share.pet2share.services.LoadOffersService;

/**
 * Created by Muki-Zenbook on 11.10.2016.
 */

public class OffersFragment extends BasicOverviewActivityFragment implements Receiver {

    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;

    private ServiceResultReceiver serviceResultReceiver;
    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers, container, false);
        ButterKnife.bind(this, view);
        setupResultReceiver();
        refresh();
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);


    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage event) {
        refresh();
    }

    ;

    private void setupResultReceiver() {
        serviceResultReceiver = new ServiceResultReceiver(new Handler());
        serviceResultReceiver.setReceiver(this);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        progressDialog.dismiss();
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(new OfferAdapter(Parcels.unwrap(resultData.getParcelable(LoadOffersService.offersKey)), getActivity()));

    }

    public void refresh() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading...");
        LoadOffersService.startLoadRestaurantService(getActivity(), progressDialog, serviceResultReceiver);

    }
}
