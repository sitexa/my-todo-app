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

package com.sitexa.app.data

import android.support.annotation.VisibleForTesting
import android.util.Log
import com.google.common.collect.Lists
import com.sitexa.app.data.source.TasksDataSource
import java.util.*

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
class FakeTasksRemoteDataSource private constructor() : TasksDataSource {

    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        Log.d("fake getTasks", "tasks:" + TASKS_SERVICE_DATA.values)
        callback.onTasksLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values))
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        val task = TASKS_SERVICE_DATA[taskId]
        Log.d("fake getTask", "task:" + task!!.title)
        callback.onTaskLoaded(task!!)
    }

    override fun saveTask(task: Task) {
        Log.d("Fake saveTask", "task:" + task.id)
        TASKS_SERVICE_DATA.put(task.id, task)
    }

    override fun completeTask(task: Task) {
        val completedTask = Task(task.title!!, task.description, task.id, true)
        TASKS_SERVICE_DATA.put(task.id, completedTask)
    }

    override fun completeTask(taskId: String) {
        // Not required for the remote data source.
    }

    override fun activateTask(task: Task) {
        val activeTask = Task(task.id, task.title, task.description, false)
        TASKS_SERVICE_DATA.put(task.id, activeTask)
    }

    override fun activateTask(taskId: String) {
        // Not required for the remote data source.
    }

    override fun clearCompletedTasks() {
        val it = TASKS_SERVICE_DATA.entries.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            if (entry.value.completed) {
                it.remove()
            }
        }
    }

    override fun refreshTasks() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    override fun deleteTask(taskId: String) {
        TASKS_SERVICE_DATA.remove(taskId)
    }

    override fun deleteAllTasks() {
        TASKS_SERVICE_DATA.clear()
    }

    @VisibleForTesting
    fun addTasks(vararg tasks: Task) {
        for (task in tasks) {
            TASKS_SERVICE_DATA.put(task.id, task)
        }
    }


    companion object factory{

        private val TASKS_SERVICE_DATA = LinkedHashMap<String, Task>()

        fun getInstance(): TasksDataSource {
            return FakeTasksRemoteDataSource()
        }

    }
}
