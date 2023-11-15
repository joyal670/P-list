package com.iroid.patrickstore.ui.my_account.my_addresses


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.iroid.patrickstore.R

/**
 * A simple [Fragment] subclass.
 */
class MyAddressesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_adresses, container, false)
    }


}
