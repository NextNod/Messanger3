package com.example.messanger3.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.messanger3.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import Data
import kotlinx.coroutines.launch
import java.net.Socket

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        CoroutineScope(IO).launch {
            val soc = Socket("nextrun.mykeenetic.by", 801)
            val writer = soc.getOutputStream()
            val reader = soc.getInputStream()
            val key = Data.key
            var data :ByteArray = ByteArray(255)

            writer.write("show_friends".toByteArray())
            Thread.sleep(50)
            reader.read(data)
            writer.write(key.toByteArray())
            Thread.sleep(50)
            reader.read(data)
        }

        return root
    }
}