package alaa.ewis.masterdetails.repository

import alaa.ewis.masterdetails.R
import alaa.ewis.masterdetails.data.model.Airline
import alaa.ewis.masterdetails.data.model.Passenger
import alaa.ewis.masterdetails.data.model.PassengerEditResponse
import alaa.ewis.masterdetails.data.model.PassengerRequest
import alaa.ewis.masterdetails.data.network.ErrorMapper
import alaa.ewis.masterdetails.data.network.NetworkService
import alaa.ewis.masterdetails.data.network.ServiceGenerator
import alaa.ewis.masterdetails.data.offline.*
import alaa.ewis.masterdetails.utils.isOnline
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PassengerActivityRepository(val application: Application) {
    val showProgress = MutableLiveData<Boolean>()
    val passenger = MutableLiveData<Passenger>()
    val addPassenger = MutableLiveData<Boolean>()
    val airlineList = MutableLiveData<List<Airline>>()
    val done = MutableLiveData<Boolean>()

    // Get passenger data from server online or from realm db offline to view it.
    fun getPassenger(passengerId: String) {
        addPassenger.value = false
        if (isOnline(application)) {
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
                    if (response.isSuccessful) passenger.value = response.body()
                    else
                        getOfflinePassenger(passengerId)

                }
            })
        } else {
            getOfflinePassenger(passengerId)
        }
    }

    // Get passenger data from realm database by id.
    private fun getOfflinePassenger(passengerId: String) {
        val passengerData = getDatabasePassengerData(application, passengerId)
        passenger.value = passengerData!!
    }

    // Get airline list from server online or from realm db offline to view it.
    fun getAirlineList() {
        if (isOnline(application)) {
            showProgress.value = true
            val apiService: NetworkService =
                ServiceGenerator.getClient()!!.create(NetworkService::class.java)

            apiService.getAirlines().enqueue(object : Callback<List<Airline>> {
                override fun onFailure(call: Call<List<Airline>>, t: Throwable) {
                    showProgress.value = false
                    getOfflineAirlineList()
                }

                override fun onResponse(
                    call: Call<List<Airline>>,
                    response: Response<List<Airline>>
                ) {
                    showProgress.value = false
                    airlineList.value = response.body()
                    if (airlineList.value != null && airlineList.value!!.isNotEmpty()) {
                        saveDatabaseAirlineList(application, airlineList.value!!)
                    } else {
                        getOfflineAirlineList()
                    }
                }
            })
        } else {
            getOfflineAirlineList()
        }
    }

    // Get airline list from realm database.
    private fun getOfflineAirlineList() {
        val airlines = getDatabaseAirlineList(application)
        if (airlines.isNotEmpty()) {
            airlineList.value = airlines
        } else Toast.makeText(application, application.getString(R.string.offline), Toast.LENGTH_LONG).show()
        showProgress.value = false
    }

    // Post passenger data to server.
    fun postPassenger(passengerAdded: PassengerRequest) {
        if (isOnline(application)) {
            showProgress.value = true
            val apiService: NetworkService =
                ServiceGenerator.getClient()!!.create(NetworkService::class.java)

            apiService.postPassenger(passengerAdded).enqueue(object : Callback<Passenger> {
                override fun onFailure(call: Call<Passenger>, t: Throwable) {
                    showProgress.value = false
                    Toast.makeText(
                        application,
                        application.getString(R.string.network_error),
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<Passenger>,
                    response: Response<Passenger>
                ) {
                    showProgress.value = false
                    if (response.isSuccessful) {
                        passenger.value = response.body()
                        passenger.value?.let { postDatabasePassenger(application, it) }
                        Toast.makeText(
                            application.baseContext,
                            application.getString(R.string.added),
                            Toast.LENGTH_LONG
                        ).show()
                        done.value = true
                    } else Toast.makeText(
                        application,
                        ErrorMapper().getErrorString(response.code()),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else
            Toast.makeText(application, application.getString(R.string.offline), Toast.LENGTH_LONG)
                .show()
    }

    // Edit passenger data in server.
    fun putPassenger(passengerID: String, passengerEdited: PassengerRequest) {
        if (isOnline(application)) {
            showProgress.value = true
            val apiService: NetworkService =
                ServiceGenerator.getClient()!!.create(NetworkService::class.java)

            apiService.putPassenger(passengerID, passengerEdited)
                .enqueue(object : Callback<PassengerEditResponse> {
                    override fun onFailure(call: Call<PassengerEditResponse>, t: Throwable) {
                        showProgress.value = false
                        Toast.makeText(
                            application,
                            application.getString(R.string.network_error),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<PassengerEditResponse>,
                        response: Response<PassengerEditResponse>
                    ) {
                        showProgress.value = false
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                Toast.makeText(
                                    application.baseContext,
                                    response.body()!!.message,
                                    Toast.LENGTH_LONG
                                ).show()

                                if (response.body()!!.message == application.getString(R.string.edited)) {
                                    done.value = true
                                    putDatabasePassenger(application, passengerEdited, passengerID)
                                }
                            }
                        } else Toast.makeText(
                            application,
                            ErrorMapper().getErrorString(response.code()),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        } else
            Toast.makeText(application, application.getString(R.string.offline), Toast.LENGTH_LONG)
                .show()
    }
}