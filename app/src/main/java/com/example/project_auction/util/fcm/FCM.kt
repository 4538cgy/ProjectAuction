package com.example.project_auction.util.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.project_auction.R
import com.example.project_auction.view.activity.lobby.LobbyActivity
import com.example.project_auction.view.activity.login.LoginActivity
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage

class FCM : FirebaseMessagingService() {

    private val TAG : String = this.javaClass.simpleName

    //메세지 수신 함수
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        p0?.data?.isNotEmpty()?.let {
            Log.d(TAG, "Message data payload: " + p0.data)
            if (true) {/* 장기 실행 작업으로 데이터를 처리해야하는지 확인*/
                //장기 실행 작업 (10 초 이상)의 경우 Firebase Job Dispatcher를 사용하십시오
                scheduleJob()
            } else {
                //10 초 이내에 메시지 처리
                handleNow()
            }
        }

        // 메시지에 알림 페이로드가 포함되어 있는지 확인하십시오.
        p0?.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(it.body!!)
        }
    }


    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        var map = mutableMapOf<String, Any>()
        map["pushtoken"] = p0!!
        FirebaseFirestore.getInstance().collection("pushtokens").document(uid!!).set(map)

        //서버로 바뀐토큰 전송
        sendRegistrationToServer(p0)
    }
    private fun sendRegistrationToServer(token: String?) {
        //서버로 바뀐 토큰 전송할 메소드 작성하는 부분
    }
    private fun sendNotification(body:String){
        
        //body 에서 들어온 데이터가 어느 부분의 알람인지 파악하고 해당 알람의 뷰로 이동
        
        val intent = Intent(this,LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = "my_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this,channelId)
            .setContentTitle("ASDFASDFASDF")
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_baseline_add_photo_alternate_24)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun scheduleJob() { //장기작업인지(10초이상)일때 처리하는 메소드
        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(this))
        val myJob = dispatcher.newJobBuilder()
            .setService(MyJobService::class.java)
            .setTag("my-job-tag")
            .build()
        dispatcher.schedule(myJob)
    }

    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    companion object {
        private val TAG = "FirebaseMessageService"
    }
}