package com.renyu.androidcommonlibrary.activity

import android.content.Intent
import android.graphics.Color
import android.text.TextUtils
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.baseact.BaseActivity
import kotlinx.android.synthetic.main.activity_fileprovider.*
import java.io.File

class FileProviderActivity : BaseActivity() {
    override fun initParams() {
        btn_openbyother.setOnClickListener {
            openByOther()
        }
    }

    override fun initViews() = R.layout.activity_fileprovider

    override fun loadData() {
        val count = handlerIntent()
    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0

    private fun openByOther() {
        val sourceFile = intent.getStringExtra("file")
        sourceFile?.also {
            val extension = it.substring(it.lastIndexOf(".") + 1)
            val mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(extension)
            val intent = Intent()
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.action = Intent.ACTION_VIEW
            val file = File(it)
            val uri = FileProvider.getUriForFile(this, "com.renyu.androidcommonlibrary.fileprovider", file)
            intent.setDataAndType(uri, mimeType)
            startActivity(intent)
        }
    }

    /**
     * 读取传入的文件
     */
    private fun handlerIntent(): Int {
        if (intent.action == Intent.ACTION_VIEW) {
            val uri = intent.data
            if (uri != null) {
                val scheme = uri.scheme
                if (!TextUtils.isEmpty(scheme)) {
                    if (scheme!!.indexOf("content") != -1) {
                        val inputStream = contentResolver.openInputStream(uri)
                        val content = ByteArray(inputStream!!.available())
                        inputStream.read(content)
                        return content.size
                    }
                }
            }
        }
        return -1
    }
}