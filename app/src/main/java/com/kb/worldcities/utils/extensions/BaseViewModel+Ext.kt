package com.kb.worldcities.utils.extensions

import androidx.lifecycle.LifecycleOwner
import com.kb.worldcities.ui.base.BaseViewModel
import com.kb.worldcities.ui.base.UIController
import com.kb.worldcities.utils.Alert

fun BaseViewModel.bindToUiController(
    lifecycleOwner: LifecycleOwner,
    controller: UIController,
) {
    onAlertEvent.observe(lifecycleOwner, {
        when (it) {
            is Alert.Popup -> controller.showPopup(
                title = it.title,
                message = it.message,
                alignment = it.alignment,
                buttonLabel = it.buttonLabel,
                onDismiss = it.onDismiss,
                errorCode = it.errorCode,
            )
        }
    })
}