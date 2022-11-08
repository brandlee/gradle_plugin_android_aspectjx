package com.lancewu.aspectj

import androidx.fragment.app.FragmentActivity

interface IAspectJActivity {
    fun getAttachedActivity(): FragmentActivity?
}