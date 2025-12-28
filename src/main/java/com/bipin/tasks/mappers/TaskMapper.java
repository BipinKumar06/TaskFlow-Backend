package com.bipin.tasks.mappers;

import com.bipin.tasks.domain.dto.TaskDto;
import com.bipin.tasks.domain.entities.Task;

public interface TaskMapper {
    Task fromDto(TaskDto taskDto);

    TaskDto toDto(Task task);
}
