### readme

- The rhino-1.7.14-67187cba-SNAPSHOT.jar is the slow jar
- The rhino-1.7.14-1e4a0003-SNAPSHOT.jar is the fast jar
- If you want to use the jar file in this directory, you can put the jar in the app/libs directory

### How to use the jar in this directory

- you can put the jar in the app/libs directory
- replace the rhino-android dependency in the app/build.gradle like this

```
    implementation('com.faendir.rhino:rhino-android:1.6.0') {
        exclude group: 'org.mozilla', module: 'rhino-runtime'
    }
```
- remove the resolutionStrategy in the root project build.gradle like this
```
subprojects {
    configurations.all {
        // resolutionStrategy {
        //     force "org.mozilla:rhino-runtime:1.7.14"
        //     // force "org.mozilla:rhino-runtime:1.7.13"
        // }
    }
}
```
- rebuild the project,the rhino-xxx-SNAPSHOT.jar will be used
- If you have problems, please submit an issue