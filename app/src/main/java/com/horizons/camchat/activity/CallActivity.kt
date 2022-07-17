package com.horizons.camchat.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.horizons.camchat.R
import com.horizons.camchat.databinding.ActivityCallBinding

class CallActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCallBinding

    private val PERMISSION_REQUEST_ID = 23

    // Ask for Android device permissions at runtime.
    private val ALL_REQUESTED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_PHONE_STATE
    )

    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
            runOnUiThread { // set first remote user to the main bg video container
                setupRemoteVideoStream(uid)
            }
        }

        // remote user has left channel
        fun onUserOffline(uid: Int, reason: Int) { // Tutorial Step 7
            runOnUiThread { onRemoteUserLeft() }
        }

        // remote user has toggled their video
        fun onUserMuteVideo(uid: Int, toggle: Boolean) { // Tutorial Step 10
            runOnUiThread { onRemoteUserVideoToggle(uid, toggle) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // If all the permissions are granted, initialize the RtcEngine object and join a channel.
        if (checkPermission(ALL_REQUESTED_PERMISSIONS[0], PERMISSION_REQUEST_ID) &&
            checkPermission(ALL_REQUESTED_PERMISSIONS[1], PERMISSION_REQUEST_ID) &&
            checkPermission(ALL_REQUESTED_PERMISSIONS[2], PERMISSION_REQUEST_ID)
        ) {
            initSession()
        }

        binding.endCall.setOnClickListener {

        }


    }

    private fun checkPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this, ALL_REQUESTED_PERMISSIONS, requestCode)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ID) {
            if (
                grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                grantResults[2] != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    applicationContext,
                    "Please grant permission to use this feature",
                    Toast.LENGTH_LONG
                ).show()
                finish()
                return
            }
            // continue only if all permissions are granted.
            initSession()
        }
    }

    private fun initSession() {
        initRtcEngine()
        setupVideoConfig()
        setupLocalVideoView()
        joinChannel()
    }

    private fun joinChannel() {

    }

    private fun setupLocalVideoView() {

    }

    private fun setupVideoConfig() {
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);

        mRtcEngine.enableVideo();

        rtcEngine.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        )
    }

    private fun initRtcEngine() {
        try {
            rtcEngine = RtcEngine.create(baseContext, getString(R.string.app_id), mRtcEventHandler)
        } catch (e: Exception) {
            Log.d("TAG", "RTC SDK Init Failure Error: $e")
        }
    }
}