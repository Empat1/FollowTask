package ru.empat.followtask.domain

import kotlinx.coroutines.flow.Flow
import ru.empat.followtask.domain.model.Settings
import ru.empat.followtask.domain.model.User

interface Repository {

    fun preferencesDataSource() : Flow<Settings>
    fun localDataSource() : Flow<List<User>>
    fun remoteDataSource() : Flow<List<User>>
}