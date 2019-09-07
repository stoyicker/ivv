[![CircleCI](https://circleci.com/gh/stoyicker/ivv.svg?style=svg&circle-token=a93f0b6033d7f45400ccdc85011af61352da6521)](https://circleci.com/gh/stoyicker/ivv)

# Building
* `./gradlew :_splash:assemble` (or Windows equivalent). Find the apk in 
`_splash/build/outputs/apk/debug/_splash-debug.apk`.

You can also download the APK already built [here](https://github.com/stoyicker/ivv/releases/latest).

# Architecture
This app follows a 'feature'-based split, with two features (not really, one of them is just the
app entry point, so it would have to go into any combination of features):
* `_splash`, for the splash screen.
* `_list`, for the item list.

`_splash` hardly has any code, not much architecture to see here.

`_list` is around a source of truth which is updated by actions triggered for example by the user
and which is observed by the UI, that adapts to each change in the source of truth. This allows 
for a highly scalable development with very low coupling which favors not only maintainability, 
but also popular practises such as flavoring, features toggles and dynamic delivery.

As a trade-off, due to Dagger limitations when it comes to cross-module dependencies, there would be
a little bit of code duplication if we were to scale straight-up from this (the schedulers and 
network modules, which are currently present in `_list`), but it should be quite feasible to 
refactor away into a pre-compiled binary (which will solve the aforementioned issues with Dagger) 
before adding more feature modules.

Finally, the app is built to follow general user expectations: 
* If started offline or a connection is list, content will be immediately fetched after the user 
comes back online (if the app is open).
* Upon rotation (or overall configuration change) no network traffic occurs.
* If content was fetched sometime in the past and the app is open while offline, the last version of
the available content will be used until a newer one is available. 
* Otherwise, contents are managed in the following manner:
    * Memory cache is held for 30 minutes from write. After that, content requests will try to fetch
    fresh data, but will fallback to the disk cache on failure (even if it was stale).
    * Disk cache is held for one hour from write. After that, content requests will try to fetch 
    fresh data, but will fallback to whatever there was on disk on failure (even if it was stale).    

# Documentation
Documentation is generated using [Dokka](https://github.com/Kotlin/dokka), which is the
code documentation generation tool for Kotlin, similar to what Javadoc is for Java.
`index.html` for the documentation of each module can be found in their `build` directories:
`module_name/build/dokka/module_name/index.html`.

# Tests
Several types of tests are written using JUnit and Espresso. Feel free to run them using the 
different `test` and `cAT` Gradle tasks in each module.

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
* Pull-to-refresh.
* Crash reporting.
* Some other TODOs left around.
