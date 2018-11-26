package com.zaze.tribe

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.zaze.tribe.common.BaseActivity
import com.zaze.tribe.common.util.PermissionUtil
import com.zaze.tribe.common.util.replaceFragmentInActivity
import com.zaze.tribe.common.util.setupActionBar
import com.zaze.tribe.databinding.ActivityMainBinding
import com.zaze.tribe.music.LocalMusicFragment
import com.zaze.tribe.music.MusicPlayerRemote
import com.zaze.tribe.music.service.MusicService
import com.zaze.tribe.util.PreferenceUtil
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-09-30 - 0:37
 */
class MainActivity : BaseActivity() {

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var viewDataBinding: ActivityMainBinding
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            ZLog.e(ZTag.TAG_DEBUG, "onServiceDisconnected : $name")
            MusicPlayerRemote.mBinder = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            ZLog.i(ZTag.TAG_DEBUG, "onServiceConnected : $name")
            MusicPlayerRemote.mBinder = service as MusicService.ServiceBinder
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupActionBar(mainToolbar) {
            setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
//            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        initNavigationBar()
        // --------------------------------------------------
        // --------------------------------------------------
        drawerToggle = ActionBarDrawerToggle(
                this, mainDrawerLayout, mainToolbar, R.string.app_name, R.string.app_name
        ).apply {
            syncState()
        }
        mainDrawerLayout.run {
            addDrawerListener(drawerToggle)
        }
        // --------------------------------------------------
        mainLeftNav.run {
            setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.drawer_clear_menu_item ->
                        FileUtil.deleteFile("data/data/$packageName")
                }
                true
            }
        }
        // ------------------------------------------------------
        if (setupPermission()) {
            MusicPlayerRemote.bindService(this, serviceConnection)
        }
    }

    override fun onDestroy() {
        MusicPlayerRemote.unbindService(this, serviceConnection)
        super.onDestroy()
    }

    private fun setupPermission(): Boolean {
//        PermissionUtil.checkAndRequestUserPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, 0)
        return PermissionUtil.checkAndRequestUserPermission(this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WAKE_LOCK
        ), 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val isGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        if (!isGranted) {
            finish()
        } else {
            MusicPlayerRemote.bindService(this, serviceConnection)
        }
    }

    private fun initNavigationBar() {
        mainBottomNav.run {
            setOnNavigationItemReselectedListener {
            }
            setOnNavigationItemSelectedListener { it ->
                PreferenceUtil.saveLastPage(it.itemId)
                findOrCreateViewFragment(it.itemId)
                true
            }
            selectedItemId = PreferenceUtil.getLastPage()
        }
    }

    private fun findOrCreateViewFragment(itemId: Int) =
            supportFragmentManager.findFragmentByTag("$itemId")
                    ?: when (itemId) {
                        R.id.action_home -> TestFragment.newInstance("$itemId")
                        R.id.action_book -> TestFragment.newInstance("$itemId")
                        R.id.action_music -> LocalMusicFragment.newInstance()
                        R.id.action_game -> TestFragment.newInstance("$itemId")
                        else -> TestFragment.newInstance("$$itemId")
                    }.also { it ->
                        replaceFragmentInActivity(it as Fragment, R.id.mainContentFl)
                    }

    // --------------------------------------------------

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onBackPressed() {
        if (mainDrawerLayout.isDrawerOpen(mainLeftNav)) {
            mainDrawerLayout.closeDrawer(mainLeftNav)
        } else {
            moveTaskToBack(false)
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_github -> {
                val intent = Intent(Intent.ACTION_VIEW)
//                val componentName = intent.resolveActivity(packageManager)
//                ZLog.i(ZTag.TAG_DEBUG, "" + componentName.packageName)
                intent.data = Uri.parse("https://github.com/zaze359/test.git")
                startActivity(intent)
                true
            }
            android.R.id.home -> {
                mainDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
