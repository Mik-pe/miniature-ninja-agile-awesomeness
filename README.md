miniature-ninja-agile-awesomeness
=================================

Simply the best agile programme for time reporting ever built. This is a project made by students at Linköpings University.

Courtesy of Karl-Johan Krantz:

Contributions
=================================
How to contribute

This project uses git, and is currently hosted on Github at https://github.com/Magrit/miniature-ninja-agile-awesomeness
Although a goal would be to have a perfect git history, multiple of the projects participants are not familiar with the complications of git. Something that can/will result in horrible git commits and amends.

How to
=================================
If you do not have git setup properly, please follow the following guides

https://help.github.com/articles/set-up-git

https://help.github.com/articles/generating-ssh-keys

To fetch the repository to start developing
```
git clone git@github.com:Magrit/miniature-ninja-agile-awesomeness.git
```

In order to start your own branch for development.
```
git checkout -b <feature to work on>
```

Or fork through Githubs interface.

Code Reviews
=================================
Either ask to become a contributer, or send of a Pull Request. This is best done through the Github interface via your own branch/fork. Code reviews should be applied as Pull Requests through Github. This means that all features should be it's own branch which then will be merged into Master.
Master should at all time be runnable, which means that all code reviews should be run before being merged into master.

How to for branches
=================================
The general workflow for code reviewing is the following.
```
git pull origin master
git checkout <branch to work on>
```
<--All work is done-->
```
git commit -m "<Commit message>"
git merge master
```

After all that is done, apply for the Pull Request through Githubs interface, tag relevant people you want to review.

When the request has been granted, either you or the reviewer should be able to automatically merge the request through Githubs interface, if not, follow the following workflow.
```
git pull --all
git checkout master
git merge <branch to to merge>
```
<-- Fix the merge conflict -->
```
git commit -m "fixed merge conflict"
git push origin master
```

License
=================================
miniature-ninja-agile-awesomeness is licensed under the [BSD License](http://www.github.com/Magrit/miniature-ninja-agile-awesomeness/blob/master/LICENSE)

Special considerations
=================================

Even if something breakes, take a deep breath and contact one of the maintainers, or try to Google for the error and try to fix it on your own if it's manageable.
