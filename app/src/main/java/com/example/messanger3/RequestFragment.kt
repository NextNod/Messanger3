package com.example.messanger3

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.net.Socket

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RequestFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_request, container, false)
        val stateText = root.findViewById<TextView>(R.id.state_textView)
        var sourse :Array<String>? = null

        val thread = Thread {
            val soc = Socket("nextrun.mykeenetic.by", 801)
            val writer = soc.getOutputStream()
            val reader = soc.getInputStream()
            var data = ByteArray(255)

            writer.write("check_requests".toByteArray())
            Thread.sleep(50)
            reader.read(data)
            writer.write(Data.key.toByteArray())
            Thread.sleep(50)
            reader.read(data)

            if (!String(data).startsWith("{EMP}")) {
                data = clearData(data)
                sourse = slice(String(data).removePrefix("{INF}"), '\n')
            }
            else {
                stateText.text = "Sorry no requests(("
            }
        }
        thread.start()
        thread.join()

        val listView = root.findViewById<ListView>(R.id.requestList)
        val dialog = AlertDialog.Builder(root.context)

        if(sourse != null) {
            stateText.visibility = View.INVISIBLE
            listView.visibility = View.VISIBLE
            root.findViewById<TextView>(R.id.textView6).visibility = View.VISIBLE
            listView.adapter = ArrayAdapter<String>(root.context, android.R.layout.simple_list_item_1, sourse!!)
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            val user = sourse?.get(position)
            dialog.setTitle(user)
            dialog.setMessage("Do you want add to friends $user?")
            dialog.setPositiveButton("Yes") { dialog, which ->  
                CoroutineScope(IO).launch {
                    if (user != null) {
                        val soc = Socket("nextrun.mykeenetic.by", 801)
                        val writer = soc.getOutputStream()
                        val reader = soc.getInputStream()
                        var data = ByteArray(255)

                        writer.write("accept_request".toByteArray())
                        Thread.sleep(50)
                        reader.read(data)

                        writer.write(Data.key.toByteArray())
                        Thread.sleep(50)
                        reader.read(data)


                        writer.write(user.toByteArray())
                        Thread.sleep(50)
                        reader.read(data)

                        if(String(data).startsWith("{ER1}"))
                            Snackbar.make(root, String(data).removePrefix("{ER1}"), Snackbar.LENGTH_LONG).show()
                        else {
                            Snackbar.make(root, String(data).removePrefix("{INF}"), Snackbar.LENGTH_LONG).show()
                        }
                        dialog.cancel()
                    }
                }
            }
            dialog.show()
        }

        return root
    }

    private fun clearData(data :ByteArray) :ByteArray
    {
        var result = ByteArray(0)
        data.forEach {
                byte: Byte ->
            if(byte != 0.toByte())
                result += byte
        }

        return result
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RequestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}