package com.zaze.tribe.reader

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.tribe.common.BaseActivity
import com.zaze.tribe.common.util.get
import com.zaze.tribe.common.util.obtainViewModel
import com.zaze.tribe.reader.databinding.ReaderActBinding
import com.zaze.tribe.reader.widget.ReaderMenuManager
import kotlinx.android.synthetic.main.reader_act.*

/**
 * Description : 阅读主页
 *
 * @author : ZAZE
 * @version : 2019-07-20 - 18:50
 */
class ReaderActivity : BaseActivity(), ReaderMenuManager.ReaderMenuListener {
    private lateinit var viewDataBinding: ReaderActBinding
    private val viewModel: ReaderViewModel by viewModels { ReaderViewModelFactory.provideFactory(application) }

    private var readerMenuManager: ReaderMenuManager? = null
    private var catalogAdapter: ReaderCatalogAdapter? = null

    override fun getStatusBarColor(): Int {
        return R.color.black
    }

    companion object {
        fun reader(context: Context, filePath: String) {
            context.startActivity(Intent(context, ReaderActivity::class.java).apply {
                putExtra("filePath", filePath)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.reader_act)
        viewDataBinding.setLifecycleOwner(this)
        viewModel.apply {
            readerBookData.observe(this@ReaderActivity, Observer {
                readerMenuManager?.setTitle(it.book.name)
                readerCatalogBookName.text = it.book.name
                readerView.startReadBook(it)
            })
            curChapterIndex.observe(this@ReaderActivity, Observer {
                readerView.loadChapter(it)
                mainDrawerLayout.closeDrawer(GravityCompat.START)
            })
            catalogBookData.observe(this@ReaderActivity, Observer { list ->
                catalogAdapter?.setDataList(list) ?: let {
                    catalogAdapter = ReaderCatalogAdapter(this@ReaderActivity, list, viewModel)
                    readerCatalog.layoutManager = LinearLayoutManager(this@ReaderActivity)
                    readerCatalog.adapter = catalogAdapter
                }
            })
        }
        readMode()
        readerView.setOnClickListener {
            menuMode()
            readerMenuManager?.show(it)
        }
        readerMenuManager = ReaderMenuManager(this, readerView).apply {
            readerMenuListener = this@ReaderActivity
            mainDrawerLayout.addDrawerListener(this)
        }

        intent.getStringExtra("filePath")?.let {
            viewModel.loadFile(it)
        }
    }

    private fun menuMode() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        supportActionBar?.show()
    }

    private fun readMode() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        supportActionBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        readerMenuManager?.dismiss()
    }


    override fun showCatalog() {
        viewModel.curChapterIndex.get()?.let {
            (readerCatalog.layoutManager as LinearLayoutManager?)?.scrollToPositionWithOffset(it, 0)
            catalogAdapter?.scrollTo(it)
        }
        mainDrawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onDismiss() {
        readMode()
    }

}