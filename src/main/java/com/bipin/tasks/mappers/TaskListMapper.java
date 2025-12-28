package com.bipin.tasks.mappers;

import com.bipin.tasks.domain.dto.TaskListDto;
import com.bipin.tasks.domain.entities.TaskList;

public interface TaskListMapper {
    TaskList fromDto(TaskListDto taskListDto);

    TaskListDto toDto(TaskList taskList);
}
