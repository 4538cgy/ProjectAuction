package com.example.project_auction.extension

import android.app.Activity
import android.widget.Toast

// toast extension 추가
fun Activity.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}