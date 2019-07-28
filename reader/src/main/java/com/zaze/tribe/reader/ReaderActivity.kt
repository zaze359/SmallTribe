package com.zaze.tribe.reader

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.zaze.tribe.common.BaseActivity
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
class ReaderActivity : BaseActivity() {
    private lateinit var viewDataBinding: ReaderActBinding
    private lateinit var viewModel: ReaderViewModel
    private var readerMenuManager: ReaderMenuManager? = null

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
        viewModel = obtainViewModel(ReaderViewModel::class.java).apply {
            readerBookData.observe(this@ReaderActivity, Observer {
                readerView.startReadBook(it)
            })
        }
        readerView.setOnClickListener {
            readerMenuManager?.show(it)
        }
        readerMenuManager = ReaderMenuManager(this, readerView)
        intent.getStringExtra("filePath")?.let {
            viewModel.loadFile(it)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        readerMenuManager?.dismiss()
    }
}