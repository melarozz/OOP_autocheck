# OOP_autocheck
Console application for the OOP teacher, allowing on the basis of configuration file of the specified format to collect statistics about GitHub Repositories of course students.

# How to use?

Go to src/main/resources/configuration.groovy
- Add/remove course tasks
- Add/remove students' names, repos, assigned tasks, deadlines
- Specify each student's group
- You can change the name of branch that is being checked for everyone

After updating configuration file, go to arc/main/java/ru/nsu/yakovleva/util/GitHubCommitCounter.java
- Paste your GitHub token in variable AUTH_TOKEN for retrieving commits activity
- You can generate your token following these instructions https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens
- Be careful, do not make commits with your token

Then, run src/main/java/ru/nsu/yakovleva/App.java
Wait until every student is checked 
Go to src/main/resources/results/output.html and open it in browser

Now you can mark deadlines for each student, see their points and GitHub activity in the bottom table

Also, you can change src/main/resources/template.ftl if you want to make some changes in html
