package com.example.imagepro

import android.graphics.BitmapFactory
import android.util.Base64
import cameraServices.CameraController.Companion.imag
import com.chaquo.python.PyObject
import com.example.imagepro.CameraActivity.Companion.cameraRotation

class Detector(
    bytes: ByteArray,
    detectorObject: PyObject,
    focalLength: Float,
    sensrHeight: Float,
    tts: Speak?
) {
    init {
        detectorObject.callAttr("result", bytes, focalLength, sensrHeight, cameraRotation)
        val outpt = detectorObject["returnFrame"].toString()
        val decodedString =
            Base64.decode(outpt.substring(2, outpt.length - 1), Base64.DEFAULT)
        CameraActivity.ins!!.runOnUiThread {
            imag!!.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    decodedString,
                    0,
                    decodedString!!.size
                )
            )
        }
        if (detectorObject["warn"]!!.toInt() == 1)
            tts!!.talk(detectorObject["warnObj"].toString())
    }
}