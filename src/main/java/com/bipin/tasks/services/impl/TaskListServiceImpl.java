package com.bipin.tasks.services.impl;

import com.bipin.tasks.domain.entities.TaskList;
import com.bipin.tasks.repositories.TaskListRepository;
import com.bipin.tasks.services.TaskListService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;

    TaskListServiceImpl(TaskListRepository taskListRepository){
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<TaskList> listTaskLists() {
        return taskListRepository.findAll();
    }

    @Override
    public TaskList createTaskList(TaskList taskList){
        if(null != taskList.getId()){
            throw new IllegalArgumentException("Task list Already has an Id!");
        }
        if(null == taskList.getTitle() || taskList.getTitle().isBlank()){
            throw new IllegalArgumentException("Task list title must be present!");
        }
        taskList.setId(UUID.randomUUID());
        return taskListRepository.save(new TaskList(
                null,
                taskList.getTitle(),
                taskList.getDescription(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        ));
    }

    @Override
    public Optional<TaskList> getTaskList(UUID id){
        if(null == id){
            throw new IllegalArgumentException("id must be present!");
        }
        return taskListRepository.findById(id);
    }

    @Override
    public TaskList updateTaskList(UUID taskListId, TaskList taskList){
        if(null == taskListId){
            throw new IllegalArgumentException("id must be present!");
        }

        if(!Objects.equals(taskListId, taskList.getId())){
        throw new IllegalArgumentException("Attempting to change task list Id, this is not permitted");
        }
        TaskList existingTaskList = taskListRepository.findById(taskListId).orElseThrow(() -> new IllegalArgumentException("task list is not found which trying to update"));

        if(null == taskList.getTitle() || taskList.getTitle().isBlank()){
            throw new IllegalArgumentException("Task list title must be present!");
        }
        existingTaskList.setTitle(taskList.getTitle());
        existingTaskList.setDescription(taskList.getDescription());
        existingTaskList.setUpdated(LocalDateTime.now());
        return taskListRepository.save(existingTaskList);
    }

    @Transactional
    @Override
    public void deleteTaskList(UUID taskListId){
        if(null == taskListId){
            throw new IllegalArgumentException("id must be present!");
        }
        taskListRepository.deleteById(taskListId);
    }
}
