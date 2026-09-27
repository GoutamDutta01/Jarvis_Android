package com.goutamdutta.jarvis.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityNodeInfo

class AccessibilityActionExecutor(private val service: AccessibilityService) {
    fun clickText(text: String): Boolean = findNodes(text).any { it.performAction(AccessibilityNodeInfo.ACTION_CLICK) }

    fun setFocusedText(text: String): Boolean {
        val node = service.rootInActiveWindow?.findFocus(AccessibilityNodeInfo.FOCUS_INPUT) ?: return false
        val args = android.os.Bundle().apply {
            putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text)
        }
        return node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)
    }

    fun findNodes(text: String): List<AccessibilityNodeInfo> {
        val root = service.rootInActiveWindow ?: return emptyList()
        return root.findAccessibilityNodeInfosByText(text).filter { it.isVisibleToUser }
    }

    fun back(): Boolean = service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
    fun home(): Boolean = service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
}
