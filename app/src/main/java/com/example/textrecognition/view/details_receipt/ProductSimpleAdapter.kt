package com.example.textrecognition.view.details_receipt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.textrecognition.R
import com.example.textrecognition.network.entity.Products

class ProductSimpleAdapter(
    private val products: List<Products>
) : RecyclerView.Adapter<ProductSimpleAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.txt_product_name)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductSimpleAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_products_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductSimpleAdapter.ViewHolder, position: Int) {
        holder.textView.text = products[position].name
    }

    override fun getItemCount(): Int = products.size
}