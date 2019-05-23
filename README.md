# Architecture
This app is split follows a 'feature' split, with three features:
* 'splash', for the splash screen.
* 'list', for the item list.
* 'detail', for the item detail.

# Language choice
I chose Kotlin over Java because:
* It is less verbose than Java.
* It is more natural both to read and write, and is easy to approach for Java developers as well.
* It can be configured to generate up to Java 6 bytecode, which means its evolution is independent of that of the platform.
* It is [officially supported by Google as a first-class language for Android](https://blog.jetbrains.com/kotlin/2017/05/kotlin-on-android-now-official/).

# Documentation
Documentation is generated using [Dokka](https://github.com/Kotlin/dokka), which is the
code documentation generation tool for Kotlin, similar to what Javadoc is for Java.
`index.html` for the documentation of each module can be found in their `build` directories:
 `module_name/build/dokka/module_name/index.html`.

# Tests
 Different tests are written using JUnit. Run them with the `test` Gradle task in each module.
 Instrumented tests are only present in the `splash` module and can be run using the `cAT` task.

# Setup for contributions
Once cloned, just setup the hooks:

```shell
$<project-dir>: ./hooks/setup (or whatever equivalent if on Windows).
```

# Potential improvements
* Running instrumented and monkey tests on CI. Also some parallelism can probably be introduced to 
speed things up.
* Refining infrastructure; there's a fair amount of duplication as things are now. Also maybe drop 
Groovy for Kotlin.
* Some more tests never hurt.
