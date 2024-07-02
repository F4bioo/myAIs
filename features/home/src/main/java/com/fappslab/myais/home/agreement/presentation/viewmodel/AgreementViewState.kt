package com.fappslab.myais.home.agreement.presentation.viewmodel

import com.fappslab.myais.arch.simplepermission.model.PermissionStatus

internal data class AgreementViewState(
    val isAlwaysDenied: Boolean = false,
    val isGrantedPermission: Boolean = false,
    val permissionStatus: PermissionStatus = PermissionStatus.Denied
) {

    fun handlePermissionResultState(status: PermissionStatus) = copy(
        isAlwaysDenied = false,
        isGrantedPermission = true,
        permissionStatus = status
    )
}
