HOW TO RUN
1. Make sure the input file "facebook_combined.txt" or "small_graph.txt" is in the src directory
2. Compile and run Main.java
3. Program will output:
   - BFS and DFS traversal paths
   - Distance between specific nodes
   - Number of connected components
   - Steps to cover the whole graph from random starting points
   - Number of nodes within distance 3 from node 3025
   - Time analysis comparing adjacency list vs adjacency matrix


QUESTIONS
a. The distance from 14 to 2014 is 4

b. Yes, the facebook graph is connected because the facebook graph has 1 connected component.

c. No, the smaller graph is not connected because it has 2 connected components.

d. My answer did change slightly when I had different sources,
   but it usually took anywhere between 5-8 steps to cover the whole graph.
   My answer changed because some of the nodes (with smaller distance values) were likely more central to the graph
   and therefore it took fewer "frontiers" to visit all.
   On the other hand, some of the less connected source nodes required a few more frontiers to reach all nodes.
   For example:
   7 steps to cover graph starting from node 233
   6 steps to cover graph starting from node 3682
   6 steps to cover graph starting from node 630
   5 steps to cover graph starting from node 1588

e. 1830 nodes are within a distance of 3 from node 3025



ASSUMPTIONS
 - I assumed that most of the nodes would be connected and it would be hard to randomly find a node that is in
 a different connected comp (if that exists) since there are so many nodes. Therefore, I assumed that if we had
 to find a new source node due to a diff connected comp, iterating through all nodes just to find unvisited ones
 would be expensive. Instead,  I just maintained a set of unvisited nodes so it would be more efficient to
 check and remove nodes from this set as you visit them.

 - To find how many steps needed to visit the whole graph, I assumed that if I found the distance of the node
 furthest from the source, this would that does not exist another node of a further distance, which would represent
 the full graph being explored.

 - To find the size of the adjacency matrix, I assumed that nodes would be numbered in order from 0 to some int n,
 so I found the number of the maxVertex and set the size to be (n+1) x (n+1).



EXTRA CREDIT
Load adjacency list: 14 ms
Load adjacency matrix: 41 ms
BFS adjacency list: 4 ms
BFS adjacency matrix: 44 ms
DFS adjacency list: 254 ms
DFS adjacency matrix: 760 ms

The adjacency matrix takes more time to load data because it needs to go through the file twice, once to find
max vertex to determine size of matrix and then to initialize the matrix. This requires O(V²) space and time since
you have to fill every cell even if there's no edge.
The adjacency list only needs to create entries for actual edges, making it O(V+E) for initialization, which is
usually faster since most graphs aren't fully connected.

Running BFS is much quicker on the adjacency list since finding neighbors for adj. list is O(degree(v)) for each
vertex v because you only traverse the list of neighbors. With adj. matrices, to find neighbors you have to check
every possible pair of vertices, which would be O(V) per vertex. This means total complexity is O(V+E) for lists
and O(V²) for matrices.

Running DFS takes much more time than running BFS because DFS pushes all unvisited neighbors onto the stack
and processes them in LIFO order, so you might traverse very deep into one path before backtracking, causing more
stack operations. BFS, on the other hand, visits vertices in levels using a queue, so it involves fewer operations.
Again, DFS is quicker on adj. lists because the complexity would be O(V+E), but O(V²) for adjacency matrices.

In general, for graphs where there is a smaller number of edges in the graph, adjacency lists would be more
efficient because we don't need to check all possible pairs, and instead just have a list of the neighbors
of a vertex.



CHALLENGES
- I initially implemented visited as a simple List, but I then decided to use Map<Integer, Integer>
for the BFS to store additional data like distances. This change improved functionality and helped me
answer the follow-up questions.

- While implementing the time analysis extra credit, I found that loading either graph data structure
the first time took a significantly longer time compared to later loads, which was skewing the results
of my time analysis. After doing some research, I learned that this may have been to JVM warmup. Therefore,
instead of including this first-time load in my times, I decided to load the data regularly at the start of
my program and then implement my time analysis at the end of the main() method to get more accurate comparisons.
Even then, I discovered switching the order of loadDataAM() and loadDataAL() caused the first load to always
take more time than if it was second. Either way, BFS and DFS timing remained consistent across multiple runs,
which was what I mostly focused on.

- I also ran into a few Exceptions along the way because after some initial runs to make sure my basic BFS and DFS
methods worked, I switched my input file to "facebook_combined.txt" to implement some changes to answer the questions,
but when I switched back to test "small_graph.txt", I realized I forgot to consider cases where a node might not
have any unvisited neighbors.