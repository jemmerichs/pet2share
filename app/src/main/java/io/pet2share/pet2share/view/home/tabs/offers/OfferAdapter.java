package io.pet2share.pet2share.view.home.tabs.offers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.pet2share.pet2share.R;

/**
 * Created by Muki-Zenbook on 13.10.2016.
 */

public class OfferAdapter {

    public static class OfferItemViewHolder extends RecyclerView.ViewHolder {


        public OfferItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }
}
