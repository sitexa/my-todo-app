/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sitexa.app.taskdetail

import com.google.common.base.Strings
import com.sitexa.app.data.Task
import com.sitexa.app.data.source.TasksDataSource
import com.sitexa.app.data.source.TasksRepository

/**
 * Listens to user actions from the UI ([TaskDetailFragment]), retrieves the data and updates
 * the UI as required.
 */
class TaskDetailPresenter(private val mTaskId: String?,
                          tasksRepository: TasksRepository,
                          taskDetailView: TaskDetailContract.View) : TaskDetailContract.Presenter {

    private val mTasksRepository: TasksRepository

    private val mTaskDetailView: TaskDetailContract.View

    init {
        mTasksRepository = tasksRepository
        mTaskDetailView = taskDetailView
        mTaskDetailView.setPresenter(this)
    }

    override fun start() {
        openTask()
    }

    private fun openTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask()
            return
        }

        mTaskDetailView.setLoadingIndicator(true)
        mTasksRepository.getTask(mTaskId!!, object : TasksDataSource.GetTaskCallback {
            override fun onTaskLoaded(task: Task) {
                // The view may not be able to handle UI updates anymore
                if (!mTaskDetailView.isActive()) {
                    return
                }
                mTaskDetailView.setLoadingIndicator(false)
                showTask(task)
            }

            override fun onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mTaskDetailView.isActive()) {
                    return
                }
                mTaskDetailView.showMissingTask()
            }
        })
    }

    override fun editTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask()
            return
        }
        mTaskDetailView.showEditTask(mTaskId!!)
    }

    override fun deleteTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask()
            return
        }
        mTasksRepository.deleteTask(mTaskId!!)
        mTaskDetailView.showTaskDeleted()
    }

    override fun completeTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask()
            return
        }
        mTasksRepository.completeTask(mTaskId!!)
        mTaskDetailView.showTaskMarkedComplete()
    }

    override fun activateTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask()
            return
        }
        mTasksRepository.activateTask(mTaskId!!)
        mTaskDetailView.showTaskMarkedActive()
    }

    private fun showTask(task: Task) {
        val title = task.title
        val description = task.description

        if (Strings.isNullOrEmpty(title)) {
            mTaskDetailView.hideTitle()
        } else {
            mTaskDetailView.showTitle(title!!)
        }

        if (Strings.isNullOrEmpty(description)) {
            mTaskDetailView.hideDescription()
        } else {
            mTaskDetailView.showDescription(description!!)
        }
        mTaskDetailView.showCompletionStatus(task.completed)
    }
}
