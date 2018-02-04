import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.io.*;


public class Editor extends JFrame implements ActionListener
{
JTextArea ja1=new JTextArea(200,200);
JButton new1=new JButton(new ImageIcon("icons/new.gif"));
JButton open=new JButton(new ImageIcon("icons/open.gif"));
JButton save=new JButton(new ImageIcon("icons/save.gif"));
JButton cut=new JButton(new ImageIcon("icons/cut.gif"));
JButton copy=new JButton(new ImageIcon("icons/copy.gif"));
JButton paste=new JButton(new ImageIcon("icons/paste.gif"));
JButton find=new JButton(new ImageIcon("icons/find.gif"));

String fontName[];
JMenuBar menuBar=new JMenuBar();
JMenu file1=new JMenu("File");
JMenuItem file_new1=new JMenuItem("New");
JMenuItem file_open=new JMenuItem("Open");
JMenuItem file_save=new JMenuItem("Save");
JMenuItem file_exit=new JMenuItem("Exit");
JMenu edit=new JMenu("Edit");
JMenuItem edit_cut=new JMenuItem("Cut");
JMenuItem edit_copy=new JMenuItem("Copy");
JMenuItem edit_paste=new JMenuItem("Paste");
JComboBox jcb1;
JComboBox jcb2;
String clipText=new String("");

Editor(String str)
{
Container c=getContentPane();
c.setLayout(new BorderLayout());
JPanel jp=new JPanel(new BorderLayout());
JPanel jp1=new JPanel(new FlowLayout(FlowLayout.LEFT));
JPanel jp2=new JPanel(new FlowLayout(FlowLayout.LEFT));
JToolBar jtb1=new JToolBar(SwingConstants.VERTICAL );
c.add(jp,"North");
jp.add (jp1,"North");
jp.add(jp2,"Center");
c.add (jtb1,"West");
jp2.setBackground(Color.lightGray);


GraphicsEnvironment ge;
ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
fontName=ge.getAvailableFontFamilyNames();
jcb1=new JComboBox(fontName);
jcb1.setSelectedItem ("Times New Roman");
jcb2=new JComboBox();
for(int i=8;i<=72;i=i+2)
jcb2.addItem (""+i);
jcb2.setSelectedItem (""+24);
ja1.setFont(new Font(jcb1.getSelectedItem ().toString(),Font.PLAIN,jcb2.getSelectedIndex ()*2+8));

menuBar.add (file1);
file1.add (file_new1);
file1.add (file_open);
file1.add(file_save);
file1.add(file_exit);
menuBar.add (edit);
edit.add(edit_cut);
edit.add(edit_copy);
edit.add(edit_paste);

new1.setToolTipText ("New");
open.setToolTipText ("Open");
save.setToolTipText ("Save");
cut.setToolTipText ("Cut");
copy.setToolTipText ("Copy");
paste.setToolTipText ("Paste");
find.setToolTipText ("Find");
jp1.add(menuBar);
jtb1.add(new1);
jtb1.add(open);
jtb1.add(save);
jtb1.add (new JSeparator());
jtb1.add(cut);
jtb1.add(copy);
jtb1.add(paste);
jtb1.add (new JSeparator());
jtb1.add(find);
jp2.add (new JLabel(" Font Style "));
jp2.add(jcb1);
jp2.add (new JLabel("Size "));
jp2.add(jcb2);


c.add(ja1,"Center");

JScrollPane jsp2=new JScrollPane(ja1,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
c.add(jsp2);
ja1.setText (str);

new1.addActionListener (this);
open.addActionListener (this);
save.addActionListener (this);
file_new1.addActionListener (this);
file_open.addActionListener (this);
file_save.addActionListener (this);
file_exit.addActionListener (this);
cut.addActionListener (this);
copy.addActionListener (this);
paste.addActionListener (this);
edit_cut.addActionListener (this);
edit_copy.addActionListener (this);
edit_paste.addActionListener (this);
find.addActionListener (this);

jcb1.addActionListener (this);
jcb2.addActionListener (this);

setTitle("Text Editor and Searcher");
setVisible(true);
setSize(640,480);
}

public void actionPerformed(ActionEvent e)
{
 if(e.getSource ()==new1 || e.getSource ()==file_new1)
 {
     ja1.setText ("");
     setTitle("New Document");
 }
 if(e.getSource ()==open || e.getSource ()==file_open)
  open_action();
 if(e.getSource ()==save || e.getSource()==file_save)
  save_action();
 if(e.getSource ()==file_exit)
 {
  int opt=JOptionPane.showConfirmDialog (this,"Do you want to save this document");
  if(opt==JOptionPane.YES_OPTION)
   save_action();
  else if(opt==JOptionPane.NO_OPTION)
   System.exit (0);
 }
 if(e.getSource ()==jcb1 || e.getSource ()==jcb2)
 {
  ja1.setFont(new Font(jcb1.getSelectedItem ().toString(),Font.PLAIN,jcb2.getSelectedIndex ()*2+8));
 }
 if(e.getSource ()==edit_cut || e.getSource ()==cut)
 {
  clipText=ja1.getSelectedText ();
  ja1.replaceRange ("",ja1.getSelectionStart (),ja1.getSelectionEnd ());
 }
 if(e.getSource ()==edit_copy || e.getSource ()==copy)
 {
  clipText=ja1.getSelectedText ();
 }
 if(e.getSource ()==edit_paste || e.getSource ()==paste)
 {
  ja1.replaceRange (clipText,ja1.getSelectionStart (),ja1.getSelectionEnd ());
 }
  if(e.getSource ()==find)
 {
   find_action();
 }

}

public void open_action()
{
     FileDialog fdopen=new FileDialog(this,"Open",FileDialog.LOAD);
     fdopen.setVisible(true);

     String fileName=fdopen.getDirectory ()+fdopen.getFile ();

       if(!(fileName.endsWith (".txt") || fileName.endsWith(".doc")))
        JOptionPane.showMessageDialog(this,
                                    "File Format Mismatch.Open .txt or .doc files only","",
                                    JOptionPane.INFORMATION_MESSAGE);
       else
       {
         ja1.setText ("");
         try
         {
          FileReader fr1=new FileReader(fileName);
          int temp;
          while(true)
          {
          temp=fr1.read ();
          if(temp==-1)
           break;
          else
           ja1.append (""+(char)temp);
          }
          fr1.close ();
          setTitle(fileName);
         }catch(Exception ex){System.out.print(ex.getMessage ());}

       }
 }

 public void find_action()
 {
  String fstring=JOptionPane.showInputDialog ("Enter a string to be searched");
  String tstring=ja1.getText ();
  int offset=0,i,c;
  while(true)
  {
   i=tstring.indexOf (fstring,offset);
   if(i==-1)
   {
     JOptionPane.showMessageDialog (this,"Searching Finished");
     break;
   }
   ja1.setSelectionStart (i);
   ja1.setSelectionEnd (i+ fstring.length ());
   offset=i+fstring.length ()+1;
   c=JOptionPane.showConfirmDialog (this,"Do you want to continue searching for '"+fstring+"' ?","Find",JOptionPane.YES_NO_OPTION);
   if(c==JOptionPane.NO_OPTION || c==JOptionPane.CLOSED_OPTION )
      break;
  }

 }

 public void save_action()
 {
   FileDialog fdsave=new FileDialog(this,"Save ",FileDialog.SAVE);
   fdsave.setVisible(true);
   if(!((fdsave.getFile ()).endsWith (".txt") || (fdsave.getFile ()).endsWith (".doc") ))
   {
     JOptionPane.showMessageDialog(this,
                                    "File should be stored as .txt or .doc only","",

                                    JOptionPane.INFORMATION_MESSAGE);
   }
   else
   {
   try
     {
      FileWriter fw1=new FileWriter(fdsave.getDirectory ()+fdsave.getFile ());
      fw1.write (ja1.getText ());
      fw1.close ();
      setTitle(fdsave.getDirectory ()+fdsave.getFile ());
      }
      catch(Exception ex){System.out.print(ex.getMessage ());}
   }   
 }

 

}
