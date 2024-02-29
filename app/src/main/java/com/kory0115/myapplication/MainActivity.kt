package com.kory0115.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.kory0115.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate( layoutInflater ) }
    private val numTextViewList : List<TextView> by lazy {
        listOf(
            binding.tvNum1
            ,binding.tvNum2
            ,binding.tvNum3
            ,binding.tvNum4
            ,binding.tvNum5
            ,binding.tvNum6)
    }

    private var didRun = false
    private val pickNumSet = hashSetOf<Int>() //우리가 직접 선택한 공의 개수에대한 배열

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.npNum.minValue = 1
        binding.npNum.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()
    }

    private fun initAddButton() = with(binding) {
        btnAdd.setOnClickListener {
            when {
                didRun -> showToast("초기화 후에 시도해주세요.")
                pickNumSet.size >= 5 -> showToast("숫자는 최대 5개까지 선택할 수 있습니다.")
                pickNumSet.contains(npNum.value) -> showToast("이미 선택한 숫자입니다.")
                else -> {
                    val textView = numTextViewList[pickNumSet.size]
                    textView.isVisible = true
                    textView.text = npNum.value.toString()

                    setNumBack(npNum.value, textView)
                    pickNumSet.add(npNum.value)
                }
            }
        }
    }

    private fun setNumBack(number: Int, textView: TextView) {
        val background = when (number) {
            in 1..10 -> R.drawable.circle_yellow
            in 11..20 -> R.drawable.circle_blue
            in 21..30 -> R.drawable.circle_red
            in 31..40 -> R.drawable.circle_gray
            else -> R.drawable.circle_green
        }
        textView.background = ContextCompat.getDrawable(this, background)
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun initRunButton() = with(binding) {
        btnRun.setOnClickListener {
            val list = getRandom()
            didRun = true
            list.forEachIndexed { index, number ->
                val textView = numTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumBack(number, textView)
            }
        }
    }

    private fun getRandom(): List<Int> {
        val numbers = (1..45).filter { it !in pickNumSet }
        return (pickNumSet + numbers.shuffled().take(6 - pickNumSet.size)).sorted()
    }

    private fun initClearButton() = with(binding) {
        btnClear.setOnClickListener {
            pickNumSet.clear()
            numTextViewList.forEach { it.isVisible = false }
            didRun = false
            npNum.value = 1
        }
    }
}