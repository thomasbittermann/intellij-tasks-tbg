/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.tasks.tbg;

import com.intellij.openapi.project.Project;
import com.intellij.tasks.TaskRepository;
import com.intellij.tasks.TaskState;
import com.intellij.tasks.config.BaseRepositoryEditor;
import com.intellij.tasks.config.TaskRepositoryEditor;
import com.intellij.tasks.impl.BaseRepositoryType;
import com.intellij.util.Consumer;
import icons.TBGIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.EnumSet;

public class TBGRepositoryType extends BaseRepositoryType<TBGRepository> {

    @NotNull
    @Override
    public String getName() {
        return "The Bug Genie";
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return TBGIcons.TBGIcon16;
    }

    @NotNull
    @Override
    public TaskRepository createRepository() {
        return new TBGRepository(this);
    }

    @Override
    public Class<TBGRepository> getRepositoryClass() {
        return TBGRepository.class;
    }

    @NotNull
    @Override
    public TaskRepositoryEditor createEditor(final TBGRepository repository,
                                             final Project project,
                                             final Consumer<TBGRepository> changeListener) {
        return new BaseRepositoryEditor(project, repository, changeListener);
    }

    @Override
    public EnumSet<TaskState> getPossibleTaskStates() {
        return EnumSet.of(TaskState.SUBMITTED, TaskState.OPEN, TaskState.RESOLVED, TaskState.OTHER);
    }

}
