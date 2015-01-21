package file.mainFrame;

import java.awt.Point;

import javax.swing.*;

import file.afilesocket.AFileSocket;
import file.mainPanel.MainPanel;

public class MainFrame extends JFrame{
	
	public MainPanel  myFile = new MainPanel();
	public MainFrame()
	{
		
		super("1152703_方志晗_文件系统");
		this.setSize(1000,700);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		//this.setLayout(null);
		Thread t1 =new Thread(myFile);
		//Thread t2 = new Thread(myFile);
		
		this.add(this.myFile);
		
		
	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		t1.start();
		//t2.start();
		this.setVisible(true);
	}
	

}
