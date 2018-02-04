import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.Math;

  public class Entry extends JPanel {
  protected Image entryImage;
  protected Graphics entryGraphics;
  protected Sample sample;
  protected int downSampleLeft;
  protected int downSampleRight;
  protected int downSampleTop;
  protected int downSampleBottom;
  protected int tempTop,tempLeft,tempRight,tempBottom;
  protected int tempLineTop,tempLineBottom,tempLineLeft,tempLineRight;
  protected double ratioX;
  protected double ratioY;
  protected int pixelMap[];
  public int recog;
  private int crop_top,crop_bottom,crop_left,crop_right;

  public Entry(Image entryImage)
  {

  enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK|
                 AWTEvent.MOUSE_EVENT_MASK|
                 AWTEvent.COMPONENT_EVENT_MASK);

  inItImage(entryImage);

  }

  public void inItImage(Image entryImage)
  {
  this.entryImage=entryImage;
  setSize(entryImage.getWidth (this)>=550?550:entryImage.getWidth(this),
          entryImage.getHeight(this)>=500?500:entryImage.getHeight(this));
  resetValues();
  }

  public void resetValues()
  {
   tempTop=-1;
   tempLeft=-1;
   tempRight=-1;
   tempBottom=-1;
   tempLineTop=-1;
   tempLineBottom=-1;
   tempLineLeft=-1;
   tempLineRight=-1;
   crop_top=0;
   crop_left=0;
   crop_right=entryImage.getWidth (this)>=550?550:entryImage.getWidth(this);
   crop_bottom=entryImage.getHeight(this)>=500?500:entryImage.getHeight(this);
   downSampleLeft=0;
   downSampleRight=0;
   downSampleTop=0;
   downSampleBottom=0;
   repaint();

  }

  public void paint(Graphics g)
  {
    g.drawImage(entryImage,0,0,this);
    g.setColor(Color.black);
    g.drawRect(0,0,getWidth(),getHeight());
    g.setColor(Color.red);
    g.drawRect(downSampleLeft,
               downSampleTop,
               downSampleRight-downSampleLeft,
               downSampleBottom-downSampleTop);
    g.setColor (Color.cyan );
    g.drawRect(tempLeft,
               tempTop,
               tempRight-tempLeft,
               tempBottom-tempTop);
    g.setColor(Color.orange );
    g.setXORMode (Color.black );

    g.fillRect (0,0,entryImage.getWidth (this)>=550?550:entryImage.getWidth(this),crop_top);
    g.fillRect (0,crop_top,crop_left,entryImage.getHeight(this)>=500?500:entryImage.getHeight(this));
    g.fillRect (crop_right,crop_top,(entryImage.getWidth (this)>=550?550:entryImage.getWidth(this))-crop_right,entryImage.getHeight(this)>=500?500:entryImage.getHeight(this)-crop_top);
    g.fillRect (crop_left,crop_bottom,crop_right-crop_left,(entryImage.getHeight(this)>=500?500:entryImage.getHeight(this))-crop_bottom);
    g.setPaintMode ();
    g.setColor (Color.gray );
    g.drawLine (crop_left,0,crop_left,entryImage.getHeight(this)>=500?500:entryImage.getHeight(this));
    g.drawLine (0,crop_top,entryImage.getWidth (this)>=550?550:entryImage.getWidth(this),crop_top);
    g.drawLine (0,crop_bottom,entryImage.getWidth (this)>=550?550:entryImage.getWidth(this),crop_bottom);
    g.drawLine (crop_right,0,crop_right,entryImage.getHeight(this)>=500?500:entryImage.getHeight(this));

  }

  protected void processMouseEvent(MouseEvent e)
  {

    if ( e.getID()==MouseEvent.MOUSE_PRESSED )
    {
     resetValues();
     crop_left=e.getX ();
     crop_top=e.getY ();
     tempLeft=0;
     tempLineTop=0;

    }



  }



  protected void processMouseMotionEvent(MouseEvent e)
  {

    if ( e.getID()!=MouseEvent.MOUSE_DRAGGED )
      return;

    crop_right=e.getX ();
    crop_bottom=e.getY ();
    repaint();
  }


  public void setSample(Sample s)
  {
    sample = s;
  }

  public Sample getSample()
  {
    return sample;
  }

  protected boolean hLineClear(int y)
  {
    int w = entryImage.getWidth(this);
    for ( int i=0;i<w;i++ ) {
      if ( pixelMap[(y*w)+i] !=-1 )
        return false;
    }
    return true;
  }

  protected boolean hLineClearWithin(int y,int left,int right)
  {

  int w = entryImage.getWidth(this);
    for ( int i=0;i<w;i++ ) {
      if ( pixelMap[(y*w)+i] !=-1 && i>left && i<right)
        return false;
    }
    return true;
  }


  protected boolean vLineClear(int x)
  {
    int w = entryImage.getWidth(this);
    int h = entryImage.getHeight(this);
    for ( int i=0;i<h;i++ ) {
      if ( pixelMap[(i*w)+x]!= -1 )
        return false;
    }
    return true;
  }

   protected boolean vLineClearWithin(int x,int t,int b)
  {
    int w = entryImage.getWidth(this);
    int h = entryImage.getHeight(this);
    for ( int i=0;i<h;i++ ) {
      if ( pixelMap[(i*w)+x]!= -1 && i>t && i<b)
        return false;
    }
    return true;
  }

  protected void findBounds(int w,int h)
  {

    // top line
    for ( int y=crop_top;y<crop_bottom;y++ ) {
      if ( !hLineClearWithin(y,crop_left,crop_right) ) {
        downSampleTop=y-1;
        break;
      }

    }
    // bottom line
    for ( int y=crop_bottom-1;y>=crop_top;y-- ) {
      if ( !hLineClearWithin(y,crop_left,crop_right) ) {
        downSampleBottom=y+1;
        break;
      }
    }
    // left line
    for ( int x=crop_left;x<crop_right;x++ ) {
      if ( !vLineClearWithin(x,crop_top,crop_bottom) ) {
        downSampleLeft = x-1;
        break;
      }
    }

    // right line
    for ( int x=crop_right-1;x>=crop_left;x-- ) {
      if ( !vLineClearWithin(x,crop_top,crop_bottom) ) {
        downSampleRight = x+1;
        break;
      }
    }
  }


  protected boolean downSampleQuadrant(int x,int y, int left, int top)
  {
    int w = entryImage.getWidth(this);
    int startX = (int)(left+(x*ratioX));
    int startY = (int)(top+(y*ratioY));
    int endX = (int)(startX + ratioX);
    int endY = (int)(startY + ratioY);

    for ( int yy=startY;yy<=endY;yy++ ) {
      for ( int xx=startX;xx<=endX;xx++ ) {
        int loc = xx+(yy*w);

        if ( pixelMap[ loc  ]!= -1 )
          return true;
      }
    }

    return false;
  }


  public boolean downSampleNextLine()
  {
     if(tempLineBottom==-1)
      tempLineBottom=downSampleTop;
     if(tempLineBottom>=downSampleBottom)
     {
     tempLeft=-1;
     tempRight=-1;
     return false;
     }
     if(tempLineTop==-1)
      tempLineTop=downSampleTop;
     else
      tempLineTop=tempLineBottom;

     int i;
     for(i=tempLineTop+1;hLineClearWithin(i,downSampleLeft,downSampleRight)==true; i++);
       tempLineTop=i-1;
     for(i=tempLineTop+1;hLineClearWithin(i,downSampleLeft,downSampleRight)!=true;i++);
       tempLineBottom=i;

     for(i=downSampleLeft; vLineClearWithin(i,tempLineTop,tempLineBottom)==true;i++);
       tempLineLeft=i-1;
     for(i=downSampleRight; vLineClearWithin(i,tempLineTop,tempLineBottom)==true;i--);
       tempLineRight=i+1;
     repaint();
     tempLeft=-1;
     tempRight=-1;
     return true;

  }


  public boolean downSampleNext()
  {
    if(tempRight==-1)
     tempRight=tempLineLeft;
    if(tempRight>=tempLineRight)
     return false;
     if(tempLeft==-1)
      tempLeft=tempLineLeft;
     else
      tempLeft=tempRight;

     int i;
     recog=0;
     
     for(i=tempLeft; vLineClearWithin(i,tempLineTop,tempLineBottom)==true; i++)
     {
       recog++;
     }

          tempLeft=i-1;
     for(i=tempLeft+1;vLineClearWithin(i,tempLineTop,tempLineBottom)!=true;i++);
     tempRight=i;

     for(i=tempLineTop; hLineClearWithin(i,tempLeft,tempRight)==true;i++);
     tempTop=i-1;
     for(i=tempLineBottom; hLineClearWithin(i,tempLeft,tempRight)==true; i--);
     tempBottom=i+1;
     repaint();
     downSample(tempLeft, tempRight, tempTop,tempBottom );
     repaint();
     return true;
     
   }




  void downSample(int left, int right, int top, int bottom)
  {
    int w = entryImage.getWidth(this);
    int h = entryImage.getHeight(this);

    try {

      SampleData data = sample.getData();

      ratioX = (double)(right-
                        left)/(double)data.getWidth();
      ratioY = (double)(bottom-
                        top)/(double)data.getHeight();

      for ( int y=0;y<data.getHeight();y++ ) {
        for ( int x=0;x<data.getWidth();x++ ) {
          if ( downSampleQuadrant(x,y,left,top) )
            data.setData(x,y,true);
          else
            data.setData(x,y,false);
        }
      }

      sample.repaint();
      repaint();
    } catch (Exception e ) {
    }
  }


  
  public void clear()
  {
    this.entryGraphics.setColor(Color.white);
    this.entryGraphics.fillRect(0,0,getWidth(),getHeight());
    this.downSampleBottom =
    this.downSampleTop =
    this.downSampleLeft =
    this.downSampleRight = 0;
    repaint();
  }

  public void crop()
  {
    int w = entryImage.getWidth(this);
    int h = entryImage.getHeight(this);

    PixelGrabber grabber = new PixelGrabber(
                                           entryImage,
                                           0,
                                           0,
                                           w,
                                           h,
                                           true);
    try {

      grabber.grabPixels();
      pixelMap = (int[])grabber.getPixels();
      findBounds(w,h);
         }
    catch(Exception e){}
    repaint();
  }

}
