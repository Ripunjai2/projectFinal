import javax.swing.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.lang.String.*;


public class MainScreen extends JFrame implements ActionListener
{
//JLabel l4=new JLabel("BATCH NO.1");
//JLabel l=new JLabel("OPTICAL CHARACTER RECOGNITION");

ImageIcon icon = new ImageIcon("icons/header.gif");
JLabel label1 = new JLabel(icon, JLabel.LEFT);

ImageIcon icon2= new ImageIcon("icons/ocr.png");
//Image i=getImage("icons/ocr.png");
JLabel l2 = new JLabel(icon2, JLabel.LEFT);

ImageIcon icon3 = new ImageIcon("icons/ocr.png");
JLabel l3 = new JLabel(icon3, JLabel.RIGHT);
//JButton header1=new JButton(new ImageIcon("icons/header.gif"));
JButton hand1=new JButton(new ImageIcon("icons/hand_selected.gif"));
JButton edit1=new JButton(new ImageIcon("icons/editor.gif"));
JButton print1=new JButton(new ImageIcon("icons/printed_selected.gif"));
JButton help1=new JButton(new ImageIcon("icons/help_selected.gif"));
JButton exit1=new JButton(new ImageIcon("icons/exit.png"));

MainScreen()
{
super("MainScreen");
Container c=getContentPane();
c.setLayout(null);
c.setBackground(Color.white);
c.add(label1);
label1.setBounds(200,0,1300,171);
label1.setVisible(true);
c.add(l2);
l2.setBounds(0,0,230,146);
c.add(l3);
l3.setBounds(1160,0,230,146);
//c.add(l4);
//l4.setBounds(600,500,290,75);
//Font f=new Font("arial",Font.BOLD,22);
//l.setFont(f);
//l.setForeground(Color.blue);
c.add(hand1);
hand1.setBounds(200,250,290,75);
hand1.addActionListener(this);
c.add(edit1);
edit1.setBounds(915,250,290,75);
edit1.addActionListener(this);
c.add(print1);
print1.setBounds(200,400,290,75);
print1.addActionListener(this);
c.add(help1);
help1.setBounds(915,400,290,75);
help1.addActionListener(this);
c.add(exit1);
exit1.setBounds(680,580,89,69);
exit1.addActionListener(this);
GraphicsEnvironment ge;
ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
setSize(640,480);
show();
addWindowListener(new WindowAdapter()
		{
		public void windowClosing(WindowEvent we)
		{
		System.exit(0);
		}
		});
}

public void actionPerformed(ActionEvent e)
{
	if(e.getSource()==hand1)
	{
	HandWrittenFrame h=new HandWrittenFrame();
	h.show();
	}
	if(e.getSource()==edit1)
	{
	new Editor("Editor Window");
	}
                   if(e.getSource()==print1)
	{
	PrintedFrame p=new PrintedFrame();
	p.show();
	}
	if(e.getSource()==help1)
	{
	new HelpFrame().show();
	}
                   if(e.getSource()==exit1)
	{
	ExitFrame ex=new ExitFrame();
	ex.show();
	}
} 
public static void main(String args[])
	{
	new MainScreen();
	}
}
