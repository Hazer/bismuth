package com.vitusortner.patterns.ui.pins

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vitusortner.patterns.LiveDataTestUtil
import com.vitusortner.patterns.TestDispatchers
import com.vitusortner.patterns.Token
import com.vitusortner.patterns.networking.ApiClient
import com.vitusortner.patterns.networking.Response
import com.vitusortner.patterns.networking.model.I
import com.vitusortner.patterns.networking.model.Image
import com.vitusortner.patterns.networking.model.O
import com.vitusortner.patterns.util.SharedPrefs
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

@RunWith(JUnit4::class)
class PinsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testToken = Token("token123")

    private val testImages =
        listOf(Image(url = "123", width = 1, height = 1), Image(url = "321", width = 2, height = 2))

    private val mockApiClient: ApiClient = mockk()

    private val mockSharedPrefs: SharedPrefs = mockk {
        every { token } returns testToken
    }

    private lateinit var underTest: PinsViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        underTest = PinsViewModel(mockApiClient, mockSharedPrefs, TestDispatchers)
    }

    @Test
    fun `should return images`() {
        every { mockApiClient.images(testToken.value) } returns CompletableDeferred(testImages.wrap())

        underTest.fetchPins()

        val value = LiveDataTestUtil.getValue(underTest.pins)

        value shouldEqual Response.Success(testImages)
    }

    @Test
    fun `should return error`() {
        val exception = IOException("Networking error")
        every { mockApiClient.images(testToken.value) } throws exception

        underTest.fetchPins()

        val value = LiveDataTestUtil.getValue(underTest.pins)

        value shouldEqual Response.Error(exception)
    }

    @Test
    fun `should start with loading`() {
        every { mockApiClient.images(testToken.value) } returns CompletableDeferred(testImages.wrap())

        underTest.pins.observeForever {
            it shouldBeInstanceOf Response.Loading::class
        }

        underTest.fetchPins()
    }

    private fun List<Image>.wrap() = this.map { I(O(it)) }

}
