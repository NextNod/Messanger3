import android.view.View
import com.google.android.material.snackbar.Snackbar

abstract class SnakBar {
    companion object {
        fun make(view: View, code: String, length: Int) : Boolean {
            val message =
                when {
                    code.startsWith("{301}") -> "Wrong password!"
                    code.startsWith("{302}") -> "Wrong login"
                    code.startsWith("{303}") -> "Session is loosed"
                    code.startsWith("{304}") -> "This username is already taken"
                    code.startsWith("{305}") -> "This mail is already busy"
                    code.startsWith("{306}") -> "Unknown user"
                    code.startsWith("{307}") -> "You're already friends"
                    code.startsWith("{308}") -> "The request is already received"
                    code.startsWith("{309}") -> "The request is already sent"
                    code.startsWith("{310}") -> "The request is no longer valid"
                    else -> null
                }

            if(message != null) {
                Snackbar.make(view, message, length).show()
            }

            return message == null
        }
    }
}