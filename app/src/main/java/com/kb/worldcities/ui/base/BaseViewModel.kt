package com.kb.worldcities.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kb.worldcities.utils.Alert
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel(), UIController {

    val onAlertEvent = MutableLiveData<Alert>()

    override fun showPopup(
        title: String?,
        message: String?,
        buttonLabel: String?,
        alignment: Int?,
        onDismiss: (() -> Unit)?,
        errorCode: String?
    ) {
        val alert = Alert.Popup(title, message, buttonLabel, alignment, onDismiss, errorCode)
        onAlertEvent.value = alert
    }


}