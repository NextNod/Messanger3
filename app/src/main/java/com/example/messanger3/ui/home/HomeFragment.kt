package com.example.messanger3.ui.home

import Data
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.messanger3.R
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
        var text = ""

        val thread = Thread {
            val soc = Socket("nextrun.mykeenetic.by", 801)
            val writer = soc.getOutputStream()
            val reader = soc.getInputStream()
            val key = Data.key
            val data = ByteArray(255)

            writer.write("show_friends".toByteArray())
            Thread.sleep(50)
            reader.read(data)
            writer.write(key.toByteArray())
            Thread.sleep(50)
            reader.read(data)

            text = if(String(data).startsWith("{EMP}"))
                "Sorry, but you haven`t got friends((("
            else
                String(data)
        }

        thread.start()
        thread.join()

        val listView = root.findViewById<ListView>(R.id.listFriends)
        listView.adapter = ArrayAdapter<String>(root.context, android.R.layout.simple_list_item_1, slice(text.removePrefix("{INF}"), '\n'))

        return root
    }

    private fun slice(string :String, suffix :Char) :Array<String> {
        var result :Array<String> = arrayOf()
        var temp = ""
        for(i in string) {
            if(i == suffix) {
                result += temp
                temp = ""
                continue
            }
            temp += i
        }

        return result
    }
}