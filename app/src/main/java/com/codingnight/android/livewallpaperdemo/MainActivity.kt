package com.codingnight.android.livewallpaperdemo

import android.Manifest
import android.app.WallpaperManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var mContext: Context

    private val permissionRequestCamera: Int = 454
    private val permissionCameraString: String = Manifest.permission.CAMERA;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this
        findViewById<TextView>(R.id.text)
            .setOnClickListener {
                selfPermission()
            }
    }

    private fun selfPermission() {
        if (ContextCompat.checkSelfPermission(
                mContext,
                permissionCameraString
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permissionCameraString),
                permissionRequestCamera
            )
        } else {
            applyWallpaper()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionRequestCamera -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    applyWallpaper()
                } else {
                    Toast.makeText(
                        mContext,
                        getString(R.string._lease_open_permissions),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun applyWallpaper() {
        startWallpaperPreview()
    }

    private fun startWallpaperPreview() {
        try {
            startActivity(
                Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
                    .putExtra(
                        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        ComponentName(
                            applicationContext,
                            TransparentScreenWallpaperService::class.java
                        )
                    )
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } catch (e: ActivityNotFoundException) {
            try {
                startActivity(
                    Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            } catch (e2: ActivityNotFoundException) {
                try {
                    val pickWallpaper = Intent(Intent.ACTION_SET_WALLPAPER);
                    val chooser: Intent =
                        Intent.createChooser(pickWallpaper, getString(R.string.choose_wallpaper))
                    startActivity(chooser)
                } catch (e3: ActivityNotFoundException) {
                    Toast.makeText(
                        applicationContext,
                        R.string.error_wallpaper_chooser,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}