package com.example.faiza_tabassum_myruns2
import android.Manifest
import androidx.core.app.ActivityCompat
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.example.myruns1.Util
import java.io.File

class UserProfileActivity : AppCompatActivity() {

    private lateinit var imageUri: Uri
    private lateinit var cameraResult: ActivityResultLauncher<Intent>
    private lateinit var galleryResult: ActivityResultLauncher<Intent>
    private lateinit var myViewModel: MyViewModel
    private val IMAGE_URI_KEY = "imageUriKey"
    private val REQUEST_CODE_READ_STORAGE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        title ="MyRuns2"
        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        Util.checkPermissions(this)

        val tempImage = findViewById<ImageView>(R.id.profile_image)
        tempImage.setImageResource(R.drawable.default_pic)
        load()


        cameraResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val bitmap = Util.getBitmap(this, imageUri)
                myViewModel.userImage.value = bitmap
            }
        }


        galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val selectedImageUri = result.data!!.data
                if (selectedImageUri != null) {
                    imageUri = selectedImageUri


                    val bitmap = Util.getBitmap(this, imageUri)
                    if (bitmap != null) {
                        myViewModel.userImage.value = bitmap
                    } else {
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



        findViewById<Button>(R.id.button_capture).setOnClickListener {
            showImagePickerDialog()
        }

        findViewById<Button>(R.id.save).setOnClickListener{
            save()
            Toast.makeText(this,"Saved", Toast.LENGTH_SHORT).show()
            finish()
        }
        findViewById<Button>(R.id.cancel).setOnClickListener{
            finish()
        }

        myViewModel.userImage.observe(this, Observer { bitmap: Bitmap? ->
            bitmap?.let {
                findViewById<ImageView>(R.id.profile_image).setImageBitmap(it)
            }
        })
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Open Camera", "Select from Gallery")

        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Pick Profile Picture")

        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> capturePhoto()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.settings -> {
                Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun capturePhoto(){
        val photoFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"profile.jpg")
        imageUri = FileProvider.getUriForFile(this,"com.example.faiza_tabassum_myruns2.fileprovider",photoFile)

        val intent= Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri)
        cameraResult.launch(intent)
    }

    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_STORAGE)
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            galleryResult.launch(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_READ_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open gallery
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                galleryResult.launch(intent)
            } else {
                Toast.makeText(this, "Permission denied. Can't access gallery.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun save(){
        val sharedPreferences = getSharedPreferences("Faiza_Tabassum_MyRuns2", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(IMAGE_URI_KEY, imageUri.toString())
        editor.putString("name", findViewById<EditText>(R.id.name).text.toString())
        editor.putString("email", findViewById<EditText>(R.id.email).text.toString())
        editor.putString("phone_number", findViewById<EditText>(R.id.phone_number).text.toString())
        val gender = when (findViewById<RadioGroup>(R.id.gender).checkedRadioButtonId){
            R.id.male-> "Male"
            R.id.female-> "Female"
            else-> ""
        }
        editor.putString("gender",gender)
        editor.putString("class", findViewById<EditText>(R.id.year).text.toString())
        editor.putString("major", findViewById<EditText>(R.id.major).text.toString())
        editor.apply()
    }

    private fun load(){
        val sharedPreferences = getSharedPreferences("Faiza_Tabassum_MyRuns2", MODE_PRIVATE)
        val savedImageUri = sharedPreferences.getString(IMAGE_URI_KEY, null)
        if (savedImageUri != null) {
            imageUri = Uri.parse(savedImageUri)
            val bitmap = Util.getBitmap(this, imageUri)
            findViewById<ImageView>(R.id.profile_image).setImageBitmap(bitmap)
        }
        findViewById<EditText>(R.id.name).setText(sharedPreferences.getString("name",""))
        findViewById<EditText>(R.id.email).setText(sharedPreferences.getString("email",""))
        findViewById<EditText>(R.id.phone_number).setText(sharedPreferences.getString("phone_number",""))
        val gender = sharedPreferences.getString("gender", "")
        if(gender == "Male"){
            findViewById<RadioButton>(R.id.male).isChecked = true
        } else if (gender == "Female") {
            findViewById<RadioButton>(R.id.female).isChecked = true
        }
        findViewById<EditText>(R.id.year).setText(sharedPreferences.getString("class",""))
        findViewById<EditText>(R.id.major).setText(sharedPreferences.getString("major",""))
    }

    class MyViewModel : ViewModel(){
        val userImage : MutableLiveData<Bitmap> = MutableLiveData()
    }
}
