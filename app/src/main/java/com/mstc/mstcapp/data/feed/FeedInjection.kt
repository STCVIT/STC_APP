package com.mstc.mstcapp.data.feed

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.mstc.mstcapp.util.RetrofitService
import com.mstc.mstcapp.ui.home.FeedViewModelFactory


/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object FeedInjection {
    private fun provideFeedRepository(context: Context): FeedRepository {
        return FeedRepository(RetrofitService.create(), FeedDatabase.getInstance(context))
    }

    fun provideViewModelFactory(
        context: Context,
        owner: SavedStateRegistryOwner,
    ): ViewModelProvider.Factory {
        return FeedViewModelFactory(owner, provideFeedRepository(context))
    }
}
