package com.wisekrakr.david.teachwise.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.actions.NotificationActions;
import com.wisekrakr.david.teachwise.adapters.NotificationAdapter;
import com.wisekrakr.david.teachwise.models.NotificationModel;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<NotificationModel>notificationList = new ArrayList<>();

        NotificationAdapter notificationAdapter = new NotificationAdapter(getContext(), notificationList);

        recyclerView.setAdapter(notificationAdapter);

        NotificationActions notificationActions = new NotificationActions(notificationAdapter, notificationList);
        notificationActions.getNotifications();

        return view;
    }


}
