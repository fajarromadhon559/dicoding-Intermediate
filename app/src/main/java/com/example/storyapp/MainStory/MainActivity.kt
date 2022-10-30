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
import com.example.storyapp.AddStory.AddStoryActivity
import com.example.storyapp.Local.SharedPreferences
import com.example.storyapp.Login.LoginActivity
import com.example.storyapp.Repo.Result
import com.example.storyapp.R
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
            R.id.menu_add -> {
                Intent(this, AddStoryActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.menu_setting -> {
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
        return super.onOptionsItemSelected(item)
    }

}