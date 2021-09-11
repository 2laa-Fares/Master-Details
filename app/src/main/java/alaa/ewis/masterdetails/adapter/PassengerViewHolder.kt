package alaa.ewis.masterdetails.adapter

import alaa.ewis.masterdetails.databinding.PassengerItemBinding
import alaa.ewis.masterdetails.data.model.Passenger
import androidx.recyclerview.widget.RecyclerView

// Handel Passenger item view holder for passengers adapter.
class PassengerViewHolder (private val itemBinding: PassengerItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(passengerItem: Passenger, recyclerItemListener: RecyclerItemListener) {
        itemBinding.passengerName.text = passengerItem.name
        itemBinding.passengerViewButton.setOnClickListener { recyclerItemListener.onItemSelected(passengerItem) }
    }
}
