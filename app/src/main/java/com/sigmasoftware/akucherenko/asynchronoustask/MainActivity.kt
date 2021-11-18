package com.sigmasoftware.akucherenko.asynchronoustask

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import com.sigmasoftware.akucherenko.asynchronoustask.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var progressThread: ProgressBar
    private lateinit var statusThread: TextView
    private lateinit var statusAsyncTask: TextView
    private var maxProgressAsyncTask = 0
    private val step = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val executorService: ExecutorService = Executors.newFixedThreadPool(4)

        progressThread = binding.progressBar
        statusThread = binding.statusThread
        statusThread.text = getString(R.string.message_stopped)
        progressThread.max = step

        binding.startButton.setOnClickListener {
            runThread()
        }

        statusAsyncTask = binding.statusAsyncTask
        statusAsyncTask.text = getString(R.string.message_stopped)


//        MyAsyncTask().execute(23L)
    }

    private fun runThread() {
        binding.startButton.isEnabled = false
        statusThread.text = getString(R.string.message_running)
        progressThread.progress = 0

        val runnable = Runnable {
            while (progressThread.progress < progressThread.max) {
                Thread.sleep(1000)
                progressThread.progress++
            }
            statusThread.post(Runnable {
                statusThread.text = getString(R.string.message_finish)
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

    private inner class MyAsyncTask : AsyncTask<Int, Int, String>() {

        override fun onPreExecute() {
//        super.onPreExecute()
            binding.startAsyncTask.isEnabled = false
            statusAsyncTask.text = getString(R.string.message_running)
        }

        override fun doInBackground(vararg p0: Int?): String {
            var progress = 0
            while (progress < p0[0]!!) {
                TimeUnit.MILLISECONDS.sleep(1000L)
                progress ++
                publishProgress(progress)
            }
            return "The End"
        }

        override fun onProgressUpdate(vararg values: Int?) {
//            super.onProgressUpdate(*values)
            binding.progressAsyncTask.text = values[0].toString()
        }

        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
            statusAsyncTask.text = getString(R.string.message_finish)
            binding.startAsyncTask.isEnabled = true
        }
    }

    fun startAsyncTask(view: android.view.View) {
        maxProgressAsyncTask = step
        MyAsyncTask().execute(maxProgressAsyncTask)
    }

    fun onClickIncrementAsyncTask(view: android.view.View) {
        MyAsyncTask().cancel(true)
        maxProgressAsyncTask += step
        MyAsyncTask().execute(maxProgressAsyncTask)
    }
    fun onClickDecrementAsyncTask(view: android.view.View) {
        MyAsyncTask().cancel(true)
        if (maxProgressAsyncTask > step) maxProgressAsyncTask -= step
        MyAsyncTask().execute(maxProgressAsyncTask)

    }
}