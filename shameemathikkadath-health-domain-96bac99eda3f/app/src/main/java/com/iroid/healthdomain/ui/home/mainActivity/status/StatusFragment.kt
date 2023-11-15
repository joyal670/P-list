package com.iroid.healthdomain.ui.home.mainActivity.status

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iroid.healthdomain.data.dummyModel.HashedModel
import com.iroid.healthdomain.data.dummyModel.NewContactsModel
import com.iroid.healthdomain.data.md5
import com.iroid.healthdomain.data.model_class.contacts_api.ContactData
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.databinding.StatusFragmentBinding
import com.iroid.healthdomain.ui.adaptor.StatusAdaptor
import com.iroid.healthdomain.ui.adptorInterface.AdaptorInterface
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.home.mainActivity.HomeRepository
import com.iroid.healthdomain.ui.home.mainActivity.all_contacts.AllContactsViewModel
import com.iroid.healthdomain.ui.home.mainActivity.person.PersonActivity
import com.iroid.healthdomain.ui.preference.AppPreferences
import com.iroid.healthdomain.ui.utils.handleApiError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.lang.reflect.Type

class StatusFragment(val data: List<ContactData>) :
    BaseFragment<AllContactsViewModel, StatusFragmentBinding, HomeRepository>() , AdaptorInterface {

    companion object {

        fun newInstance(data: List<ContactData>) = StatusFragment(data)

    }

    lateinit var hashedModel: HashedModel
    var contactList: List<ContactData> = arrayListOf()
    var newContacts = arrayListOf<NewContactsModel>()
    var builder = StringBuilder()
    private val PROJECTION = arrayOf(
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )
    private val adapter: StatusAdaptor by lazy { StatusAdaptor(this,requireActivity()) }
    var hashedPhone = arrayListOf<String>()



    private fun addObserver() {
        viewModel.matchedContacts.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                    dismissLoader()
                    handleApiError(requireView(), it)
                }
                Resource.Loading -> {
                    showLoader()
                }
                is Resource.Success -> {
                    dismissLoader()
                    if (it.value.status == 200) {
                        val gson = Gson()
                        val json1: String = AppPreferences.user_new_contacts!!
                        val type: Type = object : TypeToken<List<NewContactsModel?>?>() {}.type
                        newContacts=gson.fromJson(json1,type)
                        contactList=it.value.data
                        contactList.forEach {
                            newContacts.forEach { it2->
                                if(it.hashed_mobile.contains(it2.hashedNumber)){
                                    try {
                                        it.name=it2.name
                                    }catch (ex:Exception){
                                        Log.e("#6565656", "addObserver: $ex" )
                                    }

                                }
                            }
                        }
                        adapter.list = contactList
                    }
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()

    }

    @SuppressLint("Range")
    private fun getCont(): StringBuilder {

        val builder = StringBuilder()
        val resolver: ContentResolver = requireActivity().contentResolver
        val cursor: Cursor? = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION,
            null,
            null,
            null
        )
        if (cursor != null) {
            try {

                while (cursor.moveToNext()) {
                    val phoneNumValue = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    )
                    //  Log.i(TAG, "getContacts: $phoneNumValue")
                    var numWithCode = ""
                    if (phoneNumValue.length >= 10) {
                        numWithCode =
                            "91" + phoneNumValue.substring(phoneNumValue.length - 10)
                    }

                    val name = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    )

                    builder.append("Contact: ").append(name).append(", Phone Number: ")
                        .append(
                            numWithCode
                        ).append("\n\n")

                    hashedPhone.add(numWithCode.md5())

                    val dataModel = NewContactsModel(numWithCode.md5(), name)

                }

            } finally {
                cursor.close()
            }
        }

        hashedModel = HashedModel(hashedPhone)
        viewModel.setHashedList(hashedModel)
        return builder
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.statusRecycler.adapter = adapter
        val gson = Gson()
        val json: String = AppPreferences.user_contacts.toString()
        val json1: String = AppPreferences.user_new_contacts!!
        val has :HashedModel = gson.fromJson(json, HashedModel::class.java)
        addObserver()
        has.hashed_mobile.forEach {
            hashedPhone.add(it)
        }

        viewModel.setHashedList(has)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })

    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): StatusFragmentBinding {
        return StatusFragmentBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<AllContactsViewModel> {
        return AllContactsViewModel::class.java
    }

    override fun getFragmentRepository(): HomeRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ApiServices::class.java, authToken = token)
        return HomeRepository(api, userPreferences)
    }

    override fun onItemClick(contactModel: ContactData) {
        val intent = Intent(requireContext(), PersonActivity::class.java)
        val bundle=Bundle()
        bundle.putSerializable("Data",contactModel)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun favorite(id: Int, following: Int) {

    }

    override fun onPause() {
        super.onPause()
        binding.searchView.setQuery("", true)

    }

    override fun refreshAllContact() {

    }
}