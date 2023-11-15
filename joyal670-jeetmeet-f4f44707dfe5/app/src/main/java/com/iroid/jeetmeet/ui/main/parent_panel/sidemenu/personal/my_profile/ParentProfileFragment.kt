package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.personal.my_profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseFragment
import com.iroid.jeetmeet.databinding.FragmentParentProfileBinding
import com.iroid.jeetmeet.dialogs.FullScreenImageDialogFragment
import com.iroid.jeetmeet.listeners.FragmentTransInterface
import com.iroid.jeetmeet.modal.parent.profile_edit.ParentProfileEditResponse
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.activity.ParentSideMenuActivity
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel.ParentProfileEditViewModel
import com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel.ParentProfileViewModel
import com.iroid.jeetmeet.utils.Status
import com.iroid.jeetmeet.utils.Toaster
import com.skydoves.powerspinner.PowerSpinnerView


class ParentProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentParentProfileBinding
    private lateinit var fragmentTransInterface: FragmentTransInterface
    private lateinit var parentProfileViewModel: ParentProfileViewModel
    private lateinit var parentProfileEditViewModel: ParentProfileEditViewModel
    var parentImgUrl: String? = null
    var parentName: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ParentProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentParentProfileBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    override fun initData() {
        /* setup toolbar  */
        fragmentTransInterface = activity as ParentSideMenuActivity
        fragmentTransInterface.setTitleFromFragment(getString(R.string.nill))
        fragmentTransInterface.setTitleinCenter(true)
        fragmentTransInterface.setFontFamily(R.font.barlow_extrabold)
        fragmentTransInterface.setTitleCaptial(true)
    }

    override fun setupUI() {

        setupObserver()
    }

    override fun setupViewModel() {

    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {

        binding.swipeRefreshLayout.setRefreshing(false)

        /* parent profile api */
        parentProfileViewModel = ParentProfileViewModel()
        parentProfileViewModel.parentProfile()
        parentProfileViewModel.parentProfileData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    dismissProgress()

                    Glide.with(requireContext()).load(it.data!!.data.image_url)
                        .into(binding.ivProfilePic)

                    binding.tvProfileName.text =
                        it.data.data.first_name + " " + it.data.data.last_name
                    binding.tvEmail.text = ": " + it.data.data.email
                    binding.tvSecondaryPhone.text = ": " + it.data.data.phone
                    binding.tvAddress.text = ": " + it.data.data.address
                    binding.tvPlace.text = ": " + it.data.data.place
                    binding.tvPostCode.text = ": " + it.data.data.zip
                    binding.tvState.text = ": " + it.data.data.states.name
                    binding.tvCountry.text = ": " + it.data.data.countries.name
                    binding.tvNationality.text = ": " + it.data.data.nationalities.name

                    parentImgUrl = it.data.data.image_url
                    parentName = it.data.data.first_name + " " + it.data.data.last_name

                }
                Status.LOADING -> {
                    showProgress()
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
                Status.DATA_EMPTY -> {
                    dismissProgress()
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

    override fun onClicks() {

        binding.editProfileButton.setOnClickListener {
            showEditDialogueHere()
        }

        /* view profile pic */
        binding.ivProfilePic.setOnClickListener {
            if (parentImgUrl != null) {
                val dialog = FullScreenImageDialogFragment(parentImgUrl!!, parentName!!)
                dialog.show(parentFragmentManager, "TAG")
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupObserver()
        }
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


    private fun showEditDialogueHere() {

        /* profile edit api */
        parentProfileEditViewModel = ParentProfileEditViewModel()
        parentProfileEditViewModel.parentProfileEdit()
        parentProfileEditViewModel.parentProfileEditData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    setUpEditDialog(it.data)

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

    private fun setUpEditDialog(data: ParentProfileEditResponse?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.fragment_parent_edit_profile, null)
        builder.setView(dialogView)
        val diag: AlertDialog = builder.create()
        diag.show()
        diag.setCancelable(false)
        diag.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val countryNameList = ArrayList<String>()
        val countryIdList = ArrayList<Int>()
        val countryPhoneCodeList = ArrayList<Int>()

        val stateNameList = ArrayList<String>()
        val stateIdList = ArrayList<Int>()

        val nationalityNameList = ArrayList<String>()
        val nationalityIdList = ArrayList<Int>()

        var selectedId = 0
        var selectedName: String = ""

        val tvEmail = dialogView.findViewById<EditText>(R.id.tvParentEmail)
        val tvPhone = dialogView.findViewById<EditText>(R.id.tvParentPhoneNumber)
        val tvAddress = dialogView.findViewById<EditText>(R.id.tvParentAddress)
        val tvPlace = dialogView.findViewById<EditText>(R.id.tvParentPlace)
        val tvPincode = dialogView.findViewById<EditText>(R.id.tvParentPinCode)

        val stateInputSpinner = dialogView.findViewById<PowerSpinnerView>(R.id.stateInputSpinner)
        val countryInputSpinner =
            dialogView.findViewById<PowerSpinnerView>(R.id.countryInputSpinner)
        val nationalityInputSpinner =
            dialogView.findViewById<PowerSpinnerView>(R.id.nationalityInputSpinner)

        val closeButton = dialogView.findViewById<ImageView>(R.id.closeImageView)
        val submitBtn = dialogView.findViewById<MaterialButton>(R.id.submitBtn)

        tvEmail.setText(data!!.parent.email)
        tvPhone.setText(data.parent.phone.toString())
        tvAddress.setText(data.parent.address)
        tvPlace.setText(data.parent.place)
        tvPincode.setText(data.parent.zip)


        countryNameList.clear()
        countryIdList.clear()
        nationalityNameList.clear()
        nationalityIdList.clear()
        countryPhoneCodeList.clear()
        data.countries.forEach {
            countryNameList.addAll(listOf(it.name))
            countryIdList.addAll(listOf(it.id))
            countryPhoneCodeList.addAll(listOf(it.phonecode))
            nationalityNameList.addAll(listOf(it.name))
            nationalityIdList.addAll(listOf(it.id))
        }
        countryInputSpinner.clearSelectedItem()
        countryInputSpinner.setItems(countryNameList)
        nationalityInputSpinner.clearSelectedItem()
        nationalityInputSpinner.setItems(nationalityNameList)


        stateNameList.clear()
        stateIdList.clear()
        data.states.forEach {
            stateNameList.addAll(listOf(it.name))
            stateIdList.addAll(listOf(it.id))
        }
        stateInputSpinner.clearSelectedItem()
        stateInputSpinner.setItems(stateNameList)


        val tempCountryId = data.parent.country
        val tempStateId = data.parent.state
        val tempNationalityId = data.parent.nationality.toInt()

        countryIdList.forEach {
            if (it == tempCountryId) {
                Log.e("TAG", "showEditDialogueHere: " + countryNameList[it - 1])
                countryInputSpinner.selectItemByIndex(it - 1)
            }
        }

        countryIdList.forEach {
            if (it == tempNationalityId) {
                Log.e("TAG", "showEditDialogueHere: " + nationalityNameList[it - 1])
                nationalityInputSpinner.selectItemByIndex(it - 1)
            }
        }

        stateInputSpinner.text = data.parent.states.name
        /*stateIdList.forEach {
            if(it == tempStateId)
            {
                Log.e("TAG", "showEditDialogueHere: " + stateNameList[it-1] )
                stateInputSpinner.selectItemByIndex(it-1)
                Log.e("TAG", "setUpEditDialog: $it------$tempStateId")
            }
        }*/


        var selectedCountryId = data.parent.country
        var selectedCountryName = ""
        var selectedPhoneCode = data.parent.std_code
        countryInputSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            selectedCountryId = data.parent.country
            selectedCountryId = countryIdList[newIndex]
            selectedCountryName = newItem
            selectedPhoneCode = countryPhoneCodeList[newIndex]

            Log.e(
                "TAG",
                "setUpEditDialog: $selectedCountryId----$selectedCountryName----$selectedPhoneCode"
            )

            parentProfileEditViewModel = ParentProfileEditViewModel()
            parentProfileEditViewModel.parentProfileState(selectedCountryId)
            parentProfileEditViewModel.parentProfileStateData.observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgress()

                        stateNameList.clear()
                        stateIdList.clear()

                        it.data!!.state_list.forEach {
                            stateNameList.addAll(listOf(it.name))
                            stateIdList.addAll(listOf(it.id))
                        }
                        stateInputSpinner.clearSelectedItem()
                        stateInputSpinner.setItems(stateNameList)
                        stateInputSpinner.selectItemByIndex(0)

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

        var selectedStateId = data.parent.state
        var selectedStateName = ""
        stateInputSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            selectedStateId = data.parent.state
            selectedStateId = stateIdList[newIndex]
            selectedStateName = newItem

            Log.e("TAG", "setUpEditDialog: $selectedStateId-----$selectedStateName")
        }

        var selectedNationalityId = data.parent.nationality.toInt()
        var selectedNationalityName = ""
        nationalityInputSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            selectedNationalityId = data.parent.nationality.toInt()
            selectedNationalityId = countryIdList[newIndex]
            selectedNationalityName = newItem

            Log.e("TAG", "setUpEditDialog: $selectedNationalityId-----$selectedNationalityName")
        }

        closeButton.setOnClickListener {
            diag.dismiss()
        }

        submitBtn.setOnClickListener {
            val tempEmail = tvEmail.text.toString()
            val tempPhone = tvPhone.text.toString()
            val tempAddress = tvAddress.text.toString()
            val tempPlace = tvPlace.text.toString()
            val tempPostCode = tvPincode.text.toString()
            val tempCountryId = selectedCountryId
            val tempStateId = selectedStateId
            val tempNationalityId = selectedNationalityId
            val tempPhoneCodeId = selectedPhoneCode

            if (tempEmail.isBlank() || tempPhone.isBlank() || tempAddress.isBlank() || tempPlace.isBlank() || tempPostCode.isBlank() || tempCountryId == 0 || tempStateId == 0 || tempNationalityId == 0 || tempPhoneCodeId == 0) {
                Toaster.showToast(
                    requireContext(),
                    "All fields are required",
                    Toaster.State.WARNING,
                    Toast.LENGTH_LONG
                )
            } else {
                Log.e(
                    "TAG",
                    "setUpEditDialog: $tempEmail----$tempPhone----$tempAddress----$tempPlace-----$tempPostCode----$tempCountryId----$tempStateId----$tempNationalityId----$tempPhoneCodeId"
                )

                setUpProfileUpdateObserver(
                    tempEmail,
                    tempPhone,
                    tempAddress,
                    tempPlace,
                    tempPostCode,
                    tempCountryId,
                    tempStateId,
                    tempNationalityId,
                    tempPhoneCodeId
                )

                diag.dismiss()
            }
        }

    }

    /* profile update api */
    private fun setUpProfileUpdateObserver(
        tempEmail: String,
        tempPhone: String,
        tempAddress: String,
        tempPlace: String,
        tempPostCode: String,
        tempCountryId: Int,
        tempStateId: Int,
        tempNationalityId: Int,
        tempPhoneCodeId: Int
    ) {
        parentProfileEditViewModel = ParentProfileEditViewModel()
        parentProfileEditViewModel.parentProfileUpdate(
            tempEmail,
            tempPhoneCodeId,
            tempPhone.toInt(),
            tempAddress,
            tempPlace,
            tempPostCode.toInt(),
            tempStateId,
            tempCountryId,
            tempNationalityId
        )
        parentProfileEditViewModel.parentProfileUpdateData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    Toaster.showToast(
                        requireContext(),
                        it.data!!.message,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )

                    setupObserver()
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

}