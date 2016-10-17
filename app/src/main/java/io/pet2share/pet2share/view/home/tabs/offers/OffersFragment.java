package io.pet2share.pet2share.view.home.tabs.offers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import io.pet2share.pet2share.R;

/**
 * Created by Muki-Zenbook on 11.10.2016.
 */

public class OffersFragment extends Fragment {

    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_still_to_come, container, false);
        return view;
    }
}
