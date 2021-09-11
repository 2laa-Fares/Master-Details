package alaa.ewis.masterdetails.adapter

import alaa.ewis.masterdetails.data.model.Passenger

//Interface to reflect on item select on recycler view.
interface RecyclerItemListener {
    fun onItemSelected(passenger : Passenger)
}
