package com.covidapp.webapi.model

import com.covidapp.interfaces.OnHttpResponse
import com.covidapp.webapi.coroutine.CoroutineBase
import com.covidapp.webapi.httpType.HttpType
import com.covidapp.webapi.model.UserModel
import kotlinx.coroutines.*


class WebApi(reponseListener: OnHttpResponse) {


    var responseListener: OnHttpResponse? = reponseListener


    enum class WebURL(val webUrl:String){
        GetChannelInfo("https://reqres.in/api/users")
    }




      fun getAllChannels(){

         println("||  final res Name54 : webapi")

        CoroutineBase.instance.launch(Dispatchers.IO) {
            var params:  HashMap<String, String>?
            params= HashMap()
            params["page"] = "2"

                HttpType(object : OnHttpResponse {
                    override fun <T> onResponse(objectResponse: T) {
                        if (objectResponse != null) {

                            responseListener?.onResponse(objectResponse)
                        }
                    }
                }).response_GET(WebURL.GetChannelInfo.webUrl, params, UserModel::class.java)

        }
      }

    fun getAllDelayedChannels(){
        CoroutineBase.instance.launch(Dispatchers.IO) {
            var params:  HashMap<String, String>?
            params= HashMap()
            params["delay"] = "5"

            HttpType(object : OnHttpResponse {
                override fun <T> onResponse(objectResponse: T) {
                    if (objectResponse != null) {

                        responseListener?.onResponse(objectResponse)
                    }
                }
            }).response_GET(WebURL.GetChannelInfo.webUrl, params, UserModel::class.java)

        }
    }








}