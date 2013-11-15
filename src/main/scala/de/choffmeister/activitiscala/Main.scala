package de.choffmeister.activitiscala

import org.activiti.engine._
import org.activiti.engine.runtime._
import org.activiti.engine.task._
import org.activiti.bpmn.converter._
import scala.collection.JavaConversions._
import scala.annotation.tailrec
import org.activiti.engine.impl.util.ClockUtil
import java.util.Date

object Main {
  import de.choffmeister.activitiscala.ProcessEngine._

  def runTimeMap = Map(
    "writeReportTask" -> 10,
    "verifyReportTask" -> 20
  )

  def main(args: Array[String]) {
    repoService.createDeployment().addClasspathResource("test1.bpmn20.xml").deploy()

    val iterations = 1
    val startTime = System.currentTimeMillis
    for (i <- 1 to iterations) {
      ClockUtil.setCurrentTime(new Date(0))
      val pi = runtimeService.startProcessInstanceByKey("financialReport")
      simulate(pi)
    }

    val endTime = System.currentTimeMillis

    println(s"One execution took ${(endTime - startTime) / iterations}ms")

    val processInstances = historyService.createHistoricProcessInstanceQuery().list()

    for (pi <- processInstances) {
      val tasks = historyService.createHistoricTaskInstanceQuery().processInstanceId(pi.getId).list()
      
      for (t <- tasks) {
        println(s"Task ${t.getName}: ${t.getStartTime} - ${t.getEndTime}")
      }
    }
  }
  
  def simulate(pi: ProcessInstance): Unit = {
    var gantt = Map.empty[String, (Long, Long)]
    var now = 0L
    
    val iterable = new OpenTasksIterable(taskService)
    
    for (tasks <- iterable) {
      val task = tasks.get(0)
      
      ClockUtil.setCurrentTime(new Date(now))
      now += 1000 * 60 * 60 * 24
      taskService.claim(task.getId, "fozzie")
      ClockUtil.setCurrentTime(new Date(now))
      now += 1000 * 60 * 60 * 24
      
      taskService.complete(task.getId)
    }
  }
  
  /**
   * Returns the list of all available tasks on every iteration. Might loop
   * infinitely if the runtime state is not manipulated.
   */
  class OpenTasksIterable(val ts: TaskService) extends Iterable[List[Task]] {
    def iterator: Iterator[List[Task]] = new OpenTasksIterator(ts)
    
    class OpenTasksIterator(val ts: TaskService) extends Iterator[List[Task]] {
      private var available: Option[List[Task]] = None

      def hasNext: Boolean = available match {
      case Some(l) =>
          l.size > 0
        case _ =>
          available = Some(taskService.createTaskQuery().list().toList)
          available.get.size > 0
      }
    
      def next(): List[Task] = available match {
        case Some(l) =>
          available = None
          l
        case _ =>
          throw new Exception()
      }
    }
  }
}
