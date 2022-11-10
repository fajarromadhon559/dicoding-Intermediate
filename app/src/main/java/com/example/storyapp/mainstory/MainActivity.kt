package com.example.storyapp.mainstory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.addstory.AddStoryActivity
import com.example.storyapp.login.LoginActivity
import com.example.storyapp.R
import com.example.storyapp.utils.ViewModelFactory
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.map.MapStorys.Companion.TOKEN
import com.example.storyapp.map.MapsActivity
import com.example.storyapp.utils.LoadingStateAdapter

class MainActivity : AppCompatActivity() {
    private var _binding:ActivityMainBinding? = null
    private  val binding get() = _binding!!
    private var token : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listStory()
        swipeRefreshLayout()
        binding.rvStory.layoutManager = LinearLayoutManager(this)
    }

    private fun listStory() {
        val listStoryAdapter = StoryListAdapter()
        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        binding.rvStory.adapter = listStoryAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                listStoryAdapter.retry()
            }
        )

        viewModel.storyList.observe(this) { dayat ->
            listStoryAdapter.submitData(lifecycle, dayat)
        }
    }
    private fun swipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            listStory()
            binding.swipeRefresh.isRefreshing = false
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
            R.id.map_menu ->{
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra(TOKEN, token)
                startActivity(intent)
            }
            R.id.logout_menu -> {
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