package com.zaze.tribe

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.zaze.common.base.BaseActivity
import com.zaze.tribe.databinding.ActivityMainBinding
import com.zaze.tribe.music.MusicFragment
import com.zaze.tribe.music.MusicViewModel
import com.zaze.tribe.service.PlayerService
import com.zaze.tribe.util.obtainViewModel
import com.zaze.tribe.util.replaceFragmentInActivity
import com.zaze.tribe.util.setImmersion
import com.zaze.tribe.util.setupActionBar
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import com.zaze.utils.permission.PermissionUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun isNeedHead(): Boolean {
        return false
    }

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var viewDataBinding: ActivityMainBinding
    private lateinit var musicViewModel: MusicViewModel

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            ZLog.e(ZTag.TAG_DEBUG, "onServiceDisconnected : $name")
            musicViewModel.mBinder = null
            finish()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            ZLog.i(ZTag.TAG_DEBUG, "onServiceConnected : $name")
            musicViewModel.mBinder = service as PlayerService.ServiceBinder
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        musicViewModel = obtainViewModel(MusicViewModel::class.java)
        viewDataBinding.musicViewModel = musicViewModel
        setupPermission()
        setImmersion()
        setupActionBar(R.id.main_toolbar) {
            setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
//            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        initNavigationBar()
        // --------------------------------------------------
        // --------------------------------------------------
        drawerToggle = ActionBarDrawerToggle(
                this, main_drawer_layout, main_toolbar, R.string.app_name, R.string.app_name
        ).apply {
            syncState()
        }
        main_drawer_layout.run {
            addDrawerListener(drawerToggle)
        }
        // --------------------------------------------------
        main_left_nav.run {
            setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.drawer_clear_menu_item ->
                        FileUtil.deleteFile("data/data/$packageName")
                }
                true
            }
        }
        // --------------------------------------------------
       bindService(Intent(this, PlayerService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

    private fun setupPermission() {
        PermissionUtil.checkAndRequestUserPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val isGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        if (!isGranted) {
            finish()
        }
    }

    private fun initNavigationBar() {
        main_navigation_bar.run {
            setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
                override fun onTabReselected(position: Int) {
                }

                override fun onTabUnselected(position: Int) {
                }

                override fun onTabSelected(position: Int) {
                    findOrCreateViewFragment(position)
                }
            })
            //            setFab(fabHome)
            clearAll()
//            setMode(BottomNavigationBar.MODE_SHIFTING)
//            setBarBackgroundColor(R.color.black)
            setMode(BottomNavigationBar.MODE_FIXED)
            setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
            addItem(createItem(R.mipmap.ic_home_white_24dp, "主页", R.color.orange))
            addItem(createItem(R.mipmap.ic_book_white_24dp, "阅读", R.color.teal))
            addItem(createItem(R.mipmap.ic_music_note_white_24dp, "音乐", R.color.blue))
            addItem(createItem(R.mipmap.ic_tv_white_24dp, "视频", R.color.brown))
            addItem(createItem(R.mipmap.ic_videogame_asset_white_24dp, "游戏", R.color.grey_blue))
            initialise()
            selectTab(0)
        }
    }

    private fun findOrCreateViewFragment(position: Int) =
            supportFragmentManager.findFragmentByTag("$position")
                    ?: when (position) {
                        0 -> TestFragment.newInstance("$position")
                        1 -> TestFragment.newInstance("$position")
                        2 -> MusicFragment.newInstance().apply { setViewModel(musicViewModel) }
                        3 -> TestFragment.newInstance("$position")
                        4 -> TestFragment.newInstance("$position")
                        else -> TestFragment.newInstance("$position")
                    }.also {
                        replaceFragmentInActivity(it as Fragment, R.id.main_content_fl)
                    }


    private fun createItem(imgRes: Int, title: String, activeColor: Int) =
            BottomNavigationItem(imgRes, title).setActiveColorResource(activeColor).setInActiveColorResource(R.color.grey)
    // --------------------------------------------------

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onBackPressed() {
        if (main_drawer_layout.isDrawerOpen(main_left_nav)) {
            main_drawer_layout.closeDrawer(main_left_nav)
        } else {
            moveTaskToBack(false)
//            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_github -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://github.com/zaze359/test.git")
                startActivity(intent)
                true
            }
            android.R.id.home -> {
                main_drawer_layout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
