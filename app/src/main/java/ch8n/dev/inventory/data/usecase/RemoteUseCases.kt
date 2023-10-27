package ch8n.dev.inventory.data.usecase

import android.net.Uri
import android.util.Log


class UploadItemImageToServer {
    fun execute(file: Uri) {
        Log.d("ch8n", "ch8n ---> file $file")
    }
}

