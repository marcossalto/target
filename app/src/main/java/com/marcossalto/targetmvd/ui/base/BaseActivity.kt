package com.marcossalto.targetmvd.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.ui.custom.LoadingDialog

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), BaseView {

    private var loadingDialog: LoadingDialog? = null

    override fun showProgress() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(this, null)
        }

        loadingDialog!!.show()
    }

    override fun hideProgress() {
        if (loadingDialog != null) {
            loadingDialog!!.dismiss()
        }
    }

    override fun showError(message: String?) {
        val builderMessage = if(message.isNullOrBlank())
            getString(R.string.generic_error)
        else
            message

        val builder = AlertDialog.Builder(this)

        builder.apply {
            setTitle(getString(R.string.error))
            setMessage(builderMessage)
            setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.cancel()
            }
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    protected fun startActivityClearTask(activity: Activity) {
        val intent = Intent(this, activity.javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}
