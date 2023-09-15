package com.zaze.tribe

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.zaze.tribe.common.base.AbsActivity
import com.zaze.tribe.common.util.addFragment
import com.zaze.tribe.common.util.hideFragment
import com.zaze.tribe.common.util.setupActionBar
import com.zaze.tribe.common.util.showFragment
import com.zaze.tribe.databinding.ActivityMainBinding
import com.zaze.tribe.debug.DebugReceiver
import com.zaze.tribe.debug.TestFragment
import com.zaze.tribe.music.LocalMusicFragment
import com.zaze.tribe.music.MusicPlayerRemote
import com.zaze.tribe.music.service.MusicService
import com.zaze.tribe.reader.bookshelf.BookshelfFragment
import com.zaze.tribe.util.PreferenceUtil
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-09-30 - 0:37
 */
class MainActivity : AbsActivity() {
    private val debugReceiver = DebugReceiver()
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar(binding.appbarLayout.toolbar) {
            setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
//            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        initNavigationBar()
        // --------------------------------------------------
        // --------------------------------------------------
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.mainDrawerLayout,
            binding.appbarLayout.toolbar,
            R.string.app_name,
            R.string.app_name
        ).apply {
            syncState()
        }
        binding.mainDrawerLayout.run {
            addDrawerListener(drawerToggle)
        }
        // --------------------------------------------------
        binding.mainLeftNav.run {
            setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.drawer_clear_menu_item ->
                        FileUtil.deleteFile("data/data/$packageName")
                }
                true
            }
        }
        // ------------------------------------------------------
        MusicPlayerRemote.bindService(this, serviceConnection)
        registerReceiver(debugReceiver, IntentFilter("com.zaze.test.action"))
    }

    override fun onDestroy() {
        MusicPlayerRemote.unbindService(this, serviceConnection)
        unregisterReceiver(debugReceiver)
        super.onDestroy()
    }

    override fun getPermissionsToRequest(): Array<String> {
        return arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WAKE_LOCK
        )
    }

    override fun afterPermissionGranted() {
        super.afterPermissionGranted()
//        MusicPlayerRemote.bindService(this, serviceConnection)
//        finish()
    }

    private fun initNavigationBar() {
        binding.mainBottomNav.run {
            setOnItemReselectedListener {

            }
            setOnItemSelectedListener {
                PreferenceUtil.saveLastPage(it.itemId)
                findOrCreateViewFragment(selectedItemId, it.itemId)
                true
            }
            selectedItemId = PreferenceUtil.getLastPage()
        }
    }

    private fun findOrCreateViewFragment(preId: Int, itemId: Int) {
        (supportFragmentManager.findFragmentByTag("$itemId")
            ?: when (itemId) {
                R.id.action_home -> TestFragment.newInstance("$itemId")
                R.id.action_reader -> BookshelfFragment.newInstance()
                R.id.action_music -> LocalMusicFragment.newInstance()
                R.id.action_game -> TestFragment.newInstance("$itemId")
                else -> TestFragment.newInstance("$$itemId")
            }).also {
            if (!it.isAdded) {
                addFragment(R.id.mainContentFl, it, "$itemId")
            }
            showFragment(it)
//            replaceFragment(R.id.mainContentFl, it, "$itemId")
        }
        if (preId != itemId) {
            hideFragment(supportFragmentManager.findFragmentByTag("$preId"))
        }
    }


    // --------------------------------------------------

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onBackPressed() {
        if (binding.mainDrawerLayout.isDrawerOpen(binding.mainLeftNav)) {
            binding.mainDrawerLayout.closeDrawer(binding.mainLeftNav)
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
                binding.mainDrawerLayout.openDrawer(GravityCompat.START)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
