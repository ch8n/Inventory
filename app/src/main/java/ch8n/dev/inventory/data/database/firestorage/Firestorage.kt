package ch8n.dev.inventory.data.database.firestorage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.net.toUri
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Precision
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class RemoteUploadDAO {

    private val storage by lazy { Firebase.storage }
    private val imageStoreReference = storage.reference.child("images")

    private val TAG = "ch8n"
    suspend fun getImageUrl(context: Context, imageUri: Uri?): String {
        Log.d(TAG, "getImageUrl: imageUri $imageUri")
        imageUri ?: return ""

        return try {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(imageUri)
                .size(680)
                .precision(Precision.INEXACT)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()
            val result = (loader.execute(request) as SuccessResult).drawable
            val bitmap = (result as BitmapDrawable).bitmap
            Log.d(TAG, "getImageUrl: Got file bitmap")
            val fileName = "${UUID.randomUUID()}.webp"
            val webpFile = File(context.cacheDir, fileName)
            FileOutputStream(webpFile).use { out ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    bitmap.compress(
                        Bitmap.CompressFormat.WEBP_LOSSY, // or WEBP_LOSSY
                        50, out
                    )
                } else {
                    bitmap.compress(Bitmap.CompressFormat.WEBP, 50, out)
                }
            }
            Log.d(TAG, "getImageUrl: Firebase put file $webpFile started")
            val imageStorageRef = imageStoreReference.child(fileName)
            imageStorageRef.putFile(webpFile.toUri()).await()
            Log.d(TAG, "getImageUrl: Firebase put file $webpFile complete")
            val fireStorageURI = imageStorageRef.downloadUrl.await()
            Log.d(TAG, "getImageUrl: Firebase uploaded fireStorageURI $fireStorageURI")
            fireStorageURI.toString()
        } catch (error: Exception) {
            Log.d(TAG, "getImageUrl: Firebase uploaded error $error")
            ""
        }
    }

    suspend fun deleteImageUrl(url:String) {
        Log.d(TAG, "getImageUrl: imageUrl=$url")
        url.ifEmpty { return }
        try {
            val imageStorageRef = storage.getReferenceFromUrl(url)
            imageStorageRef.delete().await()
            Log.d(TAG, "deleted imageUrl=$url")
        } catch (e:Exception){
            Log.d(TAG, "faild to delete imageUrl=$url")
        }
    }
}