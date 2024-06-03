package net.onest.time.navigation.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.onest.time.R

class NavigationActivity : AppCompatActivity() {
    var bottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(R.layout.activity_navigation)

        bottomNavigation = findViewById(R.id.bottom_navigation)

        //获取navController
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        //通过setupWithNavController将底部导航和导航控制器进行绑定
        NavigationUI.setupWithNavController(bottomNavigation!!, navController)

        val taskName = intent?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        taskName?.run {
            val bundle = Bundle()
            bundle.putString("taskName", this.toString())

            navController.navigate(R.id.todo_fragment, bundle)
        }
    }
}