package com.example.textrecognition.view.list_receipt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.textrecognition.R
import com.example.textrecognition.db.entity.TextInfo

class ListReceiptAdapter(
    private val textInfos: List<TextInfo>
) :
    RecyclerView.Adapter<ListReceiptAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val isSync: ImageView

        init {
            textView = view.findViewById(R.id.recipe_description)
            isSync = view.findViewById(R.id.imv_is_sync)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListReceiptAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_recipe_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListReceiptAdapter.ViewHolder, position: Int) {
        holder.textView.text = textInfos[position].text
        holder.isSync.setImageResource(getIsSync(textInfos[position]))
    }

    private fun getIsSync(textInfo: TextInfo): Int {
        return if (textInfo.isSync) R.drawable.ic_check else R.drawable.ic_close
    }

    override fun getItemCount(): Int = textInfos.size
}