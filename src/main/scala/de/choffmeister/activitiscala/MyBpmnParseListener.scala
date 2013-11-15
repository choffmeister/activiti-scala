package de.choffmeister.activitiscala

import java.util.List

import org.activiti.engine.delegate.DelegateTask
import org.activiti.engine.delegate.TaskListener
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior
import org.activiti.engine.impl.bpmn.parser.BpmnParseListener
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity
import org.activiti.engine.impl.pvm.process.ActivityImpl
import org.activiti.engine.impl.pvm.process.ScopeImpl
import org.activiti.engine.impl.pvm.process.TransitionImpl
import org.activiti.engine.impl.util.xml.Element
import org.activiti.engine.impl.variable.VariableDeclaration

class MyBpmnParseListener extends BpmnParseListener {
  override def parseBoundaryErrorEventDefinition(arg0: Element, arg1: Boolean, arg2: ActivityImpl, arg3: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseBoundarySignalEventDefinition(arg0: Element, arg1: Boolean, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseBoundaryTimerEventDefinition(arg0: Element, arg1: Boolean, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseBusinessRuleTask(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseCallActivity(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseCompensateEventDefinition(arg0: Element, arg1: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseEndEvent(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseEventBasedGateway(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseExclusiveGateway(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseInclusiveGateway(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseIntermediateSignalCatchEventDefinition(arg0: Element, arg1: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseIntermediateThrowEvent(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseIntermediateTimerEventDefinition(arg0: Element, arg1: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseManualTask(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseMultiInstanceLoopCharacteristics(arg0: Element, arg1: Element, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseParallelGateway(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseProcess(arg0: Element, arg1: ProcessDefinitionEntity): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseProperty(arg0: Element, arg1: VariableDeclaration, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseReceiveTask(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseRootElement(arg0: Element, arg1: List[ProcessDefinitionEntity]): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseScriptTask(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseSendTask(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseSequenceFlow(arg0: Element, arg1: ScopeImpl, arg2: TransitionImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseServiceTask(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseStartEvent(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseSubProcess(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseTask(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseTransaction(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    // TODO Auto-generated method stub
  }

  override def parseUserTask(arg0: Element, arg1: ScopeImpl, arg2: ActivityImpl): Unit = {
    val ab = arg2.getActivityBehavior

    ab match {
      case utab: UserTaskActivityBehavior =>
        utab.getTaskDefinition.addTaskListener(TaskListener.EVENTNAME_CREATE, new My())
        utab.getTaskDefinition.addTaskListener(TaskListener.EVENTNAME_COMPLETE, new My())
    }
  }

  class My extends TaskListener {
    override def notify(arg0: DelegateTask): Unit = {
      arg0.setVariable("startedAt", System.currentTimeMillis)
      arg0.setVariable("finishedAt", System.currentTimeMillis)
    }
  }
}
