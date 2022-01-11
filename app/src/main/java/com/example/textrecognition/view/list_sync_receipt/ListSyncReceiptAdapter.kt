package com.example.textrecognition.view.list_sync_receipt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.textrecognition.R
import com.example.textrecognition.network.entity.TextInfoNetwork

class ListSyncReceiptAdapter(
    private val textInfos: List<TextInfoNetwork>,
    private val onItemClickListener: (textInfo: TextInfoNetwork, position: Int) -> Unit
) :
    RecyclerView.Adapter<ListSyncReceiptAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val companyName: TextView
        val total: TextView
        val clientName: TextView
        val date: TextView

        init {
            companyName = view.findViewById(R.id.txt_company)
            total = view.findViewById(R.id.txt_total)
            clientName = view.findViewById(R.id.txt_client_name)
            date = view.findViewById(R.id.txt_data)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListSyncReceiptAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_sync_receipt_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListSyncReceiptAdapter.ViewHolder, position: Int) {
        holder.companyName.text = textInfos[position].company?.name
        holder.total.text = textInfos[position].total.toString()
        holder.clientName.text = textInfos[position].client?.document
        holder.date.text = textInfos[position].data
        holder.itemView.setOnClickListener { onItemClickListener(textInfos[position], position) }
    }

    override fun getItemCount(): Int = textInfos.size
}