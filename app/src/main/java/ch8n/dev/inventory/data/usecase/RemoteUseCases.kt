package ch8n.dev.inventory.data.usecase

import android.net.Uri
import android.util.Log
import ch8n.dev.inventory.data.database.InMemoryDB
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventorySupplier


class UploadItemImageToServer(
) {
    fun execute(file: Uri) {
        Log.d("ch8n", "ch8n ---> file $file")
    }
}

