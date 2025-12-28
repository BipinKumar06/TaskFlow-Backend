package com.bipin.tasks.services.impl;

import com.bipin.tasks.domain.entities.Task;
import com.bipin.tasks.domain.entities.TaskList;
import com.bipin.tasks.domain.entities.TaskPriority;
import com.bipin.tasks.domain.entities.TaskStatus;
import com.bipin.tasks.repositories.TaskListRepository;
import com.bipin.tasks.repositories.TaskRepository;
import com.bipin.tasks.services.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<Task> getListOfTasks(UUID taskListId) {
        return taskRepository.findByTaskListId(taskListId)
                .stream()
                .sorted(Comparator.comparing(task -> task.getPriority().ordinal()))
                .toList();
    }

    @Override
    public Task createTask(UUID taskListId, Task task){
        if(null == taskListId){
            throw  new IllegalArgumentException("task group must be present");
        }
        UUID id = task.getId();
        if(null != id){
            throw new IllegalArgumentException("task id must be null");
        }

        if(null == task.getTitle() || task.getTitle().isBlank()){
            throw new IllegalArgumentException("Task list title must be present!");
        }

        TaskPriority priority = Optional.ofNullable(task.getPriority())
                        .orElse(TaskPriority.MEDIUM);
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new IllegalArgumentException("task list group must present"));

        return taskRepository.save(new Task(
                null,
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                TaskStatus.OPEN,
                priority,
                taskList,
                LocalDateTime.now(),
                LocalDateTime.now()
        ));
    }

    @Override
    public Optional<Task> getTask(UUID taskListId, UUID taskId){
        return taskRepository.findByTaskListIdAndId(taskListId, taskId);
    }

    @Override
    public Task updateTask(UUID taskListId, UUID taskId, Task task) {
        if (null == taskListId) {
            throw new IllegalArgumentException("task group must be present");
        }
        if (null == taskId) {
            throw new IllegalArgumentException("task id cannot be null");
        }

        if (!Objects.equals(taskId, task.getId())) {
            throw new IllegalArgumentException("trying to change the id which is not permitted");
        }

        if (null == task.getTitle() || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task list title must be present!");
        }

        Task exisingTask = taskRepository.findByTaskListIdAndId(taskListId, taskId)
                .orElseThrow(() -> new IllegalArgumentException("task is not present to update"));
        TaskPriority priority = Optional.ofNullable(task.getPriority())
                .orElse(TaskPriority.MEDIUM);
        exisingTask.setTitle(task.getTitle());
        exisingTask.setDescription(task.getDescription());
        exisingTask.setDueDate(task.getDueDate());
        exisingTask.setPriority(priority);
        exisingTask.setStatus(task.getStatus());
        return taskRepository.save(exisingTask);
    }

    @Transactional
    @Override
    public void deleteTask(UUID taskListId, UUID taskId){
        taskRepository.deleteByTaskListIdAndId(taskListId, taskId);
    }
}
