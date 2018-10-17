package com.zaze.tribe

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.zaze.tribe.base.BaseActivity
import com.zaze.tribe.databinding.ActivityMainBinding
import com.zaze.tribe.music.MainViewModel
import com.zaze.tribe.music.MusicListFragment
import com.zaze.tribe.util.*
import com.zaze.utils.FileUtil
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
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = obtainViewModel(MainViewModel::class.java)
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
        // ------------------------------------------------------
        viewModel.bindService()
    }

    override fun onDestroy() {
        viewModel.unbindService()
        super.onDestroy()
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
        main_navigation_view.run {
            setOnNavigationItemSelectedListener { it ->
                findOrCreateViewFragment(it)
                true
            }
        }
    }

//    private fun initNavigationBar() {
//        main_navigation_view.run {
//            setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
//                override fun onTabReselected(position: Int) {
//                }
//
//                override fun onTabUnselected(position: Int) {
//                }
//
//                override fun onTabSelected(position: Int) {
//                    findOrCreateViewFragment(position)
//                }
//            })
//            //            setFab(fabHome)
//            clearAll()
////            setMode(BottomNavigationBar.MODE_SHIFTING)
////            setBarBackgroundColor(R.color.black)
//            setMode(BottomNavigationBar.MODE_FIXED)
//            setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
//            addItem(createItem(R.mipmap.ic_home_white_24dp, "主页", R.color.orange))
//            addItem(createItem(R.mipmap.ic_book_white_24dp, "阅读", R.color.teal))
//            addItem(createItem(R.mipmap.ic_music_note_white_24dp, "音乐", R.color.blue))
//            addItem(createItem(R.mipmap.ic_tv_white_24dp, "视频", R.color.brown))
//            addItem(createItem(R.mipmap.ic_videogame_asset_white_24dp, "游戏", R.color.grey_blue))
//            initialise()
//            selectTab(0)
//        }
//    }

    private fun findOrCreateViewFragment(item: MenuItem) =
            supportFragmentManager.findFragmentByTag("${item.itemId}")
                    ?: when (item.itemId) {
                        R.id.action_home -> TestFragment.newInstance("${item.title}")
                        R.id.action_book -> TestFragment.newInstance("${item.title}")
                        R.id.action_music -> MusicListFragment.newInstance()
                        R.id.action_game -> TestFragment.newInstance("${item.title}")
                        else -> TestFragment.newInstance("${item.title}")
                    }.also {
                        replaceFragmentInActivity(it as Fragment, R.id.main_content_fl)
                    }

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
