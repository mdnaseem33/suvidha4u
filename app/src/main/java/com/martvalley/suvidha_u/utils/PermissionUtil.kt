package com.martvalley.suvidha_u.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


sealed class PermissionState {
    object Granted : PermissionState()
    object Denied : PermissionState()
    object PermanentlyDenied : PermissionState()
}

@JvmInline
value class Permission(val result: ActivityResultLauncher<Array<String>>)


fun Fragment.registerPermission(onPermissionResult: (PermissionState) -> Unit): Permission {
    return Permission(
        this.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            onPermissionResult(getPermissionState(activity, it as MutableMap<String, Boolean>))
        }
    )
}

fun AppCompatActivity.registerPermission(onPermissionResult: (PermissionState) -> Unit): Permission {
    return Permission(
        this.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            onPermissionResult(getPermissionState(this, it as MutableMap<String, Boolean>))
        }
    )
}

private fun getPermissionState(
    activity: Activity?,
    result: MutableMap<String, Boolean>
): PermissionState {
    val deniedList: List<String> = result.filter {
        it.value.not()
    }.map {
        it.key
    }

    var state = when (deniedList.isEmpty()) {
        true -> PermissionState.Granted
        false -> PermissionState.Denied
    }

    if (state == PermissionState.Denied) {
        val permanentlyMappedList = deniedList.map {
            activity?.let { activity ->
                ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
            }
        }

        if (permanentlyMappedList.contains(false)) {
            state = PermissionState.PermanentlyDenied
        }
    }
    return state
}


fun Permission.launchSinglePermission(permission: String) {
    this.result.launch(arrayOf(permission))
}

fun Permission.launchMultiplePermission(permissionList: Array<String>) {
    this.result.launch(permissionList)
}

fun AppCompatActivity.askUserToRequestPermissionExplicitly() {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", this@askUserToRequestPermissionExplicitly.packageName, null)
        startActivity(this)
    }
}

fun Fragment.askUserToRequestPermissionExplicitly() {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts(
            "package",
            this@askUserToRequestPermissionExplicitly.requireActivity().packageName,
            null
        )
        startActivity(this)
    }
}

private fun Activity.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

private fun Fragment.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this.requireActivity(),
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

/*
usage example

val cameraPermission = registerPermission {
    when (it) {
        PermissionState.Denied -> {
            showToast("camera permission denied")
        }
        PermissionState.Granted -> {
            showToast("camera permission granted")
        }
        PermissionState.PermanentlyDenied -> {
            showToast("camera permission permanently denied")
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        }
    }
}

cameraPermission.launchSinglePermission(android.Manifest.permission.CAMERA)
storagePermission.launchMultiplePermission(
arrayOf(
android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
android.Manifest.permission.READ_EXTERNAL_STORAGE
)
)*/
