package student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import game.Edge;
import game.EscapeState;
import game.ExplorationState;
import game.MinHeap;
import game.Node;
import game.NodeStatus;

public class Explorer {

    private Set<Long> nodesSeen = new HashSet<>();

    /**
     * Explore the cavern, trying to find the orb in as few steps as possible.
     * Once you find the orb, you must return from the function in order to pick
     * it up. If you continue to move after finding the orb rather
     * than returning, it will not count.
     * If you return from this function while not standing on top of the orb,
     * it will count as a failure.
     * <p>
     * There is no limit to how many steps you can take, but you will receive
     * a score bonus multiplier for finding the orb in fewer steps.
     * <p>
     * At every step, you only know your current tile's ID and the ID of all
     * open neighbor tiles, as well as the distance to the orb at each of these tiles
     * (ignoring walls and obstacles).
     * <p>
     * To get information about the current state, use functions
     * getCurrentLocation(),
     * getNeighbours(), and
     * getDistanceToTarget()
     * in ExplorationState.
     * You know you are standing on the orb when getDistanceToTarget() is 0.
     * <p>
     * Use function moveTo(long id) in ExplorationState to move to a neighboring
     * tile by its ID. Doing this will change state to reflect your new position.
     * <p>
     * A suggested first implementation that will always find the orb, but likely won't
     * receive a large bonus multiplier, is a depth-first search.
     *
     * @param state the information available at the current state
     */
    public void explore(ExplorationState state) {
        if (state.getDistanceToTarget() == 0) {
            return;
        }

        this.nodesSeen.add(state.getCurrentLocation());

        // Store current position so we can return to it if we reach a
        // 'dead-end'; this is important because we can only move to adjacent
        // nodes
        long originalPosition = state.getCurrentLocation();

        for (NodeStatus n : state.getNeighbours()) {
            if (this.nodesSeen.contains(n.getId())) {
                continue;
            }

            state.moveTo(n.getId());
            this.explore(state);

            // Before returning to our original position,
            // check if we have reached the orb
            if (state.getDistanceToTarget() == 0) {
                return;
            }

            // Move back to our original position because we can only move to
            // adjacent nodes
            state.moveTo(originalPosition);
        }
    }

    /**
     * Escape from the cavern before the ceiling collapses, trying to collect as much
     * gold as possible along the way. Your solution must ALWAYS escape before time runs
     * out, and this should be prioritized above collecting gold.
     * <p>
     * You now have access to the entire underlying graph, which can be accessed through EscapeState.
     * getCurrentNode() and getExit() will return you Node objects of interest, and getVertices()
     * will return a collection of all nodes on the graph.
     * <p>
     * Note that time is measured entirely in the number of steps taken, and for each step
     * the time remaining is decremented by the weight of the edge taken. You can use
     * getTimeRemaining() to get the time still remaining, pickUpGold() to pick up any gold
     * on your current tile (this will fail if no such gold exists), and moveTo() to move
     * to a destination node adjacent to your current node.
     * <p>
     * You must return from this function while standing at the exit. Failing to do so before time
     * runs out or returning from the wrong location will be considered a failed run.
     * <p>
     * You will always have enough time to escape using the shortest path from the starting
     * position to the exit, although this will not collect much gold.
     *
     * @param state the information available at the current state
     */
    public void escape(EscapeState state) {
        MinHeap<Node> frontier = new MinHeap<>();

        Map<Node, Node> prevNode = new HashMap<>();

        /** Contains an entry for each node in the Settled and Frontier sets. */
        Map<Long, Integer> pathWeights = new HashMap<>();
        Node originalPosition = state.getCurrentNode();

        pathWeights.put(originalPosition.getId(), 0);
        frontier.add(originalPosition, 0);

        while (!frontier.isEmpty()) {
            Node f = frontier.poll();

            if (f.equals(state.getExit())) {
                break;
            }

            int nWeight = pathWeights.get(f.getId());

            for (Edge e : f.getExits()) {
                Node w = e.getOther(f);
                int weightThroughN = nWeight + e.length();
                Integer existingWeight = pathWeights.get(w.getId());

                if (existingWeight == null) {
                    pathWeights.put(w.getId(), weightThroughN);
                    frontier.add(w, weightThroughN);
                } else if (weightThroughN < existingWeight) {
                    pathWeights.put(w.getId(), weightThroughN);
                    frontier.changePriority(w, weightThroughN);
                }

                if (existingWeight == null || weightThroughN < existingWeight) {
                    prevNode.put(w, f);
                }
            }
        }

        List<Node> visitOrder = new ArrayList<>();
        Node u = state.getExit();

        while (u != null) {
            visitOrder.add(u);
            u = prevNode.get(u);
        }

        Collections.reverse(visitOrder);
        // Remove original position as we don't need to move to it
        visitOrder.remove(0);

        for (Node n : visitOrder) {
            if (state.getCurrentNode().getTile().getGold() > 0) {
              state.pickUpGold();
            }

            state.moveTo(n);
        }
    }
}
