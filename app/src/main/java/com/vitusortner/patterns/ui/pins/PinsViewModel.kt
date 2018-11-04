package com.vitusortner.patterns.ui.pins

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.vitusortner.patterns.networking.ApiClient
import com.vitusortner.patterns.networking.model.Image
import com.vitusortner.patterns.ui.pins.PinsViewModel.Companion.get
import com.vitusortner.patterns.util.SharedPrefs
import com.vitusortner.patterns.util.logger
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * [ViewModel] which provides pins.
 * Constructor should only be used when testing. Otherwise use static [get].
 */
class PinsViewModel(
    private val apiClient: ApiClient,
    private val sharedPrefs: SharedPrefs
) : ViewModel(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    private val log by logger()

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

                withContext(Dispatchers.Main) {
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

        fun get(
            activity: AppCompatActivity,
            apiClient: ApiClient,
            sharedPrefs: SharedPrefs
        ): PinsViewModel {
            return ViewModelProviders
                .of(activity, PinsViewModelFactory(apiClient, sharedPrefs))
                .get(PinsViewModel::class.java)
        }
    }

}

/** Factory for [PinsViewModel] */
private class PinsViewModelFactory(
    private val apiClient: ApiClient,
    private val sharedPrefs: SharedPrefs
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PinsViewModel(apiClient, sharedPrefs) as T
    }
}
