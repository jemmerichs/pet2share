package io.pet2share.pet2share.view.create;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.common.BasicActivity;
import io.pet2share.pet2share.common.Receiver;
import io.pet2share.pet2share.common.ServiceResultReceiver;
import io.pet2share.pet2share.data.offer.OfferLoader;
import io.pet2share.pet2share.model.offer.Offer;
import io.pet2share.pet2share.services.CreateOfferService;

/**
 * Created by Muki-Zenbook on 20.10.2016.
 */

public class CreateOfferActivity extends BasicActivity implements Receiver {

    @BindView(R.id.insert) protected      Button    insert;
    @BindView(R.id.title) protected       EditText  title;
    @BindView(R.id.description) protected EditText  description;
    @BindView(R.id.image) protected       ImageView imageView;


    private ServiceResultReceiver serviceResultReceiver;
    private ProgressDialog        progressDialog;
    private Bitmap                bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer);
        ButterKnife.bind(this);
        serviceResultReceiver = new ServiceResultReceiver(new Handler());
        serviceResultReceiver.setReceiver(this);
    }

    @OnClick(R.id.insert)
    protected void insertOffer() {

        Date soon = new Date();
        soon.setTime(new Date().getTime() + 1000000);
        Date sooner = new Date();
        sooner.setTime(new Date().getTime() + 10000);
        ArrayList<Long> times = new ArrayList<>();
        times.add(sooner.getTime());
        times.add(soon.getTime());
        Offer offer = new Offer(title.getText().toString(), new Date().getTime(), soon.getTime(), 49.4844656, 8.4678411, times,
                                description.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        progressDialog = new ProgressDialog(this);
        CreateOfferService.startCreateOfferService(this, progressDialog, serviceResultReceiver, offer, bitmap);

    }

    @OnClick(R.id.image)
    protected void addPhoto(View view) {
        File imageFolder = new File(getCacheDir() + "/images");
        imageFolder.mkdir();
        File image = new File(imageFolder, String.valueOf(UUID.randomUUID()));
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = (Bitmap) data.getExtras().get("data");

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bitmap);

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        progressDialog.dismiss();
    }
}
