import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    private static Map<Integer, List<Integer>> al = new HashMap<>();
    private static int[][] am;
    private static int cc = 0;
    private static String timeAnalysis = "";

    public static void main(String[] args) {
        //change file here!
        String fileName = "src/facebook_combined.txt";

        //load data into adjacency list
        loadDataAL(fileName);
        loadDataAM(fileName);

        //paths with bfs and dfs
        System.out.println("BFS Path: " + bfsAL(0, new HashMap<Integer, Integer>(),
                new HashSet<>(al.keySet())));
        System.out.println("DFS Path: " + dfsAL(0, new HashSet<>(al.keySet())));


        System.out.println("\nQUESTIONS");
        //distance between two nodes
        Map<Integer, Integer> visitedMap = new HashMap<Integer, Integer>();
        bfsAL(14, visitedMap, new HashSet<>(al.keySet()));
        System.out.println("Distance from 14 to 2014: " + visitedMap.get(2014));

        //check connected graph with bfs
        visitedMap = new HashMap<Integer, Integer>();
        bfsAL(0, visitedMap, new HashSet<>(al.keySet()));
        System.out.println("Connected components: " + cc);


        //steps to cover whole graph
        for (int i = 0; i < 4; i++) {
            visitedMap = new HashMap<Integer, Integer>();
            int start = (int)(Math.random() * al.size()) + 1;
            bfsAL(start, visitedMap, new HashSet<>(al.keySet()));
            System.out.println(Collections.max(visitedMap.values()) +
                    " steps to cover full graph from node " + start);
        }

        //number of nodes within distance 3
        visitedMap = new HashMap<Integer, Integer>();
        bfsAL(3025, visitedMap, new HashSet<>(al.keySet()));
        int dist3 = 0;
        for (int v : visitedMap.values()) {
            if (v <= 3 && v > 0) { //do not want to include node 3025 itself
                dist3++;
            }
        }
        System.out.println(dist3 + " nodes are within distance 3 from node 3025");



        //extra credit
        System.out.println("\nTIME ANALYSIS");
        runAndFindTime("Load adjacency list: ", () -> loadDataAL(fileName));
        runAndFindTime("Load adjacency matrix: ", () -> loadDataAM(fileName));

        runAndFindTime("BFS adjacency list: ", () ->
                bfsAL(0, new HashMap<Integer, Integer>(), new HashSet<>(al.keySet())));
        runAndFindTime("BFS adjacency matrix: ", () ->
                bfsAM(0, new HashMap<Integer, Integer>(), new HashSet<>(al.keySet())));

        runAndFindTime("DFS adjacency list: ", () -> dfsAL(0, new HashSet<>(al.keySet())));
        runAndFindTime("DFS adjacency matrix: ", () -> dfsAM(0, new HashSet<>(al.keySet())));

        System.out.print(timeAnalysis);








    }

    //finds average runtime of a task
    private static void runAndFindTime(String description, Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();
        timeAnalysis += description + (endTime - startTime) + " ms\n";
    }

    public static void loadDataAL(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                String[] vertices = line.split(" ");
                int v1 = Integer.parseInt(vertices[0]);
                int v2 = Integer.parseInt(vertices[1]);

                List<Integer> v1Neighbors = al.getOrDefault(v1, new ArrayList<>());
                v1Neighbors.add(v2);

                List<Integer> v2Neighbors = al.getOrDefault(v2, new ArrayList<>());
                v2Neighbors.add(v1);

                al.put(v1, v1Neighbors);
                al.put(v2, v2Neighbors);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void loadDataAM(String fileName) {

        //initializes matrix based on vector size
        int maxVertex = getMaxVertex(fileName);
        am = new int[maxVertex + 1][maxVertex + 1];

        //fills adjacency matrix
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                String[] vertices = line.split(" ");
                int v1 = Integer.parseInt(vertices[0]);
                int v2 = Integer.parseInt(vertices[1]);
                am[v1][v2] = 1;
                am[v2][v1] = 1;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getMaxVertex(String fileName) {
        int maxVertex = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                String[] vertices = line.split(" ");
                int v1 = Integer.parseInt(vertices[0]);
                int v2 = Integer.parseInt(vertices[1]);
                maxVertex = Math.max(maxVertex, Math.max(v1, v2));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return maxVertex;
    }

    public static List<Integer> bfsAL(int source, Map<Integer, Integer> visitedMap,
                                      Set<Integer> unvisited) {
        cc = 0;
        List<Integer> path = new ArrayList<>();
        int curr = source;
        Queue<Integer> queue = new LinkedList<>();
        visitedMap.put(curr, 0);
        path.add(curr);
        unvisited.remove(curr);
        queue.add(curr);
        while (!unvisited.isEmpty()) {
            cc++;
            if (queue.isEmpty()) {
                curr = unvisited.iterator().next();
                queue.add(curr);
                visitedMap.put(curr, 0);
                path.add(curr);
                unvisited.remove(curr);
            }
            while (!queue.isEmpty()) {
                curr = queue.poll();
                int prevDist = visitedMap.get(curr);
                List<Integer> neighbors = al.get(curr);
                if (neighbors != null) {
                    for (int n : neighbors) {
                        if (!visitedMap.containsKey(n)) {
                            visitedMap.put(n, prevDist + 1);
                            path.add(n);
                            unvisited.remove(n);
                            queue.add(n);
                        }
                    }
                }

            }
        }
        return path;
    }

    public static List<Integer> dfsAL(int source, Set<Integer> unvisited) {
        List<Integer> path = new ArrayList<>();
        int curr = source;
        Stack<Integer> stack = new Stack<>();
        stack.push(curr);

        while (!unvisited.isEmpty()) {
            if (stack.isEmpty()) {
                stack.push(unvisited.iterator().next());
            }
            while (!stack.isEmpty()) {
                curr = stack.pop();
                if (unvisited.contains(curr)) {
                    path.add(curr);
                    unvisited.remove(curr);
                }
                List<Integer> neighbors = al.get(curr);
                if (!neighbors.isEmpty()) {
                    for (int n : neighbors) {
                        if (unvisited.contains(n)) { //push unvisited neighbors to stack
                            stack.push(n);
                        }
                    }
                }
            }
        }
        return path;
    }

    public static List<Integer> bfsAM(int source, Map<Integer, Integer> visitedMap,
                                      Set<Integer> unvisited) {
        cc = 0;
        List<Integer> path = new ArrayList<>();
        int curr = source;
        Queue<Integer> queue = new LinkedList<>();
        visitedMap.put(curr, 0);
        path.add(curr);
        unvisited.remove(curr);
        queue.add(curr);
        while (!unvisited.isEmpty()) {
            cc++;
            if (queue.isEmpty()) {
                curr = unvisited.iterator().next();
                queue.add(curr);
                visitedMap.put(curr, 0);
                path.add(curr);
                unvisited.remove(curr);
            }
            while (!queue.isEmpty()) {
                curr = queue.poll();
                int prevDist = visitedMap.get(curr);
                for (int i = 0; i < am[curr].length; i++) {
                    if (am[curr][i] == 1 && !visitedMap.containsKey(i)) {
                        visitedMap.put(i, prevDist + 1);
                        path.add(i);
                        unvisited.remove(i);
                        queue.add(i);
                    }
                }
            }
        }
        return path;
    }

    public static List<Integer> dfsAM(int source, Set<Integer> unvisited) {
        List<Integer> path = new ArrayList<>();
        int curr = source;
        Stack<Integer> stack = new Stack<>();
        stack.push(curr);

        while (!unvisited.isEmpty()) {
            if (stack.isEmpty()) {
                stack.push(unvisited.iterator().next());
            }
            while (!stack.isEmpty()) {
                curr = stack.pop();
                if (unvisited.contains(curr)) {
                    path.add(curr);
                    unvisited.remove(curr);
                }
                for (int i = 0; i < am[curr].length; i++) {
                    if (am[curr][i] == 1 && unvisited.contains(i)) {
                        stack.push(i);
                    }
                }
            }
        }
        return path;

    }


}