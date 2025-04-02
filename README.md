# flow

Flow is a tool to help programmers visualize and understand their workflow.

Flow is part of a pair of projects designed to help developers visualize and understand
_how_ they work.

This readme is very light, and is intended only for a quickstart. The heavier and more
complete documentation can be found at flow's sister project, [vnitpick](https://github.com/wethinkcode/nitpick2/wiki).

[![Maven Central](https://maven-badges.herokuapp.com/sonatype-central/za.co.wethinkcode/flow/badge.png)](https://central.sonatype.com/artifact/za.co.wethinkcode/flow)

# Four Types of Projects

Flow is the _recorder_ of the vnitpick system. Its job is to notice various developer activities and quietly write them to a committed log that can later be viewed and analyzed by the developer using the vnitpick application.

There are four types of git repository one might wish to put Flow in to: Authoring, Starter, Student, and Developer.

## Authoring Repositories

WeThinkCode is a school, and we set exercises for students. Those exercises are
designed and developed in authoring repositories. These repos normally contain, as sub-folders, multiple exercise projects, as well as detailed instructions and possibly textual curriculum material.

Authoring repos normally do not have the Flow monitor turned on at all, even though Flow is installed in their sub-folders.

## Starter Repositories

These are the repos that students fork from in order to work a given exercise or
assessment. At WeThinkCode, these are created automatically using the subfolders mentioned from authoring repos. It is also possible to create a starter repo manually, w/o using an authoring repo.

Flow _is_ turned on in these repos, but not initialized. (We wanted students to see all and only their own workflow, not the workflow that went in to creating their exercise.)

## Student Repositories

Students fork from a starter repo to create their own git repositories.

Recall that the starter repo has Flow turned on, but uninitialized. When the student first calls the exercise's `main()` method, Flow will initialize, and thereafter monitor tests & runs.

## Developer Repositories

Ordinary developers, neither students nor authors, may wish to track and visualize their own workflows.

Developer repos are, essentially, the same as student repos, created manually, for tracking work projects, not student exercises.

# Installing Flow

Getting Flow correctly into a repository depends on what kind of repository we want, but all actual Flow-based projects are variants on the developer repository, so we will start there.

## Installing Into A Developer or Starter Repository

There are four steps to installing flow in a developer or Starter repository.

1) Add a dependency to the project's maven or gradle to include Flow.
2) If your project does not already use slf4j, optionally add a dependency on slf4j-nop.
3) Add a .gitignore entry to ignore the temporary log files created by Flow.
4) Add a static block to the class(es) containing the project's `main()` method.

----
IMPORTANT: You should do all four of these steps in a single session, and should not commit or push until the end of the sequence
----

### The Flow Dependency

[![Maven Central](https://maven-badges.herokuapp.com/sonatype-central/za.co.wethinkcode/flow/badge.png)](https://central.sonatype.com/artifact/za.co.wethinkcode/flow)

The badge above reflects the current version.

Add Flow as a dependency in gradle:

```kotlin
dependencies {
    implementation("za.co.wethinkcode:flow:[VERSION]")
}
```

Add Flow as a dependency in maven:

```xml
    <dependencies>
        <dependency>
            <groupId>za.co.wethinkcode</groupId>
            <artifactId>flow</artifactId>
            <version>[VERSION]</version>
        </dependency>
    </dependencies>
```

### The SLF4J-nop Dependency

This step is only needed if a) your project does not already use SLF4J, and b) you are, like us, people who do not tolerate warning messages.

[![Maven Central](https://maven-badges.herokuapp.com/sonatype-central/org.slf4j/slf4j-nop/badge.png)](https://central.sonatype.com/artifact/org.slf4j/slf4j-nop)

(Note: The slf4j-nop version badge is not updating correctly. As of 2025-04-02, we have been using 2.0.17 as the version.)

Gradle
---

```kotlin
dependencies {
    implementation("org.slf4j:slf4j-nop:[VERSION]")
}
```

Maven
---

```xml
    <dependencies>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j.nop</artifactId>
            <version>[VERSION]</version>
        </dependency>
    </dependencies>
```

### Adding to .gitignore

To prevent merges and collisions, Flow creates a local log file. That file is
renamed and quietly added and committed at commit-time.

We want git to always ignore the local log.

```gitignore
# Flow Library Temp Files
*.flot
```

### Adding the static block in main

The fourth step is to add a static block to the file(s) holding the app's `main()` method.

```java
static {
    new Recorder().logRun();
}
```

# The Initialization Process
Once these steps are completed in a developer or starter repo, the resulting project is in an "uninitialized" state. It will actually initialize itself locally the first time the repo's `main()` is run.

During initialization, Flow does three things:
1) It creates the first entry in the log, at location `.lms/flow/[branch]_[shortemail].flot`
2) It adds two files under `src/test/resources`, which force JUnit runs to call the Flow recorder.
3) It adds two client-side git hooks, one for pre-commit and one for post-commit.

# Installing Flow In Authoring Repositories

Authors don't want to track flow as they are working with the exercise folders.

Authoring repositories have a top-level folder and one or more sub-folders, each of which represents a single student exercise. To properly install flow, we follow the developer repository steps in each exercise sub-folder.

If a single file, `author.txt` is placed in the top-level folder of the repository, flow will neither initialize nor track in any of the sub-folders.


