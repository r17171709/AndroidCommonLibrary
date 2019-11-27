package com.renyu.androidcommonlibrary.activity

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.SizeUtils
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.commonutils.mainThread
import com.renyu.commonlibrary.permission.annotation.NeedPermission
import com.renyu.commonlibrary.permission.annotation.PermissionDenied
import kotlinx.android.synthetic.main.activity_fileq.*

class FileQActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fileq)

        nextDo()
    }

    private fun writeImage() {
        val external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "bg_splash_1.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val insertUri = contentResolver.insert(external, contentValues)

        if (insertUri != null) {
            contentResolver.openOutputStream(insertUri)!!.use { outputStream ->
                resources.assets.open("bg_splash_1.jpg").use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(insertUri, contentValues, null, null)
        }
    }

    private fun readAllImage() {
        val external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(external, null, null, null, null)
        cursor?.use {
            while (cursor.moveToNext()) {
                val uri = ContentUris.withAppendedId(
                    external,
                    it.getLong(it.getColumnIndex(MediaStore.Images.Media._ID))
                )
                mainThread {
                    loadFresco(uri, 720f, 1080f, iv_fileq)
                }
            }
        }
    }

    private fun loadFresco(
        uri: Uri,
        width: Float,
        height: Float,
        simpleDraweeView: SimpleDraweeView
    ) {
        val request =
            ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(ResizeOptions(SizeUtils.dp2px(width), SizeUtils.dp2px(height)))
                .build()
        val draweeController: DraweeController = Fresco.newDraweeControllerBuilder()
            .setImageRequest(request).setAutoPlayAnimations(true).build()
        simpleDraweeView.controller = draweeController
    }

    @NeedPermission(
        permissions = [Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE],
        deniedDesp = "请授予存储卡读取权限"
    )
    fun nextDo() {
//        writeImage()
        readAllImage()
    }

    @PermissionDenied(permissions = [Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE])
    fun permissionDenied() {

    }
}