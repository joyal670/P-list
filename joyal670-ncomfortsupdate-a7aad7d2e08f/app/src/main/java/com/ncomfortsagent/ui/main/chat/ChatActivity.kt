package com.ncomfortsagent.ui.main.chat

import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseActivity
import com.ncomfortsagent.databinding.ActivityChatBinding
import com.ncomfortsagent.ui.main.chat.adapter.ChatAdapter
import com.ncomfortsagent.ui.main.chat.model.ChatModelClass
import com.ncomfortsagent.utils.AppPreferences
import com.ncomfortsagent.utils.CommonUtils.Companion.dismissKeyboard
import com.ncomfortsagent.utils.Toaster
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : BaseActivity<ActivityChatBinding>() {
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var userImage: String

    private lateinit var chatAdapter: ChatAdapter
    override val layoutId: Int
        get() = R.layout.activity_chat
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityChatBinding = ActivityChatBinding.inflate(layoutInflater)

    override fun initData() {

        setSupportActionBar(binding.includeToolbar.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.includeToolbar.tvToolbarTitle.text = getString(R.string.chat)

        intent.getStringExtra("user_id")?.let { id: String ->
            userId = id
        }
        intent.getStringExtra("user_name")?.let { name: String ->
            userName = name
        }
        intent.getStringExtra("user_image")?.let { image: String ->
            userImage = image
        }
        Log.e("user id", userId)
        Log.e("user name", userName)
        Log.e("user image", userImage)
        FirebaseApp.initializeApp(this)
        fireStore = FirebaseFirestore.getInstance()
        val collectionPath = "/user_agent_chat/USER$userId/AGENT${AppPreferences.agent_id}"
        val chatList: ArrayList<ChatModelClass> = ArrayList()
        fireStore.collection(collectionPath)
            .orderBy("time", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                chatList.clear()
                value!!.documents.forEach {
                    chatList.add(
                        ChatModelClass(
                            it.data!!["msg"].toString(),
                            it.data!!["time"].toString(),
                            it.data!!["from_id"].toString(),
                            it.data!!["to_id"].toString(),
                            it.data!!["type"].toString(),
                            it.data!!["image"].toString()
                        )
                    )
                }
                Log.e("initData: ", Gson().toJson(chatList))
                val linearLayoutManager = LinearLayoutManager(this)
                binding.rvChat.layoutManager = linearLayoutManager
                //Log.e("TAG", "initData:"+ Gson().toJson(chatList.sortedWith(compareBy { it.time as java.sql.Timestamp }))  )
                chatAdapter = ChatAdapter(chatList, userImage)
                binding.rvChat.adapter = chatAdapter
                binding.rvChat.scrollToPosition(binding.rvChat.adapter!!.itemCount - 1)
            }

    }

    override fun fragmentLaunch() {
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {
    }

    override fun onClicks() {
        binding.sentBtn.setOnClickListener {
            if (binding.msgEtx.text.trim().toString().isNotBlank()) {
                this.dismissKeyboard()
                senMessage()
            }
        }
    }

    private fun senMessage() {
        showProgress()
        val timestamp = Timestamp.now()
        Log.e("TAG", "senMessage: " + Calendar.getInstance(Locale.getDefault()))
        Log.e("TAG", "senMessage: " + Timestamp.now())

        val sdf = SimpleDateFormat("MM-dd-yyyy HH:mm", Locale.getDefault())
        val currentDate = sdf.format(Date())


        val data1: MutableMap<String, Any> = HashMap()
        data1["from_id"] = AppPreferences.agent_id.toString()
        data1["to_id"] = userId
        data1["type"] = "AGENT"
        data1["time"] = System.currentTimeMillis().toString()
        data1["msg"] = binding.msgEtx.text.trim().toString()
        data1["image"] = ""

        fireStore.collection("user_agent_chat").document("USER$userId")
            .collection("AGENT${AppPreferences.agent_id}").document()
            .set(data1)
            .addOnSuccessListener {
                dismissProgress()
                binding.msgEtx.text = null
                binding.rvChat.scrollToPosition(binding.rvChat.adapter!!.itemCount - 1)
                chatAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                dismissProgress()
                Toaster.showToast(
                    this,
                    "Something went wrong",
                    Toaster.State.WARNING,
                    Toast.LENGTH_LONG
                )
                Log.e("TAG", "sentMessage: " + it.message)
            }
    }

    /* override fun onBackPressed() {
         super.onBackPressed()
         fireStore.clearPersistence()
     }*/

}