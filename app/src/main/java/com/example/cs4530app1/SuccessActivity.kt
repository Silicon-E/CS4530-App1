package com.example.cs4530app1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SuccessActivity : AppCompatActivity() {
    var successMessageTextView: TextView? = null
    var firstName: String? = null
    var lastName: String? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.apply {
            putString(KEY_FIRST_NAME, firstName)
            putString(KEY_LAST_NAME, lastName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        // Collect Views.
        successMessageTextView = findViewById(R.id.successMessageTextView)

        intent.let {
            firstName = it.getStringExtra(KEY_FIRST_NAME)
            lastName = it.getStringExtra(KEY_LAST_NAME)
        }
        savedInstanceState?.let {
            firstName = it.getString(KEY_FIRST_NAME)
            lastName = it.getString(KEY_LAST_NAME)
        }
        onNamesChanged()
    }

    private fun onNamesChanged() {
        successMessageTextView?.setText(firstName+" "+lastName+" is logged in!")
    }

    companion object {
        public val KEY_FIRST_NAME = "firstName"
        public val KEY_LAST_NAME = "lastName"
    }
}