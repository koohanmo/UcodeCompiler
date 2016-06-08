package compiler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import codeGenerator.CodeGenerator;
import sun.applet.resources.MsgAppletViewer;

public class EditorFrame extends JFrame{
	
	JPanel frame,jpEdit;
	JPanel jpRight,jpLeft,jpTop;
	JTextArea jtCode,jtUcode;
	JButton btnSave,btnCompile;
	
	public EditorFrame(){
		this.setTitle("KAU-PROGRAMING_LANGUANGE_PROJECT_COMPILER");
		
		frame = new JPanel();
		frame.setLayout(new BorderLayout());
		

		setEditFrame();
		setLeft();
		setTop();
		setRight();
		
		setAction();
		
		frame.add(jpEdit,BorderLayout.CENTER);
		frame.setBackground(Color.WHITE);
		frame.setBorder(BorderFactory.createEmptyBorder(10 , 10 , 10 , 10));
		this.add(frame);
			
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		
		this.setVisible(true);
		this.setSize(1024, 768);
	}	
	
	private void setEditFrame(){
		jpEdit = new JPanel();
		jpEdit.setLayout(new GridLayout(1,2));
		jpEdit.setBackground(Color.WHITE);
	}
	
	private void setLeft(){
	
		jpLeft = new JPanel();
		jpLeft.setBackground(Color.WHITE);
		jpLeft.setLayout(new BorderLayout());
		
		
		jtCode = new JTextArea();
		jtCode.setVisible(true);
		jtCode.setBackground(Color.white);
		jtCode.setText("");
		jtCode.setBorder(BorderFactory.createEmptyBorder(10 , 10 , 10 , 10));
		JScrollPane scrollPane = new JScrollPane(jtCode);
		jpLeft.add(scrollPane,BorderLayout.CENTER);
		
		BufferedReader input;
		StringBuilder sb= new StringBuilder("");
		String s=null;
		
		try {
			input = new BufferedReader (new FileReader("test.txt"));
			while((s=input.readLine()) != null){
				sb.append(s+"\n");
			}
			jtCode.setText(sb.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File open Error");
			System.exit(ERROR);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	
		
		
	

		
		TitledBorder tb = new TitledBorder(new LineBorder(Color.BLACK),"CODE");
		jpLeft.setBorder(tb);
		
		jpEdit.add(jpLeft);
	}
	
	private void setTop(){
		
		jpTop = new JPanel();
		jpTop.setBackground(Color.WHITE);
		jpTop.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		

		
		ImageIcon icSave = new ImageIcon("Save as-80.png");
		ImageIcon icCompile = new ImageIcon("Circled Right-96.png");
		
	
		btnSave = new JButton(icSave);
		btnSave.setBorderPainted(false);
		btnSave.setContentAreaFilled(false);
		btnSave.setFocusPainted(false);
		btnCompile = new JButton(icCompile);
		btnCompile.setBorderPainted(false);
		btnCompile.setContentAreaFilled(false);
		btnCompile.setFocusPainted(false);
		
		
		jpTop.add(btnSave);
		jpTop.add(btnCompile);
		
		jpTop.setPreferredSize(new Dimension(500, 100));
		TitledBorder tb = new TitledBorder(new LineBorder(Color.GRAY),"Operation");
		jpTop.setBorder(tb);
		
		jpTop.setVisible(true);
		frame.add(jpTop,BorderLayout.NORTH);
		
	
	}
	
	private void setRight(){
		jpRight = new JPanel();
		jpRight.setBackground(Color.WHITE);
		jpRight.setLayout(new BorderLayout());
		
		jtUcode = new JTextArea();
		jtUcode.setVisible(true);
		jtUcode.setBackground(Color.WHITE);
		jtUcode.setBorder(BorderFactory.createEmptyBorder(10 , 10 , 10 , 10));
		JScrollPane scrollPane = new JScrollPane(jtUcode);
		jpRight.add(scrollPane,BorderLayout.CENTER);
		
		
		TitledBorder tb = new TitledBorder(new LineBorder(Color.BLACK),"UCODE");
		jpRight.setBorder(tb);
		
		jpEdit.add(jpRight);
	}
	
	
	private void setAction(){
		btnCompile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//save & get source
				String s = jtCode.getText();
				try {
					FileWriter writer = new FileWriter(new File("test.txt"));
					writer.write(s);
					writer.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
				//compile
				CodeGenerator codeGen = new CodeGenerator("test.txt", "uFile.uco");
				codeGen.generate();
				
				//get
				BufferedReader input;
				StringBuilder sb= new StringBuilder("");
				String s1=null;
				
				try {
					input = new BufferedReader (new FileReader("uFile.uco"));
					while((s1=input.readLine()) != null){
						sb.append(s1+"\n");
					}
					jtUcode.setText(sb.toString());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					System.out.println("File open Error");
					System.exit(ERROR);
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				
				
				JOptionPane.showMessageDialog(null, "Compile complete!");
				
				
				
			}
		});
		
		
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String s = jtCode.getText();
				try {
					FileWriter writer = new FileWriter(new File("test.txt"));
					writer.write(s);
					writer.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
				JOptionPane.showMessageDialog(null, "Save complete!!");
			}
		});
	}

}
