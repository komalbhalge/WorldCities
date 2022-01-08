package com.kb.worldcities.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment(), UIController {

  val onViewCreatedListeners = mutableListOf<(View, Bundle?) -> Unit>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    onViewCreatedListeners.forEach { it.invoke(view, savedInstanceState) }
  }

  override fun onDestroyView() {
    super.onDestroyView()
  }

  override fun onDestroy() {
    super.onDestroy()
    onViewCreatedListeners.clear()
  }

    override fun showPopup(
        title: String?,
        message: String?,
        buttonLabel: String?,
        alignment: Int?,
        onDismiss: (() -> Unit)?,
        errorCode: String?
    ) {
        (this.activity as? UIController)?.showPopup(
            title,
            message,
            buttonLabel,
            alignment,
            onDismiss,
            errorCode
        )
    }
}