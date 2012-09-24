package plugins.adufour.rating;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;

import org.jdesktop.swingx.geom.Star2D;

@SuppressWarnings("serial")
public class StarButton extends JButton
{
    private Color starFillColor = Color.black;
    
    private Color starEdgeColor = Color.black;
    
    public StarButton()
    {
        this(Color.black, Color.black);
    }
    
    public StarButton(Color fillColor, Color edgeColor)
    {
        // reset the UI to enforce an empty button border
        // this allows having neighboring buttons with no space in-between
        setUI(new BasicButtonUI());
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusable(false);
        setOpaque(false);
        this.starFillColor = fillColor;
        this.starEdgeColor = edgeColor;
    }
    
    public void setStarFillColor(Color starFillColor)
    {
        this.starFillColor = starFillColor;
        repaint();
    }
    
    public void setStarEdgeColor(Color starEdgeColor)
    {
        this.starEdgeColor = starEdgeColor;
        repaint();
    }
    
    public void setStarColor(Color fillColor, Color edgeColor)
    {
        this.starFillColor = fillColor;
        this.starEdgeColor = edgeColor;
        repaint();
    }
    
    @Override
    protected void paintChildren(Graphics g)
    {
        int size = Math.min(getWidth(), getHeight()) - 2;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float x = getWidth() / 2f;
        float y = getHeight() / 2f + getHeight() / 25f;
        g2.setColor(this.starEdgeColor);
        g2.setStroke(new BasicStroke(size / 10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.fill(g2.getStroke().createStrokedShape(new Star2D(x, y, size / 4.2f, size / 2.2f, 5)));
        g2.setColor(this.starFillColor);
        g2.fill(new Star2D(x, y, size / 4.2f - 1f, size / 2.2f - 1f, 5));
        g2.dispose();
    }
}
