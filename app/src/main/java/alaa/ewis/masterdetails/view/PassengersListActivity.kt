package alaa.ewis.masterdetails.view

import alaa.ewis.masterdetails.adapter.PassengersAdapter
import alaa.ewis.masterdetails.databinding.ActivityPassengersListBinding
import alaa.ewis.masterdetails.utils.toGone
import alaa.ewis.masterdetails.utils.toVisible
import alaa.ewis.masterdetails.viewModel.PassengersListViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class PassengersListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPassengersListBinding
    private lateinit var viewModel: PassengersListViewModel
    private lateinit var adapter: PassengersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()

        viewModel = ViewModelProvider(this).get(PassengersListViewModel::class.java)

        initRecycler()

        // View search layout.
        binding.toolbar.toolbarSearchButton.setOnClickListener {
            binding.toolbar.toolbarSearchButton.toGone()
            binding.toolbar.toolbarTitle.toGone()
            binding.toolbar.searchLayout.root.toVisible()
        }

        // Close search view layout and get back passengers list to view.
        binding.toolbar.searchLayout.cancelSearchButton.setOnClickListener {
            binding.toolbar.toolbarSearchButton.toVisible()
            binding.toolbar.toolbarTitle.toVisible()
            binding.toolbar.searchLayout.root.toGone()
            binding.toolbar.searchLayout.searchEt.text = null
            viewModel.getCurrentPassengers()
        }

        // Search on list if user enter text.
        binding.toolbar.searchLayout.confirmSearchButton.setOnClickListener {
            if (binding.toolbar.searchLayout.searchEt.text!!.isNotEmpty())
                viewModel.searchPassengers(binding.toolbar.searchLayout.searchEt.text.toString())
        }

        // Show or hide progress depend on action back from ViewModel.
        viewModel.showProgress.observe(this, {
            if (it)
                binding.pbLoading.toVisible()
            else
                binding.pbLoading.toGone()
        })

        // Show or hide no passengers label depend on action back from ViewModel
        viewModel.showNoPassenger.observe(this, {
            if (it) {
                binding.tvNoPassengers.toVisible()
                binding.rvPassengersList.toGone()
            } else {
                binding.tvNoPassengers.toGone()
                binding.rvPassengersList.toVisible()
            }
        })

        // Set passengers list in recycler view.
        viewModel.passengersList.observe(this, {
            adapter.setPassengersList(it)
            binding.rvPassengersList.adapter = adapter
        })

        // Replace recycler passengers list by list back from determine search.
        viewModel.passengersSearchList.observe(this, {
            adapter.setPassengersList(it)
            binding.rvPassengersList.adapter = adapter
        })
    }

    // Handel layout inflation.
    private fun initViewBinding() {
        binding = ActivityPassengersListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    // Init passenger list recycler view.
    private fun initRecycler(){
        val layoutManager = LinearLayoutManager(this)
        binding.rvPassengersList.layoutManager = layoutManager
        binding.rvPassengersList.setHasFixedSize(true)
        adapter = PassengersAdapter(this)
    }
}