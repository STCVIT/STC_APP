package `in`.stcvit.stcapp.data.feed

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import `in`.stcvit.stcapp.ui.home.FeedViewModelFactory
import `in`.stcvit.stcapp.util.RetrofitService


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
