name := "activiti-scala"

version := "0.0.0-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-language:postfixOps", "-encoding", "utf8")

libraryDependencies ++= Seq(
  "org.activiti" % "activiti-engine" % "5.14",
  "org.activiti" % "activiti-bpmn-model" % "5.14",
  "com.h2database" % "h2" % "1.3.173",
  "junit" % "junit" % "4.13" % "test",
  "org.specs2" %% "specs2" % "2.2.3" % "test"
)

resolvers ++= Seq(
  "alfresco repo" at "https://maven.alfresco.com/nexus/content/groups/public"
)

EclipseKeys.withSource := true
