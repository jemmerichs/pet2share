package io.pet2share.pet2share.view.detail;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Days;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.data.PictureLoader;
import io.pet2share.pet2share.data.offer.OfferLoader;
import io.pet2share.pet2share.model.offer.Offer;

public class DetailOfferActivity extends AppCompatActivity {

    private Offer offer;

    @BindView(R.id.imageView)
    protected ImageView offerImageView;

    @BindView(R.id.textDescription)
    protected TextView textViewDescription;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.contact)
    protected Button contactBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_offer);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("Loading...");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
 /*       fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConversationLoader.getINSTANCE().createConversation(getIntent().getExtras().getString("uid"),getIntent().getExtras().getString("key"));
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadOffer();
    }

    private void loadOffer() {
        String uid = getIntent().getExtras().getString("uid");
        String key = getIntent().getExtras().getString("key");
        OfferLoader.getInstance().loadSingleOffer(uid, key, offers -> initOffer(offers.get(0)));
    }

    @OnClick(R.id.contact)
    protected void sendMessage() {
        new MaterialDialog.Builder(this)
                .title(R.string.title)
                .customView(R.layout.dialog_message, false)
                .positiveText(R.string.send_message)
                .negativeText(R.string.cancel)
                .titleColorRes(R.color.colorPrimary)
                .dividerColorRes(R.color.colorPrimary)
                .positiveColorRes(R.color.colorAccent)
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                })
                .onNegative((dialog, which) -> {
                    dialog.dismiss();
                })
                .cancelable(false)
                .show();
    }

    private void initOffer(Offer offer) {
        this.offer = offer;
        this.setTitle(offer.getName());
        this.textViewDescription.setText(offer.getDescription());

        DateTime start = new DateTime(offer.getStartdate());
        DateTime end = new DateTime(offer.getEnddate());

        int days = Days.daysBetween(start.toLocalDate(), end.toLocalDate()).getDays();

        if (offer.getPictureURIs() != null) {
            PictureLoader.getInstance().loadPictureDownloadURL(offer.getPictureURIs().get(0), url -> Picasso.with(this).load(url).placeholder(R.drawable.image_placeholder).into(offerImageView));
        } else {
            offerImageView.setVisibility(View.GONE);
        }

    }


}
