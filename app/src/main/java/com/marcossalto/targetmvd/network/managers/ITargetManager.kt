package com.marcossalto.targetmvd.network.managers

import com.marcossalto.targetmvd.network.models.*
import com.marcossalto.targetmvd.network.models.Target
import com.marcossalto.targetmvd.util.extensions.Data

interface ITargetManager {
    suspend fun getTopics(): Result<Data<TopicsSerializer>>
    suspend fun getTargets(): Result<Data<TargetsSerializer>>
    suspend fun createTarget(target: Target): Result<Data<TargetSerializer>>
    suspend fun deleteTarget(id: Long): Result<Data<Void>>
}
