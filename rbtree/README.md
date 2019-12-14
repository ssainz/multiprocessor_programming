# ECE 5510 Project Red Black Tree using Flat Combining

## Contact

Sergio Sainz ssainz@vt.edu

## Overview

Linux: ` ./gradlew build -x test`

Windows: `./gradlew.bat build -x test`

### Running Benchmark main programs:

__Benchmark__:
`java  -cp build/libs/rbtree.jar RedBlackTreeTest METHOD THREAD_NUM ITERS`

METHOD can be one of

- RBTFlatCombineV6
- RBTCoarseGrained 
- RBTCompositional 
- RBTTransactional
- RBTFlatCombineV5
- RBTFlatCombineV1
- RBTFlatCombineV2
- RBTFlatCombineV4
- RBTFlatCombineV3

When using RBTCompositional or RBTTransactional you must run using 

`java -javaagent:libs/deuceAgent-1.3.0.jar -cp build/libs/rbtree.jar RedBlackTreeTest METHOD THREAD_NUM ITERS`

## Intellij

This gradle project can be imported into Intellj by going to `File -> Open` and choosing this directory. As soon as you open the project, a pop-up dialog may appear in the bottom right of the screen asking to import the project with Gradle. Accept it.

For more instructions, go to https://www.jetbrains.com/help/idea/gradle.html#gradle_import_project_start