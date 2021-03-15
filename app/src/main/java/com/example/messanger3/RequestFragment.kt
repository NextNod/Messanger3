package com.example.messanger3

import Data
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.net.Socket
class RequestFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_request, container, false)
        val stateText = root.findViewById<TextView>(R.id.state_textView)
        var sourse :Array<String>? = null;
        val listView = root.findViewById<ListView>(R.id.requestList)
        val dialog = AlertDialog.Builder(root.context)

        val thread = Thread {
            val soc = Socket("nextrun.mykeenetic.by", 801)
            val writer = soc.getOutputStream()
            val reader = soc.getInputStream()
            var data = ByteArray(255)

            writer.write("[GET_INC_REQ]".toByteArray())
            Thread.sleep(50)
            reader.read(data)
            writer.write(Data.key.toByteArray())
            Thread.sleep(50)
            reader.read(data)

            if (String(data).startsWith("{101}")) {
                data = clearData(data)
                sourse = slice(String(data).removePrefix("{101}"), '\n')
            }
            else {
                stateText.text = "Sorry no requests(("
            }
        }
        thread.start()
        thread.join()

        val layout = SwipeRefreshLayout(root.context)
        layout.setOnRefreshListener {
            val thread = Thread {
                val soc = Socket("nextrun.mykeenetic.by", 801)
                val writer = soc.getOutputStream()
                val reader = soc.getInputStream()
                var data = ByteArray(255)

                writer.write("[GET_INC_REQ]".toByteArray())
                Thread.sleep(50)
                reader.read(data)
                writer.write(Data.key.toByteArray())
                Thread.sleep(50)
                reader.read(data)

                if (String(data).startsWith("{100}")) {
                    data = clearData(data)
                    sourse = slice(String(data).removePrefix("{100}"), '\n')
                }
                else {
                    stateText.text = "Sorry no requests(("
                }

                if(sourse != null) {
                    stateText.visibility = View.INVISIBLE
                    listView.visibility = View.VISIBLE
                    root.findViewById<TextView>(R.id.textView6).visibility = View.VISIBLE
                    listView.adapter = ArrayAdapter(root.context, android.R.layout.simple_list_item_1, sourse!!)
                }
            }
            thread.start()
            thread.join()

            layout.isRefreshing = false
        }

        layout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        if(sourse != null) {
            stateText.visibility = View.INVISIBLE
            listView.visibility = View.VISIBLE
            root.findViewById<TextView>(R.id.textView6).visibility = View.VISIBLE
            listView.adapter = ArrayAdapter(root.context, android.R.layout.simple_list_item_1, sourse!!)
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val user = sourse?.get(position)
            dialog.setTitle(user)
            dialog.setMessage("Do you want add to friends $user?")
            dialog.setPositiveButton("Yes") { dialog, _ ->
                CoroutineScope(IO).launch {
                    if (user != null) {
                        val soc = Socket("nextrun.mykeenetic.by", 801)
                        val writer = soc.getOutputStream()
                        val reader = soc.getInputStream()
                        val data = ByteArray(255)

                        writer.write("[ACCEPT_REQ]".toByteArray())
                        Thread.sleep(50)
                        reader.read(data)

                        writer.write(Data.key.toByteArray())
                        Thread.sleep(50)
                        reader.read(data)


                        writer.write(user.toByteArray())
                        Thread.sleep(50)
                        reader.read(data)

                        if(String(data).startsWith("{100}"))
                            Snackbar.make(root, "Now you`re friends!", Snackbar.LENGTH_LONG).show()
                        else {
                            SnakBar.make(root, String(data), Snackbar.LENGTH_LONG)
                        }
                        dialog.cancel()
                    }
                }
            }
            dialog.setNegativeButton("No") { dialog, _ ->
                CoroutineScope(IO).launch {
                    if (user != null) {
                        val soc = Socket("nextrun.mykeenetic.by", 801)
                        val writer = soc.getOutputStream()
                        val reader = soc.getInputStream()
                        val data = ByteArray(255)

                        writer.write("[DENY_ICN_REQ]".toByteArray())
                        Thread.sleep(50)
                        reader.read(data)

                        writer.write(Data.key.toByteArray())
                        Thread.sleep(50)
                        reader.read(data)


                        writer.write(user.toByteArray())
                        Thread.sleep(50)
                        reader.read(data)

                        if (String(data).startsWith("{ER1}"))
                            Snackbar.make(
                                root,
                                String(data).removePrefix("{ER1}"),
                                Snackbar.LENGTH_LONG
                            ).show()
                        else {
                            Snackbar.make(
                                root,
                                String(data).removePrefix("{INF}"),
                                Snackbar.LENGTH_LONG
                            ).show()
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
}