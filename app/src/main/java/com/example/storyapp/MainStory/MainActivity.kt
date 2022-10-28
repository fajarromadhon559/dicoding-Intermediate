package com.example.storyapp.MainStory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.Local.SharedPreferences
import com.example.storyapp.Repo.Result
import com.example.storyapp.R
import com.example.storyapp.Register.RegisterActivity
import com.example.storyapp.Settings.SettingActivity
import com.example.storyapp.Utils.ViewModelFactory
import com.example.storyapp.Utils.gone
import com.example.storyapp.Utils.visible
import com.example.storyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences
    private val adapterList = StoryListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listStory()
        binding.btnAdd.setOnClickListener {
            Intent(this, StoryDetailActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun listStory() {
        val listStoryUser = adapterList
        val factory = ViewModelFactory.getInstance(this)
        val viewModel: MainViewModel by viewModels {
            factory
        }
        sharedPref = SharedPreferences(this)
        viewModel.getListStory(sharedPref.getToken()).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visible()
                    }
                    is Result.Success -> {
                        binding.progressBar.gone()
                        val listStory = result.data
                        listStoryUser.submitList(listStory)
                    }
                    is Result.Error -> {
                        Toast.makeText(this, "list not Found", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = listStoryUser
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> {
                Intent(this, SettingActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}