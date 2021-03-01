package com.marcossalto.targetmvd.ui.base

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.ui.custom.LoadingDialog

open class BaseFragment : Fragment(), BaseView {

    private var loadingDialog: LoadingDialog? = null

    override fun showProgress() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(requireContext(), null)
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

        val builder = AlertDialog.Builder(requireContext())

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
}
