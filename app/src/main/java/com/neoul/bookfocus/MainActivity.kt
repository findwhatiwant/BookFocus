package com.neoul.bookfocus

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // 타이머 관련 변수
    private var timerHandler: Handler? = null
    private var timerRunnable: Runnable? = null
    private var elapsedSeconds: Long = 0
    private var isTimerRunning = false

    // MediaPlayer 변수
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        // 기본 Fragment 설정 (mainFragment)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mainFragment())
                .commit()
        }

        // 바텀 네비게이션 설정
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.navigation_home -> mainFragment()
                R.id.navigation_records -> recordsFragment()
                R.id.navigation_profile -> profileFragment()
                else -> mainFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()

            true
        }
    }

    // focusmode_layout으로 전환하는 메소드
    fun switchToFocusMode() {
        setContentView(R.layout.focusmode_layout)

        // 타이머 시작
        startTimer()

        // SwitchCompat 설정
        val toggleButton = findViewById<SwitchCompat>(R.id.toggle_button)
        toggleButton?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startWhiteNoise()
            } else {
                stopWhiteNoise()
            }
        }

        // focusmode의 center_button 클릭 시 다시 main_layout으로 돌아가기
        val centerButton = findViewById<ImageButton>(R.id.center_button)
        centerButton?.setOnClickListener {
            switchToMainLayout()
        }
    }

    // main_layout으로 다시 돌아가는 메소드
    private fun switchToMainLayout() {
        // 타이머 중지
        stopTimer()

        // 화이트 노이즈 중지
        stopWhiteNoise()

        setContentView(R.layout.main_layout)

        // Fragment와 Bottom Navigation 재설정
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, mainFragment())
            .commit()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.navigation_home -> mainFragment()
                R.id.navigation_records -> recordsFragment()
                R.id.navigation_profile -> profileFragment()
                else -> mainFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()

            true
        }
    }

    // 타이머 시작 메소드
    private fun startTimer() {
        elapsedSeconds = 0
        isTimerRunning = true

        timerHandler = Handler(Looper.getMainLooper())
        timerRunnable = object : Runnable {
            override fun run() {
                if (isTimerRunning) {
                    elapsedSeconds++
                    updateTimerText()
                    timerHandler?.postDelayed(this, 1000) // 1초마다 업데이트
                }
            }
        }
        timerHandler?.post(timerRunnable!!)
    }

    // 타이머 중지 메소드
    private fun stopTimer() {
        isTimerRunning = false
        timerRunnable?.let { timerHandler?.removeCallbacks(it) }
        timerHandler = null
        timerRunnable = null
        elapsedSeconds = 0
    }

    // 타이머 텍스트 업데이트 메소드
    private fun updateTimerText() {
        val hours = elapsedSeconds / 3600
        val minutes = (elapsedSeconds % 3600) / 60
        val seconds = elapsedSeconds % 60

        val timerText = findViewById<TextView>(R.id.timer_text)
        timerText?.text = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }

    // 화이트 노이즈 재생 시작
    private fun startWhiteNoise() {
        try {
            // 이미 재생 중이면 중지하고 새로 시작
            stopWhiteNoise()

            // MediaPlayer 생성 및 설정
            mediaPlayer = MediaPlayer.create(this, R.raw.whitenoise)
            mediaPlayer?.isLooping = true // 반복 재생
            mediaPlayer?.start()
            Log.i("MainActivity", "노이즈 재생")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 화이트 노이즈 재생 중지
    private fun stopWhiteNoise() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
            mediaPlayer = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        stopWhiteNoise()
    }
}
