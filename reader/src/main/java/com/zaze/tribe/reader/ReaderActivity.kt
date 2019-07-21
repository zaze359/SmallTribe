package com.zaze.tribe.reader

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.zaze.tribe.common.BaseActivity
import com.zaze.tribe.common.util.obtainViewModel
import com.zaze.tribe.reader.databinding.ReaderActBinding

/**
 * Description : 阅读主页
 *
 * @author : ZAZE
 * @version : 2019-07-20 - 18:50
 */
class ReaderActivity : BaseActivity() {
    private lateinit var viewDataBinding: ReaderActBinding
    private lateinit var viewModel: ReaderViewModel

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
        viewModel = obtainViewModel(ReaderViewModel::class.java).apply {

        }
        intent.getStringExtra("filePath")?.let{
            viewModel.loadFile(it)
        }
    }

}