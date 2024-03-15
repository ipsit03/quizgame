package com.example.quizgame

import android.content.Intent
import android.content.IntentSender.OnFinished
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizgame.databinding.ActivityQuizBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuizActivity : AppCompatActivity() {
    lateinit var quizBinding: ActivityQuizBinding
    val database=FirebaseDatabase.getInstance()
    val databaseReference=database.reference.child("questions")
    var question=""
    var answera=""
    var answerb=""
    var answerc=""
    var answerd=""
    var correctanswer=""
    var questioncount=0
    var questionnumber=1
    var useranswer=""
    var usercorrect=0
    var userwrong=0
    lateinit var timer:CountDownTimer
    private val totaltime=60000L
    var timerContinue= false
    var leftTime=totaltime
    val auth= FirebaseAuth.getInstance()
    val user=auth.currentUser
    val scoreRef=database.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding=ActivityQuizBinding.inflate(layoutInflater)
        val view=quizBinding.root
        setContentView(view)
        gameLogic()
        quizBinding.buttonfinish.setOnClickListener{
            sendScore()

        }
        quizBinding.buttonnext.setOnClickListener{
            resetTimer()
            gameLogic()

        }
        quizBinding.textviewa.setOnClickListener{
            pauseTimer()
            useranswer="a"
            if(correctanswer==useranswer){
                quizBinding.textviewa.setBackgroundColor(Color.GREEN)
                usercorrect++
                quizBinding.textviewcorrect.text=usercorrect.toString()
            }
            else{
                quizBinding.textviewa.setBackgroundColor(Color.RED)
                userwrong++
                quizBinding.textviewwron.text=userwrong.toString()
                findAnswer()
            }
            disableClickableOfOptions()

        }
        quizBinding.textviewb.setOnClickListener{
            pauseTimer()
            useranswer="b"
            if(correctanswer==useranswer){
                quizBinding.textviewb.setBackgroundColor(Color.GREEN)
                usercorrect++
                quizBinding.textviewcorrect.text=usercorrect.toString()
            }
            else{
                quizBinding.textviewb.setBackgroundColor(Color.RED)
                userwrong++
                quizBinding.textviewwron.text=userwrong.toString()
                findAnswer()
            }
            disableClickableOfOptions()

        }
        quizBinding.textviewc.setOnClickListener{
            pauseTimer()
            useranswer="c"
            if(correctanswer==useranswer){
                quizBinding.textviewc.setBackgroundColor(Color.GREEN)
                usercorrect++
                quizBinding.textviewcorrect.text=usercorrect.toString()
            }
            else{
                quizBinding.textviewc.setBackgroundColor(Color.RED)
                userwrong++
                quizBinding.textviewwron.text=userwrong.toString()
                findAnswer()
            }
            disableClickableOfOptions()

        }
        quizBinding.textviewd.setOnClickListener{
            pauseTimer()
            useranswer="d"
            if(correctanswer==useranswer){
                quizBinding.textviewd.setBackgroundColor(Color.GREEN)
                usercorrect++
                quizBinding.textviewcorrect.text=usercorrect.toString()
            }
            else{
                quizBinding.textviewd.setBackgroundColor(Color.RED)
                userwrong++
                quizBinding.textviewwron.text=userwrong.toString()
                findAnswer()
            }
            
        }
        disableClickableOfOptions()
    }
    private fun gameLogic(){
        restoreOptions()
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questioncount=snapshot.childrenCount.toInt()
                if(questionnumber<=questioncount) {
                    question = snapshot.child(questionnumber.toString()).child("q").value.toString()
                    answera = snapshot.child(questionnumber.toString()).child("a").value.toString()
                    answerb = snapshot.child(questionnumber.toString()).child("b").value.toString()
                    answerc = snapshot.child(questionnumber.toString()).child("c").value.toString()
                    answerd = snapshot.child(questionnumber.toString()).child("d").value.toString()
                    correctanswer = snapshot.child(questionnumber.toString()).child("answer").value.toString()
                    quizBinding.textviewquestion.text = question
                    quizBinding.textviewa.text = answera
                    quizBinding.textviewb.text = answerb
                    quizBinding.textviewc.text = answerc
                    quizBinding.textviewd.text = answerd
                    quizBinding.progressbarquiz.visibility=View.INVISIBLE
                    quizBinding.linearlayoutinfo.visibility=View.VISIBLE
                    quizBinding.linearlayoutquestion.visibility=View.VISIBLE
                    quizBinding.linearlayoutbutton.visibility=View.VISIBLE
                    startTimer()
                }
                else{
                    val dialogMessage=AlertDialog.Builder(this@QuizActivity)
                    dialogMessage.setTitle("quiz game")
                    dialogMessage.setMessage("Congratulations you have answered all the questions do you want to see the results")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("see result"){dialogWindow,position ->
                        val intent=Intent(this@QuizActivity,ResultActivity::class.java)
                        intent.putExtra("correct answer",usercorrect)
                        intent.putExtra("wrong answer",userwrong)
                        startActivity(intent)
                    }
                    dialogMessage.setNegativeButton("play again"){dialogWindow, position ->
                        val intent=Intent(this@QuizActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialogMessage.create().show()
                }
                questionnumber++
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }

        })
    }
    fun findAnswer(){
        when(correctanswer){
            "a"->quizBinding.textviewa.setBackgroundColor(Color.GREEN)
            "b"->quizBinding.textviewb.setBackgroundColor(Color.GREEN)
            "c"->quizBinding.textviewc.setBackgroundColor(Color.GREEN)
            "d"->quizBinding.textviewd.setBackgroundColor(Color.GREEN)
        }
    }
    fun disableClickableOfOptions(){
        quizBinding.textviewa.isClickable=false
        quizBinding.textviewb.isClickable=false
        quizBinding.textviewc.isClickable=false
        quizBinding.textviewd.isClickable=false
    }
    fun restoreOptions(){
        quizBinding.textviewa.setBackgroundColor(Color.WHITE)
        quizBinding.textviewb.setBackgroundColor(Color.WHITE)
        quizBinding.textviewc.setBackgroundColor(Color.WHITE)
        quizBinding.textviewd.setBackgroundColor(Color.WHITE)
        quizBinding.textviewa.isClickable=true
        quizBinding.textviewb.isClickable=true
        quizBinding.textviewc.isClickable=true
        quizBinding.textviewd.isClickable=true
    }
    private fun startTimer(){
        timer= object : CountDownTimer(leftTime,1000){
            override fun onTick(millisUntilFinish: Long) {
                leftTime=millisUntilFinish
                updateCountDownText()
            }

            override fun onFinish() {
                disableClickableOfOptions()
                resetTimer()
                updateCountDownText()
                quizBinding.textviewquestion.text="sorry,timeis up continue with ne questio"
                timerContinue=false
            }

        }.start()
        timerContinue=true
    }
    fun updateCountDownText(){
        val remainingTime: Int=(leftTime/1000).toInt()
        quizBinding.textviewtime.text=remainingTime.toString()

    }
    fun pauseTimer(){
        timer.cancel()
        timerContinue=false

    }
    fun resetTimer(){
        pauseTimer()
        leftTime=totaltime
        updateCountDownText()

    }
    fun sendScore(){
        user?.let{
            val userUID=it.uid
            scoreRef.child("scores").child(userUID).child("correct").setValue(usercorrect)
            scoreRef.child("scores").child(userUID).child("wrong").setValue(userwrong).addOnSuccessListener{
                Toast.makeText(applicationContext,"scores send to database successfully",Toast.LENGTH_SHORT).show()
                val intent= Intent(this@QuizActivity,ResultActivity::class.java)
                intent.putExtra("correct answer",usercorrect)
                intent.putExtra("wrong answer",userwrong)
                startActivity(intent)
                finish()
            }
        }
    }
}