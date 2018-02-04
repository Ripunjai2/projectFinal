import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class PrintedFrame extends JFrame implements ActionListener
{

 public static final int DOWNSAMPLE_WIDTH = 19;
 public static final int DOWNSAMPLE_HEIGHT = 21;
 Entry entry,tentry;

  Sample sample;
  JPanel recPanel=new JPanel();
  JPanel trainPanel=new JPanel();
  String fileName=new String("v.gif"),
          textFileName=new String("a.txt");
  String tFileName=new String("abcd.gif");
  JButton crop=new JButton(new ImageIcon("icons/crop.gif")),
          downsample=new JButton("Down Sample"),
          recogniseAll=new JButton(new ImageIcon("icons/recognize.gif")),
          open=new JButton(new ImageIcon("icons/open.gif")),
          topen=new JButton(new ImageIcon("icons/open.gif")),
          edit=new JButton(new ImageIcon("icons/edit.gif")),
          train=new JButton(new ImageIcon("icons/train.gif"));
  JTextArea recText=new JTextArea("",100,100);
  boolean cropPerformed=false;
  KohonenNetwork net;
  DefaultListModel letterListModel = new DefaultListModel();
     JTabbedPane tab=new JTabbedPane();

  PrintedFrame()
  {
    Container c=getContentPane();
    c.add(tab);
    tab.addTab ("Recognition",recPanel);
    tab.addTab ("Training",trainPanel);
    recPanel.setLayout (null);

    JToolBar recTB=new JToolBar(SwingConstants.HORIZONTAL);
    JToolBar tjtb=new JToolBar(SwingConstants.HORIZONTAL);
    recTB.setBounds (1,1,300,40);
    tjtb.setBounds (1,1,300,40);
    recPanel.add (recTB);
    trainPanel.add (tjtb);
    open.setToolTipText ("Open file");
    crop.setToolTipText ("Find Bounds");
    recogniseAll.setToolTipText ("Recognize");
    topen.setToolTipText ("Open file");
    train.setToolTipText ("Start Training");
    recTB.add (open);
    recTB.add (crop);
    recTB.add (recogniseAll);
    edit.setRolloverIcon (new ImageIcon("icons/edit_selected.gif"));
    edit.setBounds (450,460,150,40);
    recPanel.add (edit);
    Image entryImage=(new ImageIcon(fileName)).getImage();
    entry = new Entry(entryImage);

    entry.setLocation (1,50);
    recPanel.add(entry);

    JScrollPane jsp2=new JScrollPane(recText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jsp2.setPreferredSize (new Dimension(400,200));
    jsp2.setBounds(10,300,400,200);
    recPanel.add(jsp2);


    sample = new Sample(DOWNSAMPLE_WIDTH,DOWNSAMPLE_HEIGHT);
    entry.setSample(sample);

      trainPanel.setLayout(null);
      Image tentryImage=(new ImageIcon(tFileName)).getImage();
      tentry = new Entry(tentryImage);
      tentry.setLocation(1,50);
      trainPanel.add(tentry);
      sample.setSize (200,100);
      tentry.setSample(sample);
      tjtb.add (topen);
      tjtb.add (train);


    setTitle("Printed Frame Recognition");
    setSize(800,600);
    setVisible(true);
    downsample.addActionListener (this);
    crop.addActionListener (this);
    recogniseAll.addActionListener (this);
    train.addActionListener(this);
    open.addActionListener (this);
    topen.addActionListener (this);
    edit.addActionListener (this);

    recogniseAll.setEnabled (cropPerformed);

  }



  public void actionPerformed(ActionEvent e)
  { if(e.getSource ()==open)
      open_action();
    else if(e.getSource()==topen)
      topen_action();
    else if(e.getSource()==train)
      train_action();
    else if(e.getSource ()==crop)
    {
      entry.crop();
      cropPerformed=true;
      recogniseAll.setEnabled (cropPerformed);
    }
    else if(e.getSource ()==edit)
      {
        this.setVisible (false);
        Editor e1=new Editor(recText.getText ());
        e1.show ();
 e1.addWindowListener(new WindowAdapter()
                       { public void windowClosing(WindowEvent e)
                         {MainScreen ms1=new MainScreen();
 ms1.addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){System.exit(0);}});}});
      }
    else if(e.getSource()==recogniseAll)
    {
      if(cropPerformed==true)
      recogniseAll_action();
    }
  }

 public void open_action()
 {
       JFileChooser fileChooser=new JFileChooser();
       fileChooser.setFileSelectionMode (JFileChooser.FILES_ONLY);
       fileChooser.setCurrentDirectory (new File("."));
       int result=fileChooser.showOpenDialog (this);
       if(result==JFileChooser.CANCEL_OPTION);
       else
       {
       if(!((fileChooser.getSelectedFile ()).getName ()).endsWith (".gif") &&
               !((fileChooser.getSelectedFile ()).getName ()).endsWith (".GIF") &&
               !((fileChooser.getSelectedFile ()).getName ()).endsWith (".jpg"))
          JOptionPane.showMessageDialog(this,
                                    "File Format Mismatch","",
                                    JOptionPane.INFORMATION_MESSAGE);
       else
       {
         fileName=(fileChooser.getSelectedFile ()).getAbsolutePath();


       Image entryImage=(new ImageIcon(fileName)).getImage();
       entry.inItImage(entryImage);
       repaint();
       }
       }

     }

     public void topen_action()
     {
           JFileChooser fileChooser=new JFileChooser(".");
       fileChooser.setFileSelectionMode (JFileChooser.FILES_ONLY);
       int result=fileChooser.showOpenDialog (this);
       if(result==JFileChooser.CANCEL_OPTION);
       else
       {

        if(!((fileChooser.getSelectedFile ()).getName ()).endsWith (".gif") &&
               !((fileChooser.getSelectedFile ()).getName ()).endsWith (".GIF"))
          JOptionPane.showMessageDialog(this,
                                    "File Format Mismatch","",
                                    JOptionPane.INFORMATION_MESSAGE);
        else
        {
         tFileName=((File)(fileChooser.getSelectedFile ())).getAbsolutePath();

        Image tentryImage=(new ImageIcon(tFileName)).getImage();
       tentry.inItImage(tentryImage);
       repaint();
       }
       }
      }

      public void train_action()
      {
//      JFrame jf =  new JFrame("Training .. .. ..");
//      jf.setBackground(Color.);
//      jf.setSize(200,100);
//      jf.setVisible(true);
          int status1=0;
             tentry.crop();
             char c='A';
             int i=0;
           while(tentry.downSampleNextLine()==true)
           {
             for(;tentry.downSampleNext()==true;i++)
             {

              SampleData sampleData = (SampleData)sample.getData().clone();
              sampleData.setLetter(c++);

              if(c=='Z')
              {
                c='a';
                c--;
              }

              if(c=='z')
              {
               c='0';
               c--;
              }

              letterListModel.add(letterListModel.size(),sampleData);
             }

    try {
      int inputNeuron = HandWrittenFrame.DOWNSAMPLE_HEIGHT*
        HandWrittenFrame.DOWNSAMPLE_WIDTH;
      int outputNeuron = letterListModel.size();

      TrainingSet set = new TrainingSet(inputNeuron,outputNeuron);
      set.setTrainingSetCount(letterListModel.size());

      for ( int t=0;t<letterListModel.size();t++ ) {
        int idx=0;
        SampleData ds = (SampleData)letterListModel.getElementAt(t);
        for ( int y=0;y<ds.getHeight();y++ ) {
          for ( int x=0;x<ds.getWidth();x++ ) {
            set.setInput(t,idx++,ds.getData(x,y)?.5:-.5);
          }

        }
      }

      net = new KohonenNetwork(inputNeuron,outputNeuron);
      net.setTrainingSet(set);

      net.learn();

      if(net!=null)
      {


      }

    } catch ( Exception ev ) {
      JOptionPane.showMessageDialog(this,"Error: " + ev,
                                    "Training",
                                    JOptionPane.ERROR_MESSAGE);
    }

    }
   // jf.setVisible(false);
    JOptionPane.showMessageDialog (this,"Training has been completed","OK",JOptionPane.INFORMATION_MESSAGE);
     }

     public void recogniseAll_action()
     {
    if ( net==null ) {
      JOptionPane.showMessageDialog(this,
                                    "I need to be trained first!","Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
while(entry.downSampleNextLine()==true)
{
               recText.setText(recText.getText()+'\n');


  while(entry.downSampleNext ()==true)
  {

  if(entry.recog >=6)
       {
          recText.setText(recText.getText()+"  ");
        }

      double input[] = new double[DOWNSAMPLE_WIDTH*DOWNSAMPLE_HEIGHT];
    int idx=0;
    SampleData ds = (entry.getSample()).getData();

    for ( int y=0;y<ds.getHeight();y++ ) {
     for ( int x=0;x<ds.getWidth();x++ ) {

        input[idx++] = ds.getData(x,y)?.5:-.5;
      }
    }



    double normfac[] = new double[1];
    double synth[] = new double[1];

    int best = net.winner ( input , normfac , synth ) ;

    char map[] = mapNeurons();

    recText.setText(recText.getText()+map[best]);


    }
   }
   JOptionPane.showMessageDialog(this,
                                    "Recognition Completed","",
                                    JOptionPane.INFORMATION_MESSAGE);
  entry.resetValues ();
  cropPerformed=false;
  recogniseAll.setEnabled (cropPerformed);
  repaint();
  }



  char []mapNeurons()
  {
    char map[] = new char[letterListModel.size()];
    double normfac[] = new double[1];
    double synth[] = new double[1];

    for ( int i=0;i<map.length;i++ )
      map[i]='?';
    for ( int i=0;i<letterListModel.size();i++ )
    {
      double input[] = new double[DOWNSAMPLE_HEIGHT*DOWNSAMPLE_WIDTH];
      int idx=0;
      SampleData ds = (SampleData)letterListModel.getElementAt(i);
      for ( int y=0;y<ds.getHeight();y++ )
      {
        for ( int x=0;x<ds.getWidth();x++ )
        {
          input[idx++] = ds.getData(x,y)?.5:-.5;
        }
      }

      int best = net.winner ( input , normfac , synth ) ;
      map[best] = ds.getLetter();
    }
    return map;
  }
}