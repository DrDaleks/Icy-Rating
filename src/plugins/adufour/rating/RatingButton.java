package plugins.adufour.rating;

import icy.plugin.PluginDescriptor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JButton;

import org.jdesktop.swingx.geom.Star2D;

import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.util.VarListener;

@SuppressWarnings("serial")
public class RatingButton extends JButton implements ActionListener, WindowFocusListener
{
    private final RatingDialog dialog;
    
    private long               nanoClock;
    
    private long               nanoClockThreshold = 200000000;
    
    /**
     * @param descriptor
     *            the plug-in descriptor to adjust the rating from
     * @return a new button with a star icon that will show a rate dialog pop-up when clicked. The
     *         button icon will also indicate whether the plug-in has been rated or not This
     *         component can be readily integrated into any container
     */
    public RatingButton(PluginDescriptor descriptor)
    {
        this.dialog = new RatingDialog(descriptor);
        
        setBorderPainted(false);
        setContentAreaFilled(false);
        setToolTipText("<html><font size=3>Rate this plug-in !</font></html>");
        
        addActionListener(this);
        
        dialog.addRatingListener(new VarListener<Integer>()
        {
            @Override
            public void valueChanged(Var<Integer> source, Integer oldValue, Integer newValue)
            {
                setToolTipText(null);
                repaint();
            }
            
            @Override
            public void referenceChanged(Var<Integer> source, Var<? extends Integer> oldReference, Var<? extends Integer> newReference)
            {
            }
        });
        
        dialog.addWindowFocusListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        long tic = System.nanoTime();
        
        if (tic - nanoClock > nanoClockThreshold)
        {
            System.out.println(tic-nanoClock);
            Point p = getLocationOnScreen();
            dialog.setLocation(p.x + getWidth() / 2 - dialog.getWidth() / 2, p.y + getHeight());
            dialog.setVisible(true);
        }
    }
    
    @Override
    public void windowLostFocus(WindowEvent e)
    {
        nanoClock = System.nanoTime();
        setSelected(false);
    }
    
    @Override
    public void windowGainedFocus(WindowEvent e)
    {
        
    }
    
    @Override
    protected void paintChildren(Graphics g)
    {
        int size = Math.min(getWidth(), getHeight());
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float x = getWidth() / 2f;
        float y = getHeight() / 2f + getHeight() / 25f;
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(size / 10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.fill(g2.getStroke().createStrokedShape(new Star2D(x, y, size / 4.2f, size / 2.2f, 5)));
        g2.setColor(dialog.getRatingColor());
        g2.fill(new Star2D(x, y, size / 4.2f - 1f, size / 2.2f - 1f, 5));
        g2.dispose();
    }
}
