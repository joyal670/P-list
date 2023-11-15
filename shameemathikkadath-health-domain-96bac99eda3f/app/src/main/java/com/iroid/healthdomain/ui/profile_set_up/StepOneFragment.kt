package com.iroid.healthdomain.ui.profile_set_up

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.NavHostFragment
import coil.load
import coil.transform.CircleCropTransformation
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.model_class.AccountModel
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.databinding.FragmentStepOneBinding
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.home.HomeActivity
import com.iroid.healthdomain.ui.utils.handleApiError
import com.iroid.healthdomain.ui.utils.showSnackBar
import com.iroid.healthdomain.ui.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class StepOneFragment :
    BaseFragment<ProfileSetUpViewModel, FragmentStepOneBinding, ProfileSetUpRepository>()/*,
        PhotoPickerFragment.Callback*/ {

    private var imagesPassed: String = ""
    private val PICK_CAMERA_IMAGES_CODE = 100
    private val PICK_GALLERY_IMAGES_CODE = 101
    private var imageFile: File? = null

    private var isChecking = true
    private var mCheckedId = 0
    private var mCheckedValue: String = "Male"

    companion object {
        private lateinit var imageUri: Uri
        private const val TAG = "StepOneFragment"
        lateinit var accountModel: AccountModel
    }


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStepOneBinding {
        return FragmentStepOneBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<ProfileSetUpViewModel> = ProfileSetUpViewModel::class.java

    override fun getFragmentRepository(): ProfileSetUpRepository {

        val token = runBlocking { userPreferences.authToken.first().toString() }
        val api = remoteDataSource.buildApi(api = ApiServices::class.java, authToken = token)
        return ProfileSetUpRepository(api)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* ChiliPhotoPicker.init(
                 loader = CoilImageLoader(),
                 authority = "com.iroid.healthdomain.fileprovider")*/
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userPreferences.userStatus.asLiveData().observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "onActivityCreated: ${it}")
//            if (it == true) binding.tvSkip.visible(true)
//            else binding.tvSkip.visible(false)
        })

        binding.btNext.setOnClickListener {
            checkValidation(
                binding.tieName.text.toString(),
                binding.tieAge.text.toString()
            )
        }
        binding.tvSkip.setOnClickListener {
            val intent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.genderGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1 && isChecking) {
                isChecking = false
                mCheckedId = checkedId
                setCheckedValue(mCheckedId)
            }
            isChecking = true
        }


        binding.ivLogo.setOnClickListener {
            setupImagePickerDialog()
            /*   PhotoPickerFragment.newInstance(
                       multiple = true,
                       maxSelection = 10,
                       allowCamera = true,
                       theme = R.style.ChiliPhotoPicker_Light
               ).show(childFragmentManager, "picker")*/
        }


        viewModel.profileImgResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    viewModel.setLoading(true)

                }
                is Resource.Success -> {

                    if (it.value.status == 200) {
                        viewModel.setLoading(false)
                        binding.ivLogo.load(imageUri) {
                            transformations(CircleCropTransformation())
                        }
                    }

                }
                is Resource.Failure -> {
                    viewModel.setLoading(false)
                    handleApiError(requireView(), it)
                }
            }
        })
    }

    private fun setupImagePickerDialog() {

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

    private fun checkValidation(name: String, age: String) {

        if (age.isNullOrEmpty() || name.isNullOrEmpty() || mCheckedValue.isNullOrEmpty()) requireView().showSnackBar(
            "Fields are empty..!"
        )
        else {
            accountModel = AccountModel(
                name = name,
                age = age,
                gender = mCheckedValue
            )

            val action =
                StepOneFragmentDirections.actionStepOneFragmentToStepTwoFragment(accountModel)
            NavHostFragment.findNavController(this@StepOneFragment)
                .navigate(action)

        }

    }

    private fun setCheckedValue(mCheckedId: Int) {
        val radioButton = binding.root.findViewById<View>(mCheckedId) as RadioButton
        mCheckedValue = radioButton.text.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_CAMERA_IMAGES_CODE) {
                val fileUri = data?.data

                //for image file
                imageFile = null
                imageFile = ImagePicker.getFile(data)!!
                imagesPassed = fileUri?.path.toString()

                /* Api call */
                loadSelectedImage(imagesPassed)
                //setUpImageEditObserver(imageFile!!)
                imageFile = null
            }


            if (requestCode == PICK_GALLERY_IMAGES_CODE) {
                val fileUri = data?.data

                //for image file
                imageFile = null
                imageFile = ImagePicker.getFile(data)!!
                imagesPassed = fileUri?.path.toString()


                /* Api call */
                //setUpImageEditObserver(imageFile!!)
                loadSelectedImage(imagesPassed)
                imageFile = null

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun loadSelectedImage(imagesPassed: String) {
        val image: File = File(imagesPassed)
        val requestBody = image.asRequestBody("image/jpg".toMediaTypeOrNull())
        val multiPart: MultipartBody.Part =
            MultipartBody.Part.createFormData("profile_image", image.name, requestBody)
        imageUri = image.toUri()
        viewModel.uploadImage(multiPart)
    }

    /*  override fun onImagesPicked(photos: ArrayList<Uri>) {
          val image: File = File(photos[0].path)
          val requestBody = image.asRequestBody("image/jpg".toMediaTypeOrNull())
          val multiPart: MultipartBody.Part = MultipartBody.Part.createFormData("profile_image", image.name, requestBody)
          viewModel.uploadImage(multiPart)

          imageUri = photos[0]

      }*/
}