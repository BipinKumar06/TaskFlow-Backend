package com.bipin.tasks.controllers;

import com.bipin.tasks.domain.dto.TaskDto;
import com.bipin.tasks.domain.entities.Task;
import com.bipin.tasks.mappers.TaskMapper;
import com.bipin.tasks.services.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/task-lists/{taskListId}/tasks")
public class TasksController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;


    public TasksController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    List<TaskDto> getTasksByTaskListId(@PathVariable("taskListId") UUID taskListId){
        List<Task> tasks = taskService.getListOfTasks(taskListId);
        return tasks.stream().map(taskMapper::toDto).toList();
    }

    @PostMapping
    TaskDto createTaskOfSpecificTaskListGroup(@PathVariable("taskListId") UUID taskListId, @RequestBody TaskDto taskDto){
        Task updatedTask = taskService.createTask(taskListId, taskMapper.fromDto(taskDto));
        return taskMapper.toDto(updatedTask);
    }

    @GetMapping(path = "/{taskId}")
    public Optional<TaskDto> getTask(@PathVariable("taskListId") UUID taskListId, @PathVariable("taskId") UUID taskId){
        return taskService.getTask(taskListId, taskId).map(taskMapper::toDto);
    }

    @PutMapping(path = "/{taskId}")
    public TaskDto updateTask(@PathVariable("taskListId") UUID taskListId, @PathVariable("taskId") UUID taskId, @RequestBody TaskDto taskDto){
        return taskMapper.toDto(taskService.updateTask(taskListId, taskId, taskMapper.fromDto(taskDto)));
    }

    @DeleteMapping(path = "/{taskId}")
    public void deleteTaskOfSpecificTaskListGroup(@PathVariable("taskListId") UUID taskListId, @PathVariable("taskId") UUID taskId){
        taskService.deleteTask(taskListId, taskId);
    }
}
