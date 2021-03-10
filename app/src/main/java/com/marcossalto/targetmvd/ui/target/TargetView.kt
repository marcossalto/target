package com.marcossalto.targetmvd.ui.target

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.crashlytics.internal.common.CommonUtils.hideKeyboard
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.databinding.ActivityTargetBinding
import com.marcossalto.targetmvd.models.TargetModel
import com.marcossalto.targetmvd.models.TopicModel
import com.marcossalto.targetmvd.util.DialogUtil
import com.marcossalto.targetmvd.util.Util
import com.marcossalto.targetmvd.util.extensions.getTargetIcon
import com.marcossalto.targetmvd.util.extensions.value
import kotlinx.android.synthetic.main.activity_target.view.*
import kotlinx.android.synthetic.main.layout_delete_confirmation.view.*
import kotlinx.android.synthetic.main.layout_save_target.view.*
import kotlinx.android.synthetic.main.layout_save_target.view.small_buttons_linear_layout
import kotlinx.android.synthetic.main.layout_select_topic.view.*

class TargetView(
    private val viewModel: TargetActivityViewModel,
    private val lifecycleOwner: LifecycleOwner,
    binding: ActivityTargetBinding
)  {
    private val bindingRoot = binding.root
    private val bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout> =
        BottomSheetBehavior.from(bindingRoot.save_target_layout_bottom_sheet)
    private val topicsBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout> =
        BottomSheetBehavior.from(bindingRoot.select_topic_layout_bottom_sheet)
    private lateinit var selectedTopic: TopicModel
    private lateinit var selectedTarget: TargetModel
    private lateinit var topicAdapter: TopicAdapter

    init {
        initSaveTargetBottomSheet()
        initTopicsBottomSheet()
        initListeners()
        getTopics()
        observeCreateTargetState()
        observeShowTarget()
        observeDeleteTargetState()
    }

    private fun initSaveTargetBottomSheet() {
        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            peekHeight = TargetActivity.PICK_HEIGHT_HIDDEN
        }
    }

    private fun initTopicsBottomSheet() {
        topicsBottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            peekHeight = TargetActivity.PICK_HEIGHT_HIDDEN
        }
    }

    private fun initListeners(){
        bindingRoot.apply {
            area_length_edit_text.setOnFocusChangeListener { _, _ ->
                clearErrorState()
            }
            target_title_edit_text.setOnFocusChangeListener { _, _ ->
                clearErrorState()
            }
            topic_edit_text.setOnFocusChangeListener { _, hasFocus ->
                clearErrorState()
                if (hasFocus)
                    expandTopic()
            }
            topic_edit_text.setOnClickListener {
                expandTopic()
            }
            save_target_button.setOnClickListener {
                createTarget()
            }
            delete_target_button.setOnClickListener {
                showConfirmDialog()
            }
            save_target_small_button.setOnClickListener {
                expandCollapseCreateTargetSheet()
            }
        }
    }

    private fun showConfirmDialog() {
        val dialogView = LayoutInflater.from(bindingRoot.context)
            .inflate(R.layout.layout_delete_confirmation, null)
        val builder = AlertDialog.Builder(bindingRoot.context)
            .setView(dialogView)

        val alertDialog: AlertDialog = builder.create()

        dialogView.target_title_confirm_text_view.text = selectedTarget.title
        dialogView.target_confirm_image_view.setImageResource(selectedTarget.topic.getTargetIcon())

        alertDialog.show()

        dialogView.confirm_delete_button.setOnClickListener{
            deleteTarget()
            alertDialog.dismiss()
        }
        dialogView.confirm_cancel_button.setOnClickListener{
            alertDialog.dismiss()
        }
    }

    private fun getTopics() {
        viewModel.getTopics().observe(lifecycleOwner, { initTopicList(it) })
    }

    private fun initTopicList(topics: List<TopicModel>) {
        topicAdapter = TopicAdapter(topics) {
            selectedTopic(it)
        }
        with(bindingRoot.topics_recycler_view) {
            adapter = topicAdapter
            layoutManager = LinearLayoutManager(bindingRoot.context)
        }
    }

    private fun observeShowTarget() {
        viewModel.hasToShowTargetInformation().observe(lifecycleOwner, Observer {
            showTargetInformation(it)
        })
    }

    private fun selectedTopic(topic: TopicModel) {
        selectedTopic = topic
        setTopicIcon(topic)
        collapseTopic()
    }

    private fun setTopicIcon(topic: TopicModel) {
        val textSpan = SpannableString(DOUBLE_EMPTY_SPACE_FOR_SPAN + topic.label.name)
        val iconDrawable: Drawable? = ContextCompat.getDrawable(bindingRoot.context, topic.getTargetIcon())
        iconDrawable?.setBounds(0, 0, 60, 60)

        iconDrawable?.run {
            val imageSpan = ImageSpan(iconDrawable, ImageSpan.ALIGN_CENTER)
            textSpan.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            val editable = Editable.Factory.getInstance().newEditable(textSpan)
            bindingRoot.topic_edit_text.text = editable
        }
    }

    private fun createTarget() {
        with(viewModel) {
            val title = bindingRoot.target_title_edit_text.value()
            val area: Double = bindingRoot.area_length_edit_text.value().toDoubleOrNull() ?: 0.0
            val lat = getLatitude()
            val lng = getLongitude()
            val target = TargetModel(0, title, lat, lng, area, selectedTopic)

            if (isStateSuccess() &&
                validateUserInputs(area, title, selectedTopic)
            ) {
                createTarget(target)
            }
        }
    }

    private fun deleteTarget() {
        selectedTarget?.run {
            viewModel.deleteTarget(this)
            expandCollapseCreateTargetSheet()
        }
    }

    private fun observeDeleteTargetState() {
        viewModel.deleteTargetState.observe(lifecycleOwner, Observer { state ->
            with(bindingRoot.context) {
                if (state == ActionOnTargetState.FAILURE) {
                    showError(getString(R.string.failed_deleting_target))
                }
            }
        })
    }

    private fun validateUserInputs(area: Double, title: String?, topic: TopicModel?): Boolean =
        validateArea(area) && validateTargetTitle(title) && validateTopic(topic)

    private fun validateArea(area: Double): Boolean {
        val isAreaValid = viewModel.isAreaValid(area)
        if (!isAreaValid){
            with(bindingRoot){
                with(area_length_message_text_view) {
                    text = context.getString(R.string.area_length_is_not_valid)
                    visibility = View.VISIBLE
                }
                area_length_edit_text.background = resources.getDrawable(R.drawable.edit_text_red, null)
            }
        }
        return isAreaValid
    }

    private fun validateTargetTitle(title: String?): Boolean {
        val isTitleValid = viewModel.isTitleValid(title)
        if (!isTitleValid) {
            with(bindingRoot){
                with(target_title_message_text_view) {
                    text = context.getString(R.string.target_title_is_not_valid)
                    visibility = View.VISIBLE
                }
                area_length_edit_text.background = resources.getDrawable(R.drawable.edit_text_red, null)
            }
        }
        return isTitleValid
    }

    private fun validateTopic(topic: TopicModel?): Boolean {
        val isTopicValid = viewModel.isTopicValid(topic)
        if (!isTopicValid){
            with(bindingRoot){
                with(topic_message_text_view) {
                    text = context.getString(R.string.select_a_topic)
                    visibility = View.VISIBLE
                }
                area_length_edit_text.background = resources.getDrawable(R.drawable.edit_text_red, null)
            }
        }
        return isTopicValid
    }

    fun expandCollapseCreateTargetSheet() {
        bottomSheetBehavior.state = if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            showBottomButtons(true)
            eraseTargetInformation()
            BottomSheetBehavior.STATE_EXPANDED
        } else {
            BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun showBottomButtons(isCreateTarget: Boolean) {
        with(bindingRoot) {
            if (isCreateTarget) {
                small_buttons_linear_layout.visibility = View.GONE
                save_target_button.visibility = View.VISIBLE
            } else {
                small_buttons_linear_layout.visibility = View.VISIBLE
                save_target_button.visibility = View.GONE
            }
        }
    }

    private fun eraseTargetInformation() {
        with(bindingRoot) {
            target_title_edit_text.text = Util.createEmptyEditable()
            area_length_edit_text.text = Util.createEmptyEditable()
            topic_edit_text.text = Util.createEmptyEditable()
            select_topic_text_view.text = context.getString(R.string.select_a_topic)
            area_length_text_view.text = context.getString(R.string.specify_area_length)
        }
    }

    private fun showTargetInformation(targetModel: TargetModel) {
        bottomSheetBehavior.state = if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            completeTargetInformation(targetModel)
            BottomSheetBehavior.STATE_EXPANDED
        } else {
            BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun completeTargetInformation(targetModel: TargetModel) {
        with(bindingRoot) {
            selectedTarget = targetModel
            showBottomButtons(false)
            target_title_edit_text.text = Util.createEditable(targetModel.title)
            area_length_edit_text.text = Util.createEditable("" + targetModel.radius)
            targetModel.topic?.run {
                selectedTopic(this)
            }
            select_topic_text_view.text = context.getString(R.string.select_a_topic)
            area_length_text_view.text = context.getString(R.string.specify_area_length)
        }
    }

    private fun expandTopic() {
        topicsBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        hideKeyboard(bindingRoot.context, bindingRoot)
    }

    private fun collapseTopic() {
        topicsBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun observeCreateTargetState() {
        viewModel.targetState.observe(lifecycleOwner, Observer { targetState ->
            targetState?.run {
                when (this) {
                    ActionOnTargetState.FAILURE -> showError(
                        viewModel.error ?: bindingRoot.context.getString(R.string.default_error)
                    )
                    ActionOnTargetState.SUCCESS -> {
                        successCreatingTarget()
                    }
                    ActionOnTargetState.NONE -> Unit
                }
            }
        })
    }

    private fun successCreatingTarget() {
        expandCollapseCreateTargetSheet()
        hideKeyboard(bindingRoot.context, bindingRoot)
    }

    private fun showError(message: String) {
        DialogUtil.showError(bindingRoot.context, message)
    }

    private fun clearErrorState() {
        with(bindingRoot) {
            with(area_length_message_text_view) {
                text = ""
                visibility = View.GONE
            }
            with(target_title_message_text_view) {
                text = ""
                visibility = View.GONE
            }
            with(topic_message_text_view) {
                text = ""
                visibility = View.GONE
            }
            resources.getDrawable(R.drawable.edit_text_normal, null)
            .also {
                area_length_edit_text.background = it
                target_title_edit_text.background = it
                topic_edit_text.background = it
            }
        }
    }

    companion object {
        const val DOUBLE_EMPTY_SPACE_FOR_SPAN = "  "
    }
}
