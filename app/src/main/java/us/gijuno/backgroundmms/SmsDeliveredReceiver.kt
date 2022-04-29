package us.gijuno.backgroundmms

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import java.util.*

class SmsDeliveredReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, arg1: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> Toast.makeText(context, "SMS delivered", Toast.LENGTH_SHORT)
                .show()
            Activity.RESULT_CANCELED -> Toast.makeText(context,
                "SMS not delivered",
                Toast.LENGTH_SHORT).show()
        }
    }
}


class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive() called")
        val bundle = intent.extras
        val messages: Array<SmsMessage?> = parseSmsMessage(bundle)
        if (messages.isNotEmpty()) {
            val sender: String = messages[0]?.originatingAddress.toString()
            val content: String = messages[0]?.messageBody.toString()
            val date = messages[0]?.let { Date(it.timestampMillis) }
            Log.d(TAG, "sender: $sender")
            Log.d(TAG, "content: $content")
            Log.d(TAG, "date: $date")
        }
    }

    private fun parseSmsMessage(bundle: Bundle?): Array<SmsMessage?> {
        // PDU: Protocol Data Units
        val objs = bundle!!["pdus"] as Array<*>?
        val messages: Array<SmsMessage?> = arrayOfNulls<SmsMessage>(objs!!.size)
        for (i in objs.indices) {
            messages[i] = SmsMessage.createFromPdu(objs[i] as ByteArray)
        }
        return messages
    }

    companion object {
        private const val TAG = "SMSReceiver"
    }
}