package com.mpnsk.fifteen_game.model

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.gridlayout.widget.GridLayout
import com.mpnsk.fifteen_game.R
import com.mpnsk.fifteen_game.databinding.ActivityGameBinding

class Tail(val imageView: ImageView) {

    fun init(
        i: Int,
        j: Int,
        id: Int,
        matrixSize: Int,
        image: Bitmap,
        moveTail: (currentTail: Tail) -> Unit
    ): Tail {
        val tail = this.imageView
        tail.id = id + 1
        tail.tag = "$i$j"
        val layoutParams = GridLayout.LayoutParams()
        layoutParams.rowSpec = GridLayout.spec(i)
        layoutParams.columnSpec = GridLayout.spec(j)
        tail.layoutParams = layoutParams

        tail.setOnClickListener {
            moveTail(this)
        }
        tail.setImageBitmap(image)
        if (tail.id == matrixSize * matrixSize) {
            tail.setImageBitmap(Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888))
        }
        return this
    }

    fun getNeighbours(
        binding: ActivityGameBinding,
        matrixSize: Int
    ): MutableList<ImageView> {
        val neighbours = mutableListOf<ImageView>()
        val i = imageView.tag.toString()[0] - '0'
        val j = imageView.tag.toString()[1] - '0'
        val validRange = 0..<matrixSize

        for (k in 1..4) {
            var newI = i
            var newJ = j
            when (k) {
                1 -> newI--
                2 -> newJ--
                3 -> newI++
                4 -> newJ++
            }
            if (validRange.contains(newI) && validRange.contains(newJ)) {
                neighbours.add(binding.grid.findViewWithTag("$newI$newJ"))
            }
        }
        return neighbours
    }

    fun swapTails(targetTail: ImageView) {
        val tempTag = this.imageView.tag
        this.imageView.tag = targetTail.tag
        targetTail.tag = tempTag

        val layoutParams = this.imageView.layoutParams
        this.imageView.layoutParams = targetTail.layoutParams
        targetTail.layoutParams = layoutParams
    }
}