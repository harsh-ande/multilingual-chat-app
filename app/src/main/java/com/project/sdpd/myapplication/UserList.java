package com.project.sdpd.myapplication;

import java.util.ArrayList;

/**
 * Created by Harsh on 19-Nov-17.
 */

public class UserList {
    private int count;
    private ArrayList<String> users;

    public int getCount()
    {
        return count;
    }

    public void regUser(String str)
    {
        users.add(str);
        count++;
    }

    public void removeUser(String str)
    {
        users.remove(str);
        count--;
    }

}
