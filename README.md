# EnqueueTestShellSuite-Jenkins-Plugin


[![Dependency Status](https://dependencyci.com/github/AharonShachar/EnqueueTestShellSuites-Jenkins-Plugin/badge)](https://dependencyci.com/github/AharonShachar/EnqueueTestShellSuites-Jenkins-Plugin)

##Prerequisite

1) CloudShell 8.0 and above.

2) Jenkenis server 2.0 and above.



## Installation
1) Download the hpi package.

2) Navigate to the advanced section under the plugins tab in jenkins

3) Upload the hpi file into the "upload plugin" section

4) Restart jenkins

## Configuring plugin in Jenkins
1) Navigate to the main Jenkins settings page

2) Fill all fields under "cloudshell configuration" section.

![Alt text](Pics/mainsetting.png?raw=true)

### Adding build steps
Add new Build step - "TestShell Suite to enqueue"

![Alt text](Pics/stepselector.png?raw=true)

In the "Suite Name" type the name of the requested suite.

![Alt text](Pics/snqStep.png?raw=true)


Aharon
