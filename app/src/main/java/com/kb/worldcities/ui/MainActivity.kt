package com.kb.worldcities.ui

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.kb.worldcities.R
import com.kb.worldcities.di.ListFragmentFactoryEntryPoint
import com.kb.worldcities.ui.base.UIController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UIController {

    override fun onCreate(savedInstanceState: Bundle?) {

        val entryPoint =
            EntryPointAccessors.fromActivity(
                this,
                ListFragmentFactoryEntryPoint::class.java
            )

        supportFragmentManager.fragmentFactory = entryPoint.getFragmentFactory()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Setup Navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val config = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController, config)
    }

    override fun showPopup(
        title: String?,
        message: String?,
        buttonLabel: String?,
        alignment: Int?,
        onDismiss: (() -> Unit)?,
        errorCode: String?
    ) {
        val builder = AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_error)
            .setTitle(title ?: getString(R.string.unknown_error_title))
            .setMessage(message ?: getString(R.string.unknown_error_message))
            .setPositiveButton(
                R.string.okay
            ) { dialogInterface, i -> dialogInterface.cancel() }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(Color.parseColor("#FF0B8B42"))
    }

}