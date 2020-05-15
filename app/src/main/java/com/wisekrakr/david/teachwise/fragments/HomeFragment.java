package com.wisekrakr.david.teachwise.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.actions.PostActions;
import com.wisekrakr.david.teachwise.activities.LoginActivity;
import com.wisekrakr.david.teachwise.adapters.PostAdapter;
import com.wisekrakr.david.teachwise.models.PostModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HomeFragment extends Fragment {

    //views
    private RecyclerView recyclerView;

    //post adapter
    private PostAdapter postAdapter;

    private List<PostModel>postsList;

    //post actions
    private PostActions postActions;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //init views
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //init list
        postsList = new ArrayList<>();

        //init user adapter
        postAdapter = new PostAdapter(getContext(), postsList);

        //init post actions
        postActions = new PostActions(postAdapter, postsList);

        //checking following
        postActions.checkFollowing();

        //set adapter to view
        recyclerView.setAdapter(postAdapter);

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); //show menu option in fragment
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflate option menu
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        //inflating menu
        inflater.inflate(R.menu.top_nav,menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * Handle menu item clicks
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get item id
        int id = item.getItemId();
        System.out.println("logout");
        if(id == R.id.action_logout){

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
