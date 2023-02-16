package com.example.cs4530app1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap

class MainActivity : AppCompatActivity() {
    private var firstNameEditText: EditText? = null
    private var middleNameEditText: EditText? = null
    private var lastNameEditText: EditText? = null
    private var photoButton: Button? = null
    private var photoImageView: ImageView? = null
    private var submitButton: Button? = null
    private var thumbnail : Bitmap? = null

    private val KEY_FIRST_NAME = "firstName"
    private val KEY_MIDDLE_NAME = "middleName"
    private val KEY_LAST_NAME = "lastName"
    private val KEY_THUMBNAIL = "thumbnail"

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.apply {
            putString(KEY_FIRST_NAME, firstNameEditText?.text.toString())
            putString(KEY_MIDDLE_NAME, middleNameEditText?.text.toString())
            putString(KEY_LAST_NAME, lastNameEditText?.text.toString())
            putParcelable(KEY_THUMBNAIL, thumbnail)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find Views.
        firstNameEditText = findViewById(R.id.mainFirstNameEditText)
        middleNameEditText = findViewById(R.id.mainMiddleNameEditText)
        lastNameEditText = findViewById(R.id.mainLastNameEditText)
        photoButton = findViewById(R.id.mainPhotoButton)
        photoImageView = findViewById(R.id.mainPhotoImageView)
        submitButton = findViewById(R.id.mainSubmitButton)

        // Restore any saved instance state.
        savedInstanceState?.let {
            firstNameEditText?.setText(it.getString(KEY_FIRST_NAME))
            middleNameEditText?.setText(it.getString(KEY_MIDDLE_NAME))
            lastNameEditText?.setText(it.getString(KEY_LAST_NAME))
            thumbnail = it.getParcelable(KEY_THUMBNAIL, Bitmap::class.java)
            onThumbnailChanged()
        }

        // Set up event handlers.
        photoButton?.setOnClickListener { view ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                cameraActivity.launch(intent)
            } catch(e: ActivityNotFoundException) {
                Toast.makeText(this@MainActivity, "Couldn't launch the camera! Set a default camera app.", Toast.LENGTH_LONG).show()
            }
        }
        submitButton?.setOnClickListener { view ->
            val firstName  = firstNameEditText?.text.toString()
            val middleName = middleNameEditText?.text.toString()
            val lastName   = lastNameEditText?.text.toString()
            // Check that all info has been entered.
            if(firstName.isNullOrBlank()) {
                Toast.makeText(this@MainActivity, "Enter a First Name.", Toast.LENGTH_SHORT).show()
            } else if(middleName.isNullOrBlank()) {
                Toast.makeText(this@MainActivity, "Enter a Middle Name.", Toast.LENGTH_SHORT).show()
            } else if(lastName.isNullOrBlank()) {
                Toast.makeText(this@MainActivity, "Enter a Last Name.", Toast.LENGTH_SHORT).show()
            } else if(thumbnail == null){
                Toast.makeText(this@MainActivity, "Take a Photo.", Toast.LENGTH_SHORT).show()
            } else {
                // Submit the form!
                val intent = Intent(this@MainActivity, SuccessActivity::class.java)
                intent.putExtras(Bundle().apply {
                    putString(SuccessActivity.KEY_FIRST_NAME, firstName)
                    putString(SuccessActivity.KEY_LAST_NAME,  lastName)
                })
                startActivity(intent)
            }
        }
    }

    private fun onThumbnailChanged() {
        if(thumbnail != null)
            photoImageView?.setImageBitmap(thumbnail)
    }

    val cameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK) {
            thumbnail = result.data?.getParcelableExtra("data", Bitmap::class.java)
                ?: thumbnail
            onThumbnailChanged()
        }
    }
}