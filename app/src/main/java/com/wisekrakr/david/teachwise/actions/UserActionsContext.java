package com.wisekrakr.david.teachwise.actions;


public interface UserActionsContext {
    void getAllUsers();
    void searchUsers(String searchQuery);
    void showUsers();
    void getFollowers(String id);
    void getFollowing(String id);
    void getLikes(String id);
}
