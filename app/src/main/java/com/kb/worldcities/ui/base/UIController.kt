package com.kb.worldcities.ui.base

interface UIController {
    fun showPopup(
        title: String? = null,
            message: String? = null,
            buttonLabel: String? = null,
            alignment: Int? = null,
            onDismiss: (() -> Unit)? = null,
            errorCode: String? = null
    )
}