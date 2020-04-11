package com.example.flashbeepshake

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Vibrator
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var CAMERA_PERMISSION = 200
    private var flashLightStatus: Boolean = false
    private var btAction: ImageButton? = null
    private var tvStatus: TextView? = null

    private var tvVibrate: TextView? = null
    private var vbButton: ImageButton? = null

    private var tvBeep: TextView? = null
    private var bpButton: ImageButton? = null
    private var beepingStatus: Boolean? = false

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btAction = findViewById(R.id.light_button)
        tvStatus = findViewById(R.id.tvStatus)
        tvStatus!!.setText("LIGHT OFF")

        tvVibrate = findViewById(R.id.tv_vibrate)
        vbButton = findViewById(R.id.vibration_button)
        tvVibrate!!.setText("VIBRATE OFF")

        bpButton = findViewById(R.id.beep_button)
        tvBeep = findViewById(R.id.tv_beep)
        tvBeep!!.setText("BEEP OFF")



        btAction!!.setOnClickListener {
            val permissions = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if(permissions != PackageManager.PERMISSION_GRANTED){
                    setupPermissions()
                } else {
                    openFlashLight()
                }
            } else {
                openFlashLight()
            }
        }

        val vbButton = findViewById<ImageButton>(R.id.vibration_button)
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        vbButton.setOnClickListener {
            if (tvVibrate!!.getText().toString().equals("VIBRATE OFF")) {
                vibrator.vibrate(1000)
                vbButton!!.setImageDrawable(getDrawable(R.drawable.ic_vibration))
                tvVibrate!!.setText("VIBRATE ON")
            } else {
                vibrator.cancel()
                vbButton!!.setImageDrawable(getDrawable(R.drawable.ic_vibrate_off))
                tvVibrate!!.setText("VIBRATE OFF")
            }
        }

        val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        bpButton?.setOnClickListener{
            if (tvBeep!!.getText().toString().equals("BEEP OFF")) {
                tone.startTone(ToneGenerator.TONE_DTMF_3,600) // play specific tone for 600ms
                beep_button!!.setImageDrawable(getDrawable(R.drawable.ic_volume))
                tvBeep!!.setText("BEEP ON")
            } else{
                beep_button!!.setImageDrawable(getDrawable(R.drawable.ic_volume_off))
                tv_beep!!.setText("BEEP OFF")
            }
        }
    }

    private fun setupPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION -> {
                if(grantResults.isEmpty() || !grantResults[0].equals(PackageManager.PERMISSION_GRANTED)) {
                   Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                } else {
                    openFlashLight()
             }
           }

        }

    }

    private fun openFlashLight() {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        if (!flashLightStatus) {
            try {
                cameraManager.setTorchMode(cameraId, true)
                btAction!!.setImageDrawable(getDrawable(R.drawable.ic_light_on))
                tvStatus!!.setText("LIGHT ON")
                flashLightStatus = true
            } catch (e: CameraAccessException) {
            }
        } else {
            try{
                cameraManager.setTorchMode(cameraId, false)
                btAction!!.setImageDrawable(getDrawable(R.drawable.ic_light))
                tvStatus!!.setText("LIGHT OFF")
                flashLightStatus = false
            } catch (e: CameraAccessException) {

            }
        }
    }
  }


private fun ToneGenerator.cancel() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}


