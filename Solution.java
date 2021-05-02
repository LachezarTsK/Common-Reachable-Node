import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;

public class Solution {

  public Map<Integer, HashSet<Integer>> nodesAndEdges;

  /*
    By the problem design on binarysearch.com, we have to work around the given method
    'public boolean solve(int[][] edges, int pointOne, int pointTwo)'
    so that the code can be run on the website. Even though the name 'solve' does not make
    a lot of sense, it is left as it is, so that the code can be run directly
    on the website, without any modifications.
  */
  public boolean solve(int[][] edges, int pointOne, int pointTwo) {
    if (pointOne == pointTwo) {
      return true;
    }

    fill_nodesAndEdges_byInvertingEdges(edges);
    return checkForNode_thatCanReachTwoGivenPoints_inDirectedGraph(pointOne, pointTwo);
  }

  /*
  The method checks whether there is at least one node in the directed graph, from which
  we can go both to pointOne and to pointTwo.

  Doing this by brute force - starting a check from every node in the graph - will result
  in time out. However, if we reverse the edges of the directed graph, and start the search
  from pointOne and pointTwo, and record the visited nodes from each of these two points,
  and then check wehther they have at least one visited node in common, we can find out
  whether there is such a node, without exceeding the time limit.

  @return 'true' if such a node exists, otherwise 'false'.
  */

  public boolean checkForNode_thatCanReachTwoGivenPoints_inDirectedGraph(int pointOne, int pointTwo) {

    /*
    Since the problem statement does not mention explicitly that the desginated
    integer for each node starts from '0' and goes on in increasing consequtive order,
    we assume that the desginated integer for each node can be any 32-bits integer,
    as long as it is unique for each node. Therefore, we apply a HashSet for the visited nodes.

    Otherwise, we could apply a boolean array for the visited nodes
    in order to streamline the code.
    */
    Set<Integer> visited_fromPointOne = bfs_recordNodes_reachedFromStart(pointOne);
    Set<Integer> visited_fromPointTwo = bfs_recordNodes_reachedFromStart(pointTwo);

    /*
    A node that can be reached from both pointOne and pointTwo in the reversed graph.
    Thus, in the original configuration of the graph's edges, we can reach both
    pointOne and pointTwo from this node.
    */
    boolean commonReachableNode_isFound = false;

    for (int n : visited_fromPointOne) {
      if (visited_fromPointTwo.contains(n)) {
        commonReachableNode_isFound = true;
        break;
      }
    }
    return commonReachableNode_isFound;
  }

  public Set<Integer> bfs_recordNodes_reachedFromStart(int start) {

    Set<Integer> visited = new HashSet<Integer>();
    LinkedList<Integer> queue = new LinkedList<Integer>();
    queue.add(start);
    visited.add(start);

    while (!queue.isEmpty()) {

      int current = queue.removeFirst();
      if (nodesAndEdges.containsKey(current)) {

        Set<Integer> neighbours = nodesAndEdges.get(current);
        for (int n : neighbours) {
          if (visited.add(n)) {
            queue.add(n);
          }
        }
      }
    }
    return visited;
  }

  public void fill_nodesAndEdges_byInvertingEdges(int[][] edges) {

    nodesAndEdges = new HashMap<Integer, HashSet<Integer>>();
    for (int i = 0; i < edges.length; i++) {

      if (!nodesAndEdges.containsKey(edges[i][1])) {

        HashSet<Integer> neighbours = new HashSet<Integer>();
        neighbours.add(edges[i][0]);
        nodesAndEdges.put(edges[i][1], neighbours);
      } else {
        nodesAndEdges.get(edges[i][1]).add(edges[i][0]);
      }
    }
  }
}
