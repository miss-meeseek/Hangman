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
    private var lives:Int = 0
    private var livesMax:Int = 7

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageResource(R.drawable.hangman0)

        word = this.findViewById(R.id.word)
        text = this.findViewById(R.id.text)
        editText = this.findViewById(R.id.editText)
        button = this.findViewById(R.id.button)

        //Initiate dictionary
        val array = resources.getStringArray(R.array.dictionary)
        //Choose word from dictionary
        val r = Random()
        val randomNumber = r.nextInt(array.size)
        secret = array[randomNumber].toString()
        len = secret.length

        //Create word encrypted with "????"
        var i = 0
        while(i < len) {
            mask += "?"
            i++
        }
        word.text = mask
        Toast.makeText(applicationContext, "hint: " + array[randomNumber].toString(), Toast.LENGTH_SHORT).show()

        //Listener for button "TRY"
        button.setOnClickListener {
            val input = editText.text
            editText.hideKeyboard()

            if (input.isNotEmpty()) {
                //Check if input was correct
                checkInput(input)
            } else {
                //Show Toast
                tryMore()
            }
        }
    }

    //Function check if choosed word contains sign from input
    private fun checkInput(input: Editable) {
        if(input.length != 1) { //Input is incorrect if it contains more or less than one sign
            tryMore()
        }
        else {
            var i = 0
            var contains = 0
            while(i < len) {
                if(secret[i].toString() == input.toString()) {
                    val head = mask.dropLast(len - i)
                    val tail = mask.drop(i + 1)
                    mask = head + input + tail
                    winner++
                    contains++
                }
                i++
            }
            word.text = mask
            if(contains == 0) {
                lives++
                val imageName = "hangman$lives"
                val id = resources.getIdentifier(imageName, "drawable", packageName)
                imageView.setImageResource(id)
            }
            Toast.makeText(applicationContext, "Inputted Sign is $input", Toast.LENGTH_SHORT).show()
        }
        editText.setText("")
        checkWinner()
    }


    //Function shows Toast for user if input was empty or incorrect
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
        if(lives == livesMax) {
            button.isEnabled = false;
            text.text = "You lose :'( !"
            word.text = secret
            word.setTextColor(RED)
        }
    }

    // Function hides the keyboard
    @SuppressLint("ServiceCast")
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}


