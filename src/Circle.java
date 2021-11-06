import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Circle extends JLabel {

    private final boolean isStartLabel;
    private int screenX;
    private int screenY;
    private int myX;
    private int myY;
    private PathFindingVisualizer visualizer;


    public Circle(boolean isStartLabel, PathFindingVisualizer visualizer) {
        this.isStartLabel = isStartLabel;
        this.visualizer = visualizer;

        if (isStartLabel) {
            myX = 0;
        }
        else {
            myX = 16;
        }
        myY = 0;
        screenX = 0;
        screenY = 0;

        this.setBounds(myX, myY, 16, 16);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();
                myX = getX();
                myY = getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int x = getX();
                int y = getY();

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
                setLocation(x, y);
                if(isStartLabel)
                    visualizer.getDijkstra().setStartNode(y / 16, x / 16);
                else {
                    visualizer.getDijkstra().setEndNode(y / 16, x / 16);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dX = e.getXOnScreen() - screenX;
                int dY = e.getYOnScreen() - screenY;

                setLocation(myX + dX, myY + dY);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;

        if (isStartLabel) {
            g2D.setPaint(Color.GREEN);
        } else {
            g2D.setPaint(Color.RED);
        }
        g2D.fillOval(0, 0, 16, 16);
    }
}
