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
import java.net.SocketAddress
import java.security.MessageDigest
import kotlin.random.Random

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    public fun onEnterRegister(view : View)
    {
        CoroutineScope(IO).launch {
            val soc = Socket("nextrun.mykeenetic.by", 801)
            val writer = soc.getOutputStream()
            val reader = soc.getInputStream()

            val login = findViewById<EditText>(R.id.editTextRegisterLogin).text.toString()
            val pass = findViewById<EditText>(R.id.editTextRegisterPassword).text.toString()
            val email = findViewById<EditText>(R.id.editTextRegisterEmail).text.toString()

            var data: ByteArray = ByteArray(255)
            val salt = genSalt()

            writer.write("reg".toByteArray())
            Thread.sleep(100)
            reader.read()
            writer.write(login.toByteArray())
            Thread.sleep(100)
            reader.read()
            writer.write(hashString(pass + salt).toByteArray())
            Thread.sleep(100)
            reader.read()
            writer.write(salt.toByteArray())
            Thread.sleep(100)
            reader.read()
            Thread.sleep(100)
            writer.write(email.toByteArray())
            reader.read(data)

            writer.close()
            reader.close()
            soc.close()
            if (String(data).startsWith("{ERR1}"))
                Snackbar.make(findViewById<View>(R.id.button_register_enter), String(data).removeSuffix("{ER1}"), Snackbar.LENGTH_LONG)
            else {
                val soc = Socket("nextrun.mykeenetic.by", 801)
                val writer = soc.getOutputStream()
                val reader = soc.getInputStream()
                data = ByteArray(255)

                writer.write("log".toByteArray())
                reader.read(data, 0, 255)
                writer.write(login.toByteArray())
                reader.read(data, 0, 255)
                data = clearData(data)
                writer.write(hashString(pass + String(data)).toByteArray())
                Thread.sleep(100)
                reader.read(data)
                if (String(data).startsWith("{ER1}"))
                    Snackbar.make(findViewById<View>(R.id.button_register_enter), "Wrong Password", Snackbar.LENGTH_LONG).show()
                else {
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    Data.key = String(data).removeRange(9, 10)
                    startActivity(intent)
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

    private fun genSalt() : String
    {
        var salt = ""
        for(i in 1..16) {
            salt += Random.nextInt(33, 126).toChar()
        }
        return salt
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