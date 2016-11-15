package io.pet2share.pet2share.view.home.tabs.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.data.conversation.ConversationLoader;
import io.pet2share.pet2share.data.offer.OfferLoader;

/**
 * Created by Muki-Zenbook on 11.10.2016.
 */

public class MessagesFragment extends Fragment {

    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    private void loadMessages(){
        ConversationLoader.getINSTANCE();
    }
}
