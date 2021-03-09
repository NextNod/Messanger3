package com.example.messanger3

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.apply {
            title = Data.dialog
            setLogo(R.mipmap.ic_launcher_round)
            setDisplayUseLogoEnabled(true)
        }
    }
}