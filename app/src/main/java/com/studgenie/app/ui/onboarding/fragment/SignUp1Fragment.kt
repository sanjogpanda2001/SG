package com.studgenie.app.ui.onboarding.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.studgenie.app.R
import com.studgenie.app.data.local.userStatusDatabase.UserStatusViewModel
import com.studgenie.app.data.remote.request.ApiCountryService
import com.studgenie.app.util.InternetConnectivity
import com.studgenie.app.data.model.CountryItem
import com.studgenie.app.ui.main.activity.HomeActivity
import com.studgenie.app.ui.onboarding.adapter.CountrySpinnerAdapter
import com.studgenie.app.util.Config
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.Observer
import com.studgenie.app.data.local.userStatusDatabase.UserStatusModel

@Suppress("DEPRECATION")
class SignUp1Fragment : Fragment() {

    private lateinit var statusViewModel: UserStatusViewModel
    var isStatusEmpty = 1


    lateinit var phoneNumberEditText: EditText
    lateinit var continueButton: Button
    lateinit var countryCode: String
    lateinit var toastMessage: TextView
    lateinit var exploreTextView: TextView
    var phoneNumberLength: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statusViewModel = ViewModelProvider(requireActivity()).get(UserStatusViewModel::class.java)

        val rootView: View = inflater.inflate(R.layout.fragment_sign_up_1, container, false)
        phoneNumberEditText = rootView.findViewById(R.id.edit_text_phone)
        continueButton = rootView.findViewById(R.id.textView_continue)
        toastMessage = rootView.findViewById(R.id.toast_message_1st_signup_fragment)
        exploreTextView = rootView.findViewById<TextView>(R.id.textView_explore)

        val spinner = rootView.findViewById<Spinner>(R.id.spinner_countries)

        statusViewModel.readAllData?.observe(viewLifecycleOwner, Observer { status ->
            if (status.isEmpty()) {
                isStatusEmpty = 1
                Log.d("Coroutine2", "List is empty")
            } else {
                isStatusEmpty = 0
                Log.d("Coroutine2", status[0].id.toString() + status[0].status)
            }
        })

        if (InternetConnectivity.isConnected(requireContext()) && InternetConnectivity.isConnectedFast(
                requireContext()
            )
        ) {
            val retrofit = Retrofit.Builder()
//            .baseUrl("http://192.168.43.217:3000")
//            .baseUrl("http://sg-backend-dev.ap-south-1.elasticbeanstalk.com")
                .baseUrl(Config.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(ApiCountryService::class.java)
            api.fetchAllCountries().enqueue(object : Callback<List<CountryItem>> {
                override fun onResponse(call: Call<List<CountryItem>>, response: Response<List<CountryItem>>) {
                    if (response.isSuccessful){
                        phoneNumberLength = response.body()!![0].no_of_digits
                        countryCode = response.body()!![0].code

                        val adapter = CountrySpinnerAdapter(
                            requireContext(),
                            response.body() as ArrayList<CountryItem>
                        )
                        spinner.adapter = adapter
                        Log.d("RetrofitCountryService", "OnResponse: ${response.body()!![0].code}")


                        continueButton.setOnClickListener(View.OnClickListener {
                            if (InternetConnectivity.isConnected(requireContext()) && InternetConnectivity.isConnectedFast(requireContext())) {
                                val storePhoneNo = phoneNumberEditText.text.toString().trim()
                                if (storePhoneNo.matches("".toRegex())) {
                                    toastMessage.visibility = View.VISIBLE
                                    toastMessage.text = "Enter your number first"
                                    toastMessage.setBackgroundResource(R.color.transparent_red)
                                } else {
                                    if (InternetConnectivity.isConnected(requireContext()) && InternetConnectivity.isConnectedFast(requireContext())) {
                                        if (storePhoneNo.length == phoneNumberLength) {

                                            val status = UserStatusModel("User Entered")
                                            if (isStatusEmpty == 1) {
                                                statusViewModel.addUserStatus(status)
                                                Log.d("Coroutine2", "Successfully added!")
                                            } else {
                                                statusViewModel.updateUserStatus("second time", 1)
                                                Log.d("Coroutine2", "Successfully updated!")
                                            }

                                            val signUp2Fragment = SignUp2Fragment()
                                            val args = Bundle()
                                            args.putString("phNo", phoneNumberEditText.text.toString())
                                            signUp2Fragment.arguments = args
                                            fragmentManager!!.beginTransaction()
                                                .replace(R.id.signup_fragment_container, signUp2Fragment).commit()
                                        } else {
                                            toastMessage.visibility = View.VISIBLE
                                            toastMessage.text =
                                                "Mobile number should be of $phoneNumberLength digits"
                                            toastMessage.setBackgroundResource(R.color.transparent_red)
                                        }
                                    } else {
                                        toastMessage.visibility = View.VISIBLE
                                        toastMessage.text = "Check Your Internet Connection"
                                        toastMessage.setBackgroundResource(R.color.transparent_red)
                                    }
                                }
                            } else {
                                toastMessage.visibility = View.VISIBLE
                                toastMessage.text = "Check Your Internet Connection"
                                toastMessage.setBackgroundResource(R.color.transparent_red)
                            }
                        })
                    }else{
                        toastMessage.visibility = View.VISIBLE
                        toastMessage.text = "Server Error"
                        toastMessage.setBackgroundResource(R.color.transparent_red)
                    }
                }
                override fun onFailure(call: Call<List<CountryItem>>, t: Throwable) {
                    Log.d("RetrofitCountryService", "OnFailure")
                    toastMessage.visibility = View.VISIBLE
                    toastMessage.text = "Try after some time "
                    toastMessage.setBackgroundResource(R.color.transparent_red)
                }
            })
        } else {
            toastMessage.visibility = View.VISIBLE
            toastMessage.text = "Check Your Internet Connection"
            toastMessage.setBackgroundResource(R.color.transparent_red)
        }
        exploreTextView.setOnClickListener {
            val status = UserStatusModel("User Entered")
            if (isStatusEmpty == 1) {
                statusViewModel.addUserStatus(status)
                Log.d("Coroutine2", "Successfully added!")
            } else {
                statusViewModel.updateUserStatus("second time", 1)
                Log.d("Coroutine2", "Successfully updated!")
            }

            val i = Intent(activity, HomeActivity::class.java)
            startActivity(i)
            (activity as Activity?)!!.overridePendingTransition(0, 0)
            activity?.finish()
        }
        return rootView
    }

//    private fun showData(countries: List<CountryItem>?) {
//        recyclerView_spinner.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = CountryAdapter(countries!!)
//        }
//    }

//    private fun createUserModelList(countries: List<CountryItem>?): ArrayList<CountryItem> {
//        val list = ArrayList<CountryItem>()
//
//        list.add(CountryItem("+91", R.drawable.flag_india))
//        list.add(CountryItem("+1", R.drawable.flag_usa))
//        list.add(CountryItem("+55", R.drawable.flag_brazil))
//        list.add(CountryItem("+56", R.drawable.flag_chile))
//
//        return list
//    }

}