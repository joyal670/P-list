package com.protenium.irohub.commoninterface;

import androidx.fragment.app.Fragment;

public interface FragmentTransactionListener {
    void replaceFragment(Fragment fragment);
    void replaceFragment(String fragmentTag);
    void popBackStackReplaceFragment(Fragment fragment);
    void setTitle(String title);
    void clearBackStack();void hideToolbar(boolean hideToolbar);

}
