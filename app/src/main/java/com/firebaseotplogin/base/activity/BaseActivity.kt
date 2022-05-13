package com.firebaseotplogin.base.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.app.TaskStackBuilder
import com.firebaseotplogin.utils.Util


import javax.inject.Inject

/**
 * Base Activity class that takes in
 * @param screenLayoutId layout resource ID for the activity
 * and inflates the layout.
 *
 * This should be used for skeleton classes that wrap around a fragment and majorly do nothing else.
 */
abstract class BaseActivity(
    @LayoutRes private val screenLayoutId: Int

) : AppCompatActivity() {

    lateinit var alertDialog: AlertDialog

    @Inject
    lateinit var util: Util
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDaggerComponent()
        setContentLayout(screenLayoutId)
        processIntent(intent)
    }

    open fun setContentLayout(@LayoutRes layoutRes: Int) {
        setContentView(layoutRes)
    }


    abstract fun injectDaggerComponent()

    open fun processIntent(intent: Intent) {}

    // region Menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                return onUpNavigationPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    // endregion Menu

    // region Navigation
    /**
     * Function that handles when the user presses on the back arrow in the actionbar
     * Checks if the activity is the task Root
     * If yes, checks for parentActivity
     * If parentActivity information found, creates a task with the parentActivityIntent and pushes it
     * else onBackPressed
     */
    private fun onUpNavigationPressed(): Boolean {
        if (isTaskRoot) {
            val upIntent = NavUtils.getParentActivityIntent(this)
            upIntent?.let {
                TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities()
                overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
                return true
            }
        }
        onBackPressed()
        return true
    }
    // endregion Navigation
}