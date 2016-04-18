# Coursework 4: George Osborne and the Temple of Gloom

The code here is my attempt at an [assignment][] set as coursework for the
['Programming in Java' course][] at [Birkbeck College][], London.

The [assignment][], in brief, is defined as follows:

> In this assignment, you will help the explorer and professor of archeology
> George Osborne claim the Orb of Lots, which is located in the Temple of
> Gloom. You will help him explore an unknown cavern under the Temple, claim
> the Orb, and escape before the entrance collapses. There will be great
> rewards for those who help George fill his pockets with gold on the way out.

[assignment]: https://moodle.bbk.ac.uk/pluginfile.php/474981/mod_resource/content/7/devonian.pdf
['Programming in Java' course]: https://www.dcs.bbk.ac.uk/study-with-us/modules/programming-in-java/
[Birkbeck College]: http://www.bbk.ac.uk/

## How to run:

# Run 1000 times in headless mode:

    javac student/*.java && java main.TXTmain -n 1000

# Run once in GUI mode:

    javac student/*.java && java main.GUImain

## Approach

The explore phase uses a breath-first search to find the Orb.

The escape phases uses an implementation of Dijkstra's shortest path algorithm,
using a priority queue.

Both implementations are iterative.

## Limitations

The escape phase merely looks for the shortest path to the exit, picking gold
up along the way, but does not alter the route in an effort to pick up
additional gold.
