package com.marcossalto.targetmvd.ui.target

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.marcossalto.targetmvd.databinding.ActivityTargetBinding
import com.marcossalto.targetmvd.models.TargetModel

class TargetView(
    private val viewModel: TargetActivityViewModel,
    private val lifecycleOwner: LifecycleOwner
)  {

    init {
        getTopics()
    }

    private fun getTopics() {
        viewModel.getTopics().observe(lifecycleOwner, {})
    }
}
