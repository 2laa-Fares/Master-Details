package alaa.ewis.masterdetails.repository

import alaa.ewis.masterdetails.data.model.Passenger
import alaa.ewis.masterdetails.data.network.NetworkService
import alaa.ewis.masterdetails.data.network.ServiceGenerator
import alaa.ewis.masterdetails.data.offline.getDatabasePassengerData
import alaa.ewis.masterdetails.utils.isOnline
import android.app.Application
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PassengerActivityRepository(val application: Application){
    val showProgress = MutableLiveData<Boolean>()
    val passenger = MutableLiveData<Passenger>()

    // Get passenger data from server online or from realm db offline to view it.
    fun getPassenger(passengerId: String) {
        if(isOnline(application)) {
            showProgress.value = true
            val apiService: NetworkService =
                ServiceGenerator.getClient()!!.create(NetworkService::class.java)

            apiService.getPassenger(passengerId).enqueue(object : Callback<Passenger> {
                override fun onFailure(call: Call<Passenger>, t: Throwable) {
                    showProgress.value = false
                    getOfflinePassenger(passengerId)
                }

                override fun onResponse(
                    call: Call<Passenger>,
                    response: Response<Passenger>
                ) {
                    showProgress.value = false
                    passenger.value = response.body()
                }
            })
        } else {
            getOfflinePassenger(passengerId)
        }
    }

    // Get passenger data from realm database by id.
    private fun getOfflinePassenger(passengerId: String){
        val passengerData = getDatabasePassengerData(application, passengerId)
        if(passengerData != null){
            passenger.value = passengerData
        }
    }
}