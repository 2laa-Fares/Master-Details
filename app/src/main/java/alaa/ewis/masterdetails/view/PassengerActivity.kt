package alaa.ewis.masterdetails.view

import alaa.ewis.masterdetails.R
import alaa.ewis.masterdetails.databinding.ActivityPassengerBinding
import alaa.ewis.masterdetails.utils.*
import alaa.ewis.masterdetails.viewModel.PassengerViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider

// Activity used to view passenger details.
class PassengerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPassengerBinding
    private lateinit var viewModel: PassengerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()

        viewModel = ViewModelProvider(this).get(PassengerViewModel::class.java)

        // Get passenger id from intent and pass it to view model.
        viewModel.setPassengerId(intent.getStringExtra(getString(R.string.passenger_id)).toString())

        // Handel back button fun to back to passengers list.
        binding.toolbar.toolbarBackButton.setOnClickListener {
            finish()
        }

        // Handel show passenger data in screen if data changed.
        viewModel.passenger.observe(this, {
            binding.toolbar.toolbarTitle.text = it.name
            binding.tripsNoTv.text = it.trips.toString()
            if(it.airline != null && it.airline!!.size > 0) {
                binding.airlineNameTv.text = it.airline!!.get(0)!!.name
                binding.airlineSloganTv.text = it.airline!!.get(0)!!.slogan
                binding.airlineCountryTv.text = it.airline!!.get(0)!!.country
                binding.airlineHeadTv.text = it.airline!!.get(0)!!.head_quaters
                binding.airlineWebsiteTv.text = it.airline!!.get(0)!!.website
                binding.airlineEstablishedTv.text = it.airline!!.get(0)!!.established
                it.airline!!.get(0)!!.logo?.let { it1 -> binding.airlineLogoImage.loadImage(it1) }
            }
        })

        // Handel show or hide airplane details.
        binding.airlineDetailsButton.setOnClickListener {
            if(binding.airlineDetailsCon.isVisible()){
                binding.airlineDetailsCon.toGone()
                binding.airlineDetailsButton.changeImage(R.drawable.arrow)
            } else {
                binding.airlineDetailsCon.toVisible()
                binding.airlineDetailsButton.changeImage(R.drawable.close)
            }
        }
    }

    // Handel layout inflation and toolbar components visibility.
    private fun initViewBinding() {
        binding = ActivityPassengerBinding.inflate(layoutInflater)
        val view = binding.root
        binding.toolbar.toolbarTitle.setText(
            R.string.title_passenger_details
        )
        binding.toolbar.toolbarBackButton.visibility = View.VISIBLE
        binding.toolbar.toolbarEditButton.visibility = View.VISIBLE
        binding.toolbar.toolbarSearchButton.visibility = View.GONE
        setContentView(view)
    }
}