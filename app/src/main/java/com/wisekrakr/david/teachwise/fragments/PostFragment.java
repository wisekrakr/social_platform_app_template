package com.wisekrakr.david.teachwise.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.actions.PostActions;
import com.wisekrakr.david.teachwise.adapters.PostAdapter;
import com.wisekrakr.david.teachwise.models.PostModel;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class PostFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String postId = prefs.getString("postId", "none");

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<PostModel> postList = new ArrayList<>();

        PostAdapter postAdapter = new PostAdapter(getContext(), postList);

        recyclerView.setAdapter(postAdapter);

        PostActions postActions = new PostActions(postAdapter, postList);

        postActions.getPost(postId);

        return view;
    }
}
