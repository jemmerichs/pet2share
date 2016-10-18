package io.pet2share.pet2share.common;

import android.support.v4.app.Fragment;

import io.pet2share.pet2share.view.home.OverviewActivity;

/**
 * Created by Bausch on 18.10.2016.
 */

public class BasicOverviewActivityFragment extends Fragment {

    public OverviewActivity getOverviewActivity() {
        return ((OverviewActivity)getActivity());
    }

}
