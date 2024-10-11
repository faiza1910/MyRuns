
package com.example.faiza_tabassum_myruns2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment

class StartFragment : Fragment() {

    private lateinit var inputTypeSpinner: Spinner
    private lateinit var activityTypeSpinner: Spinner
    private lateinit var startButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputTypeSpinner = view.findViewById(R.id.inputTypeSpinner)
        activityTypeSpinner = view.findViewById(R.id.activityTypeSpinner)
        startButton = view.findViewById(R.id.startButton)


        val inputTypes = listOf("Manual Entry", "GPS", "Automatic")
        inputTypeSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, inputTypes)

        val activityTypes = listOf("Running", "Walking", "Standing", "Cycling", "Hiking", "Downhill Skiing", "Cross-Country Skiing", "Snowboarding", "Skating", "Swimming", "Mountain Biking", "Wheelchair", "Elliptical", "Other")
        activityTypeSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, activityTypes)


        startButton.setOnClickListener {
            val selectedInputType = inputTypeSpinner.selectedItem.toString()

            if (selectedInputType == "Manual Entry") {
                val intent = Intent(requireContext(), ManualEntryActivity::class.java)
                startActivity(intent)
            } else {
                val intent= Intent(requireContext(), MapActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
