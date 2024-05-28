package com.gery.andwallet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gery.andwallet.data.EditorItem
import com.gery.andwallet.data.EditorItemDatabase
import com.gery.andwallet.data.EditorItemListAdapter
import com.gery.andwallet.databinding.ActivityMainBinding
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity(), EditorItemListAdapter.OnBalanceChangedListener{
    lateinit var binding: ActivityMainBinding

    private val recyclerView: RecyclerView by lazy { binding.rvItemList }
    private lateinit var adapter : EditorItemListAdapter

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
            var amountValue = binding.etItemQuantityValue.text.toString()

            validateAndSave(nameValue, amountValue)
        }

        adapter = EditorItemListAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.tvEditorBalanceValue.text = adapter.balance.toString()

        binding.btnEditorDeleteAll.setOnClickListener {
            val thread = Thread {
                val database: EditorItemDatabase = EditorItemDatabase.getInstance(this)
                database.editorItemDao().deleteAll()
                runOnUiThread {
                    adapter.deleteAll()
                }
            }
            thread.start()
        }
    }

    private fun validateAndSave(nameValue: String, amountValue: String) {
        val isNameValid = validateName(nameValue)
        val isAmountValid = validateAmount(amountValue)
        if (isNameValid && isAmountValid) {
            binding.tilItemNameContainer.error = null
            binding.tilItemQuantityContainer.error = null

            var amountNumber = amountValue.toInt()

            if (binding.btnCostPicker.isChecked) {
                amountNumber = amountValue.toInt() * -1
            }

            val editorItem = EditorItem(
                null,
                nameValue,
                amountNumber
            )
            val thread = Thread {
                val database: EditorItemDatabase = EditorItemDatabase.getInstance(this)
                database.editorItemDao().insert(editorItem)
                runOnUiThread {
                    adapter.addItem(
                        editorItem
                    )

                    binding.etItemNameValue.requestFocus()

                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

                    binding.etItemNameValue.text!!.clear()
                    binding.etItemQuantityValue.text!!.clear()
                }
            }
            thread.start()
        } else {
            Toast.makeText(this, "HOZZÁADÁS SIKERTELEN: hiányzó / hibás adatok", Toast.LENGTH_SHORT).show()
        }    }

    private fun validateName(nameValue: String): Boolean {
        if (nameValue.isEmpty()) {
            binding.tilItemNameContainer.error = "Név megadása kötelező"
            return false
        }
        return true
    }

    private fun validateAmount(amountValue: String): Boolean {
        try {
            amountValue.toInt()
        } catch (e: NumberFormatException) {
            binding.tilItemQuantityContainer.error = "Szám megadása kötelező"
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        binding.editorDividerUpper.requestFocus()
    }

    override fun onBalanceChanged(balance: Int) {
        binding.tvEditorBalanceValue.text = balance.toString()
    }

}