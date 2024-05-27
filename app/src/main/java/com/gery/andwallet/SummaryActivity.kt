package com.gery.andwallet

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gery.andwallet.databinding.ActivitySummaryBinding

class SummaryActivity : AppCompatActivity() {
    lateinit var binding: ActivitySummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySummaryBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnSummaryClose.setOnClickListener {
            finish()
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finish()
                }
            }
        )

        val bundle = intent.extras
        if (bundle != null) {
            binding.tvSummaryProfitValue.text = bundle.getString("editor_profit")
            binding.tvSummaryCostValue.text = bundle.getString("editor_cost")

            val balance = bundle.getString("editor_balance")
            binding.tvSummaryBalanceValue.text = balance

            val costColor = resources.getColor(R.color.cost_indicator, null)
            val profitColor = resources.getColor(R.color.profit_indicator, null)

            if (balance!!.toInt() < 0) {
                binding.tvSummaryBalanceLabel.setTextColor(costColor)
            } else {
                binding.tvSummaryBalanceLabel.setTextColor(profitColor)
            }
        }
    }
}