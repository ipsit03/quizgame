package com.example.quizgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizgame.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    lateinit var correctscore:TextView
    lateinit var wrongscore:TextView
    lateinit var playagai:Button
    lateinit var exi:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        correctscore=findViewById(R.id.textviewscorecorrect)
        wrongscore=findViewById(R.id.textviewscorewrong)
        playagai=findViewById(R.id.buttonplayagain)
        exi=findViewById(R.id.buttonexit)
        val correctscore2=intent.getIntExtra("correct answer",0)
        val wrongscore2=intent.getIntExtra("wrong answer",0)
        correctscore.text=correctscore2.toString()
        wrongscore.text=wrongscore2.toString()
        playagai.setOnClickListener{
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()

        }
        exi.setOnClickListener{
            finish()
        }

    }
}