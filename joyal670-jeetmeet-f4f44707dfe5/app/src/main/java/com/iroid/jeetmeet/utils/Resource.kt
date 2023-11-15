package com.iroid.jeetmeet.utils

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }
        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }
        fun <T> dataEmpty(msg: String, data: T?): Resource<T> {
            return Resource(Status.DATA_EMPTY, data, msg)
        }
        fun <T> noInternet(msg: String, data: T?): Resource<T> {
            return Resource(Status.NO_INTERNET, data, msg)
        }
        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}