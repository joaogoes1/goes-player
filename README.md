# Goes player
Android Music Player

This is a Music Player that one day it will have some nice features.

This project I started to develop when I was starting to study programming, after my first Android course.
The object of this app is study more about Android Framework and and go beyond what I learned in the course.

### Refactor
This app is in refactor (rewrite) process bringing this to the modernity (MAD skills).
Considering this is my first app after a Android course, before even start to study Computer Science, like OOP, a lot of things needs to be fixed, improved or updated.

Goals:
1. Enable to run in API 34 ✅
2. Migrate to Kotlin ✅
3. Use Single Activity arch ✅
4. Move to MVVM architecture ✅
5. Migrate layout to Jetpack Compose ✅
6. Fix Files Access permission
7. Fix Service Player ✅
8. Modularize
9. Change all icons from png to svg ✅

Steps:
1. Upgrade to latest version of AGP, Android SDK and use gradle kts ✅
2. Migrate each fragments of home to Jetpack Compose ✅
    - AlbumFragment ✅
    - HomeFragment ✅
    - ArtistFragment ✅
    - MusicFragment ✅
    - PlaylistFragment ✅
    - FolderFragment ✅
    - GenreFragment ✅
3. Migrate to SingleActivity Architecture ✅
   1. Create main fragment (HomeFragment) ✅
   2. Move layout from MainActivity ✅
   3. Isolate player layout and remove repeated code from fragments ✅
   4. Migrate others activities to fragments ✅
4. Migrate all icons and images to .svg ✅
5. Create unit and layout tests for the app