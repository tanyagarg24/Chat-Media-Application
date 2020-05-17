package com.example.chatapp;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAcessAdapter extends FragmentPagerAdapter {
    public TabsAcessAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PostFragment postFragment= new PostFragment();
                return postFragment;
            case 1:
                ChatFragment chatFragment=new ChatFragment();
                return chatFragment;
            case 2:
                ContactsFragment contactsFragment=new ContactsFragment();
                return contactsFragment;



            default:
                return null;


        }

    }

    @Override
    public int getCount() {

        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "posts";
            case 1:
                return "chats";
            case 2:
                return "contacts";



            default:
                return null;

        }
    }
}
