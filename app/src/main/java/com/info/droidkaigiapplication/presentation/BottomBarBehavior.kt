package com.info.droidkaigiapplication.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

/**
 * A custom view for showing move next or prev session
 *
 * ref: https://github.com/DroidKaigi/conference-app-2018/issues/180
 */

class BottomBarBehavior(context: Context, attrs: AttributeSet) :
        CoordinatorLayout.Behavior<LinearLayout>(context, attrs) {

    private var defaultDependencyTop = -1

    override fun layoutDependsOn(parent: CoordinatorLayout, child: LinearLayout, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, bottomBar: LinearLayout, dependency: View): Boolean {
        if (defaultDependencyTop == -1) {
            defaultDependencyTop = dependency.top
        }
        bottomBar.translationY = (-dependency.top + defaultDependencyTop).toFloat()
        return true
    }
}
