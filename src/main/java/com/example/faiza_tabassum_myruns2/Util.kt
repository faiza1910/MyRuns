package com.example.myruns1

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Util {


    fun checkPermissions(activity: Activity?) {
        if (Build.VERSION.SDK_INT < 23) return


        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), 0)
        }
    }


    fun getBitmap(context: Context, imgUri: Uri): Bitmap? {
        return try {

            val inputStream = context.contentResolver.openInputStream(imgUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()


            val matrix = Matrix()
            matrix.setRotate(0f)


            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
