package alaa.ewis.masterdetails.view

import alaa.ewis.masterdetails.R
import alaa.ewis.masterdetails.databinding.ActivityHandlePassengerBinding
import alaa.ewis.masterdetails.utils.toGone
import alaa.ewis.masterdetails.utils.toVisible
import alaa.ewis.masterdetails.viewModel.AirlineViewModel
import alaa.ewis.masterdetails.viewModel.PassengerViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider

import android.widget.ArrayAdapter
import android.widget.Toast

// Activity used for add or edit passenger data.
class HandlePassengerActivity : AppCompatActivity() {

    private var spinnerPosition: String? = null
    private var firstSpinnerClick: Boolean = true
    private lateinit var binding: ActivityHandlePassengerBinding
    private lateinit var viewModel: PassengerViewModel
    private lateinit var airlineViewModel: AirlineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()

        viewModel = ViewModelProvider(this).get(PassengerViewModel::class.java)
        airlineViewModel = ViewModelProvider(this).get(AirlineViewModel::class.java)

        // Get passenger id from intent and pass it to view model.
        viewModel.setPassengerId(intent.getStringExtra(getString(R.string.passenger_id)).toString())

        // Handel back button fun to back to previous activity.
        binding.toolbar.toolbarBackButton.setOnClickListener {
            finish()
        }

        // Close activity when click on cancel button.
        binding.cancelButton.setOnClickListener {
            finish()
        }

        // View spinner dropdown when click on aitline edittext.
        binding.airlineEditText.setOnClickListener {
            binding.airlineSpinner.performClick()
            if(binding.airlineSpinner.adapter == null || binding.airlineSpinner.adapter.isEmpty) Toast.makeText(this, getString(R.string.offline), Toast.LENGTH_LONG).show()
        }

        // Handle swipper to refresh airline list.
        binding.swipeRefresh.setOnRefreshListener{airlineViewModel.getAirlineList()}

        // When item selected from spinner set it on airline edit text.
        binding.airlineSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (firstSpinnerClick) {
                        firstSpinnerClick = false
                    }
                    else {
                        binding.airlineEditText.setText(
                            parent!!.getAdapter().getItem(position).toString()
                        )
                        spinnerPosition = airlineViewModel.getAirlineId(binding.airlineSpinner.selectedItemPosition)
                    }
                }

            }

        // Show or hide progress depend on action back from ViewModel.
        viewModel.showProgress.observe(this, {
            showLoader(it)
        })

        // Show or hide progress depend on action back from ViewModel.
        airlineViewModel.showProgress.observe(this, {
            showLoader(it)
        })

        // Show add or edit title depend on if passenger id passed or no.
        viewModel.addPassenger.observe(this, {
            if (it) {
                binding.saveButton.setText(R.string.add)
                binding.toolbar.toolbarTitle.setText(R.string.add_passenger)
            } else {
                binding.saveButton.setText(R.string.save)
                binding.toolbar.toolbarTitle.setText(R.string.edit_passenger)
                binding.swipeRefresh.isEnabled = false
            }
        })

        // Handel show passenger data in screen if data changed.
        viewModel.passenger.observe(this, {
            if (it != null) {
                binding.nameEditText.setText(it.name)
                binding.tripsEditText.setText(it.trips.toString())
                if (it.airline != null && it.airline!!.size > 0) {
                    binding.airlineEditText.setText(it.airline!!.get(0)!!.name)
                    spinnerPosition = it.airline!!.get(0)!!.id
                }
            }
        })

        // Set airline list to recycler view.
        airlineViewModel.airlineList.observe(this, {
            val airlineAdapter: ArrayAdapter<*> = ArrayAdapter<String?>(
                this,
                android.R.layout.simple_spinner_item,
                it.map { air -> air.name })
            binding.airlineSpinner.adapter = airlineAdapter
        })

        // Handle save button click to add or update passenger data.
        binding.saveButton.setOnClickListener {
            if (validData()) {
                viewModel.handlePassenger(
                    binding.nameEditText.text.toString(),
                    binding.tripsEditText.text.toString(),
                    spinnerPosition!!
                )
            }
        }

        viewModel.done.observe(this, {
            if (it)
                finish()
        })
    }

    // Handel layout inflation and toolbar components visibility.
    private fun initViewBinding() {
        binding = ActivityHandlePassengerBinding.inflate(layoutInflater)
        val view = binding.root
        binding.toolbar.toolbarTitle.setText(
            R.string.add_passenger
        )
        binding.toolbar.toolbarBackButton.toVisible()
        binding.toolbar.toolbarEditButton.toGone()
        binding.toolbar.toolbarSearchButton.toGone()
        setContentView(view)
    }

    // Validate entered data.
    private fun validData(): Boolean {
        var valid = true
        if (binding.nameEditText.text.isNullOrEmpty()) {
            valid = false
            binding.nameEditText.setError(getString(R.string.name_error))
        }
        if (binding.tripsEditText.text.isNullOrEmpty()) {
            valid = false
            binding.tripsEditText.setError(getString(R.string.trip_error))
        }
        if (binding.airlineEditText.text.isNullOrEmpty()) {
            valid = false
            binding.airlineEditText.setError(getString(R.string.airline_error))
        }
        return valid
    }

    // Handle loader visibility
    private fun showLoader(show: Boolean){
        if (show)
            binding.pbLoading.toVisible()
        else {
            binding.pbLoading.toGone()
            binding.swipeRefresh.isRefreshing = false
        }
    }
}