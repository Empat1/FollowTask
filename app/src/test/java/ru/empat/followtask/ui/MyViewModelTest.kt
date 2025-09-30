package ru.empat.followtask.ui

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.empat.followtask.domain.Repository
import ru.empat.followtask.domain.model.Settings
import ru.empat.followtask.domain.model.User

class MyViewModelTest {

    val repository = mockk<Repository>()

    @Test
    fun getCombinedUserFlow() = runTest {

        val a = flowOf(Settings(true))

        val localMockk = flowOf(
            listOf(
                User(1, "Name1", true),
                User(2, "Name2", true),
                User(4, "Name4", false),
                User(5, "Name5", false),
            )
        )

        val remoteMockk = flowOf(
            listOf(
                User(1, "Name1", true),
                User(2, "Name2", true),
                User(3, "Name3", true),
                User(5, "Name5", false),
            )
        )

        coEvery { repository.preferencesDataSource() } returns a
        coEvery { repository.localDataSource() } returns localMockk
        coEvery { repository.remoteDataSource() } returns remoteMockk

        val viewModel = MyViewModel(repository)
        val flow = viewModel.getCombinedUserFlow()

        val list = listOf(
            UserDisplayModel(id = 1, name = "Name1", isVisible = true),
            UserDisplayModel(id = 2, name = "Name2", isVisible = true),
            UserDisplayModel(id = 3, name = "Name3", isVisible = true),
            UserDisplayModel(id = 5, name = "Name5", isVisible = false),
            UserDisplayModel(id = 4, name = "Name4", isVisible = false)
        )
        flow.collect {
            assertEquals(it, list)
        }
    }

    @Test
    fun getCombinedUserFlowWithDuplicate() = runTest {
        val localMockk = flowOf(
            listOf(
                User(1, "Name", false),
            )
        )

        val remoteMockk = flowOf(
            listOf(
                User(1, "Name2", true),
            )
        )
        val settingMock = flowOf(Settings(true))

        coEvery { repository.preferencesDataSource() } returns settingMock
        coEvery { repository.localDataSource() } returns localMockk
        coEvery { repository.remoteDataSource() } returns remoteMockk

        val viewModel = MyViewModel(repository)
        val actual = viewModel.getCombinedUserFlow()

        val expect = listOf(UserDisplayModel(id = 1, name = "Name2", isVisible = true))

        actual.collect {
            assertEquals(it, expect)
        }
    }

    @Test
    fun getCombinedUserFlowOnlyInactiveUsers() = runTest {
        val a = flowOf(Settings(false))

        val localMockk = flowOf(
            listOf(
                User(1, "Name1", false),
                User(2, "Name2", true),
            )
        )

        val remoteMockk = flowOf(
            listOf(
                User(3, "Name3", true),
                User(4, "Name4", false),
            )
        )

        coEvery { repository.preferencesDataSource() } returns a
        coEvery { repository.localDataSource() } returns localMockk
        coEvery { repository.remoteDataSource() } returns remoteMockk

        val viewModel = MyViewModel(repository)
        val flow = viewModel.getCombinedUserFlow()

        val list = listOf(
            UserDisplayModel(id = 3, name = "Name3", isVisible = true),
            UserDisplayModel(id = 2, name = "Name2", isVisible = true),
        )
        flow.collect {
            assertEquals(it, list)
        }
    }
}