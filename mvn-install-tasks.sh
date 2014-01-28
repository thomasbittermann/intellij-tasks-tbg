#!/bin/sh

IDEA13_INSTALL_DIR=$HOME/devTools/idea-IU-13

version=13.0.1_133.331

mvn install:install-file -DgroupId=com.intellij            -Dversion=$version -Dpackaging=jar -DartifactId=annotations -Dfile=$IDEA13_INSTALL_DIR/lib/annotations.jar
mvn install:install-file -DgroupId=com.intellij            -Dversion=$version -Dpackaging=jar -DartifactId=openapi     -Dfile=$IDEA13_INSTALL_DIR/lib/openapi.jar
mvn install:install-file -DgroupId=com.intellij            -Dversion=$version -Dpackaging=jar -DartifactId=util        -Dfile=$IDEA13_INSTALL_DIR/lib/util.jar
mvn install:install-file -DgroupId=com.intellij.restClient -Dversion=$version -Dpackaging=jar -DartifactId=restClient  -Dfile=$IDEA13_INSTALL_DIR/plugins/restClient/lib/restClient.jar
mvn install:install-file -DgroupId=com.intellij.tasks      -Dversion=$version -Dpackaging=jar -DartifactId=tasks-api   -Dfile=$IDEA13_INSTALL_DIR/plugins/tasks/lib/tasks-api.jar
