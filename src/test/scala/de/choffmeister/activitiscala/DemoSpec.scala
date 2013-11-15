package de.choffmeister.activitiscala

import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import javax.xml.stream.XMLInputFactory
import java.io.FileReader
import scala.collection.JavaConversions._
import org.activiti.bpmn.model.StartEvent
import org.activiti.engine.impl.util.ReflectUtil

@RunWith(classOf[JUnitRunner])
class DemoSpec extends Specification {
  "Demo" should {
    "work like a charm" in {
      val converter = new org.activiti.bpmn.converter.BpmnXMLConverter()
      val inputFactory = XMLInputFactory.newInstance()
      val file = this.getClass().getClassLoader().getResourceAsStream("test1.bpmn20.xml")
      val xml = inputFactory.createXMLStreamReader(file)
      val model = converter.convertToBpmnModel(xml)
    
      model.getProcesses must haveSize(1)

      for (p <- model.getProcesses) {
        val se = p.getFlowElements() ++ Seq()
        
        println(se)
      }
      
      ok
    }
  }
}