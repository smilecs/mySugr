package com.smile.logbook

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.smile.logbook.domain.BgUnit
import com.smile.logbook.domain.Log
import com.smile.logbook.domain.LogRepository
import com.smile.logbook.ui.LogViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: LogRepository

    private lateinit var viewModel: LogViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = LogViewModel(repository, dispatcher = TestCoroutineDispatcher())
    }

    @Test
    fun `saveLog should insert entity when bgValue is not blank`() = runTest {
        val value = "5.5"
        viewModel.updateBgValueInput(value)

        val expectedLog = Log(value = value.toDouble(), bg = BgUnit.Mg)
        whenever(repository.insertEntity(expectedLog)).thenReturn(Unit)

        viewModel.saveLog()

        verify(repository).insertEntity(expectedLog)
        assertEquals("", viewModel.bgValue.first())
    }
}
