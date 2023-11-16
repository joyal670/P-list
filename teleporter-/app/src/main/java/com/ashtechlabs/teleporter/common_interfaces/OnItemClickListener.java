package com.ashtechlabs.teleporter.common_interfaces;

import android.view.View;

/**
 * Created by Jesna on 4/21/2016.
 */
public interface OnItemClickListener {

    void onItemClick(View view, int position, int which);

    void onRateCardDelete(View view, int position, int which);

}
