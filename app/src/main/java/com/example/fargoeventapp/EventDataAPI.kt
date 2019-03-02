package com.example.fargoeventapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val LOGIN_NAME = "username"
const val LOGIN_PASSWORD = "password"
const val FIRST_LOGIN = "firstLogin"
const val LOGIN = "login"
const val BASE_URL: String = "https://challenge.myriadapps.com/api/v1/"

@Parcelize
data class EventInfo(
     var id: String,
     var start_date_time: String,
     var end_date_time: String,
     var title: String,
     var image_url: String,
     var location: String,
     var featured: Boolean
) : Parcelable


data class Speaker(
    var id: String,
    var first_name: String,
    var last_name: String,
    var bio: String,
    var image_url: String
)

/** Retrieves Event data and performs authentication sing Retrofit*/
interface EventAPI {

    @GET("events/")
    fun getEvents(@Header("authorization") authenticationToken: String?)
            : Observable<List<EventInfo>>

    @GET("speakers/{id}")
    fun getSpeakers(@Header("authorization") authenticationToken: String?, @Path("id") id: String)
            : Observable<Speaker>

    @POST("login")
    @FormUrlEncoded
    fun loginAuthorization(@Field("Username") username: String, @Field("Password") password: String)
            : Observable<JsonObject>

    companion object {
        const val AUTHENTICATED = 1
        const val UNAUTHENTICATED = 0
        var token: String? = null

        private var disposableEventData: Disposable? = null
        private var disposableSpeakerData: Disposable? = null
        private var disposableLoginData: Disposable? = null


        private val eventAPI: EventAPI by lazy{
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            retrofit.create(EventAPI::class.java)
        }

        fun state(): Int{
            return if(token==null){
                UNAUTHENTICATED
            } else AUTHENTICATED
        }

        fun clearAuthentication(activity: AppCompatActivity){
            val prefs = activity.getSharedPreferences(LOGIN, Context.MODE_PRIVATE)
            val editpref = prefs.edit()
            editpref.clear()
            editpref.apply()
            token = null
        }

        fun login(activity: AppCompatActivity, username: String?, password: String?,
                  authenticationResultCallback: (result: Boolean, message: String) -> Unit){

            if((username==null) or (password==null)){
                authenticationResultCallback(false, FIRST_LOGIN)
                return
            }

            when {
                username!!.contains(Regex("\\s+")) or password!!.contains(Regex("(\\s+)")) -> {
                    authenticationResultCallback(false, activity.getString(R.string.login_spaces_error))
                    return
                }
                username.contains(Regex("\\p{Punct}")) -> {
                    authenticationResultCallback(false, activity.getString(R.string.username_invalid_punctation))
                    return
                }
                !username.contains(Regex(".+")) or !password.contains(Regex(".+")) -> {
                    authenticationResultCallback(false, activity.getString(R.string.empty_login))
                    return
                }
                else -> {
                    disposableLoginData = eventAPI.loginAuthorization(username, password)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                            { response ->
                                token = response.get("token").asString
                                authenticationResultCallback(token != null, "")
                                val prefences = activity.getSharedPreferences(LOGIN, Context.MODE_PRIVATE)
                                val prefedit = prefences.edit()
                                prefedit.putString(LOGIN_NAME, username)
                                prefedit.putString(LOGIN_PASSWORD, password)
                                prefedit.apply()
                            },
                            { error ->
                                Toast.makeText(activity,
                                    activity.getString(R.string.connection) + error.message, Toast.LENGTH_SHORT).show()
                                authenticationResultCallback(false, activity.getString(R.string.server_request_error))
                            },
                            {
                                disposableLoginData?.dispose()
                            })
                }
            }
        }

        //returns all event info except for speaker data
        fun getEventData(activity: AppCompatActivity, dataResultCallback: (eventData: List<EventInfo>?) -> Unit){
            if (state() ==  UNAUTHENTICATED){
                Toast.makeText(activity, "Not Logged In", Toast.LENGTH_LONG).show()
                return
            } else {
                disposableEventData = eventAPI.getEvents(token).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            result -> dataResultCallback(result)
                            Log.d("Events: ", result.toString())
                        },
                        {
                            error -> Toast.makeText(activity,
                            activity.getString(R.string.connection) + error.message,Toast.LENGTH_LONG).show()
                            dataResultCallback(null)},
                        {
                            disposableEventData?.dispose()
                        })
            }
        }
        // get speaker data from an event id
        fun getSpeakers(context: Context, event: EventInfo?, speakerResultCallback: (Speaker?)->Unit){
            if ((state() ==  UNAUTHENTICATED) or (event == null)){
                return
            } else {
                disposableSpeakerData = eventAPI.getSpeakers(token, event!!.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            speaker -> speakerResultCallback(speaker)
                            Log.d("Speakers: ", speaker.toString())
                        },
                        {
                            error -> Toast.makeText(context,
                            context.getString(R.string.connection) + error.message, Toast.LENGTH_SHORT).show()
                            Log.d("Speaker Error: ", error.message)
                        },
                        {
                            disposableSpeakerData?.dispose()
                        })

            }
        }
    }
}