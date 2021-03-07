package com.example.messanger3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.net.Socket
import Data
import SnakBar
import android.widget.CheckBox
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.button_Enter).setOnClickListener {
            CoroutineScope(IO).launch {
                val soc = Socket("nextrun.mykeenetic.by", 801)
                val writer = soc.getOutputStream()
                val reader = soc.getInputStream()
                var salt :ByteArray = ByteArray(255)

                writer.write("log".toByteArray())
                reader.read(salt)

                writer.write(findViewById<EditText>(R.id.editText_Login).text.toString().toByteArray())
                reader.read(salt)
                salt = clearData(salt)

                val example = String(salt)

                writer.write(hashString(findViewById<EditText>(R.id.editText_Register).text.toString() + example).toByteArray())
                Thread.sleep(50)
                reader.read(salt)

                val result = String(salt)
                if(result.startsWith("{"))
                    SnakBar.make(findViewById(R.id.button_Enter), result, Snackbar.LENGTH_LONG)
                else
                {
                    val intent = Intent(applicationContext, HomeActivity::class.java)

                    if(findViewById<CheckBox>(R.id.checkBox_saveData).isChecked) {
                        val db = DataBase(applicationContext, null)
                        if(!db.isEmpty())
                            db.deleteKey()
                        db.addKey(result.substring(0, 9))
                        db.close()
                    }

                    Data.key = result.substring(0, 9)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun clearData(data :ByteArray) :ByteArray
    {
        var result :ByteArray = ByteArray(0)
        data.forEach {
            byte: Byte ->
            if(byte != 0.toByte())
                result += byte
        }

        return result
    }

    private fun hashString(input: String): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest
                .getInstance("SHA-256")
                .digest(input.toByteArray())
        val data = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            data.append(HEX_CHARS[i shr 4 and 0x0f])
            data.append(HEX_CHARS[i and 0x0f])
        }

        var result :String = ""
        String(data).forEach {
            c: Char ->
            if(c.isLetter())
                result += c.toLowerCase()
            else
                result += c
        }
        
        return result
    }
}