package com.marcossalto.targetmvd.network.managers

import com.marcossalto.targetmvd.network.models.Target
import com.marcossalto.targetmvd.network.models.TargetSerializer
import com.marcossalto.targetmvd.network.models.TargetsSerializer
import com.marcossalto.targetmvd.network.models.TopicsSerializer
import com.marcossalto.targetmvd.network.providers.ServiceProvider
import com.marcossalto.targetmvd.network.services.ApiService
import com.marcossalto.targetmvd.util.extensions.ActionCallback
import com.marcossalto.targetmvd.util.extensions.Data

object TargetManager : ITargetManager {
    private var service = ServiceProvider.create(ApiService::class.java)

    override suspend fun getTopics(): Result<Data<TopicsSerializer>> =
        ActionCallback.call(service.getTopics())

    override suspend fun getTargets(): Result<Data<TargetsSerializer>> =
        ActionCallback.call(service.getTargets())

    override suspend fun createTarget(target: Target): Result<Data<TargetSerializer>> =
        ActionCallback.call(service.createTarget(TargetSerializer(target)))
}
