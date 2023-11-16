package com.user.student.ui.start_up.auth.adapter;




import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.user.student.ui.start_up.auth.fragment.LoginFragment;
import com.user.student.ui.start_up.auth.fragment.SignUpFragment;


public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // return fragments at every position
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:{
                Log.e("TAG", "createFragment: " );
                return new LoginFragment();
            }

            case 1:
                return new SignUpFragment();
        }
        return new LoginFragment(); //chats fragment
    }

    // return total number of tabs in our case we have 3
    @Override
    public int getItemCount() {
        return 2;
    }
}