package alaa.ewis.masterdetails.viewModel

import alaa.ewis.masterdetails.data.model.Passenger
import alaa.ewis.masterdetails.repository.PassengersListActivityRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class PassengersListViewModel (application: Application) : AndroidViewModel(application){

    private val repository  = PassengersListActivityRepository(application)
    val showProgress : LiveData<Boolean>
    val showNoPassenger : LiveData<Boolean>
    val passengersList : LiveData<List<Passenger>>
    val passengersSearchList : LiveData<List<Passenger>>

    init {
        this.showProgress = repository.showProgress
        this.showNoPassenger = repository.showNoPassenger
        this.passengersList = repository.passengersList
        this.passengersSearchList = repository.passengersSearchList
        this.getPassengers()
    }

    // Get passengers list to view it.
    private fun getPassengers(){
        repository.getPassengers()
    }

    // Get current list of passenger and if not found show no passenger TV.
    fun getCurrentPassengers(){
        repository.getCurrentPassengers()
    }

    // Search in passenger list by passenger name.
    fun searchPassengers(searchString: String){
        repository.searchPassengers(searchString)
    }
}