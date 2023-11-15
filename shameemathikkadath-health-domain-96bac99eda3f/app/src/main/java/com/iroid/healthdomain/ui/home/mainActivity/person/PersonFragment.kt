package com.iroid.healthdomain.ui.home.mainActivity.person

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.asLiveData
import androidx.navigation.NavArgument
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import com.example.awesomedialog.*
import com.google.gson.Gson
import com.iroid.healthdomain.R
import com.iroid.healthdomain.data.dummyModel.FollowerModel
import com.iroid.healthdomain.data.dummyModel.HashedModel
import com.iroid.healthdomain.data.model_class.contacts_api.ContactData
import com.iroid.healthdomain.data.model_class.user_challenge.DataX
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.databinding.PersonFragmentBinding
import com.iroid.healthdomain.ui.adaptor.UserPastChallengesAdapter
import com.iroid.healthdomain.ui.base.BaseFragment
import com.iroid.healthdomain.ui.home.mainActivity.HomeFragmentDirections
import com.iroid.healthdomain.ui.home.mainActivity.HomeRepository
import com.iroid.healthdomain.ui.preference.AppPreferences
import com.iroid.healthdomain.ui.preference.ConstantPreference
import com.iroid.healthdomain.ui.utils.CommonUtils
import com.iroid.healthdomain.ui.utils.handleApiError
import com.iroid.healthdomain.worker.MyWorkManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.ArrayList
import java.util.concurrent.TimeUnit


class PersonFragment : BaseFragment<PersonViewModel,PersonFragmentBinding, HomeRepository>() {

    val args: PersonFragmentArgs by navArgs()

    var currentUserId: Int = 0
    var currentFavStatus: Int = 0
    private var loded=false

    private val adapter: UserPastChallengesAdapter by lazy { UserPastChallengesAdapter(currentUserId) }

    companion object {
        fun newInstance() = PersonFragment()
        private const val TAG = "PersonFragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.model = args.passValue
        Log.e(TAG, "onActivityCreated: ", )


        var followersList: ArrayList<String> = AppPreferences.getArray(ConstantPreference.NEW_FOLLOWER)!!
        if(followersList.contains(args.passValue!!.id.toString())){
            binding.imgFav.setBackgroundResource(R.drawable.ic_heart_filled)
            currentFavStatus=0
        }else{
            binding.imgFav.setBackgroundResource(R.drawable.ic_heart_outlined)
            followersList.add(args.passValue!!.id.toString())
            currentFavStatus=1
        }

        viewModel.getUserChallenge(args.passValue!!.id.toString(),"1")

        //creating a data object
        //to pass the data with workRequest
        //we can put as many variables needed
        //creating a data object
        //to pass the data with workRequest
        //we can put as many variables needed

        val data: Data = Data.Builder()
            .putString(MyWorkManager.TASK_DESC, "The task data passed from PersonFragment")
            .build()

        val constraints: Constraints = Constraints.Builder()
            .setRequiresCharging(true) // you can add as many constraints as you want
            .build()

        val workRequest = OneTimeWorkRequest.Builder(MyWorkManager::class.java)
            .setInputData(data)
            .setConstraints(constraints)
            .build()


        val periodicWorkRequest = PeriodicWorkRequestBuilder<MyWorkManager>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()


//        val workManager = WorkManager.getInstance(requireContext())
//        workManager.enqueueUniqueWork(periodicWorkRequest)

        //  viewModel = ViewModelProvider(this).get(PersonViewModel::class.java)

        binding.inviteButton.setOnClickListener {
            // WorkManager.getInstance(requireContext()).enqueue(workRequest)
            viewModel.sentChallengeRequest(args.passValue!!.id.toString())

        }

        binding.imgFav.setOnClickListener {
            viewModel.sentFavRequest(args.passValue!!.id.toString(), currentFavStatus.toString())
        }

        /* WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(workRequest.id)
             .observe(viewLifecycleOwner, Observer {

                 if (it != null && it.state.isFinished) {

                     Toast.makeText(requireContext(), "Here", Toast.LENGTH_SHORT).show()
                     binding.materialTextView14.text =
                         "${it.outputData.getString(MyWorkManager.TASK_DESC)} \n ${it.state.name}"

                 }

             })*/

        addObserver()
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }

    private fun addObserver() {
        viewModel.createChallengeApiResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                    dismissLoader()
                    handleApiError(requireView(), it)
                }
                is Resource.Loading -> {
                    showLoader()
                }
                is Resource.Success -> {
                    AppPreferences.isLoaded=true
                    dismissLoader()
                    if (it.value.status == 200) {
                        successDialog()
                    }
                }
            }
        }

        viewModel.favResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                    dismissLoader()
                    handleApiError(requireView(), it)
                }
                is Resource.Loading -> {
                    showLoader()
                }
                is Resource.Success -> {
                    dismissLoader()
                    if (it.value.status == 200) {
                        var followersList: ArrayList<String> = AppPreferences.getArray(ConstantPreference.NEW_FOLLOWER)!!
                        Log.e("#969696", "addObserver: ${followersList}" )
                        Log.e("#56989866","${args.passValue!!.id.toString()}")
                        if(followersList.contains(args.passValue!!.id.toString())){
                            followersList.remove(args.passValue!!.id.toString())
                        }else{
                            followersList.add(args.passValue!!.id.toString())
                        }
                        AppPreferences.setArray(ConstantPreference.NEW_FOLLOWER,followersList)
                        currentFavStatus = if (currentFavStatus == 1) {
                            binding.imgFav.setBackgroundResource(R.drawable.ic_heart_filled)
                            0
                        } else {
                            binding.imgFav.setBackgroundResource(R.drawable.ic_heart_outlined)
                            1
                        }

                    }
                }
            }
        }

        viewModel.userChallengeResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Failure -> {
                    AppPreferences.isLoaded=true
                    dismissLoader()
                    handleApiError(requireView(), it)
                }
                is Resource.Loading -> {
                    showLoader()
                }
                is Resource.Success -> {
                    dismissLoader()
                    if (it.value.status == 200) {
                        loadPastChallenges(it.value.data.past.data)
                    }

                }
            }
        })
    }

    private fun loadPastChallenges(data: List<DataX>) {
        if (data.size >= 0) {
            binding.rvPastChallenges.isVisible = true
            binding.tvNoData.isVisible = false
        } else {
            binding.rvPastChallenges.isVisible = false
            binding.tvNoData.isVisible = true
        }
        binding.rvPastChallenges.adapter = adapter
        adapter.list = data
    }


    private fun successDialog() {
        AwesomeDialog.build(requireActivity())
            .title("Congratulations..!")
            .body("Challenge request send successfully.")
            .icon(R.drawable.ic_congrts)
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onNegative("Continue", buttonBackgroundColor = R.drawable.drawable_button) {
                Log.d("TAG", "negative ")
            }

    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): PersonFragmentBinding {
        return PersonFragmentBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): Class<PersonViewModel> {
        return PersonViewModel::class.java
    }

    override fun getFragmentRepository(): HomeRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        GlobalScope.launch {
            currentUserId = runBlocking { userPreferences.userId.first()!! }
        }
        println("Token : ${token}")
        val api = remoteDataSource.buildApi(ApiServices::class.java, authToken = token)
        return HomeRepository(api, userPreferences)
    }
}
