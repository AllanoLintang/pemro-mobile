package com.example.tipcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.widget.Toast
import com.example.tipcalc.databinding.ActivityMainBinding
import com.example.tipcalc.ui.theme.TipcalcTheme

class MainActivity : ComponentActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calculate.setOnClickListener {
            hitung()
        }
    }
    private fun hitung(){
        val cost = binding.costOfService.text.toString().toDouble()
        val tip = binding.tipOptions.checkedRadioButtonId
        val tipPercent = when (tip) {
            R.id.option_twenty -> 0.20
            R.id.option_eighteen -> 0.18
            else -> 0.15
        }

        var total = tipPercent * cost
        val roundUp = binding.roundUp.isChecked
        if(roundUp){
            total = kotlin.math.ceil(total)
        }
        if(cost <= 0){
            Toast.makeText(this, "Masukkan nilai yang benar", Toast.LENGTH_SHORT).show()
        }else{
            binding.total.text = total.toString()
        }
    }
}