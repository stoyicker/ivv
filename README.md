[![CircleCI](https://circleci.com/gh/stoyicker/ivv.svg?style=svg&circle-token=a93f0b6033d7f45400ccdc85011af61352da6521)](https://circleci.com/gh/stoyicker/ivv)

# Building
* `./gradlew :_splash:assemble` (or Windows equivalent). Find the apk in 
`_splash/build/outputs/apk/debug/_splash-debug.apk`.

# Architecture
This app follows a 'feature'-based split, with two-ish (`_splash is just the app entry point) 
features:
* `_splash`, for the splash screen.
* `_list`, for the item list.

`_splash` hardly has any code, not much architecture to see here.

`_list` is built around a source of truth which is updated by actions triggered for example by the 
user and which is observed by the UI, that adapts to each change in the source of truth. This allows 
for a highly scalable structure with very low coupling which favors not only maintainability, 
but also popular practises such as flavoring, features toggles and dynamic delivery.
The feature module is split in the 3 classic Clean Architecture layers clearly represented in the 
package structure. The 'data' (impl) layer is initialized by using a content provider for increased
isolation. The 'domain' layer contains use cases that use interfaces to define requirements that 
must be implemented by the 'data' layer, and the 'presentation' layer features MVP to bind use case 
triggers and react to data updates.

Each of these features is represented by a module, and then there's also the `_common` module, which contains some shared items.

Finally, the app is built to follow general user expectations: 
* If started offline or a connection is lost, content will be immediately fetched after the user 
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
White-box JVM-based unit tests are written using JUnit and instrumented black-box ones using 
Espresso. Feel free to run them using the different `test` and `cAT` Gradle tasks.
Also note that for the JVM tests, although the requirements mentioned 'Complete coverage of clean 
unit tests', I'm assuming that this does not mean 100% statement/line coverage. I've written tests
for some of the classes, but obviously not enough for full code coverage.

# Points of discussion
* Mockk vs Mockito: Not too different from each other. Mockk is mostly a re-write of mockito with a
Kotlin DSL for convenience.
* Why no AAC? First, I think AAC are too constraining. Everything works very well as long as you use
the whole suite, but the use cases it is capable of filling in are very limited, and even a simple
infinite scrolling list takes a ridiculously large amount of code to implement, plus other minor 
issues such as static factories that repeatedly require being wrapped for testability. But most 
importantly, AAC is built around [implicit lifecycle awareness](https://developer.android.com/reference/androidx/lifecycle/LifecycleOwner.html)
via the use of hidden hook Fragments. In my opinion this is a negative trade-off, managing the
Activity lifecycle manually is not complicated and more often than not there are going to be 
conditions where a component that would otherwise be managed automatically needs to be manipulated 
manually, in which case if we're using AAC we're taking the Fragment pretty much for free. Moreover,
over the years the complexity (let alone the bugs) in the Fragment lifecycle has caused problems 
left and right and introducing it so that in exchange you can lift a slight bit of complexity from 
somewhere else seems counter-intuitive to me.
* Full-stack integration vs mocked-data instrumentation tests: The only instrumented tests in this 
project are using mocked data to avoid false negatives - failures where the culprit is outside of 
the app, such as for example server issues or network problems in a test where we need/assume that 
these work fine). These are good tests, but I also think that tests that rely on the actual 
situation also provide useful information when the entire stack is part of the product, as they can 
help identify situations that are still relevant to the product, such as breaking changes to the 
contract between server and clients. It's also important to mention however that they should not be 
relied on to test the client itself as, like mentioned before, can fail due to circumstances 
independent of the correctness of the app.
In this case there are no tests of this kind as I've chosen to focus on other things, but it may
have been a good idea to have a couple if only to verify that the endpoint is correct, for example.
* White- vs black-box JVM tests: As you can see the only JVM tests I've written are white-box. This
is because I also verify the output of the method being tested, so the white-box tests give me 
strictly more value (information). This is not to say black-box tests are useless, as they also
give relevant information and are easier to develop and maintain, but in my opinion the trade-off is
generally worth as white-box tests eliminate the possibility of having false positives and, if a 
black-box test breaks, a white-box inspection is likely going to be needed anyway.
* Custom instrumentation test runner vs component factory: When using a DI framework such as Dagger,
a common practice to inject stubs for instrumented tests is to replace the default test runner by a 
custom one that replaces the application class for a test-specific one. Supposing that the 
application class is the root of dependency injection in the module, the test one can override the 
components that are being use, switching them with test versions that inherit the interface of the 
real ones, but use stub modules to fill in the dependencies required instead. This works in a fair
amount of cases, but has a couple of issues:
    * It requires DI root to be in the application class. If this is not the case or there are other
    roots, such as is the case when using library modules that hook up to the app lifecycle by using
    a ContentProvider - see the [Firebase example](https://firebase.googleblog.com/2016/12/how-does-firebase-initialize-on-android.html))
    - there are some 'hacks' that can be done to work around the situation, but they involve 
    undesirable trade-offs such as replacing classes outside of the DI graph.
    * It works at the application level. In simple apps with only one component that is going to be
    stubbed and no dependencies or subcomponents, this is not an issue. But as projects grow, tests
    will enter situations where let's say 10 different activities need to be tested. This kind of 
    setup forces all components to be mocked. Not a big problem, and there are ways to work around 
    this, such as lazy evaluation or subcomponents (in the particular case of Dagger), but certainly
    a constraint on flexibility.
The approach that I show on this project leverages the capabilities of Espresso with regards to 
exposing the Activity lifecycle by injecting functions that create components using a tool I wrote 
(which makes things slightly better but isn't really necessary, I just thought it'd be good to show
off). Read more about it in `dependencies.gradle`.

# Potential improvements
* CI: Run instrumented and monkey tests. Automated deployments.
* Refining infrastructure; there's a bit of duplication as things are now. The current setup helps 
Gradle when building its dependency graph by avoiding things such as `allProjects`, but it hurts 
maintainability slightly in exchange. Also there is the possibility to drop Groovy for Kotlin. I am 
not sure I would do it on a production app because I think it's still [a little problematic](https://youtrack.jetbrains.com/issues?q=-Resolved%20build.gradle.kts%20sort%20by:%20Priority%20asc) 
(particularly serious examples could be [this one](https://youtrack.jetbrains.com/issue/KT-11978) 
or [this other one](https://youtrack.jetbrains.com/issue/KT-26983)) but it could be considered.
* Include assets for missing densities. Some assets are only present for mdpi densities, which will 
cause crashes in devices in lower resolution screen buckets and memory overhead to upscale the 
resource in devices in higher resolution buckets (plus, in some cases, visual artifacts).
* Pull-to-refresh.
* Crash reporting.
* Module-specific dependency declaration of binaries: Non-project dependencies declared in 
dependencies.gradle are only split by scope, but not grouped by modules (so all of the modules use 
the same non-project dependencies). In this case the APK is not affected because all modules are in
use anyway, but build performance is probably impacted and in the case where we were to introduce a
more complex structure (such as flavoring, where not all flavors may use all modules) there's the
risk of introducing unused dependencies in the final APK (which would be lowered in a real case
because we'd use some minification tool such as ProGuard or R8, but still).
* There are anonymous classes and some static DI violations around the project. These will greatly 
difficult testing and should be refactored into named classes/injected dependencies respectively to
appropriately address the problems they create.
* Static @Provides methods for dependencies that do not need instance information: See [this link](
https://stackoverflow.com/questions/38607503/static-provide-method-in-dagger2) for more detail. 
Basically just a tiny bit of theoretical performance gain.
* A couple of other TODOs left around.
