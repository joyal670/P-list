package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.personal.my_profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentStudentProfileBinding
import com.iroid.jeetmeet.dialogs.FullScreenImageDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.ui.main.student_panel.chat.activity.StudentChatActivity
import com.iroid.jeetmeet.ui.main.student_panel.home.viewmodel.StudentProfileViewModel
import com.iroid.jeetmeet.ui.main.student_panel.sidemenu.activity.StudentSideMenuActivity
import com.iroid.jeetmeet.utils.AppPreferences.prefStudentName
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import java.io.File


class StudentProfileFragment : BaseFragment() {
    private lateinit var binding: FragmentStudentProfileBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var studentProfileViewModel: StudentProfileViewModel

    private val PICK_CAMERA_IMAGES_CODE = 100
    private val PICK_GALLERY_IMAGES_CODE = 101

    private var imageFile: File? = null

    var studImgUrl: String? = null
    var studName: String? = null
    var parntImgUrl: String? = null
    var parntName: String? = null

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentProfileBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {

        /* setup toolbar  */
        fragmentTransInterface = activity as StudentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.nill))
        fragmentTransInterface.setTitleinCenter(true)
        fragmentTransInterface.setFontFamily(R.font.barlow_extrabold)
        fragmentTransInterface.setTitleCaptial(true)

        setupObserver()
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        binding.swipeRefreshLayout.setRefreshing(false)
        studentProfileViewModel = StudentProfileViewModel(requireContext())
        studentProfileViewModel.studentProfileApi()
        studentProfileViewModel.studentProfileData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    /* load data */
                    studImgUrl = it.data!!.data.profile_image_url

                    Glide.with(requireActivity()).load(studImgUrl).into(binding.ivStudentProfile)
                    parntImgUrl = it.data.data.parents.image_url
                    Glide.with(requireActivity()).load(parntImgUrl).into(binding.ivParentPic)

                    studName =
                        it.data.data.first_name + " " + it.data.data.middle_name + " " + it.data.data.last_name
                    binding.sampleLiner.text = studName

                    binding.tvStudentRegNo.text = "Reg.No : " + it.data.data.reg_number

                    parntName =
                        it.data.data.parents.first_name + " " + it.data.data.parents.last_name
                    binding.tvStudentParentName.text = parntName


                    binding.tvStudentGender.text = ": " + it.data.data.gender
                    binding.tvStudentClass.text = ": " + it.data.data.classname.name
                    binding.tvStudentDivision.text = ": " + it.data.data.divisions.name
                    binding.tvStudentRollNo.text = ": " + it.data.data.roll_number.toString()
                    binding.tvStudentDob.text = ": " + it.data.data.dob
                    binding.tvStudentPhone.text = ": " + it.data.data.phone

                    binding.tvStudentEmail.text = ": " + it.data.data.email
                    binding.tvStudentSecndPhone.text = ": " + it.data.data.phone2
                    binding.tvStudentAddress.text = ": " + it.data.data.address
                    binding.tvStudentPlace.text = ": " + it.data.data.place
                    binding.tvStudentPostCode.text = ": " + it.data.data.post
                    binding.tvStudentBloodGrp.text = ": " + it.data.data.blood
                    binding.tvStudentState.text = ": " + it.data.data.states.name
                    binding.tvStudentCountry.text = ": " + it.data.data.countries.name
                    binding.tvStudentNationality.text = ": " + it.data.data.nationalities.name

                }
                Status.LOADING -> {
                    showProgress()

                    /* load no values */
                    studImgUrl = null
                    parntImgUrl = null
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivStudentProfile)
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivParentPic)

                    binding.sampleLiner.text = "Nill"
                    binding.tvStudentRegNo.text = "Reg.No : "
                    binding.tvStudentParentName.text = "Nill"

                    binding.tvStudentGender.text = ": "
                    binding.tvStudentClass.text = ": "
                    binding.tvStudentDivision.text = ": "
                    binding.tvStudentRollNo.text = ": "
                    binding.tvStudentDob.text = ": "
                    binding.tvStudentPhone.text = ": "

                    binding.tvStudentEmail.text = ": "
                    binding.tvStudentSecndPhone.text = ": "
                    binding.tvStudentAddress.text = ": "
                    binding.tvStudentPlace.text = ": "
                    binding.tvStudentPostCode.text = ": "
                    binding.tvStudentBloodGrp.text = ": "
                    binding.tvStudentState.text = ": "
                    binding.tvStudentCountry.text = ": "
                    binding.tvStudentNationality.text = ": "
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()

                    /* load no values */
                    studImgUrl = null
                    parntImgUrl = null
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivStudentProfile)
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivParentPic)

                    binding.sampleLiner.text = "Nill"
                    binding.tvStudentRegNo.text = "Reg.No : "
                    binding.tvStudentParentName.text = "Nill"

                    binding.tvStudentGender.text = ": "
                    binding.tvStudentClass.text = ": "
                    binding.tvStudentDivision.text = ": "
                    binding.tvStudentRollNo.text = ": "
                    binding.tvStudentDob.text = ": "
                    binding.tvStudentPhone.text = ": "

                    binding.tvStudentEmail.text = ": "
                    binding.tvStudentSecndPhone.text = ": "
                    binding.tvStudentAddress.text = ": "
                    binding.tvStudentPlace.text = ": "
                    binding.tvStudentPostCode.text = ": "
                    binding.tvStudentBloodGrp.text = ": "
                    binding.tvStudentState.text = ": "
                    binding.tvStudentCountry.text = ": "
                    binding.tvStudentNationality.text = ": "
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )

                    /* load no values */
                    studImgUrl = null
                    parntImgUrl = null
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivStudentProfile)
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivParentPic)

                    binding.sampleLiner.text = "Nill"
                    binding.tvStudentRegNo.text = "Reg.No : "
                    binding.tvStudentParentName.text = "Nill"

                    binding.tvStudentGender.text = ": "
                    binding.tvStudentClass.text = ": "
                    binding.tvStudentDivision.text = ": "
                    binding.tvStudentRollNo.text = ": "
                    binding.tvStudentDob.text = ": "
                    binding.tvStudentPhone.text = ": "

                    binding.tvStudentEmail.text = ": "
                    binding.tvStudentSecndPhone.text = ": "
                    binding.tvStudentAddress.text = ": "
                    binding.tvStudentPlace.text = ": "
                    binding.tvStudentPostCode.text = ": "
                    binding.tvStudentBloodGrp.text = ": "
                    binding.tvStudentState.text = ": "
                    binding.tvStudentCountry.text = ": "
                    binding.tvStudentNationality.text = ": "
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )

                    /* load no values */
                    studImgUrl = null
                    parntImgUrl = null
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivStudentProfile)
                    Glide.with(requireActivity()).load(R.drawable.ic_profile_user)
                        .into(binding.ivParentPic)

                    binding.sampleLiner.text = "Nill"
                    binding.tvStudentRegNo.text = "Reg.No : "
                    binding.tvStudentParentName.text = "Nill"

                    binding.tvStudentGender.text = ": "
                    binding.tvStudentClass.text = ": "
                    binding.tvStudentDivision.text = ": "
                    binding.tvStudentRollNo.text = ": "
                    binding.tvStudentDob.text = ": "
                    binding.tvStudentPhone.text = ": "

                    binding.tvStudentEmail.text = ": "
                    binding.tvStudentSecndPhone.text = ": "
                    binding.tvStudentAddress.text = ": "
                    binding.tvStudentPlace.text = ": "
                    binding.tvStudentPostCode.text = ": "
                    binding.tvStudentBloodGrp.text = ": "
                    binding.tvStudentState.text = ": "
                    binding.tvStudentCountry.text = ": "
                    binding.tvStudentNationality.text = ": "
                }

            }
        })

    }

    override fun onClicks() {

        /* name loading parameters */
        /* 0-> Student */
        /* 1-> Parent */
        /* student profile picture */
        binding.ivStudentProfile.setOnClickListener {
            if (studImgUrl != null) {
                val dialog = FullScreenImageDialogFragment(studImgUrl!!, studName!!)
                dialog.show(parentFragmentManager, "TAG")
            }
        }

        /* parent profile picture */
        binding.ivParentPic.setOnClickListener {
            if (parntImgUrl != null) {
                val dialog = FullScreenImageDialogFragment(
                    parntImgUrl!!,
                    parntName!!
                )
                dialog.show(parentFragmentManager, "TAG")
            }
        }

        /* profile button */
        binding.ivStudentProfileUpdateBtn.setOnClickListener {
            setupDialog()
        }

        /* swipe to refresh */
        binding.swipeRefreshLayout.setOnRefreshListener {
            setupObserver()
        }

        binding.ivEditName.setOnClickListener {
            setUpNameEditDialog()
        }
    }

    /* Dialog for edit name */
    @SuppressLint("InflateParams")
    private fun setUpNameEditDialog() {
        /* setup bottom sheet dialog */
        val bottom = BottomSheetDialog(requireContext(), R.style.ThemeOverlay_App_BottomSheetDialog)
        val bottomSheet: View = this.layoutInflater.inflate(R.layout.name_edit_layout, null)

        val etFirstName = bottomSheet.findViewById<EditText>(R.id.etFirstName)
        val etMiddleName = bottomSheet.findViewById<EditText>(R.id.etMiddleName)
        val etLastName = bottomSheet.findViewById<EditText>(R.id.etLastName)
        val btnSave = bottomSheet.findViewById<MaterialButton>(R.id.btnSave)
        val sam1 = bottomSheet.findViewById<TextInputLayout>(R.id.sample1)
        val sam2 = bottomSheet.findViewById<TextInputLayout>(R.id.sample2)
        val sam3 = bottomSheet.findViewById<TextInputLayout>(R.id.sample3)

        btnSave.setOnClickListener {

            val first_name = etFirstName.text.toString()
            val middle_name = etMiddleName.text.toString()
            val last_name = etLastName.text.toString()

            if (first_name.isBlank() || middle_name.isBlank() || last_name.isBlank()) {
                if (first_name.isBlank()) {
                    sam1.error = "First name required"
                }
                if (middle_name.isBlank()) {
                    sam2.error = "Middle name required"
                }
                if (last_name.isBlank()) {
                    sam3.error = "Last name required"
                }
            } else {
                setUpNameEditObserver(first_name, middle_name, last_name)
                bottom.dismiss()
            }
        }


        bottom.setContentView(bottomSheet)
        bottom.show()
    }

    /* name edit api */
    private fun setUpNameEditObserver(firstName: String, middleName: String, lastName: String) {
        studentProfileViewModel = StudentProfileViewModel(requireContext())
        studentProfileViewModel.studentNameEditApi(firstName, middleName, lastName)
        studentProfileViewModel.studentEditNameData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        "Name Updated",
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_SHORT
                    )

                    prefStudentName =
                        it.data!!.data.first_name + it.data.data.middle_name + it.data.data.last_name

                    initData()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }

            }
        })
    }

    /* dialog for image picker */
    @SuppressLint("InflateParams")
    private fun setupDialog() {

        /* setup bottom sheet dialog */
        val bottom = BottomSheetDialog(requireContext(), R.style.ThemeOverlay_App_BottomSheetDialog)
        val bottomSheet: View = this.layoutInflater.inflate(R.layout.select_gallery, null)

        val ivCameraPick = bottomSheet.findViewById<ShapeableImageView>(R.id.ivCameraPick)
        val ivgalleryPick = bottomSheet.findViewById<ShapeableImageView>(R.id.ivgalleryPick)

        /* camera pick action */
        ivCameraPick.setOnClickListener {
            permissionsBuilder(Manifest.permission.CAMERA).build().send { result ->
                if (result.allGranted()) {
                    ImagePicker.with(this)
                        .crop()
                        .cameraOnly()
                        .compress(1024)
                        .maxResultSize(
                            1080,
                            1080
                        )
                        .start(PICK_CAMERA_IMAGES_CODE)
                    bottom.dismiss()
                }
            }
        }

        /* gallery pick action */
        ivgalleryPick.setOnClickListener {
            permissionsBuilder(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).build().send { result ->
                if (result.allGranted()) {
                    ImagePicker.with(this)
                        .crop()
                        .galleryOnly()
                        .compress(1024)
                        .maxResultSize(
                            1080,
                            1080
                        )
                        .start(PICK_GALLERY_IMAGES_CODE)
                    bottom.dismiss()
                }
            }
        }

        bottom.setContentView(bottomSheet)
        bottom.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_CAMERA_IMAGES_CODE) {
                val fileUri = data?.data

                //for image file
                imageFile = null
                imageFile = ImagePicker.getFile(data)!!

                /* Api call */
                setUpImageEditObserver(imageFile!!)
            }


            if (requestCode == PICK_GALLERY_IMAGES_CODE) {
                val fileUri = data?.data

                //for image file
                imageFile = null
                imageFile = ImagePicker.getFile(data)!!

                /* Api call */
                setUpImageEditObserver(imageFile!!)

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    /* profile image api */
    private fun setUpImageEditObserver(imageFile: File) {
        studentProfileViewModel = StudentProfileViewModel(requireContext())
        studentProfileViewModel.studentProfileImageEditApi(imageFile)
        studentProfileViewModel.studentEditImageData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        "Photo Updated",
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_SHORT
                    )

                    /*prefStudentImg = it.data.data.profile_image*/
                    initData()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    Toaster.showToast(
                        requireContext(),
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }

            }
        })
    }

    /* for option menu */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /* show and hide option menu */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val item1: MenuItem = menu.findItem(R.id.customtoolbar_search)
        item1.isVisible = false

        val item2: MenuItem = menu.findItem(R.id.customtoolbar_chat)
        item2.isVisible = true

    }

    /* option menu  */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.customtoolbar_search -> {

            true
        }
        R.id.customtoolbar_chat -> {
            val intent = Intent(requireContext(), StudentChatActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

}