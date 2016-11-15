package io.pet2share.pet2share.view.create;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.common.BasicActivity;
import io.pet2share.pet2share.common.Receiver;
import io.pet2share.pet2share.common.ServiceResultReceiver;
import io.pet2share.pet2share.common.StaticContent;
import io.pet2share.pet2share.model.offer.Offer;
import io.pet2share.pet2share.services.CreateOfferService;

/**
 * Created by Muki-Zenbook on 20.10.2016.
 */

public class CreateOfferActivity extends BasicActivity implements Receiver {

    @BindView(R.id.insert)
    protected Button insert;
    @BindView(R.id.title)
    protected EditText title;
    @BindView(R.id.description)
    protected EditText description;
    @BindView(R.id.image)
    protected ImageView imageView;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.datestart)
    protected EditText startText;
    @BindView(R.id.dateend)
    protected EditText endText;
    @BindView(R.id.spinner)
    protected MaterialSpinner categorySpinner;
    @BindView(R.id.dates)
    protected EditText meetings;

    private ServiceResultReceiver serviceResultReceiver;
    private ProgressDialog progressDialog;
    private Bitmap bitmap;
    private Date start;
    private Date end;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final int REQUEST_CODE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer);
        ButterKnife.bind(this);
        serviceResultReceiver = new ServiceResultReceiver(new Handler());
        serviceResultReceiver.setReceiver(this);
        setSupportActionBar(toolbar);
        setTitle(R.string.create_offer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, StaticContent.petCategories());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    @OnClick(R.id.insert)
    protected void insertOffer() {


        Offer offer = new Offer(title.getText().toString(), start.getTime(),end.getTime(), 49.4844656, 8.4678411, meetings.getText().toString(),
                description.getText().toString(), StaticContent.petCategories()[categorySpinner.getSelectedItemPosition()],FirebaseAuth.getInstance().getCurrentUser().getUid());
        progressDialog = new ProgressDialog(this);
        CreateOfferService.startCreateOfferService(this, progressDialog, serviceResultReceiver, offer, bitmap);

    }

    @OnClick(R.id.image)
    protected void addPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 0);
    }

    @OnClick(R.id.datestart)
    protected void setStartDate(View view) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                (view1, year, monthOfYear, dayOfMonth) -> {
                    String dateString = String.format("%s-%s-%s",year,monthOfYear,dayOfMonth);
                    try {
                        start = simpleDateFormat.parse(dateString);
                        startText.setText(String.format("%s-%s-%s",year,monthOfYear+1,dayOfMonth));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @OnClick(R.id.dateend)
    protected void setEndDate(View view) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                (view1, year, monthOfYear, dayOfMonth) -> {
                    String dateString = String.format("%s-%s-%s",year,monthOfYear,dayOfMonth);
                    try {
                        end = simpleDateFormat.parse(dateString);
                        endText.setText(String.format("%s-%s-%s",year,monthOfYear+1,dayOfMonth));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getExtras() != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        progressDialog.dismiss();
        onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
