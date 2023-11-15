package com.iroid.healthdomain.ui.home.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
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
import com.iroid.healthdomain.data.model_class.user_profile.Contact
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.databinding.ProfileFragmentBinding
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.home.profile.edit_profile.EditProfileSheet
import com.iroid.healthdomain.ui.home.profile.view_pager.FollowerViewPagerFragment
import com.iroid.healthdomain.ui.utils.handleApiError
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.math.nextUp


class ProfileFragment : BaseFragment<ProfileViewModel, ProfileFragmentBinding, ProfileRepository>(),
    /*PhotoPickerFragment.Callback,*/ EditProfileSheet.onClickListener {

    private var imagesPassed: String = ""
    private val PICK_CAMERA_IMAGES_CODE = 100
    private val PICK_GALLERY_IMAGES_CODE = 101
    private var imageFile: File? = null

    companion object {
        fun newInstance() = ProfileFragment()
        private lateinit var imageUri: Uri
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ProfileFragmentBinding {
        return ProfileFragmentBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<ProfileViewModel> {
        return ProfileViewModel::class.java
    }

    override fun getFragmentRepository(): ProfileRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ApiServices::class.java, token)
        return ProfileRepository(api, userPreferences)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* ChiliPhotoPicker.init(
             loader = CoilImageLoader(),
             authority = "com.iroid.healthdomain.fileprovider"
         )*/
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserProfile()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.userInfo.materialTextView3.visibility = View.GONE
        binding.userInfo.materialTextView6.visibility = View.GONE

        binding.editProfileIcon.setOnClickListener {
            val editProfileSheet: EditProfileSheet = EditProfileSheet(this)
            editProfileSheet.show(
                requireActivity().supportFragmentManager,
                EditProfileSheet.TAG
            )
        }


        viewModel.getUserProfile()
        addObserver()
        userPreferences.hdStatus.asLiveData().observe(requireActivity(), Observer {
            binding.userInfo.materialTextView5.text=it

        })

        //   launchFollowersFragment()

        binding.cameraFab.setOnClickListener {
            setupImagePickerDialog()
            /* PhotoPickerFragment.newInstance(
                 multiple = false,
                 maxSelection = 1,
                 allowCamera = true,
                 theme = R.style.ChiliPhotoPicker_Light
             ).show(childFragmentManager, "picker")*/
        }
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

    private fun addObserver() {
        viewModel.userApiResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    viewModel.setLoading(true)

                }
                is Resource.Success -> {


                    if (it.value.status == 200) {
                        viewModel.setLoading(false)

                        binding.userModel = it.value.data

                        GlobalScope.launch {
                            it.value.data.id?.let { it1 -> userPreferences.saveUserId(it1) }
                        }

                        //   it.value.data.profile_image_url?.let { it1 -> setData(it1) }
                        EditProfileSheet.userData = it.value.data
                        if (!it.value.data.height.isNullOrEmpty() && !it.value.data.weight.isNullOrEmpty()) {
                            binding.heightValue.text=
                                ((it.value.data.height.toDouble()/2.54)/12).toString()+" Ft"

                            setBmi(it.value.data.height, it.value.data.weight)
                        }

                        launchFollowersFollowing(it.value.data.contacts)
                    }

                }
                is Resource.Failure -> {
                    viewModel.setLoading(false)

                    handleApiError(requireView(), it)
                }
            }
        })

        viewModel.profileImgResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    viewModel.setLoading(true)

                }
                is Resource.Success -> {

                    if (it.value.status == 200) {
                        viewModel.setLoading(false)
                        binding.profileImage.load(imageUri) {
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

        viewModel.getUpdateUserResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    viewModel.setLoading(true)

                }
                is Resource.Success -> {

                    if (it.value.status == 200) {
                        viewModel.setLoading(false)

                        viewModel.getUserProfile()
                    }

                }
                is Resource.Failure -> {
                    viewModel.setLoading(false)

                    handleApiError(requireView(), it)
                }
            }
        }
    }

    private fun launchFollowersFollowing(contacts: List<Contact>) {

        requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.followersFragment,
            FollowerViewPagerFragment.newInstance(contacts),
            FollowerViewPagerFragment::class.java.toString()
        ).commit()

    }

    private fun setBmi(height: String?, weight: String?) {
        val value: Double = height!!.toDouble().nextUp()
        val meters = value / 3.2808
        val h = height!!.toDouble() / 100
        val w = weight!!.toDouble()
        val bmi = w/(meters * meters)

        Log.i("TAG321", "setBmi: $h" )
        binding.bmiValue.text = String.format("%.1f", bmi)
    }

    private fun setData(url: String) {
        binding.profileImage.load(url) {
            transformations(CircleCropTransformation())
        }

    }


    /* private fun launchFollowersFragment() {
         requireActivity().supportFragmentManager.beginTransaction().replace(
                 R.id.followersFragment,
                 FollowerViewPagerFragment(), FollowerViewPagerFragment::class.java.toString()
         ).commit()
     }*/

    /*override fun onImagesPicked(photos: ArrayList<Uri>) {

        Log.i("TAG456", "onImagesPicked: $photos")

        val image: File = File(photos[0].path)
        val requestBody = image.asRequestBody("image/jpg".toMediaTypeOrNull())
        val multiPart: MultipartBody.Part = createFormData("profile_image", image.name, requestBody)
        viewModel.uploadImage(multiPart)

        imageUri = photos[0]


    }
*/


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
        val multiPart: MultipartBody.Part = createFormData("profile_image", image.name, requestBody)
        imageUri = image.toUri()
        viewModel.uploadImage(multiPart)
    }


    override fun onUpdate(accountModel: AccountModel) {
        viewModel.updateUser(accountModel)
    }


}



