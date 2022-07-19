package com.horizons.camchat.activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.horizons.camchat.R
import com.horizons.camchat.databinding.ActivityCallBinding
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration

private const val PERMISSION_REQUEST_ID = 23

// Ask for Android device permissions at runtime.
private val ALL_REQUESTED_PERMISSIONS = arrayOf(
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.CAMERA,
    Manifest.permission.READ_PHONE_STATE
)

class CallActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCallBinding

    private lateinit var rtcEngine: RtcEngine

    private var remoteView: SurfaceView? = null
    private var localView: SurfaceView? = null

    private val rtcEventHandler = object : IRtcEngineEventHandler() {
        // received remote user first video frame
        override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
            runOnUiThread {
                setupRemoteVideoView(uid)
            }
        }

        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            Log.d("TAG", "Success")
        }

        // remote user has left channel
        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread {
                removeRemoteView()
            }
        }

        override fun onLeaveChannel(stats: RtcStats?) {
            runOnUiThread {
                finish()
                Toast.makeText(this@CallActivity, "Call ended", Toast.LENGTH_SHORT).show()
            }
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
            endCall()
        }

        binding.switchCamera.setOnClickListener {
            rtcEngine.switchCamera()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        leaveChannel()
        RtcEngine.destroy()
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
        joinChannel()
        setupLocalVideoView()
    }

    private fun joinChannel() {
        rtcEngine.joinChannel(
            getString(R.string.agora_token),
            "CamChat-Channel",
            "Extra Optional Data",
            0
        )
    }

    private fun setupVideoConfig() {
        rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)

        rtcEngine.enableVideo()

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
            rtcEngine = RtcEngine.create(baseContext, getString(R.string.app_id), rtcEventHandler)
        } catch (e: Exception) {
            Log.d("TAG", "RTC SDK Init Failure Error: $e")
        }
    }

    private fun setupLocalVideoView() {
        localView = RtcEngine.CreateRendererView(baseContext)
        localView!!.setZOrderMediaOverlay(true)
        binding.localView.addView(localView)

        rtcEngine.setupLocalVideo(VideoCanvas(localView, VideoCanvas.RENDER_MODE_FILL, 0))
    }

    private fun setupRemoteVideoView(uid: Int) {
        if (binding.remoteView.childCount > 1) {
            return
        }
        remoteView = RtcEngine.CreateRendererView(baseContext)
        binding.remoteView.addView(remoteView)

        // Try out different RENDER_MODE
        rtcEngine.setupRemoteVideo(VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_FILL, uid))
    }

    private fun removeRemoteView() {
        if (remoteView != null) {
            binding.remoteView.removeView(remoteView)
        }
        remoteView = null
    }

    private fun removeLocalView() {
        if (localView != null) {
            binding.localView.removeView(localView)
        }
        localView = null
    }


    private fun leaveChannel() {
        rtcEngine.leaveChannel()
    }

    private fun endCall() {
        removeLocalView()
        removeRemoteView()
        leaveChannel()
    }
}