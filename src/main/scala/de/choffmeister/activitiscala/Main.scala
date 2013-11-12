package de.choffmeister.activitiscala

import org.activiti.engine._
import org.activiti.engine.runtime._
import org.activiti.engine.task._
import scala.collection.JavaConversions._

object Main extends App {
  import de.choffmeister.activitiscala.ProcessEngine._

  repoService.createDeployment().addClasspathResource("test1.bpmn20.xml").deploy()

  val iterations = 10
  val startTime = System.currentTimeMillis
  for (i <- 1 to iterations) runProcessInstance()
  val endTime = System.currentTimeMillis

  println(s"One execution took ${(endTime - startTime) / iterations}ms")

  val processInstances = historyService.createHistoricProcessInstanceQuery().list()
  
  for (pi <- processInstances) {
    println(pi)
    
    println(pi.getStartTime())
    println(pi.getEndTime())
  }

  def runProcessInstance() {
    val processInstance = runtimeService.startProcessInstanceByKey("financialReport")

    completeAllTasks()

    val task2 = taskService.createTaskQuery().taskCandidateGroup("management").list().get(0)

    completeAllTasks()
  }
  
  def completeAllTasks(): Unit = {
    val tasks = taskService.createTaskQuery().list()
    
    for (task <- tasks) {
      taskService.claim(task.getId, "fozzie")
      taskService.complete(task.getId)
    }
  }
}
