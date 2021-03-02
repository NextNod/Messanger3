import android.view.View
import com.google.android.material.snackbar.Snackbar

abstract class SnakBar {
    companion object {
        fun make(view: View, code: String, length: Int) {
            val message =
                when {
                    code.startsWith("{110}") -> "Wrong password(("
                    code.startsWith("{111}") -> "Wrong login(("
                    code.startsWith("{200}") -> "Success!"
                    code.startsWith("{210}") -> "That login is already have("
                    code.startsWith("{211}") -> "That email is already have("
                    code.startsWith("{400}") -> "Success!"
                    code.startsWith("{410}") -> "Wrong user("
                    code.startsWith("{501}") -> "You haven`t messages"
                    code.startsWith("{700}") -> "Request sent"
                    code.startsWith("{710}") -> "You`re already friends"
                    code.startsWith("{711}") -> "The incoming request has already been sent"
                    code.startsWith("{712}") -> "The outgoing request has already been sent"
                    code.startsWith("{714}") -> "User not found"
                    code.startsWith("{801}") -> "Users are already friends"
                    code.startsWith("{901}") -> "Users are already friends"
                    code.startsWith("{301}") -> "You don't have any friends"
                    else -> "Some thing went wrong(("
                }
            Snackbar.make(view, message, length).show()
        }
    }
}