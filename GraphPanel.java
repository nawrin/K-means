import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class GraphPanel extends JPanel {

    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = new Color(44, 102, 230, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 6;
    private int numberYDivisions = 10;
    private List<Point> cluster1;
    private List<Point> cluster2;
    private List<Point> cluster3;

    public GraphPanel(List<Point> cluster1, List<Point> cluster2, List<Point> cluster3) {
        this.cluster1 = cluster1;
        this.cluster2 = cluster2;
        this.cluster3 = cluster3;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (3 - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < cluster1.size(); i++) {
            int x1 = (int) (cluster1.get(i).getX() * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - cluster1.get(i).getY()) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }
        
        List<Point> graphPoints1 = new ArrayList<>();
        for (int i = 0; i < cluster2.size(); i++) {
            int x1 = (int) (cluster2.get(i).getX() * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - cluster2.get(i).getY()) * yScale + padding);
            graphPoints1.add(new Point(x1, y1));
        }
        
        List<Point> graphPoints2 = new ArrayList<>();
        for (int i = 0; i < cluster3.size(); i++) {
            int x1 = (int) (cluster3.get(i).getX() * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - cluster3.get(i).getY()) * yScale + padding);
            graphPoints2.add(new Point(x1, y1));
        }

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (cluster1.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < 3; i++) {
            if (3 > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (3 - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((3 / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = i + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes 
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);

        g2.setStroke(oldStroke);
        g2.setPaint(Color.RED);
        for (int i = 0; i < graphPoints.size(); i++) {
            double x = graphPoints.get(i).getX() - pointWidth / 2;
            double y = graphPoints.get(i).getY() - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fill(new Ellipse2D.Double(x, y, ovalW, ovalH));
        }
        
        g2.setPaint(Color.BLUE);
        for (int i = 0; i < graphPoints1.size(); i++) {
            double x = graphPoints1.get(i).getX() - pointWidth / 2;
            double y = graphPoints1.get(i).getY() - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fill(new Ellipse2D.Double(x, y, ovalW, ovalH));
        }
        
        g2.setPaint(Color.GREEN);
        for (int i = 0; i < graphPoints2.size(); i++) {
            double x = graphPoints2.get(i).getX() - pointWidth / 2;
            double y = graphPoints2.get(i).getY() - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fill(new Ellipse2D.Double(x, y, ovalW, ovalH));
        }
    }

    private double getMinScore() {
    	return 0.0;
    }

    private double getMaxScore() {
        return 2.2;
    }

    public void setScores(List<Point> scores) {
        this.cluster1 = scores;
        invalidate();
        this.repaint();
    }

    public List<Point> getScores() {
        return cluster1;
    }
}
