package alaa.ewis.masterdetails.repository

import alaa.ewis.masterdetails.data.model.Passenger
import alaa.ewis.masterdetails.data.model.Passengers
import alaa.ewis.masterdetails.data.network.NetworkService
import alaa.ewis.masterdetails.data.network.ServiceGenerator
import alaa.ewis.masterdetails.data.offline.getDatabasePassengers
import alaa.ewis.masterdetails.data.offline.saveDatabasePassengers
import alaa.ewis.masterdetails.utils.isOnline
import android.app.Application
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PassengersListActivityRepository(val application: Application){
    val showProgress = MutableLiveData<Boolean>()
    val showNoPassenger = MutableLiveData<Boolean>()
    val passengersList = MutableLiveData<List<Passenger>>()
    val passengersSearchList = MutableLiveData<List<Passenger>>()

    // Get passengers list from server online or from realm db offline to view it.
    fun getPassengers() {
        showProgress.value = true
        if(isOnline(application)) {
            val apiService: NetworkService =
                ServiceGenerator.getClient()!!.create(NetworkService::class.java)

            apiService.getPassengersList(0, 10).enqueue(object : Callback<Passengers> {
                override fun onFailure(call: Call<Passengers>, t: Throwable) {
                    showProgress.value = false
                    showNoPassenger.value = true
                    getOfflinePassengers()
                }

                override fun onResponse(
                    call: Call<Passengers>,
                    response: Response<Passengers>
                ) {
                    showProgress.value = false
                    passengersList.value = response.body()?.data
                    if(passengersList.value != null && passengersList.value!!.isNotEmpty()){
                        saveDatabasePassengers(application, passengersList.value!!)
                        showNoPassenger.value = false
                    } else{
                        getOfflinePassengers()
                    }
                }
            })
        } else {
            getOfflinePassengers()
        }
    }

    // Get current list of passenger and if not found show no passenger TV to handel view it after close search.
    fun getCurrentPassengers(){
        if(passengersList.value != null && passengersList.value?.size!! > 0){
            passengersList.value = passengersList.value
            showNoPassenger.value = false
        }else {
            showNoPassenger.value = true
        }
    }

    // Search current list of passenger passanger by name.
    fun searchPassengers(searchString: String) {
        showProgress.value = true
        val searchList :  MutableList<Passenger> = ArrayList()
        passengersList.value?.let {
            if (it.isNotEmpty()) {
                for (passenger in it) {
                    if (passenger.name?.toLowerCase(Locale.ROOT)?.contains(
                            searchString.toLowerCase(
                                Locale.ROOT
                            )
                        ) == true
                    ) {
                        searchList.add(passenger)
                    }
                }
            }
        }

        if(searchList.size > 0) {
            passengersSearchList.postValue(searchList)
            showNoPassenger.value = false
        }
        else {
            passengersSearchList.value = searchList
            showNoPassenger.value = true
        }
        showProgress.value = false
    }

    // Get passengers list from realm database.
    private fun getOfflinePassengers(){
        val passengers = getDatabasePassengers(application)
        if(passengers.isNotEmpty()){
            passengersList.value = passengers
            showNoPassenger.value = false
        } else {
            showNoPassenger.value = true
        }
        showProgress.value = false
    }
}