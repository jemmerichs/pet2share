package io.pet2share.pet2share.view.home.tabs.offers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.data.PictureLoader;
import io.pet2share.pet2share.model.offer.Offer;
import io.pet2share.pet2share.view.detail.DetailOfferActivity;

/**
 * Created by Muki-Zenbook on 13.10.2016.
 */

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferItemViewHolder> {

    private List<Offer> offerList = new ArrayList<>();
    private Context context;

    public OfferAdapter(List<Offer> offerList, Context context) {
        this.offerList = offerList;
        this.context = context;
    }

    @Override
    public OfferItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer, parent, false);

        return new OfferAdapter.OfferItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OfferItemViewHolder holder, int position) {

        Offer offer = offerList.get(position);
        holder.name.setText(offer.getName());
        holder.name.setTypeface(EasyFonts.caviarDreams(holder.image.getContext()));


        holder.cardView.setOnClickListener(v -> {
            Intent i = new Intent(context, DetailOfferActivity.class);
            i.putExtra("uid", offer.getUserId());
            i.putExtra("key", offer.getKey());
            context.startActivity(i);
        });

        if (offer.getPictureURIs() != null) {
            PictureLoader.getInstance().loadPictureDownloadURL(offer.getPictureURIs().get(0), url -> {
                Picasso.with(context).load(url).into(holder.image);
            });
        }
        holder.image.setImageDrawable(context.getDrawable(R.drawable.image_placeholder));

    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }


    public static class OfferItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        protected ImageView image;

        @BindView(R.id.name)
        protected TextView name;

        @BindView(R.id.cardView)
        protected CardView cardView;

        public OfferItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }
}
