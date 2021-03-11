package com.marcossalto.targetmvd.ui.target

import android.content.Context
import android.location.Location
import androidx.lifecycle.*
import com.marcossalto.targetmvd.models.*
import com.marcossalto.targetmvd.network.managers.ILocationManager
import com.marcossalto.targetmvd.network.managers.ITargetManager
import com.marcossalto.targetmvd.network.managers.LocationManager
import com.marcossalto.targetmvd.network.managers.TargetManager
import com.marcossalto.targetmvd.network.models.*
import com.marcossalto.targetmvd.network.models.Target
import com.marcossalto.targetmvd.ui.base.BaseViewModel
import com.marcossalto.targetmvd.util.NetworkState
import kotlinx.coroutines.launch
import java.io.IOException

class TargetActivityViewModel(
        private val locationManager: ILocationManager,
        private val targetManager: ITargetManager
    ) : BaseViewModel(null)  {

    var networkStateObservable: MutableLiveData<NetworkState> = MutableLiveData()
    var topics: MutableLiveData<List<TopicModel>> = MutableLiveData()
    val targets: MutableLiveData<List<TargetModel>> = MutableLiveData()
    var targetState: MutableLiveData<ActionOnTargetState> = MutableLiveData()
    var newTarget: MutableLiveData<TargetModel> = MutableLiveData()
    var newMatch: MutableLiveData<MatchedUserModel> = MutableLiveData()
    var newConversation: MutableLiveData<ConversationModel> = MutableLiveData()
    private val showTarget: MutableLiveData<TargetModel> = MutableLiveData()
    val deletedTarget: MutableLiveData<TargetModel> = MutableLiveData()
    val deleteTargetState: MutableLiveData<ActionOnTargetState> = MutableLiveData()

    fun getLocation(context: Context, successAction: (location: Location) -> Unit) {
        locationManager.getLocation(context, successAction)
    }

    fun getTopics(): LiveData<List<TopicModel>> {
        try {
            viewModelScope.launch {
                val result = targetManager.getTopics()
                if (result.isSuccess) {
                    val topicsSerializer: List<TopicSerializer> = result.getOrNull()?.value?.topics ?: emptyList()
                    val topicsModel: List<TopicModel> = topicsSerializer.map { it.topic }.mapNotNull { it.toTopicModel() }
                    getTargets(topicsModel)
                    topics.postValue(topicsModel)
                } else {
                    handleError(result.exceptionOrNull())
                }
            }
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
        return topics
    }

    private fun getTargets(topics: List<TopicModel>) {
        viewModelScope.launch {
            val result = targetManager.getTargets()
            if (result.isSuccess) {
                val values: List<Target> = result.getOrNull()?.value?.targets?.map { it.target } ?: emptyList()

                val targetsModels =
                    values.map { target ->
                        target.toTargetModel(topics.first { topic -> target.topicId == topic.id })
                    }
                targets.postValue(targetsModels)
            } else {
                targets.postValue(emptyList())
            }
        }
    }

    fun createTarget(targetModel: TargetModel) {
        try {
            networkStateObservable.postValue(NetworkState.LOADING)
            viewModelScope.launch {
                val result = targetManager.createTarget(targetModel.toTargetRequest())
                if (result.isSuccess) {
                    result.getOrNull()?.value?.let{
                        handleSuccess(
                            it.target.toTargetModel(targetModel.topic),
                            it.matchedUser?.toMatchedUserModel(),
                            it.matchConversation?.toConversationModel()
                        )
                    }
                } else {
                    handleError(result.exceptionOrNull())
                }
            }
        } catch (exception: IOException) {
            handleError(exception)
            exception.printStackTrace()
        }
    }

    fun deleteTarget(target: TargetModel) {
        try {
            networkStateObservable.postValue(NetworkState.LOADING)
            viewModelScope.launch {
                val result = targetManager.deleteTarget(target.id)
                if (result.isSuccess) {
                    deletedTarget.postValue(target)
                    deleteTargetState.postValue(ActionOnTargetState.SUCCESS)
                    networkStateObservable.postValue(NetworkState.IDLE)
                }
            }
        } catch (ex: IOException) {
            deleteTargetState.postValue(ActionOnTargetState.FAILURE)
            networkStateObservable.postValue(NetworkState.IDLE)
        }
    }

    private fun handleError(exception: Throwable?) {
        targetState.postValue(ActionOnTargetState.FAILURE)
        networkStateObservable.postValue(NetworkState.ERROR)
        error = getErrorMessageFromException(exception)
    }

    private fun handleSuccess(
        target: TargetModel?,
        matchedUser: MatchedUserModel?,
        matchConversation: ConversationModel?
    ) {
        targetState.postValue(ActionOnTargetState.SUCCESS )
        networkStateObservable.postValue(NetworkState.IDLE)
        target?.let {
            newTarget.postValue(it)
        }
        matchedUser?.let {
            newMatch.postValue(it)
        }
        matchConversation?.let {
            newConversation.postValue(it)
        }
    }

    fun showTargetInformation(targetModel: TargetModel) {
        showTarget.postValue(targetModel)
    }

    fun hasToShowTargetInformation(): LiveData<TargetModel> {
        return showTarget
    }

    fun getLatitude(): Double = locationManager.getLatitude()

    fun getLongitude(): Double = locationManager.getLongitude()

    fun isStateSuccess(): Boolean = locationManager.isStateSuccess()

    fun isAreaValid(area: Double): Boolean = area > 0

    fun isTitleValid(title: String?): Boolean = title.isNullOrEmpty().not()

    fun isTopicValid(topic: TopicModel?): Boolean = topic != null

    fun getNewMatch(): LiveData<MatchedUserModel> {
        return newMatch
    }
}

enum class ActionOnTargetState {
    FAILURE,
    SUCCESS,
    NONE
}

class TargetActivityViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TargetActivityViewModel(LocationManager, TargetManager)  as T
    }
}
