package com.example.mydiaryproject

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.mydiaryproject.databinding.ActivityAddDiaryBinding
import com.example.mydiaryproject.db.DiariesDatabase
import com.example.mydiaryproject.db.entity.DiaryDb
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*

class AddDiaryActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAddDiaryBinding
    private lateinit var diariesDatabase: DiariesDatabase
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var image: ImageView


    var imageUrl = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diary)
        image = findViewById(R.id.image)


        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        diariesDatabase = DiariesDatabase.invoke(applicationContext)
        binding = ActivityAddDiaryBinding.inflate(layoutInflater)

        //Resim ekleme için tıklama
        image.setOnClickListener {
            launchGallery()
            uploadImage()
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


    }

    //lokasyon bilgilerinin alındığı kısım
    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                //Konum bilgileri
                Toast.makeText(
                    applicationContext,
                    "${it.latitude} ${it.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
                //loc_latitude = it.latitude
                //loc_longitude = it.longitude
            }
        }

    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        if (filePath != null) {
            val ref = storageReference?.child("myImages/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)
            Log.d("TAG", "REF: $ref")
            Log.d("TAG", "UUI: " + UUID.randomUUID().toString())

            imageUrl = ref.toString()

        } else {
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }


    fun onClick(view: View) {


        AsyncTask.execute {
            diariesDatabase.dairyDao().insertDiary(
                DiaryDb(
                    Calendar.getInstance().time.toString(),
                    binding.name.getText().toString(),
                    binding.password.text.toString(),
                    binding.description.text.toString(),
                    "GOOD"
                )


            )
        }


        //Toast.makeText(this, "${binding.name.text.toString()}", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        Toast.makeText(this, "The moment has been successfully added", Toast.LENGTH_SHORT).show()


    }

    fun onLocation(view: View) {
        fetchLocation()
    }


}