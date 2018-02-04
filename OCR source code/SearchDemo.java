package components; 
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;


public class SearchDemo extends JFrame implements DocumentListener
{
  Color  HILIT_COLOR = Color.LIGHT_GRAY;
  Color  ERROR_COLOR = Color.PINK;
  String CANCEL_ACTION = "cancel-search";  

  final Color entryBg;
  Highlighter hilit;
  Highlighter.HighlightPainter painter;  

  JTextField entry;
  JTextArea ja1;

   String fstring=JOptionPane.showInputDialog ("Enter a string to be searched");
   String s=entry.getText();
   int offset=0,i,c;

    public SearchDemo() 
  {
        initComponents();
        hilit = new DefaultHighlighter();
        painter = new DefaultHighlighter.DefaultHighlightPainter(HILIT_COLOR);
        ja1.setHighlighter(hilit);
       entryBg = entry.getBackground();
       entry.getDocument().addDocumentListener(this);

        InputMap im = entry.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = entry.getActionMap();
        im.put(KeyStroke.getKeyStroke("ESCAPE"), CANCEL_ACTION);
    //    am.put(CANCEL_ACTION, new CancelAction());

}

public void initComponents()
{
      entry=new JTextField();
       ja1=new JTextArea();

}  
public void search()
 {
        hilit.removeAllHighlights();
        String s = entry.getText(); 
       if(s.length()<=0)
         {
             JOptionPane.showMessageDialog (this,"Nothing to search");
             //break;
        }
   String tstring=ja1.getText ();
   i=tstring.indexOf (fstring,offset);
   if(i>=0)
   {
        try
       {
         int end = i+ s.length();
         hilit.addHighlight(i, end, painter);
         ja1.setCaretPosition(end);
         entry.setBackground(entryBg);
       // JOptionPane.showMessageDialog ("'" + s + "' found. Press ESC to end search");    
       }
       catch(BadLocationException e) 
        {
               // e.printStackTrace();
         }
    } 
     else 
    {
           entry.setBackground(ERROR_COLOR);
      //    JOptionPane.showMessageDialog("'" + s + "' not found. Press ESC to start a new search");
     }
   //c=JOptionPane.showConfirmDialog (this,"Do you want to continue searching for '"+s+"' ?","Find",JOptionPane.YES_NO_OPTION);
   //if(c==JOptionPane.NO_OPTION || c==JOptionPane.CLOSED_OPTION )
   //break;
 }

    public void insertUpdate(DocumentEvent ev) {
        search();
    }
    
    public void removeUpdate(DocumentEvent ev) {
        search();
    }
    
    public void changedUpdate(DocumentEvent ev) {
    }
}