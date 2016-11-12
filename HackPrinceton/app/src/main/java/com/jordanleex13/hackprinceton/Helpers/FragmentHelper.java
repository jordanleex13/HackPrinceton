package com.jordanleex13.hackprinceton.Helpers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Jordan on 2016-11-11.
 */

public class FragmentHelper {

    // utility method to set up the action bar accordingly in a fragment
    public static void setUpActionBar(Context c, boolean enabled, String title) {
        ((AppCompatActivity)c).getSupportActionBar().setTitle(title);
        ((AppCompatActivity)c).getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);
        ((AppCompatActivity)c).getSupportActionBar().setHomeButtonEnabled(enabled);
    }

    // utility method to facilitate fragment transactions (without custom animations)
    public static void swapFragments(FragmentManager manager, int placeholder,
                                     Fragment newFragment, boolean add, boolean addToBackStack,
                                     String transactionTag, String nextFragmentTag) {

        swapFragments(manager, placeholder, newFragment, add,
                addToBackStack, transactionTag, nextFragmentTag, false, 0, 0);
    }

    // utility method to facilitate fragment transactions (with custom animations)
    public static void swapFragments(FragmentManager manager, int placeholder,
                                     Fragment newFragment, boolean add, boolean addToBackStack,
                                     String transactionTag, String nextFragmentTag, boolean addAnimation, int enterAnimation,
                                     int exitAnimation) {

        FragmentTransaction transaction = manager.beginTransaction();

        // set the custom (entrance and exit) animations
        if(addAnimation)
            transaction.setCustomAnimations(enterAnimation, 0, 0, exitAnimation);

        // if replacing (not adding) the current fragment, explicitly
        // remove it from the fragment placeholder before adding the new fragment
        // note: only for code sanity. replace() is identical in this context
        if(!add) {
            transaction.remove(manager.findFragmentById(placeholder));
        }

        // add the new fragment to the fragment container
        transaction.add(placeholder, newFragment, nextFragmentTag);

        // push the transaction onto the back-stack
        if(addToBackStack) {
            transaction.addToBackStack(transactionTag);
        }

        // commit the transaction
        transaction.commit();
    }
}

