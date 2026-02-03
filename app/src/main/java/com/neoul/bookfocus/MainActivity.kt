package com.neoul.bookfocus

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.neoul.bookfocus.ui.theme.BookFocusTheme

private lateinit var onClickListener: () -> Unit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        val startButton = findViewById<ImageButton>(R.id.center_button)
        startButton?.setOnClickListener {
            setContentView(R.layout.focusmode_layout)
        }
    }
}

