package com.codingnight.android.livewallpaperdemo

import android.hardware.Camera
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import java.io.IOException

class TransparentScreenWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine {
        return CameraWallPaperEngine()
    }

    inner class CameraWallPaperEngine : WallpaperService.Engine(), Camera.PreviewCallback {
        var camera: Camera? = null

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            startPreview()
            setTouchEventsEnabled(true)
        }

        override fun onTouchEvent(event: MotionEvent?) {
            super.onTouchEvent(event)
        }

        override fun onDestroy() {
            super.onDestroy()
            stopPreview()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if (visible)
                startPreview()
            else
                stopPreview()
        }

        private fun startPreview() {
            camera = Camera.open()
            camera?.setDisplayOrientation(90)

            try {
                camera?.setPreviewDisplay(surfaceHolder)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            camera?.startPreview()
        }

        private fun stopPreview() {
            try {
                camera?.stopPreview()
                camera?.setPreviewCallback(null)
                camera?.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            camera = null
        }

        override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
            camera?.addCallbackBuffer(data)
        }
    }
}