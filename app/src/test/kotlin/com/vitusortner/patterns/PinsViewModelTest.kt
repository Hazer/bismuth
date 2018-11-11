package com.vitusortner.patterns

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vitusortner.patterns.networking.ApiClient
import com.vitusortner.patterns.networking.model.I
import com.vitusortner.patterns.networking.model.Image
import com.vitusortner.patterns.networking.model.O
import com.vitusortner.patterns.ui.pins.PinsViewModel
import com.vitusortner.patterns.util.SharedPrefs
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PinsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testToken = Token("token123")

    private val testImages =
        listOf(Image(url = "123", width = 1, height = 1), Image(url = "321", width = 2, height = 2))

    private val mockApiClient: ApiClient = mockk {
        every { images(testToken.value) } returns CompletableDeferred(testImages.wrap())
    }
    private val mockSharedPrefs: SharedPrefs = mockk {
        every { token } returns testToken
    }

    private lateinit var underTest: PinsViewModel

    @Before
    fun setUp() {
        underTest = PinsViewModel(mockApiClient, mockSharedPrefs, TestDispatchers)
    }

    @Test
    fun `should return images`() {
        underTest.fetchPins()

        val value = LiveDataTestUtil.getValue(underTest.pins)

        value shouldEqual testImages
    }

    private fun List<Image>.wrap() = this.map { I(O(it)) }

}
