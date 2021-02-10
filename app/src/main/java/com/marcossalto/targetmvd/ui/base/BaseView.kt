package com.marcossalto.targetmvd.ui.base

interface BaseView {
    fun showProgress()
    fun hideProgress()
    fun showError(message: String?)
}