package com.example.imagepro


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import cameraServices.Camera2Api
import cameraServices.CameraController.Companion.flag
import cameraServices.CameraController.Companion.imag
import cameraServices.CameraController.Companion.instance
import cameraServices.CameraController.Companion.recreate
import cameraServices.CameraController.Companion.singleDetect
import cameraServices.isServiceRunning
import com.example.imagepro.CameraActivity.Companion.cameraRotation
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_main.imageView
import kotlin.math.atan2
import kotlin.math.roundToInt


class CameraActivity : AppCompatActivity() {

    companion object {
        const val CODE_PERM_SYSTEM_ALERT_WINDOW = 6112
        var ins: CameraActivity? = null
        var cameraRotation: Int = 90
    }

    private lateinit var permissionHandler: PermissionHandler
    private var camera2Support = false
    private var cameraClass: Class<*>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        initView()
        permissionHandler = PermissionHandler(this)
        permissionHandler.requestCameraPermission()
        val manager =
                this.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        for (cameraId in manager.cameraIdList) {
            if (manager.getCameraCharacteristics(cameraId!!)
                            .get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL) == CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_FULL ||
                    manager.getCameraCharacteristics(cameraId)
                            .get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL) == CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_3
            ) {
                camera2Support = true
                break
            }
        }
        camera2Support = false
        cameraClass = Camera2Api::class.java
        ins = this
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHandler.onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun initView() {
        if (!isServiceRunning(this, cameraClass as Class<*>)) {
            val intent = Intent(this, cameraClass)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else
                startService(intent)
        }
    }
}
