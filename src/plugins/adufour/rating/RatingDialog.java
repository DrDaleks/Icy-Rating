package plugins.adufour.rating;

import icy.plugin.PluginDescriptor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.Point2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import plugins.adufour.vars.lang.VarInteger;
import plugins.adufour.vars.util.VarListener;

import com.sun.awt.AWTUtilities;

@SuppressWarnings("serial")
public class RatingDialog extends JDialog implements WindowFocusListener
{
    private static final String  DEFAULT_COMMENT = "Enter your comment here...";
    
    private static final Color[] ratingColorCode = { Color.black, Color.red, Color.orange, Color.yellow, new Color(173, 255, 47), Color.green };
    
    private VarInteger           rating          = new VarInteger("rating", 0);
    
    private String               comment         = DEFAULT_COMMENT;
    
    /**
     * Creates a new dialog letting the user rate and comment the specified plug-in
     */
    public RatingDialog(PluginDescriptor pd)
    {
        super((Frame) null, "Rate me");
        
        // TODO retrieve rating and comment online
        
        addWindowFocusListener(this);
        
        AWTUtilities.setWindowOpaque(this, false);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setUndecorated(true);
        setPreferredSize(new Dimension(170, 180));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel()
        {
            private final Color darkColor   = Color.darkGray.darker();
            private final Color brightColor = Color.darkGray;
            
            @Override
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                
                // arrow background (triangle)
                g2.setColor(darkColor);
                g2.fillPolygon(new int[] { getWidth() / 2 - 6, getWidth() / 2 + 1, getWidth() / 2 + 7 }, new int[] { 5, 0, 5 }, 3);
                
                // rating background: dark-bright-dark
                g2.setPaint(new GradientPaint(new Point2D.Float(0, 4), darkColor, new Point2D.Float(0, getHeight() / 4), brightColor, true));
                g2.fillRoundRect(0, 4, getWidth(), getHeight() / 2, 20, 20);
                
                // comment background: bright-dark
                g2.setPaint(new GradientPaint(new Point2D.Float(0, getHeight() / 4), brightColor, new Point2D.Float(0, getHeight() / 2), darkColor, false));
                g2.fillRoundRect(0, getHeight() / 4, getWidth(), getHeight() - getHeight() / 4, 20, 20);
                
                // white rounded rectangle border
                g2.setColor(Color.white);
                g2.drawRoundRect(1, 5, getWidth() - 3, getHeight() - 7, 18, 18);
                
                // white arrow (triangle)
                g2.setColor(darkColor);
                g2.drawLine(getWidth() / 2 - 5, 5, getWidth() / 2 + 5, 5);
                g2.setColor(Color.white);
                g2.drawLine(getWidth() / 2 - 5, 5, getWidth() / 2, 1);
                g2.drawLine(getWidth() / 2, 1, getWidth() / 2 + 5, 5);
                
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 5, 4, 5));
        mainPanel.setOpaque(false);
        
        Font font = new Font("Helvetica", Font.PLAIN, 12);
        
        mainPanel.add(Box.createVerticalStrut(5));
        
        JLabel jLabelRating = new JLabel(" Your rating ");
        jLabelRating.setOpaque(false);
        jLabelRating.setFont(font.deriveFont(Font.BOLD + Font.ITALIC));
        jLabelRating.setForeground(Color.white);
        jLabelRating.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(jLabelRating);
        
        mainPanel.add(Box.createVerticalStrut(5));
        
        JPanel rate = new JPanel();
        rate.setBorder(new EmptyBorder(0, 5, 0, 5));
        rate.setOpaque(false);
        rate.setLayout(new GridLayout(1, 5, 0, 0));
        
        final StarButton[] stars = new StarButton[5];
        
        for (int i = 0; i < 5; i++)
        {
            final int index = i;
            StarButton star = new StarButton(getRatingColor(), getRatingColor());
            star.setPreferredSize(new Dimension(24, 24));
            star.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseEntered(MouseEvent e)
                {
                    for (int j = 0; j <= index; j++)
                    {
                        stars[j].setStarColor(ratingColorCode[index + 1], ratingColorCode[index + 1]);
                    }
                    for (int j = index + 1; j < 5; j++)
                    {
                        stars[j].setStarColor(ratingColorCode[0], ratingColorCode[0]);
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e)
                {
                    int currentRating = rating.getValue();
                    
                    for (int j = 0; j < currentRating; j++)
                    {
                        stars[j].setStarFillColor(getRatingColor());
                    }
                    for (int j = currentRating; j < 5; j++)
                    {
                        stars[j].setStarColor(ratingColorCode[0], ratingColorCode[0]);
                    }
                }
                
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    super.mouseClicked(e);
                    setRating(index + 1);
                }
            });
            stars[i] = star;
            rate.add(star);
        }
        
        rate.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(rate);
        
        mainPanel.add(Box.createVerticalStrut(7));
        
        JLabel comm = new JLabel(" Your comment ");
        comm.setFont(font.deriveFont(Font.BOLD + Font.ITALIC));
        comm.setForeground(Color.white);
        comm.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(comm);
        
        mainPanel.add(Box.createVerticalStrut(7));
        
        final JTextArea text = new JTextArea(comment, 6, 15);
        text.setOpaque(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(true);
        text.setCaretColor(Color.white);
        text.setForeground(Color.white);
        text.setBackground(Color.darkGray);
        text.setFont(font);
        text.setAlignmentX(CENTER_ALIGNMENT);
        if (comment.equalsIgnoreCase(DEFAULT_COMMENT))
        {
            text.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    text.setText("");
                    text.removeMouseListener(this);
                    //super.mouseClicked(e);
                }
            });
        }
        
        JScrollPane js = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        js.setBorder(new Border()
        {
            
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(92, 92, 92));
                g2.drawRoundRect(x, y, width - 1, height - 1, 14, 14);
                g2.dispose();
            }
            
            @Override
            public Insets getBorderInsets(Component c)
            {
                return new Insets(4, 4, 4, 2);
            }
            
            @Override
            public boolean isBorderOpaque()
            {
                return false;
            }
            
        });
        js.getViewport().setOpaque(false);
        js.setOpaque(false);
        js.setAutoscrolls(true);
        
        mainPanel.add(js);
        
        setContentPane(mainPanel);
        
        pack();
    }
    
    public void setVisible(boolean visible)
    {
        super.setVisible(visible);
        if (!visible) uploadRating();
    }
    
    private void setRating(int newRating)
    {
        rating.setValue(newRating);
    }
    
    private void uploadRating()
    {
        // TODO Auto-generated method stub
    }
    
    // WindowFocusListener
    
    @Override
    public void windowGainedFocus(WindowEvent e)
    {
        
    }
    
    @Override
    public void windowLostFocus(WindowEvent e)
    {
        setVisible(false);
    }
    
    public void addRatingListener(VarListener<Integer> listener)
    {
        rating.addListener(listener);
    }
    
    public Color getRatingColor()
    {
        return ratingColorCode[rating.getValue()];
    }
}
