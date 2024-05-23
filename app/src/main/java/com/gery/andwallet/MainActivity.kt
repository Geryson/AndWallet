package com.gery.andwallet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gery.andwallet.data.EditorItem
import com.gery.andwallet.data.EditorItemListAdapter
import com.gery.andwallet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
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
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add some data to the adapter
        adapter.submitList(listOf(
            EditorItem(1, "Title 1", 3000000),
            EditorItem(2, "Title 2", -2000000),
            EditorItem(3, "Title 3", 4000000)
        ))
    }

    override fun onResume() {
        super.onResume()
        binding.editorDividerUpper.requestFocus()
    }

}