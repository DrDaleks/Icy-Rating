package plugins.adufour.rating;

import java.awt.Dimension;

import plugins.adufour.ezplug.EzPlug;

public class Rating extends EzPlug
{
    @Override
    public void clean()
    {
        
    }
    
    @Override
    protected void execute()
    {
    }
    
    @Override
    protected void initialize()
    {
        getUI().setActionPanelVisible(false);
        getUI().setProgressBarVisible(false);
        RatingButton b = new RatingButton(getDescriptor());
        b.setPreferredSize(new Dimension(50, 50));
        addComponent(b);
    }
    
}