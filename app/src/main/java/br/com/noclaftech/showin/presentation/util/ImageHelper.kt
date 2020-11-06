package br.com.noclaftech.showin.presentation.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import androidx.annotation.NonNull

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import br.com.noclaftech.showin.R
import id.zelory.compressor.Compressor


import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.text.DecimalFormat
import java.text.SimpleDateFormat

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

class ImageHelper(private val activity: Activity) {

    var actualImage: File? = null
    private var compressedImage: File? = null
    private var image64: String? = null
    private var selectedImage: Bitmap? = null
    private var callback: Callback? = null
    private var photoURI: Uri? = null
    private val disposables = CompositeDisposable()

    fun getImageFromGalery(allowMultiple: Boolean = false) {
        val permissionReadExternalStorage = ActivityCompat.checkSelfPermission(activity,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionWriteExternalStorage = ActivityCompat.checkSelfPermission(activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionReadExternalStorage != PackageManager.PERMISSION_GRANTED ||
            permissionWriteExternalStorage != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_CODE_GALERY)
            return
        }

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
        //todo refactor to framework and implement allow multiple
        activity.startActivityForResult(Intent.createChooser(intent, "Escolha uma opção:"),
            GALLERY_REQUEST_CODE)
    }

    fun onRequestPermissionsResult(requestCode: Int,
                                   @NonNull grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE_CAMERA -> if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImageFromCamera()
            }
            REQUEST_PERMISSION_CODE_GALERY -> if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImageFromGalery()
            }
        }
    }

    fun getImageFromCamera() {
        val permissionCamera = ContextCompat.checkSelfPermission(activity,
            Manifest.permission.CAMERA)
        val permissionReadExternalStorage = ActivityCompat.checkSelfPermission(activity,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionWriteExternalStorage = ActivityCompat.checkSelfPermission(activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionCamera != PackageManager.PERMISSION_GRANTED ||
            permissionReadExternalStorage != PackageManager.PERMISSION_GRANTED ||
            permissionWriteExternalStorage != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_CODE_CAMERA)
            return
        }
        val packageManager = activity.packageManager
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null

            try {
                photoFile = ImageFileHandler.create(activity)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (photoFile != null) {
                photoURI = if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                    Uri.fromFile(photoFile)
                else
                    FileProvider.getUriForFile(activity,
                        activity.packageName + ".provider",
                        photoFile)

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                activity.startActivityForResult(takePictureIntent, PHOTO_REQUEST_CODE)
            } else {
                callback!!.onError()
            }
        }
    }

    fun handleGaleryResult(requestCode: Int, resultCode: Int, data: Intent?, callback: Callback) {
        this.callback = callback
        if (data != null && requestCode == GALLERY_REQUEST_CODE) {
            when(resultCode){
                AppCompatActivity.RESULT_OK -> try {
                    actualImage = FileUtil.from(activity, data.data)
                    compressImage(activity)
//                    selectedImage = BitmapFactory.decodeFile(actualImage!!.absolutePath)
//                    image64 = getFormattedBase64(selectedImage)
//                    callback.onImageCompressed(image64, selectedImage)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                AppCompatActivity.RESULT_CANCELED -> callback.onCanceled()
                else -> callback.onError()
            }
        }
    }

    fun handleResult(requestCode: Int, resultCode: Int, data: Intent?, callback: Callback){
        if(requestCode == GALLERY_REQUEST_CODE)
            handleGaleryResult(requestCode, resultCode, data, callback)
        else if(requestCode == PHOTO_REQUEST_CODE)
            handlePhotoResult(requestCode, resultCode, callback)
    }

    //Result Criado para usar o framework api 'com.theartofdev.edmodo:android-image-cropper:2.8.0'
    fun handleResult(requestCode: Int, resultCode: Int, data: Intent?) : Uri?{
        if (requestCode == GALLERY_REQUEST_CODE){
            if (data != null && requestCode == GALLERY_REQUEST_CODE) {
                when(resultCode){
                    AppCompatActivity.RESULT_OK -> try {
                        actualImage = FileUtil.from(activity, data.data)
                        return Uri.fromFile(actualImage)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        else if (requestCode == PHOTO_REQUEST_CODE){
            if (photoURI != null && requestCode == PHOTO_REQUEST_CODE) {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> try {
                        actualImage = FileUtil.from(activity, photoURI)
                        return Uri.fromFile(actualImage)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        return null
    }

    fun handleResultForConvertBase64(uri: Uri,callback: Callback){
        this.callback = callback
        try {
            actualImage = FileUtil.from(activity, uri)
            selectedImage = BitmapFactory.decodeFile(actualImage!!.absolutePath)
            image64 = getFormattedBase64(selectedImage)
            callback.onImageCompressed(image64, selectedImage)
        }catch (e : Throwable){
            callback.onError()
        }
    }

    fun handlePhotoResult(requestCode: Int, resultCode: Int, callback: Callback) {
        this.callback = callback
        if (photoURI != null && requestCode == PHOTO_REQUEST_CODE) {
            when (resultCode) {
                AppCompatActivity.RESULT_OK -> try {
                    actualImage = FileUtil.from(activity, photoURI)
                    compressImage(activity)
//                    selectedImage = BitmapFactory.decodeFile(actualImage!!.absolutePath)
//                    image64 = getFormattedBase64(selectedImage)
//                    callback.onImageCompressed(image64, selectedImage)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                AppCompatActivity.RESULT_CANCELED -> callback.onCanceled()
                else -> callback.onError()
            }
        }
    }

    fun showDialogGalleryOrCamera(){
        AlertHelper.alertGenericTwoButtons(activity,
            activity.getString(R.string.add_image),
            activity.getString(R.string.chose_origin),
            activity.getString(R.string.camera),
            Color.parseColor("#4089e7"),
            DialogInterface.OnClickListener { _, _ -> getImageFromCamera() },
            activity.getString(R.string.gallery),
            DialogInterface.OnClickListener { _, _ -> getImageFromGalery() })
    }

    private fun encodeToBase64(image: Bitmap): String {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 40, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    @Throws(IOException::class)
    private fun getImageFromLink(image: String): String {
        val url = URL(image)
        val inputStream = BufferedInputStream(url.openStream())
        val out = ByteArrayOutputStream()
        val buf = ByteArray(1024)
        var n = inputStream.read(buf)
        while (-1 != n) {
            out.write(buf, 0, n)
            n = inputStream.read(buf)
        }
        out.close()
        inputStream.close()
        val response = out.toByteArray()

        return "data:image/png;base64," + Base64.encodeToString(response, Base64.DEFAULT)
    }


    fun getFormattedBase64(bMap: Bitmap?): String {
        return "data:image/png;base64," + encodeToBase64(bMap!!)
    }

    private fun compressImage(context: Context) {
        disposables.add(
            Compressor(context)
                .setQuality(30)
                .compressToFileAsFlowable(actualImage!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ file ->
                    compressedImage = file
                    selectedImage = BitmapFactory.decodeFile(compressedImage!!.absolutePath)
                    image64 = getFormattedBase64(selectedImage)
                    callback!!.onImageCompressed(image64, selectedImage)
                }, { throwable ->
                    callback!!.onError()
                    throwable.printStackTrace()
                })
        )
    }

    fun onStop(){
        disposables.clear()
    }

    fun getReadableFileSize(size: Long): String {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#")
            .format(size / 1024.0.pow(digitGroups.toDouble())) +
                " " +
                units[digitGroups]
    }

    interface Callback {
        fun onImageCompressed(image64: String?, imageBitmap: Bitmap?)
        fun onCanceled()
        fun onError()
    }

    object FileUtil {
        private const val EOF = -1
        private const val DEFAULT_BUFFER_SIZE = 1024 * 4

        @Throws(IOException::class)
        fun from(context: Context, uri: Uri?): File {
            val inputStream = context.contentResolver.openInputStream(uri!!)
            val fileName = getFileName(context, uri)
            val splitName = splitFileName(fileName)
            var tempFile = File.createTempFile(splitName[0], splitName[1])
            tempFile = rename(tempFile, fileName)
            tempFile.deleteOnExit()
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(tempFile)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            if (inputStream != null) {
                copy(inputStream, out)
                inputStream.close()
            }

            out?.close()
            return tempFile
        }

        private fun splitFileName(fileName: String): Array<String> {
            var name = fileName
            var extension = ""
            val i = fileName.lastIndexOf(".")
            if (i != -1) {
                name = fileName.substring(0, i)
                extension = fileName.substring(i)
            }

            return arrayOf(name, extension)
        }

        private fun getFileName(context: Context, uri: Uri): String {
            var result: String? = null
            if (uri.scheme == "content") {
                val cursor = context.contentResolver.query(uri,
                    null,
                    null,
                    null,
                    null)
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        result = cursor.getString(
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    cursor?.close()
                }
            }
            if (result == null) {
                result = uri.path
                val cut = result!!.lastIndexOf(File.separator)
                if (cut != -1) {
                    result = result.substring(cut + 1)
                }
            }
            return result
        }

        private fun rename(file: File, newName: String): File {
            val newFile = File(file.parent, newName)
            if (newFile != file) {
                if (newFile.exists() && newFile.delete()) {
                    Log.d("FileUtil", "Delete old $newName file")
                }
                if (file.renameTo(newFile)) {
                    Log.d("FileUtil", "Rename file to $newName")
                }
            }
            return newFile
        }

        @Throws(IOException::class)
        private fun copy(input: InputStream, output: OutputStream?) {
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var n = input.read(buffer)
            while (EOF != n) {
                output!!.write(buffer, 0, n)
                n = input.read(buffer)
            }
        }
    }

    private object ImageFileHandler {
        @Throws(IOException::class)
        fun create(context: Context): File {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val imageFileName = "ANDROID_" + timeStamp + "_"
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

            return File.createTempFile(imageFileName, /* prefix */
                ".jpeg", /* suffix */
                storageDir      /* directory */)

        }
    }

    companion object {
        const val GALLERY_REQUEST_CODE = 6565
        const val PHOTO_REQUEST_CODE = 6564
        const val REQUEST_PERMISSION_CODE_CAMERA = 56
        const val REQUEST_PERMISSION_CODE_GALERY = 57
    }

}
