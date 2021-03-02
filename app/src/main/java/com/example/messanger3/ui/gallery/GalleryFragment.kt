package com.example.messanger3.ui.gallery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.messanger3.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.net.Socket

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        val search = root.findViewById<EditText>(R.id.editTextSearch)
        val button = root.findViewById<Button>(R.id.buttonSearch)
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        button.setOnClickListener { view ->
            imm.hideSoftInputFromWindow(root.windowToken, 0)
            CoroutineScope(IO).launch {
                val soc = Socket("nextrun.mykeenetic.by", 801)
                val writer = soc.getOutputStream()
                val reader = soc.getInputStream()
                val data = ByteArray(255)

                writer.write("add_friend".toByteArray())
                Thread.sleep(50)
                reader.read(data)
                writer.write(Data.key.toByteArray())
                Thread.sleep(50)
                reader.read(data)
                writer.write(search.text.toString().toByteArray())
                Thread.sleep(50)
                reader.read(data)

                val result = String(data)
                SnakBar.make(view, result, Snackbar.LENGTH_LONG)
            }
        }

        return root
    }
}