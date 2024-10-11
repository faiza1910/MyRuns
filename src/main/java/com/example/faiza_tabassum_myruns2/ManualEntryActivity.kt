package com.example.faiza_tabassum_myruns2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class ManualEntryActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private lateinit var listView: ListView
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private val calendar = Calendar.getInstance()
    private var selectedTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_entry)
        title="MyRuns2"
        listView = findViewById(R.id.listView)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)

        val entryItems = listOf("Date", "Time", "Duration", "Distance", "Calories", "Heart Rate", "Comment")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, entryItems)
        listView.adapter = adapter


        listView.setOnItemClickListener { parent, view, position, id ->
            selectedTextView = view as TextView
            when (position) {
                0 -> showDatePicker()
                1 -> showTimePicker()
                else -> showInputDialog(entryItems[position])
            }
        }

        saveButton.setOnClickListener{

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        cancelButton.setOnClickListener{
            Toast.makeText(this, "Entry Discarded", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this, this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(
            this, this,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), false
        )
        timePickerDialog.show()
    }

    private fun showInputDialog(fieldName: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("$fieldName")


        val input = EditText(this)
        builder.setView(input)


        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
    }
}