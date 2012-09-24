package plugins.adufour.rating;

import icy.plugin.PluginDescriptor;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.util.VarListener;

@SuppressWarnings("serial")
public class RatingButton extends StarButton implements ActionListener, WindowFocusListener
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
        
        setToolTipText("<html><font size=3>Rate this plug-in !</font></html>");
        
        addActionListener(this);
        
        dialog.addRatingListener(new VarListener<Integer>()
        {
            @Override
            public void valueChanged(Var<Integer> source, Integer oldValue, Integer newValue)
            {
                setToolTipText(null);
                setStarFillColor(dialog.getRatingColor());
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
            Point p = getLocationOnScreen();
            dialog.setLocation(p.x + getWidth() / 2 - dialog.getWidth() / 2, p.y + getHeight());
            dialog.setVisible(true);
        }
    }
    
    @Override
    public void windowLostFocus(WindowEvent e)
    {
        nanoClock = System.nanoTime();
    }
    
    @Override
    public void windowGainedFocus(WindowEvent e)
    {
        
    }
}
