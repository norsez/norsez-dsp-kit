package com.norsez.dsp.synth.swing;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import com.norsez.dsp.synth.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Dragger extends JPanel {
  private DraggerModel model;
  /**
   * The distance difference of pixel that makes up one value change in Dragger.
   */
  private static final int DISTANCE = 2;

  /**
   * history of the mouse last Y coordinate.
   */
  private int lastY ;

  private static CAction clickAction;
  private MAction mouseAction;


  static Font myfont = new Font ("Verdana",0,10);
  public Dragger ( SynthModel3 synth, int tag, int init ){
    this (synth,tag, 0,255,init);

  }


  private String prefix;
  public Dragger( SynthModel3 synth, int tag, int min, int max, int init ) {

    if (clickAction == null) clickAction = new CAction ();
    model = new DraggerModel (synth,tag,min,max,init);
    this.addMouseMotionListener(new MAction());
    this.addMouseListener(clickAction);
    this.setBackground( model.getBackgroundColor());
    this.setForeground(model.getForegroundColor());
    this.setBorder(BorderFactory.createEtchedBorder());
    this.setFocusable(true);
    this.setPreferredSize(new Dimension (70,15));
    this.setToolTipText(model.getParameterName());

    this.setFont(myfont);
    setPrefix ( "" );
  }

  public void setTag (int t){
    model.setTag(t);
    this.repaint();
  }

  public DraggerModel getModel (){
    return model;
  }

  /**
   *
   * @param s the String that will be added before the parameter value displayed.
   */
  public void setPrefix (String s ){
    prefix = s;
  }

  public void paintComponent ( Graphics g){
    super.paintComponent(g);
    String s = model.getValueDisplay();
    g.drawString(prefix + s,
                 //(int)(this.getWidth() * 0.5) - (int)(s.length()*5  * 0.5),
                 10,
                 (int)(this.getHeight() *0.5) + (int)(this.getFont().getSize()*0.5));
  }

  public void setLastY ( int y ){
    lastY = y;
  }

  public int getValue (){
    return model.getValue();
  }

  public void setValue ( int v){
    model.setValue( v );
  }

  public int getMininum (){
    return model.getMinimum();
  }

  public int getMaximum (){
    return model.getMaximum();
  }

  public int getLastY ( ) {
    return lastY;
  }

  static class CAction extends MouseAdapter {

    public void mousePressed ( MouseEvent e ){
      ((Dragger)e.getSource()).setLastY( e.getY());
    }

    public void mouseClicked ( MouseEvent e){
      if (e.getClickCount() == 2){
        ((Dragger)e.getSource()).getModel().reset();
        ((Dragger)e.getSource()).repaint();
      }
    }
  }

  static class MAction extends MouseMotionAdapter {



    public void mouseDragged ( MouseEvent m){
      Dragger d = (Dragger) m.getSource();

      if ( Math.abs( m.getY() - d.getLastY ()) >= DISTANCE){

         //decrease
         if ( m.getY() > d.getLastY()){
           if (m.getButton() == 0 && !m.isShiftDown()){
              d.getModel().decrement();
           }
           else
           if (m.getButton() == 0 && m.isShiftDown())
             d.getModel().decrement10();
         }
         else
         {
           if (m.getButton() == 0 && !m.isShiftDown()){
              d.getModel().increment();
              //System.out.println (d.model.getValue());
           }
           else
             if (m.getButton() == 0 && m.isShiftDown())
             d.getModel().increment10();

         }

         d.setLastY(m.getY());
         ((Dragger)m.getSource()).repaint();
      }


    }

  }

  public static void main ( String [] args ){
    JPanel p1 = new JPanel ();

    Dragger ds [] = new Dragger [5];
    for( int i=0; i< ds.length; i++){
      ds [i] = new Dragger (new com.norsez.dsp.project.monolith.Monolith (),i,100);
      p1.add(ds[i]);
    }
    Dragger d = new Dragger (new com.norsez.dsp.project.monolith.Monolith (),5,100);
    FlowLayout lm = new FlowLayout();

    p1.setLayout( lm);

    p1.add(d);
    d.setBorder(BorderFactory.createRaisedBevelBorder());
    d.setValue( 100);
    JOptionPane.showMessageDialog(null,new JScrollPane ( p1 ));
    System.exit(0);
  }

}
