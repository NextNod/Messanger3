package com.example.messanger3.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.messanger3.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.Exception
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
        val textView: TextView = root.findViewById(R.id.text_gallery)
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val search = root.findViewById<EditText>(R.id.editTextSearch)
        val button = root.findViewById<Button>(R.id.buttonSearch)
        button.setOnClickListener { view ->
            CoroutineScope(IO).launch {
                val soc = Socket("nextrun.mykeenetic.by", 801)
                val writer = soc.getOutputStream()
                val reader = soc.getInputStream()
                var data = ByteArray(255)

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
                if (result.startsWith("{ER1}"))
                    Snackbar.make(view, result.removeSuffix("{ER1}"), Snackbar.LENGTH_LONG).show()
                else {
                    Snackbar.make(view, result.removeSuffix("{INF}"), Snackbar.LENGTH_LONG).show()
                }
            }
        }

        return root
    }
}