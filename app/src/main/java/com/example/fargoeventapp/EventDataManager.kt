package com.example.fargoeventapp

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val BASE_URL: String = "https://challenge.myriadapps.com/api/v1/"

/** Retrieves Event data **/
interface EventAPI {

    @GET("events/{id}")
    fun getInfo(@Header("authorization") token: String?, @Path("id") id: Int)
            : Observable<EventDataManager.EventInfo>

    @POST("login")
    @FormUrlEncoded
    fun loginAuthorization(@Field("Username") username: String, @Field("Password") password: String)
            : Observable<JsonObject>
}

class EventDataManager (private val parent: Loginable) {

    interface Loginable{
        fun onLoginResult(result: Boolean)
        fun getApplicationContext(): Context
    }

    private val eventAPI: EventAPI by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        retrofit.create(EventAPI::class.java)
    }

    fun getEventData(id: Int) : EventInfo? {
        return if (token == null) {
            null
        } else {
           null // eventAPI.getInfo(token, id).subscribeWith()
        }
    }

    @SuppressLint("CheckResult")
    fun login(username: String, password: String){
        clearAuthentication()

        if(username.contains(Regex("[\\p{Space}][\\p{Punct}][^$]"))
                or password.contains(" ") or (password == "")){

            parent.onLoginResult(false)
            return
        }

        eventAPI.loginAuthorization(username, password)
            //.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {response -> token = response.get("token").asString
                    if(token!=null){
                        parent.onLoginResult(true)
                    }
                },
                {error -> Toast.makeText(parent.getApplicationContext(), error.message.toString(),Toast.LENGTH_SHORT).show()}) //http request failed
    }

    fun clearAuthentication(){
        token = null
    }

    data class EventInfo(
        var startDate: String,
        var endDate: String,
        var speakers: List<String>,
        var title: String,
        var img_url: String,
        var location: String,
        var featured: Boolean
    )

    companion object {
        var token: String? = null
    }
}