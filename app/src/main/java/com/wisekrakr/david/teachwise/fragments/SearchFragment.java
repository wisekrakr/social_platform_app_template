package com.wisekrakr.david.teachwise.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.actions.UserActions;
import com.wisekrakr.david.teachwise.adapters.UserAdapter;
import com.wisekrakr.david.teachwise.models.UserModel;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText searchBar;

    private List<UserModel> usersList;

    private UserAdapter userAdapter;

    private UserActions userActions;

    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        //init views
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        searchBar = view.findViewById(R.id.search_bar);

        //init userslist
        usersList = new ArrayList<>();

        //init user adapter
        userAdapter = new UserAdapter(getContext(), usersList, true);

        //set adapter to view
        recyclerView.setAdapter(userAdapter);

        //init user actions
        userActions = new UserActions(userAdapter, usersList);

        //init user actions
        userActions.getAllUsers();
        handleSearch();

        return view;
    }

    private void handleSearch(){
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userActions.searchUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}
