package com.mpnsk.fifteen_game

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mpnsk.fifteen_game.databinding.ActivityGameBinding
import com.mpnsk.fifteen_game.model.Desk
import com.mpnsk.fifteen_game.model.Tail
import java.util.stream.IntStream
import kotlin.streams.toList


class GameActivity : AppCompatActivity() {
    companion object {
        var matrixSize = 4
        var deskSize = 950
    }

    private lateinit var binding: ActivityGameBinding
    private lateinit var images: List<Bitmap>
    private lateinit var bounceAnimation: Animation
    private var tails = mutableListOf<Tail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        images = Desk(MainActivity.imgUri!!, matrixSize, deskSize).getSplitImage(contentResolver)
        bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_anim)
        binding.header.findViewById<ImageButton>(R.id.homeButton).setOnClickListener {
            startActivity(
                Intent(
                    this@GameActivity,
                    MainActivity::class.java
                )
            )
        }
        binding.header.findViewById<ImageButton>(R.id.reloadButton).setOnClickListener {
            restartActivity()
        }
        initGame()
        displayGame()
    }

    private fun initGame() {
        val row = IntStream.range(0, matrixSize).toList().toMutableList()
        val col = IntStream.range(0, matrixSize).toList().toMutableList()
        val tailsNumbersInShuffledOrder = IntArray(matrixSize * matrixSize)
        do {
            var n = 0
            row.shuffle()
            col.shuffle()
            row.forEach { i ->
                col.forEach { j ->
                    tailsNumbersInShuffledOrder[matrixSize * i + j] = n++
                }
            }
        } while (!isSolvable(tailsNumbersInShuffledOrder, row.last()))

        row.forEach { i ->
            col.forEach { j ->
                val tail = createTail(i, j)
                tails.add(tail)
            }
        }
    }

    private fun isSolvable(tailsNumbers: IntArray, emptyTileRow: Int): Boolean {
        val emptyTileRowFromBottom = matrixSize - emptyTileRow
        var invCount = 0
        val size = matrixSize * matrixSize
        for (i in 0 until size - 1) {
            for (j in i + 1 until size) {
                if (tailsNumbers[j] != size - 1 && tailsNumbers[i] != size - 1
                    && tailsNumbers[i] > tailsNumbers[j]
                ) {
                    invCount++
                }
            }
        }

        return if (matrixSize % 2 == 1) {
            invCount % 2 == 0
        } else {
            if (emptyTileRowFromBottom % 2 == 1) {
                invCount % 2 == 0
            } else {
                invCount % 2 == 1
            }
        }
    }

    private fun createTail(i: Int, j: Int): Tail {
        val tailView = ImageView(this)
        return Tail(tailView).init(i, j, tails.size, matrixSize, images, ::moveTail)
    }

    @SuppressLint("SetTextI18n")
    private fun moveTail(currentTail: Tail) {
        val neighbours = currentTail.getNeighbours(binding, matrixSize)
        val targetTail = binding.grid.findViewById<ImageView>(matrixSize * matrixSize)

        if (neighbours.contains(targetTail)) {
            currentTail.swapTails(targetTail)

            val swapCounter = binding.swapCounter
            val counter = swapCounter.text.split(" ")[1].toInt() + 1
            swapCounter.text = "Steps: $counter"
            swapCounter.startAnimation(bounceAnimation)
            isSolved(targetTail)
        }
    }

    private fun isSolved(targetTail: ImageView): Boolean {
        if (targetTail.tag == "${matrixSize - 1}${matrixSize - 1}") {
            for (i in 0..<matrixSize) {
                for (j in 0..<matrixSize - 1) {
                    val requiredIndex = matrixSize * i + j
                    if (tails[requiredIndex].imageView.tag != "$i$j") {
                        return false
                    }
                }
            }
            targetTail.id = 0
            targetTail.setImageBitmap(images.last())
            val toast = Toast.makeText(applicationContext, "Well Done!!!", Toast.LENGTH_SHORT)
            toast.show()
            return true
        }
        return false
    }

    private fun displayGame() {
        tails.forEach {
            binding.grid.addView(it.imageView)
        }
    }

    private fun restartActivity() {
        val intent = Intent(this, GameActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}