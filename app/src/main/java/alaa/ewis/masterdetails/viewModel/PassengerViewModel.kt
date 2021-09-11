package alaa.ewis.masterdetails.viewModel

import alaa.ewis.masterdetails.data.model.Passenger
import alaa.ewis.masterdetails.repository.PassengerActivityRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class PassengerViewModel(application: Application) : AndroidViewModel(application){

    private val repository  = PassengerActivityRepository(application)
    private lateinit var passengerID: String
    val passenger: LiveData<Passenger>
    val showProgress : LiveData<Boolean>

    init {
        this.showProgress = repository.showProgress
        this.passenger = repository.passenger
    }
    // Init default value of passenger data which passed from passengers list.
    fun setPassengerId(id: String) {
        passengerID = id
        getPassengerData()
    }

    // Get passenger data to view it.
    private fun getPassengerData(){
        repository.getPassenger(passengerID)
    }
}