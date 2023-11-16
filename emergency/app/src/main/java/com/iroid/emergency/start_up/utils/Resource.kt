package com.iroid.emergency.start_up.utils

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> accepted(data: T?): Resource<T> {
            return Resource(Status.ACCEPTED_DATA, data, null)
        }

        fun <T> rejected(data: T?): Resource<T> {
            return Resource(Status.REJECTED_DATA, data, null)
        }

        fun <T> request(data: T?): Resource<T> {
            return Resource(Status.NEW_REQUEST_DATA, data, null)
        }

        fun <T> errorAccepted(msg: String, data: T?): Resource<T> {
            return Resource(Status.ACCEPTED_EMPTY, data, msg)
        }

        fun <T> errorRejected(msg: String, data: T?): Resource<T> {
            return Resource(Status.REJECTED_EMPTY, data, msg)
        }

        fun <T> errorNewRequest(msg: String, data: T?): Resource<T> {
            return Resource(Status.NEW_REQUEST_EMPTY, data, msg)
        }


        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> noInterNet(msg: String, data: T?): Resource<T> {
            return Resource(Status.NO_INTERNET, data, msg)
        }

    }
}
