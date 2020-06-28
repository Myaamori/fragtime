Reproducible repo for muxing Fragtime using SubKt.

1. Install the JDK and mkvmerge as explained in the [SubKt repo](https://github.com/Myaamori/subkt/#quickstart-guide)
2. Place `[HRI] Fragtime (BD 1080p) [688D90E4].mkv` in the root of the repo
3. Run `./gradlew mux` (or simply `gradlew mux` on Windows)

See the `build.gradle.kts` and `sub.properties` files for the definition of the automation toolchain, as well as the [SubKt repo](https://github.com/Myaamori/subkt) for documentation.
