[![CircleCI](https://circleci.com/gh/stoyicker/ivv.svg?style=svg&circle-token=a93f0b6033d7f45400ccdc85011af61352da6521)](https://circleci.com/gh/stoyicker/ivv)

# Building
* Put your api key in a gradle.properties file, like this, at the top level of the repo:
api.key="yourApiKeyHereTheQuotesAreRequired"
* `./gradlew :_splash:assemble` (or Windows equivalent)

# Architecture
This app is split follows a 'feature' split, with two features (not really, one of them is just the
app entry point):
* `_splash`, for the splash screen.
* `_list`, for the item list.

`_splash` hardly has any code, not much architecture to see here.

`_list` is built upon a Redux-like architecture, where the UI reacts to a source
of truth which is updated by UI actions. This allows for a highly scalable development with very low
coupling which favors not only maintainability, but also popular practises such as flavoring, 
features toggles and dynamic delivery.

As a trade-off, due to Dagger limitations when it comes to cross-module dependencies, there would be
a little bit of code duplication if we were to scale straight-up from this (the schedulers and 
network modules, which are currently present in `_list`), but it should be quite feasible to 
refactor away into a pre-compiled binary (which will solve the aforementioned issues with Dagger) 
before adding more feature modules.

Finally, the app is built to follow general user expectations: 
* If started offline, content will be immediately fetched after the user goes back offline (if 
the app is open).
* Upon rotation (or overall configuration change) no network traffic occurs.
* If content was fetched sometime in the past and the app is open while offline, the last version of
the available content will be used until a newer one is available.

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
Several types of tests are written using JUnit and Espresso. Feel free to run them using the 
different `test` and `cAT` Gradle tasks in each module.

# Setup for contributions
Once cloned, just setup the hooks:
```shell
$<project-dir>: ./hooks/setup.
```
If you face any issues with execution of the hook, push from a bash prompt. Also, the hook needs a 
connected device to run instrumentation tests on.

# Potential improvements
* Running instrumented and monkey tests on CI. Also there's some room for optimization in the CI 
pipeline.
* Refining infrastructure; there's a fair amount of duplication as things are now. Also maybe drop 
Groovy for Kotlin.
* There are very few tests. The existing ones are there more to show how different types of tests 
are written rather than to judge the quality of the app (even though they are meaningful, just not
enough).
* Some assets only include mdpi densities, which will cause crashes in devices in lower resolution 
screen buckets and memory overhead to upscale the resource in device in higher resolution buckets 
(plus, in some cases, visual artifacts).
* Crash reporting!
* Some other TODOs left around.
