So, I've forked LTE Cleaner, that was originally developed by @TheRedSpy15. The reason why I'm sharing this information with you is because
the app has been permanently suspended from the Play Store due to a disagreement with Google's policy. This project initially started as a learning opportunity
for @TheRedSpy15 back in 2018 when he was in 10th grade. After putting ads on the Play Store variant, it becomes a source of monthly income
to cover his student debt bill while he was in college. Unfortunately, the app was suspended due to a screenshot that apparently deceived users. As a result,
@TheRedSpy15 has given up on the project and is unable to continue supporting the F-Droid variant without some compensation. You can read more in here: https://github.com/TheRedSpy15/LTECleanerFOSS
Despite hitting 100k users, he is no longer able to devote time to it. Thank you for your support.
*(btw message above was rephrased using ChatGPT)*

## LTECleanerFOSS
<img src="https://imgur.com/ykSLpTS.png" width="300">

![Android CI](https://github.com/TheRedSpy15/mdp43140/workflows/Android%20CI/badge.svg)
[![GitHub issues](https://img.shields.io/github/issues/mdp43140/LTECleanerFOSS?color=red)](https://github.com/mdp43140/LTECleanerFOSS/issues)
[![Crowdin](https://badges.crowdin.net/lte-cleaner/localized.svg)](https://crowdin.com/project/lte-cleaner)
[![GitHub license](https://img.shields.io/github/license/mdp43140/LTECleanerFOSS?color=lightgrey)](/blob/master/LICENSE)

![Alt](https://repobeats.axiom.co/api/embed/e57b4b0c0e47daffc4e7feb4cff54fa6a1bc4120.svg "Repobeats analytics image")

***The last Android cleaner you will ever need!***

Tired of the abundance of phone cleaners on the Play Store? Tired of
them being extremely shady? Tired of them doing nothing? Tired of ads?
Tired of having to pay? Me too.

There are simply way too many apps out there that claim to "speed up your device". In reality, they don't do anything.
LTE Cleaner only aims to clean your phone by removing safe to delete files, which not only frees up a lot of space, but also improve your privacy. Since LTE Cleaner removes .log files, which well, log what you do.

__LTE Cleaner is 100% free, open source, ad free, and deletes everything it claims to.__

<details><summary>Notice</summary>
May 13th, 2022: This project is temporaily on maintainence mode. @TheRedSpy15 is currently working on projects for my startup and all his coding time is going into that for now. He will accept just about any pull request. But no real updates for some time. This will not be as long of a break as last time. But probably a couple months
April 4th, 2023: Google has permanently banned LTE Cleaner for "deceiving" users, read more here: https://github.com/TheRedSpy15/LTECleanerFOSS
</details>

## Install
[<img src="https://f-droid.org/badge/get-it-on.png"
     alt="Get it on F-Droid (official version)"
     height="90">](https://f-droid.org/packages/theredspy15.ltecleanerfoss)
[<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png"
    alt="Get it on Google Play (banned from Play Store)"
    height="90">](https://play.google.com/store/apps/details?id=theredspy15.ltecleanerfoss)
[Or build it yourself](#compiling-the-app)

## Features
- Clipboard clearing
- Easy whitelists

Cleans:
- Empty folders
- Logs
- Temporary files
- Caches
- Advertisement folders

Upcoming features:
- (Fixing) Daily cleans (WorkManager?)
- Clean SD card
- Shortcuts
- Community made filters

## Screenshots
<img src="/Screenshots/Screenshot_20211110-234739_LTE Cleaner_framed.png" width="200">
<!-- TODO: put new screenshots here (especially the one with some material design 3 changes) -->

## Join the team
  * Test the app with different devices
  * Report issues and feature requests in the [issue tracker](https://github.com/mdp43140/LTECleanerFOSS/issues)
  * Create a [Pull Request](https://opensource.guide/how-to-contribute/#opening-a-pull-request)
  * Translate this app into more languages

Changes are first made on the master branch, then cherry picked on to the others

## Compiling the app
Just go to the project root directory, and run this command: ```./gradlew assembleRelease``` (if you're using Windows, change `./gradlew` to `gradlew.bat`)

#### The Team
<a href="https://github.com/TheRedSpy15/LTECleanerFOSS/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=TheRedSpy15/LTECleanerFOSS" />
</a>

</details>

**Privacy Policy:** [Link](https://cdn.rawgit.com/TheRedSpy15/LTECleanerFOSS/d9522c76/privacy_policy.html)

[![license](https://imgur.com/QQlcEVT.png)](https://www.gnu.org/licenses/gpl-3.0.en.html)