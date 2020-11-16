package com.example.smartzi

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class BookingActivity : AppCompatActivity() , View.OnClickListener {
    private lateinit var txtDate:EditText
    private lateinit var txtTime:EditText
    private lateinit var driverN:EditText
    private lateinit var bookingDate:EditText
    private lateinit var bookingTime:EditText

    private var mYear = 0
    private  var mMonth:Int = 0
    private  var mDay:Int = 0
    private  var mHour:Int = 0
    private  var mMinute:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)


        txtDate=findViewById(R.id.in_date)
        txtTime=findViewById(R.id.in_time)
        driverN = findViewById(R.id.DriverName)
        bookingDate = findViewById(R.id.Booking_in_date)
        bookingTime = findViewById(R.id.Booking_in_time)

        val driverName = intent?.extras?.getString("DriverName")
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        val date = calendar.get(Calendar.DATE)
        val month =calendar.get(Calendar.MONTH)+1
        val year = calendar.get(Calendar.YEAR)
        val time = calendar.get(Calendar.HOUR_OF_DAY)
        val min = calendar.get(Calendar.MINUTE)
        val dateInString = date.toString()+"-"+month.toString()+"-"+year.toString()
        bookingDate.setText(dateInString)

        bookingTime.setText(time.toString()+":"+min.toString())

        driverN.setText(driverName)
        txtDate.setOnClickListener(this)
        txtTime.setOnClickListener(this)

    }
    // Date picker and Time picker
    override fun onClick(v: View?) {
        if (v == txtDate) {
            // Get Current Date
            val c: Calendar = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    txtDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog.show()
        }
        if (v === txtTime) {

            // Get Current Time
            val c: Calendar = Calendar.getInstance()
            mHour = c.get(Calendar.HOUR_OF_DAY)
            mMinute = c.get(Calendar.MINUTE)

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                this,
                { view, hourOfDay, minute -> txtTime.setText("$hourOfDay:$minute") },
                mHour,
                mMinute,
                false
            )
            timePickerDialog.show()
        }
    }
}