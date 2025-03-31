# flow
Flow is a tool to help programmers visualize and understand their workflow.

Flow is part of a pair of projects designed to help developers visualize and understand 
_how_ they work.

This readme is very light, and is intended only for a quickstart. The heavier and more 
complete documentation can be found at flow's sister project, [vnitpick](https://github.com/wethinkcode/nitpick2/wiki).

## Four Types of Projects

Flow is the _recorder_ of the vnitpick system. Its job is to notice various developer activities 
and quietly write them to a committed log that can late be viewed and analyzed by the developer using 
the vnitpick application.

There are four types of git repository one might wish to put Flow in to: Authoring, Starter, Student, and Developer.


### Authoring Repositories

WeThinkCode is a school, and we set exercises for students. Those exercises are 
designed and developed in authoring repositories. These repos normally contain, as sub-folders, 
multiple exercise projects, as well as detailed instructions and possibly textual curriculum material.

Authoring repos normally do not have the Flow monitor turned on at all, even though Flow is installed 
in their sub-folders.

### Starter Repositories

These are the resulting repos that students fork from in order to work a given exercise or 
assessment. At WeThinkCode, these are created automatically using the subfolders mentioned from 
authoring repos. It is also possible to create a starter repo manually, w/o using an authoring repo.

Flow _is_ turned on in these repos, but not initialized, so that it will be turned on when the student forks the repo 
to start their exercise.

### Student Repositories

Students fork from a starter repo to create their own git repositories.

They begin monitoring from the time the exercise's `main()` method is first run.

### Developer Repositories

Ordinary developers, neither students nor authors, may wish to track and visualize their own workflows.

Developer repos are, essentially, the same as student repos, only created manually, and for tracking 
work projects, not student exercises.

