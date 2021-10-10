package com.sigmasoftware.akucherenko.asynchronoustask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import com.sigmasoftware.akucherenko.asynchronoustask.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var progressThread: ProgressBar
    private lateinit var statusTextView: TextView
    private val step = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val executorService: ExecutorService = Executors.newFixedThreadPool(4)

        progressThread = binding.progressBar
        statusTextView = binding.statusTexView
        statusTextView.text = getString(R.string.message_stopped)
        progressThread.max = step

        binding.startButton.setOnClickListener {
            runThread()
        }
    }

    fun runThread() {
        binding.startButton.isEnabled = false
        statusTextView.text = getString(R.string.message_running)
        progressThread.progress = 0

        val runnable = Runnable {
            while (progressThread.progress < progressThread.max) {
                Thread.sleep(1000)
                progressThread.progress++
            }
            statusTextView.post(Runnable {
                statusTextView.text = getString(R.string.message_finish)
            });
            binding.startButton.post(Runnable{
                binding.startButton.isEnabled = true
            })
        }
        val thread = Thread(runnable)
        thread.start()
    }

    fun onClickIncrement(view: android.view.View) {
        progressThread.max += step
        progressThread.progress = 0
    }

    fun onClickDecrement(view: android.view.View) {
        if (progressThread.max > step) progressThread.max -= step
        progressThread.progress = 0
    }
}