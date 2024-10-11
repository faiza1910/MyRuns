package com.example.faiza_tabassum_myruns2

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment


class SettingsFragment : Fragment() {

    private lateinit var userProfile: TextView
    private lateinit var privacy: CheckBox
    private lateinit var unitPreference: Spinner
    private lateinit var comments: EditText
    private lateinit var webpage: TextView

    private var selectedUnitPreference = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userProfile = view.findViewById(R.id.userProfile)
        privacy = view.findViewById(R.id.privacy)
        unitPreference = view.findViewById(R.id.unitPreference)
        comments = view.findViewById(R.id.comments)
        webpage = view.findViewById(R.id.webpage)


        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf(selectedUnitPreference))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        unitPreference.adapter = adapter


        unitPreference.setOnTouchListener { _, _ ->
            showUnitPreferenceDialog()
            true
        }


        userProfile.setOnClickListener {
            val intent = Intent(requireContext(), UserProfileActivity::class.java)
            startActivity(intent)
        }


        privacy.setOnCheckedChangeListener { _, isChecked ->

        }
    }


    private fun showUnitPreferenceDialog() {
        val options = arrayOf("Metric (Kilometers)", "Imperial (Miles)")
        var selectedOptionIndex = if (selectedUnitPreference == "Metric (Kilometers)") 0 else 1

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Unit Preference")
            .setSingleChoiceItems(options, selectedOptionIndex) { dialogInterface: DialogInterface, which: Int ->
                selectedOptionIndex = which
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }
}
