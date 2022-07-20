package com.example.shop

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONArray
import org.json.JSONObject

class SingleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singleactivity)
        //Get the preference/memory that currently holds our info
        val prefs: SharedPreferences = this.getSharedPreferences("shop", Context.MODE_PRIVATE)

//call the views from singleactivity.xml
        val prodname = findViewById<TextView>(R.id.p_name)
        val prodesc = findViewById<TextView>(R.id.p_desc)
        val prodcost = findViewById<TextView>(R.id.p_cost)
        val img = findViewById<ImageView>(R.id.img_url)


//Get the data from the preference
        val flashname = prefs.getString("prod_name", "")
        val flashdesc = prefs.getString("prod_desc", "")
        val flashcost = prefs.getString("prod_cost", "")
        val flashimg = prefs.getString("image_url", "")

//replace the current views with the data from the preference
        prodname.text = flashname
        prodesc.text = flashdesc
        prodcost.text = flashcost

        Glide.with(applicationContext).load(flashimg)
            .apply(RequestOptions().centerCrop())
            .into(img)


        val progressbar = findViewById<ProgressBar>(R.id.progressbar)
        progressbar.visibility = View.GONE
        val phone = findViewById<EditText>(R.id.phone)
        val pay = findViewById<Button>(R.id.pay)


        pay.setOnClickListener {
            progressbar.visibility = View.VISIBLE
            //initialize loop
            val client = AsyncHttpClient(true, 88, 443)
            //create a jsonobject
            val json = JSONObject()
            //Convert data you are sending to json
            json.put("amount", "1")
            json.put("phone", phone.text.toString())
            //Create the body as a json entity
            val body = StringEntity(json.toString())
            //use loop j to post a request

            client.post(this,
                "https://modcom.pythonanywhere.com/mpesa_payment",
                body,
                "application/json",
                object : JsonHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        response: JSONArray?
                    ) {
                        Toast.makeText(applicationContext, "Paid Successfully", Toast.LENGTH_LONG)
                            .show()
                        progressbar.visibility = View.GONE
                    }


                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseString: String?,
                        throwable: Throwable?
                    ) {
                        Toast.makeText(
                            applicationContext,
                            "error during payment",
                            Toast.LENGTH_LONG
                        ).show()
                        progressbar.visibility = View.GONE

                    }


                })
        }
    }
}
