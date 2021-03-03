package com.marcossalto.targetmvd.ui.target

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.databinding.ActivityTargetBinding
import com.marcossalto.targetmvd.metrics.Analytics
import com.marcossalto.targetmvd.metrics.PageEvents
import com.marcossalto.targetmvd.metrics.VISIT_TARGET
import com.marcossalto.targetmvd.ui.profile.ProfileActivity
import com.marcossalto.targetmvd.util.permissions.PermissionActivity

class TargetActivity : PermissionActivity() {
    private lateinit var viewModel: TargetActivityViewModel
    private lateinit var binding: ActivityTargetBinding
    private lateinit var targetView: TargetView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTargetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Analytics.track(PageEvents.visit(VISIT_TARGET))

        val factory = TargetActivityViewModelFactory()
        viewModel = ViewModelProvider(this, factory)
            .get(TargetActivityViewModel::class.java)

        initView()
    }

    private fun initView() {
        initMap()
        binding.toolbar.profileImageView.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ProfileActivity::class.java
                )
            )
        }
        initTargetView()
    }

    private fun initMap() {
        val fragment = MapFragment.getInstance()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_map, fragment).commit()
    }

    private fun initTargetView() {
        targetView = TargetView(viewModel, this)
    }
}
