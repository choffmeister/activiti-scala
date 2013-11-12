name := "activiti-scala"

version := "0.0.0-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-language:postfixOps", "-encoding", "utf8")

libraryDependencies ++= Seq(
  "org.activiti" % "activiti-engine" % "5.9",
  "com.h2database" % "h2" % "1.3.173"
)

resolvers ++= Seq(
  "alfresco repo" at "https://maven.alfresco.com/nexus/content/groups/public"
)

EclipseKeys.withSource := true
