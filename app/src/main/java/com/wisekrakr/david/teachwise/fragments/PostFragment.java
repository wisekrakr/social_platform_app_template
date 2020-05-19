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

    private String postId;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<PostModel> postList;

    private PostActions postActions;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        postId = prefs.getString("postId", "none");

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        postList = new ArrayList<>();

        postAdapter = new PostAdapter(getContext(), postList);

        recyclerView.setAdapter(postAdapter);

        postActions = new PostActions(postAdapter, postList);

        postActions.getPost(postId);

        return view;
    }
}
