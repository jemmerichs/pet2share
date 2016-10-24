package io.pet2share.pet2share.view.home.tabs.offers;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.common.BasicOverviewActivityFragment;
import io.pet2share.pet2share.data.offer.OfferLoader;
import io.pet2share.pet2share.model.offer.Offer;
import io.pet2share.pet2share.services.LoadOffersService;

/**
 * Created by Bausch on 24.10.2016.
 */

public class DiscoverFragment extends BasicOverviewActivityFragment {

    @BindView(R.id.flingAdapter)
    SwipeFlingAdapterView swipeFlingAdapterView;
    DiscoverAdapter arrayAdapter;
    int mode = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        ButterKnife.bind(this, view);

        initSwipeView();


        return view;
    }

    private void initSwipeView() {
        System.out.println("init");
        arrayAdapter = new DiscoverAdapter(this, OfferLoader.getInstance().getOffersForDiscovery());
        swipeFlingAdapterView.setAdapter(arrayAdapter);
        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

                OfferLoader.getInstance().getOffersForDiscovery().remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {

            }

            @Override
            public void onRightCardExit(Object o) {

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                if (scrollProgressPercent > 0.01) {
                    // leftBorder.setVisibility(View.GONE);
                    // rightBorder.setVisibility(View.VISIBLE);
                    if (mode != 2) {
                        if (mode == 0) {
                            //    YoYo.with(Techniques.FadeInRight).duration(200).playOn(rightBorder);
                        } else {
                            //  YoYo.with(Techniques.FadeInRight).duration(200).playOn(rightBorder);
                            //   YoYo.with(Techniques.FadeOutLeft).duration(200).playOn(leftBorder);
                        }

                    }
                    mode = 2;


                } else if (scrollProgressPercent < -0.01) {
                    //  leftBorder.setVisibility(View.VISIBLE);
                    // rightBorder.setVisibility(View.GONE);
                    if (mode != 1) {
                        if (mode == 0) {
                            //    YoYo.with(Techniques.FadeInLeft).duration(200).playOn(leftBorder);
                        } else {
                            //    YoYo.with(Techniques.FadeOutRight).duration(200).playOn(rightBorder);
                            //  YoYo.with(Techniques.FadeInLeft).duration(200).playOn(leftBorder);
                        }
                    }
                    mode = 1;

                } else {
                    if (mode != 0) {
                        //  YoYo.with(Techniques.FadeOutRight).duration(200).playOn(rightBorder);
                        // YoYo.with(Techniques.FadeOutLeft).duration(200).playOn(leftBorder);
                    }
                    mode = 0;
                    //   leftBorder.setVisibility(View.GONE);
                    //  rightBorder.setVisibility(View.GONE);
                }
            }
        });


    }
}
