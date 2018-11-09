package com.vitusortner.patterns.ui.pins

import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.vitusortner.patterns.networking.ApiClient
import com.vitusortner.patterns.networking.model.Image
import com.vitusortner.patterns.ui.pins.PinsViewModel.Companion.invoke
import com.vitusortner.patterns.util.Logger
import com.vitusortner.patterns.util.PatternsDispatchers
import com.vitusortner.patterns.util.SharedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * [ViewModel] which provides pins.
 * Constructor should only be used when testing. Otherwise use static [invoke].
 */
class PinsViewModel @VisibleForTesting constructor(
    private val apiClient: ApiClient,
    private val sharedPrefs: SharedPrefs,
    private val dispatchers: PatternsDispatchers
) : ViewModel(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.IO + job

    private val log by Logger()

    private val _pins = MutableLiveData<List<Image>>()
    val pins: LiveData<List<Image>>
        get() = _pins

    fun fetchPins() {
        val token = sharedPrefs.token ?: run {
            log.w("Not logged in!")
            return
        }

        launch {
            try {
                val response = apiClient.images(token.value).await()
                val images = response.map { it.image.original }

                withContext(dispatchers.Main) {
                    _pins.value = images
                }
            } catch (error: Throwable) {
                log.w("Error occured while fetching images.", error)
            }
        }
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }

    companion object {

        operator fun invoke(
            activity: AppCompatActivity,
            apiClient: ApiClient,
            sharedPrefs: SharedPrefs,
            dispatchers: PatternsDispatchers
        ): PinsViewModel {
            return ViewModelProviders
                .of(activity, PinsViewModelFactory(apiClient, sharedPrefs, dispatchers))
                .get(PinsViewModel::class.java)
        }
    }

}

/** Factory for [PinsViewModel] */
private class PinsViewModelFactory(
    private val apiClient: ApiClient,
    private val sharedPrefs: SharedPrefs,
    private val dispatchers: PatternsDispatchers
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PinsViewModel(apiClient, sharedPrefs, dispatchers) as T
    }
}
