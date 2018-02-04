import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;



public class HandWrittenFrame extends JFrame implements Runnable {

  static final int DOWNSAMPLE_WIDTH = 19;
  static final int DOWNSAMPLE_HEIGHT = 21;
  HEntry entry;
  Sample sample;
  DefaultListModel letterListModel = new DefaultListModel();
  KohonenNetwork net;
  Thread trainThread = null;

  HandWrittenFrame()
  {

    getContentPane().setLayout(null);
    entry = new HEntry();
    entry.reshape(168,150,200,128);
    getContentPane().add(entry);

    sample = new Sample(DOWNSAMPLE_WIDTH,DOWNSAMPLE_HEIGHT);
    sample.reshape(250,40,65,70);
    entry.setSample(sample);
    getContentPane().add(sample);

    setTitle("Hand Written Character Recognition");
    getContentPane().setLayout(null);
    setSize(405,382);
    setVisible(false);
    JLabel1.setText("Stored Patterns");
    getContentPane().add(JLabel1);
    JLabel1.setBounds(12,12,84,12);
    downSample.setText("Down Sample");
    downSample.setActionCommand("Down Sample");
    getContentPane().add(downSample);
    downSample.setBounds(252,305,120,24);
    add.setText("Add");
    add.setActionCommand("Add");
    getContentPane().add(add);
    add.setBounds(168,281,84,24);
    clear.setText("Clear");
    clear.setActionCommand("Clear");
    getContentPane().add(clear);
    clear.setBounds(168,305,84,24);
    recognize.setText("Recognize");
    recognize.setActionCommand("Recognize");
    getContentPane().add(recognize);
    recognize.setBounds(252,281,120,24);
    JScrollPane1.setVerticalScrollBarPolicy(
    javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    JScrollPane1.setOpaque(true);
    getContentPane().add(JScrollPane1);
    JScrollPane1.setBounds(12,24,144,132);
    JScrollPane1.getViewport().add(letters);
    letters.setBounds(0,0,126,129);
    del.setText("Delete");
    del.setActionCommand("Delete");
    getContentPane().add(del);
    del.setBounds(12,156,144,24);
    load.setText("Load");
    load.setActionCommand("Load");
    getContentPane().add(load);
    load.setBounds(12,180,72,24);
    save.setText("Save");
    save.setActionCommand("Save");
    getContentPane().add(save);
    save.setBounds(84,180,72,24);
    train.setText("Begin Training");
    train.setActionCommand("Begin Training");
    getContentPane().add(train);
    train.setBounds(12,204,144,24);
    JLabel8.setHorizontalTextPosition(
    javax.swing.SwingConstants.CENTER);
    JLabel8.setHorizontalAlignment(
    javax.swing.SwingConstants.CENTER);
    JLabel8.setFont(new Font("Dialog", Font.BOLD, 14));
    JLabel8.setBounds(12,240,120,24);
    JLabel5.setText("Draw Letters Here");
    getContentPane().add(JLabel5);
    JLabel5.setBounds(204,137,144,12);

     SymAction lSymAction = new SymAction();
    downSample.addActionListener(lSymAction);
    clear.addActionListener(lSymAction);
    add.addActionListener(lSymAction);
    del.addActionListener(lSymAction);
    SymListSelection lSymListSelection = new SymListSelection();
    letters.addListSelectionListener(lSymListSelection);
    load.addActionListener(lSymAction);
    save.addActionListener(lSymAction);
    train.addActionListener(lSymAction);
    recognize.addActionListener(lSymAction);

    letters.setModel(letterListModel);

  }

  javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
  javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
  javax.swing.JButton downSample = new javax.swing.JButton();
  javax.swing.JButton add = new javax.swing.JButton();
  javax.swing.JButton clear = new javax.swing.JButton();
  javax.swing.JButton recognize = new javax.swing.JButton();
  javax.swing.JScrollPane JScrollPane1 = new javax.swing.JScrollPane();
  javax.swing.JList letters = new javax.swing.JList();
  javax.swing.JButton del = new javax.swing.JButton();
  javax.swing.JButton load = new javax.swing.JButton();
  javax.swing.JButton save = new javax.swing.JButton();
  javax.swing.JButton train = new javax.swing.JButton();
  javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
  javax.swing.JLabel JLabel4 = new javax.swing.JLabel();
  javax.swing.JLabel tries = new javax.swing.JLabel();
  javax.swing.JLabel lastError = new javax.swing.JLabel();
  javax.swing.JLabel bestError = new javax.swing.JLabel();
  javax.swing.JLabel JLabel8 = new javax.swing.JLabel();
  javax.swing.JLabel JLabel5 = new javax.swing.JLabel();

  class SymAction implements java.awt.event.ActionListener {
    public void actionPerformed(java.awt.event.ActionEvent event)
    {
      Object object = event.getSource();
      if ( object == downSample )
        downSample_actionPerformed(event);
      else if ( object == clear )
        clear_actionPerformed(event);
      else if ( object == add )
        add_actionPerformed(event);
      else if ( object == del )
        del_actionPerformed(event);
      else if ( object == load )
        load_actionPerformed(event);
      else if ( object == save )
        save_actionPerformed(event);
      else if ( object == train )
        train_actionPerformed(event);
      else if ( object == recognize )
        recognize_actionPerformed(event);
    }
  }

  void downSample_actionPerformed(java.awt.event.ActionEvent event)
  {
    entry.downSample();

  }

  void clear_actionPerformed(java.awt.event.ActionEvent event)
  {
    entry.clear();
    sample.getData().clear();
    sample.repaint();

  }

  void add_actionPerformed(java.awt.event.ActionEvent event)
  {
    int i;

    String letter = JOptionPane.showInputDialog(
      "Please enter a letter you would like to assign this sample to.");
    if ( letter==null )
      return;

    if ( letter.length()>1 ) {
      JOptionPane.showMessageDialog(this,
                                    "Please enter only a single letter.","Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }

    entry.downSample();
    SampleData sampleData = (SampleData)sample.getData().clone();
    sampleData.setLetter(letter.charAt(0));

    for ( i=0;i<letterListModel.size();i++ ) {
      Comparable str = (Comparable)letterListModel.getElementAt(i);
      if ( str.equals(letter) ) {
        JOptionPane.showMessageDialog(this,
                                      "That letter is already defined, delete it first!","Error",
                                      JOptionPane.ERROR_MESSAGE);
        return;
      }

      if ( str.compareTo(sampleData)>0 ) {
        letterListModel.add(i,sampleData);
        return;
      }
    }
    letterListModel.add(letterListModel.size(),sampleData);
    letters.setSelectedIndex(i);
    entry.clear();
    sample.repaint();

  }


  void del_actionPerformed(java.awt.event.ActionEvent event)
  {
    int i = letters.getSelectedIndex();

    if ( i==-1 ) {
      JOptionPane.showMessageDialog(this,
                                    "Please select a letter to delete.","Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }

    letterListModel.remove(i);
  }

  class SymListSelection implements javax.swing.event.ListSelectionListener {
    public void valueChanged(javax.swing.event.ListSelectionEvent event)
    {
      Object object = event.getSource();
      if ( object == letters )
        letters_valueChanged(event);
    }
  }

   void letters_valueChanged(javax.swing.event.ListSelectionEvent event)
  {
    if ( letters.getSelectedIndex()==-1 )
      return;
    SampleData selected =
      (SampleData)letterListModel.getElementAt(letters.getSelectedIndex());
    sample.setData((SampleData)selected.clone());
    sample.repaint();
    entry.clear();

  }

   void load_actionPerformed(java.awt.event.ActionEvent event)
  {
    try {
      FileReader f;
      BufferedReader r;

      f = new FileReader( new File("./sample.dat") );
      r = new BufferedReader(f);
      String line;
      int i=0;

      letterListModel.clear();

      while ( (line=r.readLine()) !=null ) {
        SampleData ds =
          new SampleData(line.charAt(0),HandWrittenFrame.DOWNSAMPLE_WIDTH,HandWrittenFrame.DOWNSAMPLE_HEIGHT);
        letterListModel.add(i++,ds);
        int idx=2;
        for ( int y=0;y<ds.getHeight();y++ ) {
          for ( int x=0;x<ds.getWidth();x++ ) {
            ds.setData(x,y,line.charAt(idx++)=='1');
          }
        }
      }

      r.close();
      f.close();
      clear_actionPerformed(null);
      JOptionPane.showMessageDialog(this,
                                    "Loaded from 'sample.dat'.","Training",
                                    JOptionPane.PLAIN_MESSAGE);

    } catch ( Exception e ) {
      JOptionPane.showMessageDialog(this,
                                    "Error: " + e,"Training",
                                    JOptionPane.ERROR_MESSAGE);
    }

  }

  void save_actionPerformed(java.awt.event.ActionEvent event)
  {
    try {
      OutputStream os;
      PrintStream ps;

      os = new FileOutputStream( "./sample.dat",false );
      ps = new PrintStream(os);

      for ( int i=0;i<letterListModel.size();i++ ) {
        SampleData ds = (SampleData)letterListModel.elementAt(i);
        ps.print( ds.getLetter() + ":" );
        for ( int y=0;y<ds.getHeight();y++ ) {
          for ( int x=0;x<ds.getWidth();x++ ) {
            ps.print( ds.getData(x,y)?"1":"0" );
          }
        }
        ps.println("");
      }

      ps.close();
      os.close();
      clear_actionPerformed(null);
      JOptionPane.showMessageDialog(this,
                                    "Saved to 'sample.dat'.",
                                    "Training",
                                    JOptionPane.PLAIN_MESSAGE);

    } catch ( Exception e ) {
      JOptionPane.showMessageDialog(this,"Error: " +
                                    e,"Training",
                                    JOptionPane.ERROR_MESSAGE);
    }

  }

  public void run()
  {
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

      net = new KohonenNetwork(inputNeuron,outputNeuron,this);
      net.setTrainingSet(set);
      net.learn();
    } catch ( Exception e ) {
      JOptionPane.showMessageDialog(this,"Error: " + e,
                                    "Training",
                                    JOptionPane.ERROR_MESSAGE);
    }

  }

  void updateStats(long trial,double error,double best)
  {
    if ( (((trial%100)!=0) || (trial==10)) && !net.halt )
      return;

    if ( net.halt ) {
      trainThread = null;
      train.setText("Begin Training");
      JOptionPane.showMessageDialog(this,
                                    "Training has completed.","Training",
                                    JOptionPane.PLAIN_MESSAGE);
    }
    UpdateStats stats = new UpdateStats();
    stats._tries = trial;
    stats._lastError=error;
    stats._bestError=best;
    try {
      SwingUtilities.invokeAndWait(stats);
    } catch ( Exception e ) {
      JOptionPane.showMessageDialog(this,"Error: " + e,"Training",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

   void train_actionPerformed(java.awt.event.ActionEvent event)
  {
    if ( trainThread==null ) {
      train.setText("Stop Training");
      train.repaint();
      trainThread = new Thread(this);
      trainThread.start();
    } else {
      net.halt=true;
    }
  }

   void recognize_actionPerformed(java.awt.event.ActionEvent event)
  {
    if ( net==null ) {
      JOptionPane.showMessageDialog(this,
                                    "I need to be trained first!","Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
    entry.downSample();


    double input[] = new double[19*21];
    int idx=0;
    SampleData ds = sample.getData();
    for ( int y=0;y<ds.getHeight();y++ ) {
      for ( int x=0;x<ds.getWidth();x++ ) {
        input[idx++] = ds.getData(x,y)?.5:-.5;
      }
    }

    double normfac[] = new double[1];
    double synth[] = new double[1];

    int best = net.winner ( input , normfac , synth ) ;
    char map[] = mapNeurons();
    JOptionPane.showMessageDialog(this,
                                  "  " + map[best] + "   (Neuron #"
                                  + best + " fired)","That Letter Is",
                                  JOptionPane.PLAIN_MESSAGE);
    clear_actionPerformed(null);

  }

  char []mapNeurons()
  {
    char map[] = new char[letterListModel.size()];
    double normfac[] = new double[1];
    double synth[] = new double[1];

    for ( int i=0;i<map.length;i++ )
      map[i]='?';
    for ( int i=0;i<letterListModel.size();i++ ) {
      double input[] = new double[19*21];
      int idx=0;
      SampleData ds = (SampleData)letterListModel.getElementAt(i);
      for ( int y=0;y<ds.getHeight();y++ ) {
        for ( int x=0;x<ds.getWidth();x++ ) {
          input[idx++] = ds.getData(x,y)?.5:-.5;
        }
      }

      int best = net.winner ( input , normfac , synth ) ;
      map[best] = ds.getLetter();
    }
    return map;
  }


  public class UpdateStats implements Runnable {
    long _tries;
    double _lastError;
    double _bestError;

    public void run()
    {
      tries.setText(""+_tries);
      lastError.setText(""+_lastError);
      bestError.setText(""+_bestError);
    }
  }


}