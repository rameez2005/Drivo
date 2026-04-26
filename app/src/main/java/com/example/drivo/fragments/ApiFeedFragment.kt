package com.example.drivo.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivo.BuildConfig
import com.example.drivo.R
import com.example.drivo.adapters.FuelPriceAdapter
import com.example.drivo.data.repository.FuelRepository
import kotlinx.coroutines.launch

class ApiFeedFragment : Fragment() {

    private companion object {
        private const val TAG = "ApiFeedFragment"
    }

    private val fuelRepository = FuelRepository()
    private lateinit var adapter: FuelPriceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_api_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_fuel_prices)
        val refreshButton = view.findViewById<Button>(R.id.btn_refresh_api)

        adapter = FuelPriceAdapter { article ->
            if (article.articleUrl.isBlank()) {
                Toast.makeText(requireContext(), R.string.article_url_missing, Toast.LENGTH_SHORT).show()
                return@FuelPriceAdapter
            }
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(article.articleUrl)))
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        refreshButton.setOnClickListener { loadFuelPrices() }

        loadFuelPrices()
    }

    private fun loadFuelPrices() {
        val root = view ?: return
        val progressBar = root.findViewById<ProgressBar>(R.id.progress_api)
        val errorText = root.findViewById<TextView>(R.id.tv_api_error)

        if (isApiConfigPlaceholder()) {
            adapter.submitList(emptyList())
            progressBar.visibility = View.GONE
            errorText.visibility = View.VISIBLE
            errorText.text = getString(
                R.string.api_config_missing,
                BuildConfig.FUEL_API_BASE_URL,
                BuildConfig.FUEL_API_ENDPOINT
            )
            return
        }

        progressBar.visibility = View.VISIBLE
        errorText.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            val result = fuelRepository.fetchFuelPrices()
            progressBar.visibility = View.GONE

            result.onSuccess { prices ->
                adapter.submitList(prices)
                errorText.visibility = if (prices.isEmpty()) View.VISIBLE else View.GONE
                if (prices.isEmpty()) {
                    errorText.text = getString(R.string.news_empty_state)
                }
            }.onFailure {
                Log.e(TAG, "Fuel API request failed", it)
                adapter.submitList(emptyList())
                errorText.visibility = View.VISIBLE
                errorText.text = getString(
                    R.string.news_error_with_reason,
                    it.message ?: getString(R.string.api_unknown_error)
                )
            }
        }
    }

    private fun isApiConfigPlaceholder(): Boolean {
        val base = BuildConfig.FUEL_API_BASE_URL.trim()
        val endpoint = BuildConfig.FUEL_API_ENDPOINT.trim()
        return base.contains("example.com", ignoreCase = true) || endpoint.isBlank() || !endpoint.contains("apikey=")
    }
}

