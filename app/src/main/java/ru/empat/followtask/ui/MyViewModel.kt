package ru.empat.followtask.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import ru.empat.followtask.domain.Repository
import ru.empat.followtask.domain.model.Settings
import ru.empat.followtask.domain.model.User
import ru.empat.followtask.ui.Mappers.mapToUserDisplayModel

class MyViewModel(val repository: Repository) : ViewModel(){

    fun getCombinedUserFlow(): Flow<List<UserDisplayModel>> {
        return repository.remoteDataSource().combine(repository.localDataSource()){ a, b ->
            (a + b).distinctBy{it.id}
        }.zip(repository.preferencesDataSource()){ a, b ->
            if(b.showInactiveUsers){
                a
            }else{
                a.filter { it.isActive }
            }
        }.map { list ->
            list.map{ it.mapToUserDisplayModel() }
        }
    }
}