package com.fappslab.myais.libraries.design.components.modal.model

import androidx.annotation.StringRes

class ButtonModel {
    @StringRes
    var buttonTextRes: Int? = null
    var buttonText: String? = null
    var onCLicked: () -> Unit = {}
}
