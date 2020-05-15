package com.wisekrakr.david.teachwise.actions;


import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public interface AuthActionsContext {

    void createUser(DatabaseReference reference, HashMap<String, Object> userMap, Context context);
    void authenticateUser(DatabaseReference reference, Context context);

}
