import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.weshare.R
import com.example.weshare.user.User

class UserAdapter(context: Context, private val users: List<User>) : ArrayAdapter<User>(context, 0, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)

        val user = getItem(position)
        val userNameTextView = view.findViewById<TextView>(R.id.userNameTextView)
        val userEmailTextView = view.findViewById<TextView>(R.id.userEmailTextView)
        val removeUserButton = view.findViewById<Button>(R.id.removeUserButton)

        userNameTextView.text = user?.name
        userEmailTextView.text = user?.email

        removeUserButton.setOnClickListener {
            // Implement removal logic here
            // For example, remove the user from the list and notify the adapter
            // users.removeAt(position)
            // notifyDataSetChanged()
        }

        return view
    }
}
