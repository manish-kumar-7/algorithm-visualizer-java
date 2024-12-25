import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MainVisualizer extends JFrame {
    private AlgorithmVisualizer algorithmVisualizer;
    private GraphTraversalVisualizer graphTraversalVisualizer;
    private JComboBox<String> visualizerSelector;

    public MainVisualizer() {
        setTitle("Visualizer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        algorithmVisualizer = new AlgorithmVisualizer(6);
        graphTraversalVisualizer = new GraphTraversalVisualizer();

        String[] visualizerOptions = { "Algorithm Visualizer", "Graph Traversal Visualizer" };
        visualizerSelector = new JComboBox<>(visualizerOptions);
        visualizerSelector.addActionListener(e -> switchVisualizer());

        setLayout(new BorderLayout());
        add(visualizerSelector, BorderLayout.NORTH);
        add(algorithmVisualizer, BorderLayout.CENTER);

        setVisible(true);
    }

    private void switchVisualizer() {
        String selected = (String) visualizerSelector.getSelectedItem();
        if ("Algorithm Visualizer".equals(selected)) {
            remove(graphTraversalVisualizer);
            add(algorithmVisualizer, BorderLayout.CENTER);
        } else {
            remove(algorithmVisualizer);
            add(graphTraversalVisualizer, BorderLayout.CENTER);
        }
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainVisualizer::new);
    }
}

class AlgorithmVisualizer extends JPanel {
    private int[] array;
    private int currentIndex = -1;
    private int nextIndex = -1;
    public boolean stop = true;
    private final int delay = 1000;
    private JPanel controlPanel;
    private JButton bubbleSortButton, selectionSortButton, insertionSortButton, mergeSortButton, quickSortButton,
            stopButton, resetButton;
    private JComboBox<Integer> sizeSelector;

    public AlgorithmVisualizer(int size) {
        array = new int[size];
        setArraySize(size);
        createControlPanel();
    }

    private void createControlPanel() {
        controlPanel = new JPanel();

        bubbleSortButton = new JButton("Bubble Sort");
        selectionSortButton = new JButton("Selection Sort");
        insertionSortButton = new JButton("Insertion Sort");
        mergeSortButton = new JButton("Merge Sort");
        quickSortButton = new JButton("Quick Sort");
        stopButton = new JButton("stop");

        resetButton = new JButton("Reset");

        sizeSelector = new JComboBox<>(new Integer[] { 6, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 });
        sizeSelector.addActionListener(e -> {
            int selectedSize = (int) sizeSelector.getSelectedItem();
            setArraySize(selectedSize);
        });

        bubbleSortButton.addActionListener(e -> bubbleSort());
        selectionSortButton.addActionListener(e -> selectionSort());
        insertionSortButton.addActionListener(e -> insertionSort());
        mergeSortButton.addActionListener(e -> mergeSort());
        quickSortButton.addActionListener(e -> quickSort());
        stopButton.addActionListener(e -> stopSorting());

        resetButton.addActionListener(e -> setArraySize((int) sizeSelector.getSelectedItem()));

        controlPanel.add(sizeSelector);
        controlPanel.add(bubbleSortButton);
        controlPanel.add(selectionSortButton);
        controlPanel.add(insertionSortButton);
        controlPanel.add(mergeSortButton);
        controlPanel.add(quickSortButton);
        controlPanel.add(stopButton);

        controlPanel.add(resetButton);

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.SOUTH); // Place the control panel at the bottom
    }

    private void setArraySize(int size) {
        stop = true;
        array = new int[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * 400 + 50);
        }
        currentIndex = -1;
        nextIndex = -1;
        repaint();
    }

    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < array.length; i++) {
            if (i == currentIndex) {
                g.setColor(Color.RED);
            } else if (i == nextIndex) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(new Color(255, 105, 180));
            }

            int rectWidth = getWidth() / array.length;
            int rectHeight = array[i];
            int x = i * rectWidth;
            int y = getHeight() - rectHeight;

            g.fillRect(x, y, rectWidth, rectHeight);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            String value = Integer.toString(array[i]);
            int textX = x + rectWidth / 2 - g.getFontMetrics().stringWidth(value) / 2;
            int textY = y - 6;
            g.drawString(value, textX, textY);
        }
    }

    public void bubbleSort() {
        stop = false;
        Thread bubbleSortThread = new Thread(() -> {
            boolean swapped;
            for (int i = 0; i < array.length - 1; i++) {
                if (stop) {
                    return;
                }
                swapped = false;
                for (int j = 0; j < array.length - 1 - i; j++) {
                    if (stop) {
                        return;
                    }

                    currentIndex = j;
                    nextIndex = j + 1;
                    SwingUtilities.invokeLater(this::repaint);

                    if (array[j] > array[j + 1]) {
                        swap(j, j + 1);
                        swapped = true;
                    }

                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                if (!swapped) {
                    break;
                }

                SwingUtilities.invokeLater(this::repaint);
            }

            currentIndex = -1;
            nextIndex = -1;
            SwingUtilities.invokeLater(this::repaint);
        });
        bubbleSortThread.start();
    }

    public void selectionSort() {
        stop = false;
        Thread selectionSortThread = new Thread(() -> {
            for (int i = 0; i < array.length - 1; i++) {
                if (stop) {
                    return;
                }
                int minIndex = i;
                for (int j = i + 1; j < array.length; j++) {
                    if (stop) {
                        return;
                    }

                    currentIndex = i;
                    nextIndex = j;
                    SwingUtilities.invokeLater(this::repaint);

                    if (array[j] < array[minIndex]) {
                        minIndex = j;
                    }

                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                if (i != minIndex) {
                    swap(i, minIndex);
                }

                SwingUtilities.invokeLater(this::repaint);
            }

            currentIndex = -1;
            nextIndex = -1;
            SwingUtilities.invokeLater(this::repaint);
        });
        selectionSortThread.start();
    }

    public void insertionSort() {
        stop = false;
        Thread insertionSortThread = new Thread(() -> {
            for (int i = 1; i < array.length; i++) {
                if (stop) {
                    return;
                }
                int key = array[i];
                int j = i - 1;

                currentIndex = i;
                nextIndex = j;
                SwingUtilities.invokeLater(this::repaint);

                while (j >= 0 && array[j] > key) {
                    if (stop) {
                        return;
                    }

                    array[j + 1] = array[j];
                    currentIndex = j;
                    nextIndex = j + 1;
                    SwingUtilities.invokeLater(this::repaint);

                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    j--;
                }
                array[j + 1] = key;

                SwingUtilities.invokeLater(this::repaint);

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            currentIndex = -1;
            nextIndex = -1;
            SwingUtilities.invokeLater(this::repaint);
        });
        insertionSortThread.start();
    }

    public void mergeSort() {
        stop = false;
        Thread mergeSortThread = new Thread(() -> {
            if (stop) {
                return;
            }
            mergeSortHelper(0, array.length - 1);
            currentIndex = -1;
            nextIndex = -1;
            SwingUtilities.invokeLater(this::repaint);
        });
        mergeSortThread.start();
    }

    private void mergeSortHelper(int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortHelper(left, mid);
            mergeSortHelper(mid + 1, right);
            merge(left, mid, right);
        }
    }

    private void merge(int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];
        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, mid + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (stop) {
                return;
            }
            currentIndex = k;
            if (leftArray[i] <= rightArray[j]) {
                array[k++] = leftArray[i++];
            } else {
                array[k++] = rightArray[j++];
            }
            SwingUtilities.invokeLater(this::repaint);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (i < n1) {
            if (stop) {
                return;
            }
            currentIndex = k;
            array[k++] = leftArray[i++];
            SwingUtilities.invokeLater(this::repaint);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (j < n2) {
            if (stop) {
                return;
            }
            currentIndex = k;
            array[k++] = rightArray[j++];
            SwingUtilities.invokeLater(this::repaint);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void quickSort() {
        stop = false;
        Thread quickSortThread = new Thread(() -> {
            quickSortHelper(0, array.length - 1);
            currentIndex = -1;
            nextIndex = -1;
            SwingUtilities.invokeLater(this::repaint);
        });
        quickSortThread.start();
    }
    
    private void quickSortHelper(int low, int high) {
        if (stop) {
            return; // If stop is set, exit the recursion
        }
        if (low < high) {
            int pi = partition(low, high);
            quickSortHelper(low, pi - 1);
            quickSortHelper(pi + 1, high);
        }
    }
    
    private int partition(int low, int high) {
        if (stop) {
            return low; // If stop is set, exit the partition function
        }
        int pivot = array[low];
        int i = low + 1;
        int j = high;
    
        while (i <= j) {
            if (stop) {
                return low; // If stop is set, exit the partition function
            }
    
            while (i <= j && array[i] <= pivot) {
                if (stop) {
                    return low; // If stop is set, exit the partition function
                }
                i++;
                currentIndex = i;
                SwingUtilities.invokeLater(this::repaint);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    
            while (i <= j && array[j] > pivot) {
                if (stop) {
                    return low; // If stop is set, exit the partition function
                }
                j--;
                currentIndex = j;
                SwingUtilities.invokeLater(this::repaint);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    
            if (i < j) {
                if (stop) {
                    return low; // If stop is set, exit the partition function
                }
                swap(i, j);
                SwingUtilities.invokeLater(this::repaint);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    
        swap(low, j);
        SwingUtilities.invokeLater(this::repaint);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return j;
    }
    

    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public void stopSorting() {

        stop = true;

    }

}

class GraphTraversalVisualizer extends JPanel {
    private GraphPanel graphPanel;
    private JPanel controlPanel;

    public GraphTraversalVisualizer() {
        graphPanel = new GraphPanel();
        controlPanel = new JPanel();

        String[] traversalOptions = { "BFS", "DFS" };
        JComboBox<String> traversalSelector = new JComboBox<>(traversalOptions);
        JButton startButton = new JButton("Start Traversal");
        JButton reuseButton = new JButton("Reuse Graph");
        JButton resetButton = new JButton("Reset Graph");

        startButton.addActionListener(e -> {
            String method = (String) traversalSelector.getSelectedItem();
            graphPanel.startTraversal(method);
        });

        reuseButton.addActionListener(e -> graphPanel.reuseGraph());

        resetButton.addActionListener(e -> graphPanel.resetGraph());

        controlPanel.add(traversalSelector);
        controlPanel.add(startButton);
        controlPanel.add(reuseButton);
        controlPanel.add(resetButton);

        setLayout(new BorderLayout());
        add(graphPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }
}

class GraphPanel extends JPanel {
    private final int NODE_RADIUS = 40;
    private java.util.List<Node> nodes;
    private java.util.List<Edge> edges;
    private javax.swing.Timer timer;
    private Queue<Node> traversalQueue;
    private Stack<Node> traversalStack;
    private String currentTraversalMethod;
    private boolean isTraversalRunning;
    private Node selectedNode;

    public GraphPanel() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        isTraversalRunning = false;
        selectedNode = null;

        addMouseListener(new MouseAdapter() {
            
            public void mousePressed(MouseEvent e) {
                if (isTraversalRunning)
                    return;

                if (SwingUtilities.isLeftMouseButton(e)) {
                    Node clickedNode = getNodeAtPosition(e.getX(), e.getY());
                    if (clickedNode == null) {
                        addNode(e.getX(), e.getY());
                    } else if (selectedNode == null) {
                        selectedNode = clickedNode;
                    } else if (selectedNode != clickedNode) {
                        addEdge(selectedNode, clickedNode);
                        selectedNode = null;
                    } else {
                        selectedNode = null;
                    }
                }
                repaint();
            }
        });
    }

    public void addNode(int x, int y) {
        nodes.add(new Node(x, y));
    }

    public void addEdge(Node start, Node end) {
        edges.add(new Edge(start, end));
    }

    private Node getNodeAtPosition(int x, int y) {
        for (Node node : nodes) {
            if (Math.hypot(node.getX() - x, node.getY() - y) < NODE_RADIUS) {
                return node;
            }
        }
        return null;
    }

    public void startTraversal(String method) {
        if (nodes.isEmpty() || isTraversalRunning)
            return;
        resetTraversalData();
        currentTraversalMethod = method;
        isTraversalRunning = true;

        Node startNode = nodes.get(0);
        if (method.equals("BFS")) {
            traversalQueue.add(startNode);
            startTraversalTimer(this::bfsStep);
        } else {
            traversalStack.push(startNode);
            startTraversalTimer(this::dfsStep);
        }
    }

    private void resetTraversalData() {
        traversalQueue = new LinkedList<>();
        traversalStack = new Stack<>();
        for (Node node : nodes) {
            node.setVisited(false);
        }
        isTraversalRunning = false;
        selectedNode = null;
        if (timer != null)
            timer.stop();
    }

    private void bfsStep() {
        if (!traversalQueue.isEmpty()) {
            Node node = traversalQueue.poll();
            if (!node.isVisited()) {
                node.setVisited(true);
                for (Node neighbor : getNeighbors(node)) {
                    if (!neighbor.isVisited())
                        traversalQueue.add(neighbor);
                }
            }
        } else {
            timer.stop();
            isTraversalRunning = false;
        }
    }

    private void dfsStep() {
        if (!traversalStack.isEmpty()) {
            Node node = traversalStack.pop();
            if (!node.isVisited()) {
                node.setVisited(true);
                for (Node neighbor : getNeighbors(node)) {
                    if (!neighbor.isVisited())
                        traversalStack.push(neighbor);
                }
            }
        } else {
            timer.stop();
            isTraversalRunning = false;
        }
    }

    private java.util.List<Node> getNeighbors(Node node) {
        java.util.List<Node> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getStart() == node && !edge.getEnd().isVisited()) {
                neighbors.add(edge.getEnd());
            } else if (edge.getEnd() == node && !edge.getStart().isVisited()) {
                neighbors.add(edge.getStart());
            }
        }
        return neighbors;
    }

    private void startTraversalTimer(Runnable traversalStep) {
        timer = new javax.swing.Timer(500, e -> {
            traversalStep.run();
            repaint();
        });
        timer.start();
    }

    public void resetGraph() {
        resetTraversalData();
        nodes.clear();
        edges.clear();
        repaint();
    }

    public void reuseGraph() {
        resetTraversalData();

        repaint();
    }

    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

       
        for (Edge edge : edges) {
            g.drawLine(edge.getStart().getX(), edge.getStart().getY(), edge.getEnd().getX(), edge.getEnd().getY());
        }

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            g.setColor(node.isVisited() ? Color.GREEN : Color.RED);
            g.fillOval(node.getX() - NODE_RADIUS / 2, node.getY() - NODE_RADIUS / 2, NODE_RADIUS, NODE_RADIUS);
            g.setColor(Color.BLACK);
            g.drawOval(node.getX() - NODE_RADIUS / 2, node.getY() - NODE_RADIUS / 2, NODE_RADIUS, NODE_RADIUS);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            String indexText = Integer.toString(i + 1);

            FontMetrics metrics = g.getFontMetrics();
            int textWidth = metrics.stringWidth(indexText);
            int textHeight = metrics.getHeight();

            int textX = node.getX() - textWidth / 2; 
            int textY = node.getY() + textHeight / 4; 
            g.drawString(indexText, textX, textY);

            if (node == selectedNode) {
                g.setColor(Color.BLUE);
                g.drawOval(node.getX() - NODE_RADIUS, node.getY() - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
            }
        }
    }
}

class Node {
    private final int x, y;
    private boolean visited;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.visited = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}

class Edge {
    private final Node start, end;

    public Edge(Node start, Node end) {
        this.start = start;
        this.end = end;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }
}