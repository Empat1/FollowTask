package ru.empat.followtask.ui

import ru.empat.followtask.domain.model.User

object Mappers {
    fun User.mapToUserDisplayModel(): UserDisplayModel {
        return UserDisplayModel(
            id = this.id,
            name = this.name,
            isVisible = this.isActive
        )
    }
}