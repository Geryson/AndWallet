package com.gery.andwallet

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chaos.view.PinView
import com.gery.andwallet.databinding.ActivityPinBinding


class PinActivity : AppCompatActivity() {
    lateinit var binding: ActivityPinBinding

    lateinit var pinView: PinView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPinBinding.inflate(layoutInflater)

        pinView = binding.pvPasscodeValue

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length == 4) {
                    if (p0.toString() == getString(R.string.SECURITY_PIN_CODE)) {
                        // Grant access to MainActivity
                        binding.tvPasscodeIncorrect.visibility = View.INVISIBLE
                        val intent = Intent(this@PinActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Show an error message
                        pinView.setText("")
                        binding.tvPasscodeIncorrect.visibility = View.VISIBLE
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }

        pinView.addTextChangedListener(textWatcher)
    }

    override fun onResume() {
        super.onResume()
        val inputManager = pinView.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(pinView, InputMethodManager.SHOW_IMPLICIT)
    }
}