package com.example.hangm

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import java.util.*
import android.graphics.Color.*

class MainActivity : AppCompatActivity() {
    lateinit var word: TextView
    lateinit var text: TextView
    lateinit var editText: EditText
    lateinit var button: Button
    lateinit var imageView:ImageView
    private var len: Int = 0
    private var secret: String = ""
    var mask:String = ""
    var winner: Int = 0
    private var lifes:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val array = resources.getStringArray(R.array.dictionary)

        imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageResource(R.drawable.hangman0)

        word = this.findViewById(R.id.word)
        text = this.findViewById(R.id.text)
        editText = this.findViewById(R.id.editText)
        button = this.findViewById(R.id.button)

        val r = Random()
        val randomNumber = r.nextInt(array.size)
        secret = array[randomNumber].toString()
        len = secret.length

        var i = 0
        while(i < len) {
            mask += "?"
            i++
        }
        word.text = mask
        Toast.makeText(applicationContext, "hint: " + array[randomNumber].toString(), Toast.LENGTH_SHORT).show()

        button.setOnClickListener {
            val input = editText.text
            editText.hideKeyboard()

            if (input.isNotEmpty()) {
                checkInput(input)
            } else {
                tryMore()
            }
        }
    }

    private fun checkInput(input: Editable) {
        if(input.length != 1) {
            tryMore()
        }
        else {
            Toast.makeText(applicationContext, "Inputed Sign is $input", Toast.LENGTH_SHORT).show()
            var i = 0
            var j = 0
            while(i < len) {
                if(secret[i].toString() == input.toString()) {
                    var head = mask.dropLast(len - i)
                    var tail = mask.drop(i + 1)
                    mask = "$head$input$tail"
                    winner++
                    j++
                }
                i++
            }
            word.text = mask
            if(j == 0) {
                lifes++
                var imageName = "hangman"+lifes
                val id = resources.getIdentifier(imageName, "drawable", packageName)
                imageView.setImageResource(id)
            }
        }
        editText.setText("")
        checkWinner()
    }


    private fun tryMore() {
        Toast.makeText(applicationContext, "Try one more time!", Toast.LENGTH_SHORT).show()
    }

    private fun checkWinner() {
        if(winner == len) {
            button.isEnabled = false;
            word.setTextColor(BLUE)
            editText.isEnabled = false;
            text.text = "You win :--) !"
        }
        if(lifes == 7) {
            button.isEnabled = false;
            text.text = "You lose :'( !"
            word.text = secret
            word.setTextColor(RED)
        }
    }

    @SuppressLint("ServiceCast")
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}


