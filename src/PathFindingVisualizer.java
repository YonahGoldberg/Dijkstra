import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PathFindingVisualizer extends JFrame implements ActionListener{

    private PathFinding dijkstra;
    private final JLayeredPane grid;
    private final JLabel[][] boxes;
    private final JButton start;
    private final JButton reset;
    private final JPanel menu;
    private final Timer timer;
    private final Circle startLabel;
    private final Circle endLabel;
    ArrayList<Node> shortestPath;
    private int shortestPathIndex;
    private int screenX;
    private int screenY;
    private int myX;
    private int myY;

    public PathFindingVisualizer() {
        this.setTitle("Path Finding");
        this.setSize(1120, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        this.setLocationRelativeTo(null);

        timer = new Timer(50, this);
        dijkstra = null;
        screenX = 0;
        screenY = 0;
        myX = 0;
        myY = 0;
        shortestPath = null;
        shortestPathIndex = 0;

        menu = new JPanel();
        menu.setSize(new Dimension(1120, 50));
        menu.setFocusable(true);

        start = new JButton();
        start.setText("START");
        start.setMaximumSize(new Dimension(100,25));
        start.addActionListener(this);
        menu.add(start);

        reset = new JButton();
        reset.setText("RESET");
        reset.setMaximumSize(new Dimension(100, 25));
        reset.addActionListener(this);
        menu.add(reset);

        grid = new JLayeredPane();
        grid.setPreferredSize(new Dimension(1120, 640));
        grid.setLayout(null);
        grid.setFocusable(true);

        boxes = new JLabel[40][70];
        for (int row = 0; row < boxes.length; row++) {
            for (int col = 0; col < boxes[0].length; col++) {
                boxes[row][col] = new JLabel();
                boxes[row][col].setBorder(new LineBorder(Color.GRAY));
                boxes[row][col].setOpaque(true);
                boxes[row][col].setBackground(Color.WHITE);
                boxes[row][col].setLayout(new BorderLayout());
                grid.add(boxes[row][col], Integer.valueOf(0));
                boxes[row][col].setBounds((col * 16), (row * 16), 16, 16);
            }
        }

        startLabel = new Circle(true, this);
        endLabel = new Circle(false, this);

        grid.add(startLabel, Integer.valueOf(1));
        grid.add(endLabel, Integer.valueOf(1));

        this.add(menu);
        this.add(grid);
        this.setVisible(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();

                myX = e.getX();
                myY = e.getY() - 50;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dX = e.getXOnScreen() - screenX;
                int dY = e.getYOnScreen() - screenY;

                int x = dX + myX;
                int y = dY + myY;

                for (int i = 0; i < 16; i++) {
                    if ((x + i) % 16 == 0) {
                        x = x + i;
                        break;
                    }
                    else if ((x - i) % 16 == 0) {
                        x = x - i;
                        break;
                    }
                }
                for (int i = 0; i < 16; i++) {
                    if ((y + i) % 16 == 0) {
                        y = y + i;
                        break;
                    }
                    else if ((y - i) % 16 == 0) {
                        y = y - i;
                        break;
                    }
                }
                if (x / 16 < 70 && y / 16 < 40) {
                    dijkstra.getGraph()[y / 16][x / 16].setIsWall(true);
                    boxes[y / 16][x / 16].setBackground(Color.BLACK);
                }
            }
        });
    }

    public void setDijkstra(PathFinding dijkstra) {
        this.dijkstra = dijkstra;
    }

    public PathFinding getDijkstra() {
        return dijkstra;
    }

    public void setVisitedBackground(int row, int col) {
        boxes[row][col].setBackground(Color.CYAN);
    }

    public void removeSetVisitedBackground() {
        for(JLabel[] row : boxes) {
            for (JLabel col : row) {
                if (col.getBackground().equals(Color.CYAN))
                    col.setBackground(Color.WHITE);
            }
        }
    }

    public void displayShortestPath(ArrayList<Node> shortestPath) {
        if(shortestPath != null) {
            this.shortestPath = shortestPath;
            timer.start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            dijkstra.dijkstra();
        }
        else if (e.getSource() == timer) {
            boxes[shortestPath.get(shortestPathIndex).getRow()][shortestPath.get(shortestPathIndex).getCol()].setBackground(Color.BLUE);
            shortestPathIndex++;
            if(shortestPathIndex == shortestPath.size()) {
                timer.stop();
                shortestPathIndex = 0;
            }
        }
        else if (e.getSource() == reset) {
            dijkstra.reset();
            timer.stop();
            for (JLabel[] row : boxes) {
                for (JLabel col : row) {
                    col.setBackground(Color.WHITE);
                }
            }
        }
    }
}
