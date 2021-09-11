package alaa.ewis.masterdetails.viewModel

import alaa.ewis.masterdetails.data.model.Airline
import alaa.ewis.masterdetails.repository.PassengerActivityRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class AirlineViewModel (application: Application) : AndroidViewModel(application){

    private val repository  = PassengerActivityRepository(application)
    val showProgress : LiveData<Boolean>
    val airlineList : LiveData<List<Airline>>

    init {
        this.showProgress = repository.showProgress
        this.airlineList = repository.airlineList
        this.getAirlineList()
    }

    // Get airlines list to view in dropdown.
    fun getAirlineList(){
        repository.getAirlineList()
    }

    // Get airline id.
    fun getAirlineId(airlinePosition: Int): String?{
        return airlineList.value?.get(airlinePosition)?.id
    }
}