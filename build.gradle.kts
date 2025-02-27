plugins {
	id("com.android.application") version "8.8.1" apply false
	kotlin("android") version "2.1.10" apply false // kotlin("android") == "org.jetbrains.kotlin.android"
}
tasks.withType(JavaCompile::class.java){
	options.compilerArgs.add("-Xlint:all")
}
tasks.register("clean",Delete::class){
	delete(rootProject.layout.buildDirectory)
}