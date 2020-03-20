package com.covidapp.interfaces

interface OnHttpResponse {

   open fun<T> onResponse(objectResponse:T)
}