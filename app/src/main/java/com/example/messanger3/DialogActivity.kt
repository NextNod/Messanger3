package com.example.messanger3

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.messanger3.dialog.Model
import com.example.messanger3.dialog.MyListAdapter
import java.net.Socket

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

        findViewById<Button>(R.id.button_send).setOnClickListener {
            val message = findViewById<EditText>(R.id.editText_message).text.toString()
            findViewById<EditText>(R.id.editText_message).setText("")
            var isGood = false
            var thread = Thread {
                val soc = Socket("nextrun.mykeenetic.by", 801)
                val reader = soc.getInputStream()
                val writer = soc.getOutputStream()
                val data = ByteArray(255)

                writer.write("[SEND]".toByteArray())
                Thread.sleep(50)
                reader.read(data)

                writer.write(Data.key.toByteArray())
                Thread.sleep(50)
                reader.read(data)

                writer.write(Data.dialog.toByteArray())
                Thread.sleep(50)
                reader.read(data)

                writer.write(message.toByteArray())
                Thread.sleep(50)
                reader.read(data)

                isGood = String(data).startsWith("{101}")
            }

            if(message.isNotEmpty()) {
                thread.start()
                thread.join()
            }

            if(isGood) {
                val fragment = findViewById<FrameLayout>(R.id.fragment)
                val listView = fragment.findViewById<ListView>(R.id.dialog_list)
                var result = ""
                val list = mutableListOf<Model>()

                thread = Thread {
                    val soc = Socket("nextrun.mykeenetic.by", 801)
                    val writer = soc.getOutputStream()
                    val reader = soc.getInputStream()
                    val data = ByteArray(9999)

                    writer.write("[GET_DIALOG]".toByteArray())
                    Thread.sleep(50)
                    reader.read(data)

                    writer.write(Data.key.toByteArray())
                    Thread.sleep(50)
                    reader.read(data)

                    writer.write(Data.dialog.toByteArray())
                    Thread.sleep(50)
                    reader.read(data)

                    if(String(data).startsWith("{101}"))
                        result = String(data).removePrefix("{101}")
                }

                thread.start()
                thread.join()

                if (result == "") {
                    list.add(Model("Hello", true))
                    list.add(Model("World", false))
                } else {
                    val div = slice(result, '}')
                    div.forEach {
                        val data = slice(it.removeSuffix("{endl") + "|", '|')
                        val isYou = data[3] != Data.dialog
                        list.add(Model(data[4], isYou))
                    }
                }

                listView.adapter = MyListAdapter(fragment.context, R.layout.row, list)
            }
        }
    }

    private fun slice(string :String, suffix :Char) :Array<String> {
        var result: Array<String> = arrayOf()
        var temp = ""
        for (i in string) {
            if (i == suffix) {
                result += temp
                temp = ""
                continue
            }
            temp += i
        }

        return result
    }
}