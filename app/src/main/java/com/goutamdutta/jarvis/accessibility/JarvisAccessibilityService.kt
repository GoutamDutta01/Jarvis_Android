package com.goutamdutta.jarvis.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class JarvisAccessibilityService : AccessibilityService() {
    lateinit var actions: AccessibilityActionExecutor
        private set

    override fun onServiceConnected() {
        super.onServiceConnected()
        actions = AccessibilityActionExecutor(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit
    override fun onInterrupt() = Unit

    fun goBack(): Boolean = performGlobalAction(GLOBAL_ACTION_BACK)
    fun goHome(): Boolean = performGlobalAction(GLOBAL_ACTION_HOME)
}
