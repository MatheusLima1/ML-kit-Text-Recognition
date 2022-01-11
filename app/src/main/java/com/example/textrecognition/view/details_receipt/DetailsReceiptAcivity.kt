package com.example.textrecognition.view.details_receipt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SimpleAdapter
import android.widget.Toast
import com.example.textrecognition.R
import com.example.textrecognition.network.entity.TextInfoNetwork
import com.example.textrecognition.network.webclient.TextInfoWebClient
import kotlinx.android.synthetic.main.activity_details_receipt.*

class DetailsReceiptAcivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_receipt)

        val extras = intent.extras
        if (extras != null) {
            val id_receipt = extras.getString("id_receipt")
            val uuid_user = extras.getString("uuid_user")
            getDetails(id_receipt, uuid_user)
        }

    }

    private fun getDetails(idReceipt: String?, uuidUser: String?) {
        TextInfoWebClient().getReceipt(
            uuid = uuidUser!!,
            id = idReceipt!!,
            preExecute = {},
            finished = {},
            sucess = {
                fillViewInformation(it)
            },
            failure = {
                Toast.makeText(this, "Falha ao carregar o produto", Toast.LENGTH_LONG).show()
            })
    }

    private fun fillViewInformation(textInfoNetwork: TextInfoNetwork) {
        txt_company_document_value.text = textInfoNetwork.company?.document
        txt_company_name_value.text = textInfoNetwork.company?.name
        txt_client_document_value.text = textInfoNetwork.clientId
        txt_data_value.text = textInfoNetwork.data
        txt_total_value.text = textInfoNetwork.total.toString()
        if (textInfoNetwork.products.isNotEmpty()) {
            recylcer_products.adapter = ProductSimpleAdapter(textInfoNetwork.products)
        } else {
            recylcer_products.visibility = View.GONE
        }
    }
}