package com.example.project_auction.extension

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(requireActivity(), message, duration).show()
}