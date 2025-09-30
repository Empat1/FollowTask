package ru.empat.followtask.ui

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.empat.followtask.domain.model.User
import ru.empat.followtask.ui.Mappers.mapToUserDisplayModel

class MappersUnitTest {
    @Test
    fun mapToUserDisplayModel() {
        val user = User(1, "stepan" , true)
        val userDisplayModel = user.mapToUserDisplayModel()
        assertEquals(user.id, userDisplayModel.id)
        assertEquals(user.name, userDisplayModel.name)
        assertEquals(user.isActive, userDisplayModel.isVisible)
    }
}