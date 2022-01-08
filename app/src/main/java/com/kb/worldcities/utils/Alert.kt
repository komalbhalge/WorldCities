package com.kb.worldcities.utils

import android.view.Gravity

sealed class Alert {
    /**
     * Represents a popup alert dialog.
     */

    data class Popup(
        val title: String?,
        val message: String?,
        val buttonLabel: String?,
        val alignment: Int,
        val onDismiss: (() -> Unit)? = null,
        val errorCode: String?
    ) : Alert() {
        constructor(
            title: String? = null,
            message: String? = null,
            buttonLabel: String? = null,
            alignment: Int? = null,
            onDismiss: (() -> Unit)? = null,
            errorCode: String? = null
        ) : this(
            title = title ?: "Unknown error",
            message = message ?: "An unknown error has occurred.",
            buttonLabel = buttonLabel,
            alignment = alignment ?: Gravity.CENTER,
            onDismiss = onDismiss,
            errorCode = errorCode
        )

    }
}