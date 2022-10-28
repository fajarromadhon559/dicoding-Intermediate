package com.example.storyapp.Settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.Login.LoginActivity
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnLogout.setOnClickListener{
                showLogDialog()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun showLogDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.caution))
        builder.setMessage(resources.getString(R.string.To_do))
        builder.setPositiveButton(resources.getString(R.string.Yes)) { _, _ ->
            this.getSharedPreferences("data_user", 0).edit().clear()
                .apply()
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }
        builder.setNegativeButton(resources.getString(R.string.No)) { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }
}