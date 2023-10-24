package com.mpnsk.fifteen_game.model

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.net.Uri


class Desk(private val matrixSize: Int, private val deskSize: Int) {
    fun getSplitImage(imageUri: Uri, contentResolver: ContentResolver): List<Bitmap> {
        val images = mutableListOf<Bitmap>()
        val originalBitmap =
            resizeBitmap(BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri)))

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

    fun getDefaultImage(): List<Bitmap> {
        val images = mutableListOf<Bitmap>()
        // Image size
        val imageSize = deskSize / matrixSize

        // Border paint
        val borderPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
        }
        // Draw a border
        val borderRect = Rect(0, 0, imageSize, imageSize)

        // Background paint
        val bgPaint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }
        // Draw a background
        val bgRect = Rect(2, 2, imageSize - 2, imageSize - 2)

        // Text paint
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = imageSize.toFloat() / 2
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }
        // Draw the number in the center
        val xPos = imageSize / 2
        val yPos = (imageSize / 2 - (textPaint.descent() + textPaint.ascent()) / 2)

        for (i in 1..matrixSize*matrixSize) {
            val bitmap = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawRect(borderRect, borderPaint)
            canvas.drawRect(bgRect, bgPaint)
            canvas.drawText("$i", xPos.toFloat(), yPos, textPaint)
            images.add(bitmap)
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