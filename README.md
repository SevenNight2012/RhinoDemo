### A simple rhino demo in android platform
#### ENV
```
  Android Studio Flamingo | 2022.2.1 Patch 2
  Build #AI-222.4459.24.2221.10121639, built on May 12, 2023
  Runtime version: 17.0.6+0-17.0.6b802.4-9586694 aarch64
  VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
  macOS 13.2.1
  GC: G1 Young Generation, G1 Old Generation
  Memory: 2048M
  Cores: 8
  Metal Rendering is ON
  Registry:
  external.system.auto.import.disabled=true
  ide.text.editor.with.preview.show.floating.toolbar=false
  ide.instant.shutdown=false
  gradle.version.catalogs.dynamic.support=true
```
#### gradle
```
jdk:  zulu-11,11.0.19
wrap: gradle-7.5-all
AGP:  7.4.2
```
#### Switch rhino version
build.gradle in the root project
```
subprojects {
    configurations.all {
        resolutionStrategy {
            // switch 1.7.14 or 1.7.13
            force "org.mozilla:rhino-runtime:1.7.14"
            // force "org.mozilla:rhino-runtime:1.7.13"
        }
    }
}
```


