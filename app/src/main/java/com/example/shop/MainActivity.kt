package com.example.shop

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    lateinit var productList:ArrayList<Product>
    lateinit var recyclerAdapter: RecyclerAdapter //call the adapter
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler)


        val client = AsyncHttpClient(true,80,443)
        //        //pass the product list to adapter
        recyclerAdapter = RecyclerAdapter(applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        client.get(this, "https://modcom.pythonanywhere.com/api/all",
            null,
            "application/json",
            //receiving response from API
            object: JsonHttpResponseHandler(){
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONArray?) {
                    //we convert json array to a list of a given model
                    val gson = GsonBuilder().create()
                    val list = gson.fromJson(response.toString(),
                        //from model into a list
                        Array<Product>::class.java).toList()
                    //now pass the converted list to adapter
                    recyclerAdapter.setProductListItems(list)
                }
                    //Incase of a failure
                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseString: String?,
                    throwable: Throwable?
                ) {
                    Toast.makeText(applicationContext, "no products on sale"+statusCode, Toast.LENGTH_LONG).show()
                }
            }//end handler
        )//end post

        //now put the adapter to recycler view
        recyclerView.adapter = recyclerAdapter
    }
}