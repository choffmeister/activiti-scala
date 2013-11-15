package de.choffmeister.activitiscala

import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import javax.xml.stream.XMLInputFactory
import java.io.FileReader
import scala.collection.JavaConversions._
import org.activiti.engine.impl.util.ReflectUtil
import org.activiti.bpmn.converter.BpmnXMLConverter
import java.io.InputStream
import javax.xml.stream.XMLStreamReader
import org.activiti.bpmn.model._
import java.util.Date
import scala.annotation.tailrec

object BpmnSimulator {
  lazy val converter = new BpmnXMLConverter()
  lazy val xmlInputFactory = XMLInputFactory.newInstance()

  def apply(inputStream: InputStream): BpmnSimulator = {
    val xml = xmlInputFactory.createXMLStreamReader(inputStream)

    BpmnSimulator(xml)
  }

  def apply(xml: XMLStreamReader): BpmnSimulator = {
    new BpmnSimulator(converter.convertToBpmnModel(xml))
  }
}

trait BpmnElementProcessor[+T <: BaseElement] {
  val sim: BpmnSimulator
  val model: BpmnModel
  val element: BaseElement

  def process(t: Token): Unit = {}
  def earliestAction: Date = new Date(0)
}

class StartEventProcessor(val sim: BpmnSimulator, val model: BpmnModel, val element: StartEvent) extends BpmnElementProcessor[StartEvent] {
  override def process(t: Token): Unit = {
    sim.destroyToken(t)
    element.getOutgoingFlows.foreach(sf => sim.createToken(Token(), sf))
  }
}

class EndEventProcessor(val sim: BpmnSimulator, val model: BpmnModel, val element: EndEvent) extends BpmnElementProcessor[EndEvent] {
  override def process(t: Token): Unit = {
    sim.destroyToken(t)
  }
}

class UserTaskProcessor(val sim: BpmnSimulator, val model: BpmnModel, val element: UserTask) extends BpmnElementProcessor[UserTask] {
  override def process(t: Token): Unit = {
    sim.destroyToken(t)
    element.getOutgoingFlows.foreach(sf => sim.createToken(Token(), sf))
  }
}

class SequenceFlowProcessor(val sim: BpmnSimulator, val model: BpmnModel, val element: SequenceFlow) extends BpmnElementProcessor[SequenceFlow] {
  override def process(t: Token): Unit = {
    sim.destroyToken(t)
    sim.createToken(Token(), model.getFlowElement(element.getTargetRef).asInstanceOf[FlowNode])
  }
}

case class Token(val variables: Map[String, Any] = Map.empty)

class BpmnSimulator(val model: BpmnModel) {
  private var tokens = List.empty[(Token, BpmnElementProcessor[BaseElement])]

  def createToken(t: Token, se: BaseElement): Unit =
    tokens = (t, createProcessor(se)) :: tokens
   
  def destroyToken(t: Token): Unit = {
    val pre = tokens.takeWhile(_._1 != t)
    val post = tokens.dropWhile(_._1 != t).tail
    tokens = pre ::: post
  }

  def createProcessor(e: BaseElement): BpmnElementProcessor[BaseElement] = e match {
    case se: StartEvent => new StartEventProcessor(this, model, se)
    case ee: EndEvent => new EndEventProcessor(this, model, ee)
    case sf: SequenceFlow => new SequenceFlowProcessor(this, model, sf)
    case ut: UserTask => new UserTaskProcessor(this, model, ut)
    case _ => throw new Exception(s"Elements of type '${e.getClass.getName}' are not supported")
  }
  
  def iterate(): Boolean = {
    val sorted = tokens.sortWith((a, b) => a._2.earliestAction.compareTo(b._2.earliestAction) < 0)

    sorted match {
      case Nil => false
      case first :: rest =>
        catchAll(first._2.process(first._1))
        true
    }
  }
  
  /**
   * Catches all errors and prints them to std out.
   */
  def catchAll(inner: => Any) {
    try {
      inner
    } catch {
      case e: Throwable => println(e.getMessage)
    }
  }
}

@RunWith(classOf[JUnitRunner])
class DemoSpec extends Specification {
  "Demo" should {
    "work like a charm" in {
      val simulator = BpmnSimulator(getClass.getResourceAsStream("/test1.bpmn20.xml"))
      val se = simulator.model.getProcesses()(0).findFlowElementsOfType(classOf[StartEvent]).get(0)
      
      val iterations = 4000
      
      val time1 = benchmark {
        
        for (i <- 1 to iterations) {
          simulator.createToken(Token(), se)

          while (simulator.iterate()) {
            //println("iterated")
          }
        }
      }
      
      println(s"Isolated: ${time1} seconds")
      println(s"Isolated: ${iterations / time1} simulations per second")
      
      val time2 = benchmark {
        for (i <- 1 to iterations) {
          simulator.createToken(Token(), se)
        }
        
        while (simulator.iterate()) {
          //println("iterated")
        }
      }

      println(s"Concurrent: ${time2} seconds")
      println(s"Concurrent: ${iterations / time2} simulations per second")
      
      ok
    }
  }
  
  def benchmark(inner: => Any): Double = {
    val startTime = System.currentTimeMillis
    inner
    val endTime = System.currentTimeMillis
    
    return (endTime - startTime).toDouble / 1000.0
  }
}