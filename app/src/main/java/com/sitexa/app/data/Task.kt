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

import java.util.*

/**
 * Immutable model class for a Task.
 */
data class Task(
        val id: String = UUID.randomUUID().toString(),
        val title: String? = null,
        val description: String? = null,
        val completed: Boolean = false) {

    fun getTitleForList(): String? {
        if (title != null && !title.equals("")) return title
        else return description
    }

    fun isEmpty(): Boolean {
        return (title == null || "" == title) && (description == null || "" == description)
    }

    override fun toString() = "Task with title " + title

}
