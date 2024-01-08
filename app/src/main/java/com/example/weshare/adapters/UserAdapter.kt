import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.weshare.R

class UserAdapter(context: Context,
                  private val users: List<String>,
                  private val onRemoveClicked: (String) -> Unit)
    : ArrayAdapter<String>(context, 0, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)

        val memberEmailTextView: TextView = view.findViewById(R.id.userEmailTextView)
        val removeButton: Button = view.findViewById(R.id.removeButton)

        val memberEmail = getItem(position)
        memberEmailTextView.text = memberEmail.toString()

        removeButton.setOnClickListener {
            val memberEmail = getItem(position)
            memberEmail?.let { it1 -> onRemoveClicked(it1) }
        }

        return view
    }
}
