package us.gijuno.backgroundmms

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private fun requirePerms() {
        val permissions = arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS)
        fun hasPermissions(vararg permissions: String): Boolean = permissions.all {
            Log.d("MainActivity", "Checking permission: $it")
            ActivityCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (hasPermissions()) {
            Log.d("MainActivity", "Requesting permissions")
            ActivityCompat.requestPermissions(this, permissions, 1)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requirePerms()

        findViewById<Button>(R.id.send_button).setOnClickListener {
            sendMessage()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun sendMessage() {
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(
            "+821084353611",
            null,
            "테스트",
            null,
            null)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendMessage()
                }
            }
        }
    }
}
