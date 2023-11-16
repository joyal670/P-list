package com.iroid.healthdomain.ui.home.mainActivity.all_contacts

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import com.example.awesomedialog.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.dummyModel.HashedModel
import com.iroid.healthdomain.data.dummyModel.NewContactsModel
import com.iroid.healthdomain.data.md5
import com.iroid.healthdomain.data.model_class.contacts_api.ContactData
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.databinding.AllContactsFragmentBinding
import com.iroid.healthdomain.ui.adaptor.AllContactAdaptor
import com.iroid.healthdomain.ui.adaptor.InvitationAdapter
import com.iroid.healthdomain.ui.adptorInterface.AdaptorInterface
import com.iroid.healthdomain.ui.adptorInterface.InviteInterface
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.home.mainActivity.HomeRepository
import com.iroid.healthdomain.ui.home.mainActivity.person.PersonActivity
import com.iroid.healthdomain.ui.preference.AppPreferences
import com.iroid.healthdomain.ui.preference.AppPreferences.contacts_loaded
import com.iroid.healthdomain.ui.preference.AppPreferences.user_contactList
import com.iroid.healthdomain.ui.preference.AppPreferences.user_contacts
import com.iroid.healthdomain.ui.preference.ConstantPreference
import com.iroid.healthdomain.ui.utils.disableItemAnimator
import com.iroid.healthdomain.ui.utils.handleApiError
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*


class AllContactsFragment(val data: List<ContactData>, val function: () -> Unit) :
    BaseFragment<AllContactsViewModel, AllContactsFragmentBinding, HomeRepository>(),
    AdaptorInterface, InviteInterface {

    var builder = StringBuilder()

    var hashedPhone = arrayListOf<String>()
    var matchedHashedPhone = arrayListOf<String>()
    var inviteHashedPhone = arrayListOf<String>()
    var newContacts = arrayListOf<NewContactsModel>()
    var matchedContacts = arrayListOf<NewContactsModel>()
    var invitationList = arrayListOf<NewContactsModel>()
    var contactList: List<ContactData> = arrayListOf()
    var selectedId = 0
    private var isLoded = false
    private lateinit var dialog: AlertDialog
    private var isHashed = false

    private val PROJECTION = arrayOf(
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )


    lateinit var hashedModel: HashedModel

    private val adapter: AllContactAdaptor by lazy { AllContactAdaptor(this) }

    private val inviteAdapter: InvitationAdapter by lazy { InvitationAdapter(this) }

    companion object {
        private const val TAG = "AllContactsFragment"
        fun newInstance(data: List<ContactData>, function: () -> Unit) =
            AllContactsFragment(data, function)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): AllContactsFragmentBinding {
        return AllContactsFragmentBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<AllContactsViewModel> {
        return AllContactsViewModel::class.java
    }

    override fun getFragmentRepository(): HomeRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ApiServices::class.java, authToken = token)
        return HomeRepository(api, userPreferences)
    }


    override fun onResume() {
        super.onResume()
        binding.inviteRecycler.itemAnimator!!.changeDuration = 0
        binding.allContactRecycler.itemAnimator!!.changeDuration = 0
        adapter.notifyDataSetChanged()

    }

    override fun onPause() {
        super.onPause()
        binding.searchView.setQuery("", true)

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.allContactRecycler.adapter = adapter
        binding.allContactRecycler.setHasFixedSize(true)
        binding.inviteRecycler.adapter = inviteAdapter
        binding.inviteRecycler.setHasFixedSize(true)
        binding.allContactRecycler.isNestedScrollingEnabled = false
        binding.inviteRecycler.isNestedScrollingEnabled = false
        binding.inviteRecycler.itemAnimator!!.changeDuration=0
        binding.allContactRecycler.itemAnimator!!.changeDuration=0

        val gson = Gson()
        val json: String = user_contacts.toString()
        val json1: String = AppPreferences.user_new_contacts!!
        val has :HashedModel = gson.fromJson(json, HashedModel::class.java)
        val type: Type = object : TypeToken<List<NewContactsModel?>?>() {}.type
        newContacts=gson.fromJson(json1,type)




        hashedModel = has


        /* hashedModel = HashedModel(hashedPhone)
         GlobalScope.launch {
             builder = getCont()
         }*/


        // showLoader()
        addObserver()

        if (contacts_loaded) {


            has.hashed_mobile.forEach {
                hashedPhone.add(it)
            }

            viewModel.setHashedList(has)


            Log.e(TAG, "onActivityCreated: has" + has)
            Log.e(TAG, "onActivityCreated: hashedPhone" + hashedPhone)

        } else {
            GlobalScope.launch {
                builder = getCont()
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                inviteAdapter.filter.filter(newText)

                /* if(newText!!.isEmpty()){
                     adapter.list=contactList
                 }else{

                 }*/


                return false
            }
        })


    }

    private fun addObserver() {
        viewModel.favResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                    //dismissLoader()
                    handleApiError(requireView(), it)
                }
                is Resource.Loading -> {
                    //showLoader()
                }
                is Resource.Success -> {
                    //dismissLoader()
                    if (it.value.status == 200) {
                        var followersList: ArrayList<String> =
                            AppPreferences.getArray(ConstantPreference.NEW_FOLLOWER)!!
                        contactList.forEach { contactData ->


//                            if(contactData.id==selectedId){
//                                if(contactData.is_following==0){
//                                    contactData.is_following=1
//                                }else{
//                                    contactData.is_following=0
//                                }
//                                GlobalScope.launch {
//                                    userPreferences.saveFollower(false)
//                                }
//                            }

                        }
                        if (followersList.contains(selectedId.toString())) {
                            followersList.remove(selectedId.toString())
                        } else {
                            followersList.add(selectedId.toString())
                        }
                        AppPreferences.setArray(ConstantPreference.NEW_FOLLOWER, followersList)
                        val gson = Gson()
                        val json1 = gson.toJson(contactList)
                        user_contactList = json1
                        adapter.list = contactList
                    }
                }
            }
        }
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
                        contactList = it.value.data
                        var followersList: ArrayList<String> = ArrayList()
                        contactList.forEach {
                            try {
                                if (it.is_following == 1) {
                                    followersList.add(it.id.toString())
                                }
                            } catch (ex: Exception) {

                            }

                            newContacts.forEach { it2 ->
                                if (it.hashed_mobile.contains(it2.hashedNumber)) {
                                    try {
                                        it.name = it2.name
                                    } catch (ex: Exception) {
                                    }

                                }
                            }
                        }
                        AppPreferences.setArray(ConstantPreference.NEW_FOLLOWER, followersList)
                        adapter.list = contactList
                        if (!isLoded) {
                            isLoded = true
                            prepareInviteList(it.value.data)
                        }


                    }
                }
            }
        }

        viewModel.userInvite.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                    handleApiError(requireView(), it)
                }
                Resource.Loading -> {

                }
                is Resource.Success -> {


                }
            }
        }
    }

    private fun prepareInviteList(data: List<ContactData>) {
        val s = SimpleDateFormat("hh:mm:ss")
        val format: String = s.format(Date())
        Log.e("123456", "prepareInviteList: $format")
        for (i in data.indices) {
            try {
                val dataModel = NewContactsModel(data[i].hashed_mobile, data[i].name)
                matchedContacts.add(dataModel)
                matchedHashedPhone.add(data[i].hashed_mobile)
            } catch (ex: Exception) {

            }

        }
        val format1: String = s.format(Date())
        Log.e("123456", "format1: $format1")

        Log.e(TAG, "prepareInviteList: newContacts" + newContacts.size)
        Log.e(TAG, "prepareInviteList: data" + data.size)
        inviteHashedPhone = hashedPhone
        inviteHashedPhone.removeAll(matchedHashedPhone.toSet())

        val set: HashSet<String> = HashSet(inviteHashedPhone)
        inviteHashedPhone.clear()
        inviteHashedPhone.addAll(set)
        if (newContacts.size > 200) {
            for (i in 0..200) {
                if (inviteHashedPhone.contains(newContacts[i].hashedNumber)) {
                    invitationList.add(newContacts[i])
                }
            }
        } else {
            newContacts.forEach { newContacts ->
                if (inviteHashedPhone.contains(newContacts.hashedNumber)) {
                    invitationList.add(newContacts)
                }
            }
        }

//        if(newContacts.size>100){
//            for (i in 0..100) {
//
//            }
//        }else{
//            for (i in newContacts.indices) {
//                for (j in inviteHashedPhone.indices) {
//                    if (newContacts[i].hashedNumber == inviteHashedPhone[j]) {
//                        invitationList.add(newContacts[i])
//                    }
//                }
//            }
//        }

        val format3: String = s.format(Date())
        Log.e("123456", "format3: $format3")

        val inviteSet: HashSet<NewContactsModel> = HashSet(invitationList)
        invitationList.clear()
        invitationList.addAll(inviteSet)

//        invitationList.sortWith(Comparator { p0, p1 -> p0!!.name.compareTo(p1!!.name) })

        inviteAdapter.list = invitationList
        /* val handler = Handler()
         handler.postDelayed(Runnable {
             dismissLoader()
         }, 15000)*/

    }

    override fun onItemClick(contactModel: ContactData) {
        //   Log.i(TAG, "onItemClick: $contactModel")

        selectedId = contactModel.id
        val intent = Intent(requireContext(), PersonActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("Data", contactModel)
        intent.putExtras(bundle)
        startActivity(intent)
//        selectedId=contactModel.id


        /*NavHostFragment.findNavController(this)
            .navigate(R.id.action_homeFragment_to_personFragment)*/

        // listener?.onLoadChallenge(contactModel)
    }

    override fun favorite(id: Int, following: Int) {
        selectedId = id
        if (following == 1) {
            viewModel.sentFavRequest(id.toString(), "0")
        } else {
            viewModel.sentFavRequest(id.toString(), "1")
        }
    }

    override fun refreshAllContact() {
        binding.allContactRecycler.setHasFixedSize(true)
        binding.inviteRecycler.setHasFixedSize(true)
        binding.inviteRecycler.itemAnimator!!.changeDuration = 0
        binding.allContactRecycler.itemAnimator!!.changeDuration = 0
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
                    newContacts.add(dataModel)

                }

            } finally {
                cursor.close()
            }
        }

        hashedModel = HashedModel(hashedPhone)
        viewModel.setHashedList(hashedModel)
        return builder
    }


    @SuppressLint("Range")
    private fun getContacts(): StringBuilder {
        val builder = StringBuilder()
        val resolver: ContentResolver = requireActivity().contentResolver
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null
        )
        if (cursor != null) {

            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber = (cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    )).toInt()

                    if (phoneNumber > 0) {
                        val cursorPhone = resolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            arrayOf(id),
                            null
                        )

                        if (cursorPhone?.count!! > 0) {
                            while (cursorPhone.moveToNext()) {
                                val phoneNumValue = cursorPhone.getString(
                                    cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                )
                                //  Log.i(TAG, "getContacts: $phoneNumValue")
                                var numWithCode = ""
                                if (phoneNumValue.length >= 10) {
                                    numWithCode =
                                        "91" + phoneNumValue.substring(phoneNumValue.length - 10)
                                }
                                //   Log.i(TAG, "getContacts: $numWithCode")

                                val name = cursorPhone.getString(
                                    cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                                )

                                builder.append("Contact: ").append(name).append(", Phone Number: ")
                                    .append(
                                        numWithCode
                                    ).append("\n\n")
                                // Log.e(TAG, "$name : $numWithCode  : ${numWithCode.md5()}")
                                hashedPhone.add(numWithCode.md5())

                                var dataModel = NewContactsModel(numWithCode.md5(), name)
                                newContacts.add(dataModel)

                            }
                        }
                        cursorPhone.close()
                    }
                }
            } else {
                //   toast("No contacts available!")
            }
        }

        if (cursor != null) {
            cursor.close()
        }

        hashedModel = HashedModel(hashedPhone)
        viewModel.setHashedList(hashedModel)
        return builder

    }

    override fun onInviteItemClick(string: String) {
        AwesomeDialog.build(requireActivity())
            .title(string)
            .body("$string don't have Health Domain App")
            .icon(R.drawable.ic_round_account_grey)
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive("INVITE", buttonBackgroundColor = R.drawable.drawable_button) {
                sendInvite(string)
            }
            .onNegative("LATER", buttonBackgroundColor = R.drawable.drawable_button) {

            }
    }

    override fun onRefresh() {
        binding.allContactRecycler.setHasFixedSize(true)
        binding.inviteRecycler.setHasFixedSize(true)
        binding.inviteRecycler.itemAnimator!!.changeDuration = 0
        binding.allContactRecycler.itemAnimator!!.changeDuration = 0
    }

    private fun sendInvite(string: String) {
        val message =
            "Hey $string, I'm inviting you to use Health Domain, a simple and motivational way to track your fitness. Download now and stay connected with your best buddies."
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Invite")
        requireContext().startActivity(shareIntent)

    }
}



