package com.marcossalto.targetmvd.metrics

import android.content.Context
import com.marcossalto.targetmvd.BuildConfig
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.metrics.base.Provider
import com.marcossalto.targetmvd.metrics.base.TrackEvent
import com.marcossalto.targetmvd.metrics.base.UserProperty
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONException

/**
 * Mix panel android reference: https://developer.mixpanel.com/docs/android
 * */
class MixPanelAnalytics(context: Context) : Provider {
    var analytic: MixpanelAPI =
        MixpanelAPI.getInstance(context, BuildConfig.MIXPANEL_TOKEN)

    /**
     * Track any event in the app
     * @param event Action to track
     * */
    override fun track(event: TrackEvent) {
        analytic.let {
            if (event.eventName.isNotEmpty()) {
                event.eventData?.let { _ ->
                    try {
                        it.track(event.eventName, event.actionDataToJsonObject())
                    } catch (e: JSONException) {
                        it.track(event.eventName)
                        e.printStackTrace()
                    }
                } ?: it.track(event.eventName)
            }
        }
    }

    /**
     * Reset analytic
     */
    override fun reset() {
        analytic.reset()
    }

    /**
     * Initialize mixpanel user and default data
     *  analytic.identify(user.id)
     *  analytic.alias(user.id, null)
     *  val people = analytic.people
     *  people.identify(user.Id)
     *  people.set("\$first_name", user.username)
     *  people.set("\$email", user.email())
     *  analytic!!.flush()
     * */
    override fun identifyUser() {
        // TODO see the comments ↑↑↑
    }

    /**
     * @param userProperty
     * Sample
     * UserProperty("Type","Admin")
     * UserProperty("Type","Customer")
     * UserProperty("Type","Anonymous")
     * https://help.mixpanel.com/hc/en-us/articles/360001355526
     * */
    override fun addOrEditUserSuperProperty(userProperty: UserProperty) {
        analytic.registerSuperProperties(userProperty.toJsonObject())
    }
}
