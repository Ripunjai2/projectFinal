import javax.swing.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

class HelpFrame extends JFrame
{
HelpFrame()
{
 Container c=getContentPane();
 JButton b1=new JButton(new ImageIcon("icons/help_screen.gif"));
 c.add (b1);
 setSize(800,555);
 setTitle("Help");
}
}