# Telegram Notifier

Telegram Notifier provides a bridge between Jenkins and Microsoft Teams through the built-in webhook functionality.
- Get success and fail messages about your job

## The purpose

The Jenkins Telegram Notifier plugin was made to share results of a build to a Telegram channel using the bot with access token.


## Pipeline

Telegram Notifier supports Jenkins Pipeline.

### Parameters

- accessToken (required)
  - The access token of the bot ([Get accessToken here](https://core.telegram.org/bots/features#botfather)). 
- chatId (required)
  - The chatId of the chanel ([Get chatId here](https://stackoverflow.com/questions/32423837/telegram-bot-how-to-get-a-group-chat-id)). 
- title (required)
  - The title of the message.
- result (required)
  - Sets notice message (SUCCESS - green, UNSTABLE - yellow, FAILURE - red, ABORTED - grey).
- branchName
  - If set, current branch build will show. 
- commitId
  - If set, current commit build will show.
- description
  - If set, the description will show on the message.
- webUrl
  - If set, the web url will show on the message.
- jobLink
  - If set, the job url will show on the message.
- buildNumber
  - If set, current build number build will show.
- timeZone
  - By default, this will set time zone is UTC ([Get Time Zone here](https://docs.oracle.com/middleware/1221/wcs/tag-ref/MISC/TimeZones.html)).

### Example

```
pipeline {
  agent any

  post {
    always {
      	telegramNotifier accessToken: "${TELEGRAM_ACCESS_TOKEN}", chatId: "${TELEGRAM_CHAT_ID}", title: "${PROJECT_NAME}", jobLink: "${JENKINS_SERVER}/job/${JOB_NAME}/${BUILD_NUMBER}/", branchName: "${BRANCH_NAME}", commitId: "${COMMIT_ID}", description: "${PROJECT_DESCRIPTION}", result: currentBuild.currentResult, webUrl: "${WEBSITE_URL}", buildNumber: currentBuild.number, timeZone: 'Asia/Bangkok'
    }
  }
}
```

### Install Plugin

- Go to your Jenkins Server with url /pluginManager/advanced
- Scroll to section Deploy Plugin
- Select the file hpi in project with path: ../telegram-notifier/target
- Finally click button deploy