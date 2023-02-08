package com.netflix.conductor.tasks.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.tasks.http.providers.RestTemplateProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.netflix.conductor.common.metadata.tasks.TaskType.TASK_TYPE_HTTP_WAIT;
import static com.netflix.conductor.model.TaskModel.Status.IN_PROGRESS;


@Component(TASK_TYPE_HTTP_WAIT)
public class HttpWaitTask extends HttpTask {

  private final ExecutionDAOFacade executionDAOFacade;

  @Autowired
  public HttpWaitTask(RestTemplateProvider restTemplateProvider, ObjectMapper objectMapper,
                      ExecutionDAOFacade executionDAOFacade) {
    super(TASK_TYPE_HTTP_WAIT, restTemplateProvider, objectMapper);
    this.executionDAOFacade = executionDAOFacade;
  }

  @Override
  public void start(WorkflowModel workflow, TaskModel task, WorkflowExecutor executor) {
    //if scheduled by conductor
    if (executor != null) {
      task.setStatus(IN_PROGRESS);
      return;
    }
    //trigger by http request if executor is null
    super.start(workflow, task, null);
    executionDAOFacade.updateTask(task);
  }
}
