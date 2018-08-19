package com.vitusortner.patterns.ui.pins

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vitusortner.patterns.await
import com.vitusortner.patterns.launchAsync
import com.vitusortner.patterns.networking.ApiClient
import com.vitusortner.patterns.networking.model.Image
import com.vitusortner.patterns.util.Logger
import com.vitusortner.patterns.util.SharedPrefs
import kotlinx.coroutines.experimental.Job

class PinsViewModel(
    private val apiClient: ApiClient,
    private val sharedPrefs: SharedPrefs
) : ViewModel() {

    private val log = Logger(javaClass)

    private val job = Job()

    //

    private val _pins = MutableLiveData<List<Image>>()
    val pins: LiveData<List<Image>>
        get() = _pins

    fun fetchPins() {
        val token = sharedPrefs.token ?: run {
            log.w("Not logged in!")
            return
        }

        launchAsync(job) {
            try {
                val response =
                    apiClient.images(token.value).await()
                val images = response.body()?.map { it.image.original } ?: return@launchAsync

                _pins.value = images
            } catch (error: Throwable) {
                log.w("Error occured while fetching images.", error)
            }
        }
    }

    //

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    //

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

//sealed class Response<T> {
//    data class Loading<T>(val loading: Boolean) : Response<T>()
//    data class Result<T>(val items: T)
//    data class Error(val throwable: Throwable)
//}

class PinsViewModelFactory(
    private val apiClient: ApiClient,
    private val sharedPrefs: SharedPrefs
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PinsViewModel(apiClient, sharedPrefs) as T
    }
}