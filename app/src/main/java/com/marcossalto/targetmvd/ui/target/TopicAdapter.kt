package com.marcossalto.targetmvd.ui.target

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.databinding.CustomTopicItemBinding
import com.marcossalto.targetmvd.models.TopicModel
import com.marcossalto.targetmvd.util.extensions.getTargetIcon
import com.marcossalto.targetmvd.util.extensions.inflate

class TopicAdapter(
    private var topics: List<TopicModel>,
    private val onItemSelected: (TopicModel) -> Unit
) : RecyclerView.Adapter<TopicViewHolder>() {

    private val itemsCopy = mutableListOf<TopicModel>()

    init {
        itemsCopy.addAll(topics)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = parent.inflate(R.layout.custom_topic_item)
        return TopicViewHolder(view)
    }

    override fun getItemCount(): Int {
        return topics.size
    }

    @ExperimentalStdlibApi
    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val item = topics[position]
        holder.binding.topicNameTextView.text = item.label.name.lowercase().capitalize()
        Glide.with(holder.itemView)
            .load(item.getTargetIcon())
            .centerCrop()
            .into(holder.binding.topicImageView)
        holder.bindListener(onItemSelected, item)
    }
}

class TopicViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
    val binding = CustomTopicItemBinding.bind(item)

    fun bindListener(onItemSelected: (TopicModel) -> Unit, topic: TopicModel) {
        binding.topicContainer.setOnClickListener {
            onItemSelected(topic)
        }
    }
}
