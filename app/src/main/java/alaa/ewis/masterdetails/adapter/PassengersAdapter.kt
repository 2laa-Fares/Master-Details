package alaa.ewis.masterdetails.adapter

import alaa.ewis.masterdetails.R
import alaa.ewis.masterdetails.databinding.PassengerItemBinding
import alaa.ewis.masterdetails.data.model.Passenger
import alaa.ewis.masterdetails.view.PassengerActivity
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

// Adapter which link to passengers list recycler view.
class PassengersAdapter(
    private val context: Context) : RecyclerView.Adapter<PassengerViewHolder>() {
    private var passengers: List<Passenger> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setPassengersList(list: List<Passenger>){
        this.passengers = list
        notifyDataSetChanged()
    }

    // Handel when user click to view passenger data.
    private val onItemClickListener: RecyclerItemListener = object : RecyclerItemListener {
        override fun onItemSelected(passenger: Passenger) {
            val intent = Intent(context, PassengerActivity::class.java)
            intent.putExtra(context.getString(R.string.passenger_id), passenger._id)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengerViewHolder {
        val itemBinding =
            PassengerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PassengerViewHolder(itemBinding)
    }

    // Bind passenger data to view.
    override fun onBindViewHolder(holder: PassengerViewHolder, position: Int) {
        holder.bind(passengers[position], onItemClickListener)
    }

    // Get count of passengers.
    override fun getItemCount(): Int {
        return passengers.size
    }
}