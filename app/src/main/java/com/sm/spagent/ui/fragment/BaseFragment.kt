package com.sm.spagent.ui.fragment

import android.app.ProgressDialog
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

open class BaseFragment: Fragment() {
  private var progressDialog: ProgressDialog? = null

  protected fun showProgress() {
    progressDialog = ProgressDialog(context)
    progressDialog!!.setMessage("Please Wait...")
    progressDialog!!.setCancelable(false)
    progressDialog!!.show()
  }

  protected fun hideProgress() {
    if (progressDialog != null && progressDialog!!.isShowing) {
      progressDialog!!.dismiss()
    }
  }

  protected fun shortToast(message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
  }

  protected fun shortToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
  }

  protected fun longToast(message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
  }

  protected fun longToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
  }

  protected fun shortSnack(view: View, message: Int) {
    Snackbar.make(view, getString(message), Snackbar.LENGTH_SHORT).show()
  }

  protected fun longSnack(view: View, message: Int) {
    Snackbar.make(view, getString(message), Snackbar.LENGTH_LONG).show()
  }

  protected fun actionSnack(view: View, message: Int, action: Int, listener: View.OnClickListener) {
    Snackbar.make(view, getString(message), Snackbar.LENGTH_INDEFINITE).setAction(action, listener).show()
  }
}
