package io.pet2share.pet2share.view.home.tabs.offers;

import android.graphics.Picture;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import io.pet2share.pet2share.R;
import io.pet2share.pet2share.data.PictureLoader;
import io.pet2share.pet2share.data.offer.OfferLoader;
import io.pet2share.pet2share.model.offer.Offer;
import io.pet2share.pet2share.view.home.OverviewActivity;

/**
 * Created by Bausch on 24.10.2016.
 */

public class DiscoverAdapter extends ArrayAdapter<Offer> {

    private DiscoverFragment discoverFragment;

    public DiscoverAdapter(DiscoverFragment discoverFragment, List<Offer> objects) {
        super(discoverFragment.getContext(), 0, objects);
        this.discoverFragment = discoverFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Offer offer = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_discover, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        TextView textView = (TextView) convertView.findViewById(R.id.textView);

        if(offer.getPictureURIs() != null) {
            PictureLoader.getInstance().loadPictureDownloadURL(offer.getPictureURIs().get(0), offerURL -> {
                Picasso.with(getContext()).load(offerURL).placeholder(R.drawable.image_placeholder).into(imageView);
            });
        }
        textView.setText(offer.getName());

        return convertView;
    }
}
