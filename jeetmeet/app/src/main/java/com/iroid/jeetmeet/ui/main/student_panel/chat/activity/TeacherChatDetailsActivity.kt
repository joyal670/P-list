package com.iroid.jeetmeet.ui.main.student_panel.chat.activity

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
import com.iroid.jeetmeet.databinding.ActivityTeacherChatDetailsBinding
import com.iroid.jeetmeet.ui.main.student_panel.chat.adapter.TeacherStudentChatAdapter
import com.iroid.jeetmeet.ui.main.student_panel.chat.model.TeacherChatModelClass
import com.iroid.jeetmeet.ui.main.student_panel.chat.viewmodel.StudentChatTeacherStudentReadViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import kotlinx.android.synthetic.main.toolbar_chat.*

class TeacherChatDetailsActivity : BaseActivity<ActivityTeacherChatDetailsBinding>() {

    private lateinit var studentChatTeacherStudentReadViewModel: StudentChatTeacherStudentReadViewModel
    private lateinit var fireStore: FirebaseFirestore
    val chatMessages = ArrayList<TeacherChatModelClass>()

    private var intentName = ""
    private var intentPicUrl = ""
    private var intentChat = ""
    private var intentTeacher = 10000
    private var intentStudent = 10000

    override val layoutId: Int
        get() = R.layout.activity_teacher_chat_details
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityTeacherChatDetailsBinding =
        ActivityTeacherChatDetailsBinding.inflate(layoutInflater)

    override fun initData() {

        intentName = intent.getStringExtra("name").toString()
        intentPicUrl = intent.getStringExtra("pic_url").toString()
        intentChat = intent.getStringExtra("chat").toString()
        intentTeacher = intent.getIntExtra("teacher", 10000)
        intentStudent = intent.getIntExtra("student", 10000)

        Log.e(
            "TAG",
            "onCreate: $intentName---$intentPicUrl----$intentChat----$intentTeacher----$intentStudent"
        )

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

    }

    override fun fragmentLaunch() {

    }

    override fun setupUI() {
        try {
            FirebaseApp.initializeApp(this)
            fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("teacher_student_chats")
                .orderBy("date", Query.Direction.ASCENDING)
                .whereEqualTo("chat_id", intentChat)
                .addSnapshotListener { value, error ->

                    chatMessages.clear()
                    try {
                        for (d: DocumentSnapshot in value!!.documents) {
                            val c = d.toObject(TeacherChatModelClass::class.java)
                            chatMessages.add(c!!)
                        }
                    } catch (ex: Exception) {
                        Log.e("TAG", "setupUI: Chat$ex")
                    }
                    Log.e("TAG", "setupUI: $chatMessages")
                    val linearLayoutManager = LinearLayoutManager(this)
                    linearLayoutManager.stackFromEnd = true
                    binding.rvTeacherChat.layoutManager = linearLayoutManager
                    binding.rvTeacherChat.adapter = TeacherStudentChatAdapter(chatMessages)
                }
        } catch (ex: Exception) {
            Log.e("TAG", "setupUI: Chat$ex")
        }

        setupObserver()
    }

    override fun setupViewModel() {
    }

    override fun setupObserver() {

        /* api for student teacher read */
        studentChatTeacherStudentReadViewModel = StudentChatTeacherStudentReadViewModel()
        studentChatTeacherStudentReadViewModel.studentTeacherChatRead(intentChat)
        studentChatTeacherStudentReadViewModel.studentTeacherChatReadData.observe(this, Observer {
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
                Toaster.showToast(this, "Type message", Toaster.State.WARNING, Toast.LENGTH_LONG)
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
        data1["student"] = intentStudent.toString()


        data["chat_id"] = intentChat
        data["date"] = timestamp
        data["from_student"] = intentStudent.toString()
        data["message"] = msg
        data["users"] = data1

        fireStore.collection("teacher_student_chats").document()
            .set(data)
            .addOnSuccessListener {

                studentChatTeacherStudentReadViewModel = StudentChatTeacherStudentReadViewModel()
                studentChatTeacherStudentReadViewModel.studentTeacherChatUpdate(intentChat, msg)
                studentChatTeacherStudentReadViewModel.studentTeacherChatUpdateData.observe(
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
}