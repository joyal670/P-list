package com.iroid.jeetmeet.ui.main.parent_panel.chat

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseActivity
import com.iroid.jeetmeet.databinding.ActivityParentChatBinding
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import kotlinx.android.synthetic.main.toolbar_chat.*


class ParentChatActivity : BaseActivity<ActivityParentChatBinding>() {

    private lateinit var parentChatTeacherReadUpdateViewModel: ParentChatTeacherReadUpdateViewModel
    private lateinit var fireStore: FirebaseFirestore
    val chatMessages = ArrayList<ParentTeacherChatModelClass>()

    private var intentName = ""
    private var intentPicUrl = ""
    private var intentChat = ""
    private var intentTeacher = 10000
    private var intentParent = 10000

    override val layoutId: Int
        get() = R.layout.activity_parent_chat
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityParentChatBinding =
        ActivityParentChatBinding.inflate(layoutInflater)


    override fun initData() {

        intentName = intent.getStringExtra("name").toString()
        intentPicUrl = intent.getStringExtra("pic_url").toString()
        intentChat = intent.getStringExtra("chat").toString()
        intentTeacher = intent.getIntExtra("teacher", 10000)
        intentParent = intent.getIntExtra("parent", 10000)

        setSupportActionBar(toolbarChat)
        tvToolbarChatTitle.text = intentName
        if (intentPicUrl == "no") {
            Glide.with(this).load(R.drawable.ic_profile_user).into(img)
        } else {
            Glide.with(this).load(intentPicUrl).into(img)
        }
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_grey)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        try {
            FirebaseApp.initializeApp(this)
            fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("teacher_parent_chats")
                .orderBy("date", Query.Direction.ASCENDING)
                .whereEqualTo("chat_id", intentChat)
                .addSnapshotListener { value, error ->

                    chatMessages.clear()
                    try {
                        if (value != null) {
                            for (d: DocumentSnapshot in value.documents) {
                                val c = d.toObject(ParentTeacherChatModelClass::class.java)
                                chatMessages.add(c!!)
                            }
                        }
                    } catch (ex: Exception) {
                        Log.e("TAG", "setupUI: Chat$ex")
                    }
                    Log.e("TAG", "setupUI: $chatMessages")
                    val linearLayoutManager = LinearLayoutManager(this)
                    linearLayoutManager.stackFromEnd = true
                    binding.rvChat.layoutManager = linearLayoutManager
                    binding.rvChat.adapter = TeacherParentChatAdapter(chatMessages)
                }
        } catch (ex: Exception) {
            Log.e("TAG", "setupUI: Chat$ex")
        }

        setupObserver()
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

        parentChatTeacherReadUpdateViewModel = ParentChatTeacherReadUpdateViewModel()
        parentChatTeacherReadUpdateViewModel.parentChatTeacherMessageRead(intentChat)
        parentChatTeacherReadUpdateViewModel.parentChatTeacherReadData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.DATA_EMPTY -> {
                }

                Status.NO_INTERNET -> {
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )

                }
            }
        })
    }

    override fun onClicks() {

        binding.sentBtn.setOnClickListener {
            val msg = binding.msgEtx.text.toString()
            if (msg.isBlank()) {
                Toaster.showToast(
                    this,
                    "Type message",
                    Toaster.State.WARNING,
                    Toast.LENGTH_LONG
                )
            } else {
                sentMessage(msg)
                binding.msgEtx.setText("")
            }
        }
    }

    private fun sentMessage(msg: String) {

        val timestamp = Timestamp.now()
        val data: MutableMap<String, Any> = HashMap()
        val data1: MutableMap<String, Any> = HashMap()

        data1["teacher"] = intentTeacher.toString()
        data1["parent"] = intentParent.toString()

        data["chat_id"] = intentChat
        data["date"] = timestamp
        data["from_parent"] = intentParent.toString()
        data["message"] = msg
        data["users"] = data1

        fireStore.collection("teacher_parent_chats").document()
            .set(data)
            .addOnSuccessListener {
                parentChatTeacherReadUpdateViewModel = ParentChatTeacherReadUpdateViewModel()
                parentChatTeacherReadUpdateViewModel.parentChatTeacherMessageUpdate(intentChat, msg)
                parentChatTeacherReadUpdateViewModel.parentChatTeacherUpdateData.observe(
                    this,
                    Observer {
                        when (it.status) {
                            Status.SUCCESS -> {
                            }
                            Status.LOADING -> {
                            }
                            Status.ERROR -> {
                                Toaster.showToast(
                                    this,
                                    it.message!!,
                                    Toaster.State.ERROR,
                                    Toast.LENGTH_LONG
                                )
                            }
                            Status.DATA_EMPTY -> {
                            }
                            Status.NO_INTERNET -> {
                                Toaster.showToast(
                                    this,
                                    it.message!!,
                                    Toaster.State.ERROR,
                                    Toast.LENGTH_LONG
                                )
                            }
                        }
                    })

            }
            .addOnFailureListener {
                Toaster.showToast(
                    this,
                    "Something went wrong",
                    Toaster.State.WARNING,
                    Toast.LENGTH_LONG
                )
                Log.e("TAG", "sentMessage: " + it.message)
            }
    }


    override fun fragmentLaunch() {

    }

}