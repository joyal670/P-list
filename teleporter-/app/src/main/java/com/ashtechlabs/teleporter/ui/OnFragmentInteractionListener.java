package com.ashtechlabs.teleporter.ui;

import android.net.Uri;

/**
 * Created by VIDHU on 1/19/2017.
 */

public interface OnFragmentInteractionListener extends ICommonProgressCallback{

    void onFragmentInteraction(Uri uri);

    void setToolbarTitle(String toolbarTitle);

    void replaceFragment(String fragment_tag);
}
