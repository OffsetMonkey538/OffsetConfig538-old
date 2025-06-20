Turns out I was making a custom config format two years ago? Seems like it was even working? Well there must've been something that made me give up on it?  
Anyway the actual config library I use is here: https://github.com/OffsetMods538/OffsetConfig538

Oh, and here's the original description:

# OffsetConfig538
[![](https://jitpack.io/v/top.offsetmonkey538/OffsetConfig538.svg)](https://jitpack.io/#top.offsetmonkey538/OffsetConfig538)
## Why?
Uhm... so I uhh... wanted to make a config library for my minecraft mods and didn't find a format I liked... so I made my own!

## How do I use it?
I'll do one of those GitHub wiki things at some point, but currently you can view the [javadoc](https://javadoc.jitpack.io/top/offsetmonkey538/OffsetConfig538/latest/javadoc/) and copy the following into the correct parts of your `build.gradle` file: 
```gradle
repositories {
    maven {
        name = "JitPack"
        url = "https://jitpack.io"
        content {
            includeGroup "top.offsetmonkey538"
        }
    }
}

dependencies {
    implementation "top.offsetmonkey538:OffsetConfig538:[VERSION HERE]" // fixme: put the latest version here
}
```
Please don't forget to actually use the latest version.
