package de.choffmeister.activitiscala

import org.activiti.engine._

object ProcessEngine {
  val processEngine = ProcessEngineConfiguration
    .createProcessEngineConfigurationFromResourceDefault()
    .buildProcessEngine()

  val repoService = processEngine.getRepositoryService
  val runtimeService = processEngine.getRuntimeService
  val taskService = processEngine.getTaskService
  val historyService = processEngine.getHistoryService
}