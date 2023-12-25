package com.escodro.domain.usecase.task

/**
 * Use case to update a task award.
 */
interface UpdateTaskAward {

    /**
     * Updates a task award.
     *
     * @param taskId the task id to be updated
     * @param award the award to be set
     */
    suspend operator fun invoke(taskId: Long, award: Int)
}
