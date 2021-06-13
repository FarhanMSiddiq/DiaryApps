package com.tugas.zaky.diaryapps

import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.tugas.zaky.diaryapps.Fragment.AboutFragment
import com.tugas.zaky.diaryapps.Fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        displayScreen(R.id.nav_home)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_grid -> {
                val fragment = HomeFragment.newInstance("grid")
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, fragment).commit()
                return true
            }
            R.id.action_list -> {
                val fragment = HomeFragment.newInstance("list")
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, fragment).commit()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun displayScreen(id: Int){

       // val fragment =  when (id){

        when (id){
            R.id.nav_home -> {
                val fragment = HomeFragment.newInstance("grid")
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, fragment).commit()
            }

            R.id.nav_about -> {
                supportFragmentManager.beginTransaction().replace(R.id.relativelayout, AboutFragment()).commit()
            }


        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        displayScreen(item.itemId)

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


}
