package com.example.textrecognition.view.list_sync_receipt

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.textrecognition.R
import com.example.textrecognition.network.webclient.TextInfoWebClient
import kotlinx.android.synthetic.main.activity_list_sync_receipt.*
import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.textrecognition.view.details_receipt.DetailsReceiptAcivity


class ListSyncReceiptActivity : AppCompatActivity() {

    var uuid_user = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_sync_receipt)
        val extras = intent.extras
        if (extras != null) {
            uuid_user = extras.getString("uuid_user").orEmpty()
        }
        fillRecipeList()
    }

    private fun fillRecipeList() {
            TextInfoWebClient().list(
                uuid = uuid_user,
            preExecute = {},
            finished = {},
            sucess = { textInfos ->
                recycler_sync_recipe.layoutManager = LinearLayoutManager(this)
                recycler_sync_recipe.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
                recycler_sync_recipe.adapter =
                    ListSyncReceiptAdapter(textInfos) { textInfo, position ->
                        val i = Intent(this, DetailsReceiptAcivity::class.java)
                        i.putExtra("id_receipt", textInfo.id)
                        i.putExtra("uuid_user", uuid_user)
                        startActivity(i)
                    }
            },
            failure = {
                Toast.makeText(this, "Erro ao listar comprovantes", Toast.LENGTH_LONG).show()
            }
        )

    }
}