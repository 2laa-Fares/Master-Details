package alaa.ewis.masterdetails.viewModel

import alaa.ewis.masterdetails.data.model.Passenger
import alaa.ewis.masterdetails.data.model.PassengerRequest
import alaa.ewis.masterdetails.repository.PassengerActivityRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class PassengerViewModel(application: Application) : AndroidViewModel(application){

    private val repository  = PassengerActivityRepository(application)
    private lateinit var passengerID: String
    val passenger: LiveData<Passenger>
    val showProgress : LiveData<Boolean>
    val addPassenger : LiveData<Boolean>
    val done : LiveData<Boolean>

    init {
        this.showProgress = repository.showProgress
        this.passenger = repository.passenger
        this.addPassenger = repository.addPassenger
        this.done = repository.done
    }

    // Init default value of passenger data which passed from passengers list.
    fun setPassengerId(id: String?) {
        passengerID = id!!
        if(!id.equals("null") && id.isNotEmpty()) getPassengerData()
    }

    // Return passenger id to pass to edit passenger activity.
    fun getPassengerId(): String {
        return passengerID
    }

    // Post or put passenger data.
    fun handlePassenger(name: String, trips: String, airlineId: String){
        if(addPassenger.value != false)
            repository.postPassenger(PassengerRequest(name, trips.toInt(), airlineId.toInt()))
        else
            repository.putPassenger(passengerID, PassengerRequest(name, trips.toInt(), airlineId.toInt()))
    }

    // Get passenger data to view it.
    private fun getPassengerData(){
        repository.getPassenger(passengerID)
    }
}