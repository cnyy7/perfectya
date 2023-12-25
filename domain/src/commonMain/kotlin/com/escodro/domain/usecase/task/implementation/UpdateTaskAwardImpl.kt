package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.usecase.task.LoadTask
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.domain.usecase.task.UpdateTaskAward

internal class UpdateTaskAwardImpl(
    private val loadTask: LoadTask,
    private val updateTask: UpdateTask,
) : UpdateTaskAward {

    override suspend fun invoke(taskId: Long, award: Int) {
        val task = loadTask(taskId) ?: return
        val updatedTask = task.copy(award = award)
        updateTask(updatedTask)
    }
}
