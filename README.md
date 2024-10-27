# advent-of-code-2024

Solving puzzles from https://adventofcode.com/2024

One folder per programming language / environment in case I get bored of one.

## JVM

Setup to use Java 21.

### Gradle

Custom task `setup-java-puzzle` allows easy setup of the usual structure I use to
solve the puzzles. The folder `templates` contains the files I start with. Package structure is
`ch.neukom.advent2024.dayX` containing at least one file each for both parts of the puzzle.
