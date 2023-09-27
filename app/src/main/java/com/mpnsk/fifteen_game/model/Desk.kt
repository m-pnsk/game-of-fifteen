package com.mpnsk.fifteen_game.model

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri

class Desk(private val imageUri: Uri, private val matrixSize: Int, private val deskSize: Int) {
    fun getSplitImage(contentResolver: ContentResolver): List<Bitmap> {
        val images = mutableListOf<Bitmap>()
        val originalBitmap =
            resizeBitmap(BitmapFactory.decodeStream(contentResolver.openInputStream(this.imageUri)))

        val partSize = deskSize / matrixSize

        for (i in 0 until matrixSize) {
            for (j in 0 until matrixSize) {
                val xCoordinate = j * partSize
                val yCoordinate = i * partSize

                // Create a new bitmap for each part
                val partBitmap = Bitmap.createBitmap(
                    originalBitmap,
                    xCoordinate,
                    yCoordinate,
                    partSize,
                    partSize
                )
                images.add(partBitmap)
            }
        }
        return images
    }

    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postScale(
            deskSize.toFloat() / bitmap.width,
            deskSize.toFloat() / bitmap.height
        )
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}