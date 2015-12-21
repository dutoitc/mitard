# mitard
The Missing Talend Roadside-documentation.

Ever tried. Ever failed. No matter. Try again. Fail again. Fail better. And maybe there is a little light shining more and more at the end of the corridor, the hope to make it better, to measure it, and to make it work. Mitard will help you on this hard road, along the road, as a roadside documentation. Feel free to contribute !

[![Build Status](https://travis-ci.org/dutoitc/mitard.svg?branch=master)](https://travis-ci.org/dutoitc/mitard)

Mitard needs Java 8 and has been tested with Linux and Windows. Checked Talend version is 5.6.1

# Functionalities
* Dashboard: last documentation build, number of routes, services, processes, components, violations.
* Routes, Services and Processes searchable documentation with author name, first and last modification and screenshot
* Dependency graph and searchable one-level text graph
* Violations (general, components), searchable
* Versions check (something pointing on 'latest', and major pointing on 'minors')
* Text search in latest versions

# Usage
## context.properties
Create a new context.properties file:
    talendWorkspacePath=MYPROJECT
    productionPath=out
    blacklist=.*TEMPLATE.*,.*test_.*,.*Copy_of.*,.*MOCK.*,.*Old
    dotPath=/usr/bin
    jiraUrl=issuetracker.myserver.com/outils/jira/browse/
    jiraPrefix=MYJIRA-
    properties=configuration/context.csv
* talendWorkspacePath: path to the project from the Mitard runtime directory
* productionPath: where to produce the Mitard website
* blacklist: regex of process/jobs to be blacklistes
* dotPath: path to dot, for producing the dependency graph
* jiraUrl: URL prefix for Jira in job comments
* jiraPrefix: Jira project, used to detect jira link in job comments
* properties: a file containing context implicit values, whose content will be loaded for data replacements

## Run
    export JAVA_HOME=/my/bin/jdk-1.8
    export PATH=$JAVA_HOME/bin:$PATH
    java -classpath mitard-1.0-SNAPSHOT.jar ch.mno.talend.mitard.Main context.properties



# Contribute
Feel free to contribute by sending pull requests, patches, bugs...
