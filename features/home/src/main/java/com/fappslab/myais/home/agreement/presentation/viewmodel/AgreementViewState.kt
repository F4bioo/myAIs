package com.fappslab.myais.home.agreement.presentation.viewmodel

import com.fappslab.myais.arch.simplepermission.model.PermissionStatus
import com.fappslab.myais.design.components.button.model.ButtonState

internal data class AgreementViewState(
    val isAlwaysDenied: Boolean = false,
    val isGrantedPermission: Boolean = false,
    val permissionStatus: PermissionStatus = PermissionStatus.Denied,
    val buttonState: ButtonState = ButtonState.Disabled,
) {

    fun handlePermissionResultState(status: PermissionStatus) = copy(
        isAlwaysDenied = false,
        isGrantedPermission = true,
        permissionStatus = status,
        buttonState = ButtonState.Enabled
    )
}
