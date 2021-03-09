package com.example.messanger3.dialog

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.util.AttributeSet
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import androidx.core.view.marginLeft
import com.example.messanger3.R
import java.net.Socket

class DialogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dialog, container, false)
        val listView = root.findViewById<ListView>(R.id.dialog_list)
        val list = mutableListOf<Model>()
        var result = ""


        val thread = Thread {
            val soc = Socket("nextrun.mykeenetic.by", 801)
            val writer = soc.getOutputStream()
            val reader = soc.getInputStream()
            var data = ByteArray(9999)

            writer.write("get_dialog".toByteArray())
            Thread.sleep(50)
            reader.read(data)

            writer.write(Data.key.toByteArray())
            Thread.sleep(50)
            reader.read(data)

            writer.write(Data.dialog.toByteArray())
            Thread.sleep(50)
            reader.read(data)

            if(String(data).startsWith("{500}"))
            result = String(data).removePrefix("{500}")
        }

        thread.start()
        thread.join()
        if (result == "") {
            list.add(Model("Hello", true))
            list.add(Model("World", false))
        } else {
            val div = slice(result, '\n')
            div.forEach {
                val data = slice(slice(it, '}')[1].removeSuffix("{endl") + ":", ':')
                val isYou = data[0] != Data.dialog
                list.add(Model(data[1], isYou))
            }
        }
        listView.adapter = MyListAdapter(root.context, R.layout.row, list)
        listView.divider = null
        return root
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

class Model(val title :String, var isYou :Boolean)

class MyListAdapter(var mCtx: Context, var resource:Int, var items:List<Model>)
    :ArrayAdapter<Model>( mCtx , resource , items ){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View = LayoutInflater.from(mCtx).inflate(resource , null)
        val button = view.findViewById<Button>(R.id.message)
        val fill = view.findViewById<LinearLayout>(R.id.message_fill)
        val item = items[position]

        button.text = item.title
        if(item.isYou)
            fill.gravity = Gravity.RIGHT

        return view
    }

}