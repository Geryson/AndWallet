package com.gery.andwallet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gery.andwallet.data.EditorItem
import com.gery.andwallet.data.EditorItemListAdapter
import com.gery.andwallet.databinding.ActivityMainBinding
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity(), EditorItemListAdapter.OnBalanceChangedListener{
    lateinit var binding: ActivityMainBinding

    private val recyclerView: RecyclerView by lazy { binding.rvItemList }
    private val adapter = EditorItemListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.mbtgItemTypeContainer.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                Log.d("Check", checkedId.toString())
            }
        }

        binding.btnEditorOpenSummary.setOnClickListener {
            val intent = Intent(this@MainActivity, SummaryActivity::class.java)

            val bundle = Bundle()
            bundle.putString("editor_balance", adapter.balance.toString())

            var profit = 0
            var cost = 0

            for (item in adapter.items) {
                if (item.amount > 0) {
                    profit += item.amount
                } else {
                    cost += item.amount
                }

            }

            bundle.putString("editor_profit", profit.toString())
            bundle.putString("editor_cost", cost.absoluteValue.toString())

            intent.putExtras(bundle)
            startActivity(intent)
        }

        binding.btnItemAdd.setOnClickListener {
            val nameValue = binding.etItemNameValue.text.toString()
            var amountValue = binding.etItemQuantityValue.text.toString().toInt()
            if (binding.btnCostPicker.isChecked) {
                amountValue = amountValue * -1
            }

            adapter.addItem(
                EditorItem(
                    1,
                    nameValue,
                    amountValue
                )
            )

            binding.etItemNameValue.requestFocus()

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            binding.etItemNameValue.text!!.clear()
            binding.etItemQuantityValue.text!!.clear()
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.tvEditorBalanceValue.text = adapter.balance.toString()

        binding.btnEditorDeleteAll.setOnClickListener {
            adapter.deleteAll()
        }
    }



    override fun onResume() {
        super.onResume()
        binding.editorDividerUpper.requestFocus()
    }

    override fun onBalanceChanged(balance: Int) {
        binding.tvEditorBalanceValue.text = balance.toString()
    }

}