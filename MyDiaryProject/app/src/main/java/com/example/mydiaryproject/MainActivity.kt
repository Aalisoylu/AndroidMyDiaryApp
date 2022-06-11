package com.example.mydiaryproject

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiaryproject.adapter.DiariesAdapter
import com.example.mydiaryproject.databinding.ActivityMainBinding
import com.example.mydiaryproject.db.DiariesDatabase
import com.example.mydiaryproject.db.entity.DiaryDb
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var diariesDatabase: DiariesDatabase



    private val diaryAdapter by lazy(LazyThreadSafetyMode.NONE) {
        //Adapter kullanımı gerçekleştirildi.

        // Anı paylaşımı
        DiariesAdapter(shareAction = {

            // Creating intent with action send
            val intent = Intent(Intent.ACTION_SEND)

            // Setting Intent type
            intent.type = "text/plain"

            // Setting whatsapp package name
            intent.setPackage("com.whatsapp")

            // Give your message here
            intent.putExtra(Intent.EXTRA_TEXT, it.imageUrl + "\n" + it.title + "\n" + it.description + "\n" + it.personnelMood )



            // Checking whether whatsapp is installed or not
            if (intent.resolveActivity(packageManager) == null) {
                Toast.makeText(this,
                    "Please install whatsapp first.",
                    Toast.LENGTH_SHORT).show()

            }

            // Starting Whatsapp
            startActivity(intent)

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()

        setSupportActionBar(binding.toolbar)
        getDairyList()
    }

    private fun initialize() {
        diariesDatabase = DiariesDatabase.invoke(applicationContext)

        binding.dairyRecyclerView.apply {
            adapter = diaryAdapter
            setHasFixedSize(true)
        }

        binding.addDiary.setOnClickListener {
            val intent = Intent(this, AddDiaryActivity::class.java)
            startActivity(intent)
        }
        AsyncTask.execute {
            diariesDatabase.dairyDao().insertDiary(
                DiaryDb( Calendar.getInstance().time.toString(),"My App Diary","","binding.password.text.toString()", "binding.description.text.toString()", "GOOD")
            )
        }
    }

    private fun getDairyList() {
            AsyncTask.execute {
              diaryAdapter.setItems(diariesDatabase.dairyDao().getDiaryList())
            }
    }


}

