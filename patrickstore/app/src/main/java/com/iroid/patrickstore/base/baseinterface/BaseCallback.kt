package com.iroid.patrickstore.base.baseinterface

interface BaseCallback {

    fun onSuccess(data: Any)

    fun onFail(error : Error)
}