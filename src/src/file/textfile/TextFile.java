package file.textfile;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultMutableTreeNode;

import file.CPoint;
import file.filepanel.FilePanel;
import file.mainPanel.MainPanel;

public class TextFile extends JPanel implements MouseListener{
	
	public static final int FileLength =690;
	public static final int FileHeight =20;
	
	   public FilePanel lastSocket=null;
	   public int FileVolume = 0;
	   public boolean isSelect =false;
	   public boolean isOpen = true;
	   
	   
	   FilePanel temp =null;
		public Icon ImgIcon= new ImageIcon(getClass().getResource("/file/file.png"));
	   public String FileName ;
	   //图标
		public JLabel FileLabel = new JLabel(this.ImgIcon);
		//文件名
		public JLabel FileNameLabel =new JLabel("");
		//文件数目
		public JLabel FileNumLabel =new JLabel(""+1);
		//文件大小
		public  JLabel  FileVolumeLabel = new JLabel(""+this.FileVolume+"B");
	
	    //保存文本信息
		public String content="";
		public boolean isDelete= false;
		public int rootNum =0;
		
		
		//树形图
		public DefaultMutableTreeNode myFileTree = null;
		
	public TextFile(String name,int i)
	{
		this.FileName=name;
		rootNum=i;
		//this.setLayout(null);
		this.setBackground(Color.WHITE);
		this.setFileMessage();
		//this.setOpaque(false);
		this.addMouseListener(this);
	    this.updateFile();
	   
		
	}
	
	public TextFile  getACopy()
	{
		TextFile  temp = new TextFile(this.FileName,this.rootNum);
		temp.content=this.content;
		temp.FileVolume=this.FileVolume;
		temp.updateFile();
		return temp;
	}
	
	public void setOneLabel(double weight,JLabel label, GridBagLayout gridbag, GridBagConstraints c)
	{
		c.weightx=weight;
		gridbag.setConstraints(label, c);
        add(label);

	}
	
	public void setFileMessage()
	{
		 
        this.FileLabel.setOpaque(false);
        this.FileNameLabel.setOpaque(false);
        this.FileNumLabel.setOpaque(false);
        this.FileVolumeLabel.setOpaque(false);
        this.FileNumLabel.setEnabled(false);
        this.FileVolumeLabel.setEnabled(false);
       
  
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setFont(new Font("SansSerif", Font.PLAIN, 13));
        setLayout(gridbag);

        c.fill = GridBagConstraints.BOTH;
        this.setOneLabel(0.5, this.FileLabel, gridbag, c);
        this.setOneLabel(4, this.FileNameLabel, gridbag, c);
        this.setOneLabel(4, this.FileNumLabel, gridbag, c);

        
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        gridbag.setConstraints(this.FileVolumeLabel, c);
        this.add(this.FileVolumeLabel);
  
        

	}
	//根据上下序号设置初始位置
	public void setFileBountds(Point a)
	{
		this.setBounds(a.x,a.y,FileLength,FileHeight);
	}
	
	//根据上下序号设置位置
		public void setFileLocation(int count)
		{
			this.setLocation(CPoint.getFilePoint(count));
		}
		
		//更新信息
		public void updateFile()
		{
			
			//this.FileVolumeLabel =this.FileVolume;
			this.FileNameLabel.setText(this.FileName);
			//this.FileNumLabel.setText(""+this.filen);
			this.FileVolumeLabel.setText(""+this.FileVolume+"B");
		}
	public void setTextFile()
	{  

	}

	@Override

		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stu
			if(e.getButton()==e.BUTTON1)
			{
				//设置选择的暗影
				this.isSelect=!this.isSelect;
				
				if(this.isSelect)
				{
					this.setBackground(Color.green);

					MainPanel.ShowFileMessage.setText("");
					MainPanel.ShowFileMessage.append("文件名："+this.FileName+"\n");
					MainPanel.ShowFileMessage.append("文件种类： 文本文件"+"\n");
					MainPanel.ShowFileMessage.append("文件大小："+this.FileVolume+"B\n");
					MainPanel.ShowFileMessage.append("文件个数："+1+"\n");
				}
				
				//两次则进入
				if(!this.isSelect)
				{
					this.setBackground(Color.white);
				    this.isOpen =true;
				    new myFrame();
				
				}
				//其他文件取消选择
				for(int i=0;i<this.lastSocket.filecount;i++)
				{
					if(this.lastSocket.HoldFile[i]!=this)
					{
						this.lastSocket.HoldFile[i].isSelect=false;
					    this.lastSocket.HoldFile[i].setBackground(Color.white);
					}
					
				}
				
				
				//文件夹取消选择
				for(int i=0;i<this.lastSocket.socketcount;i++)
				{
					this.lastSocket.HoldSocket[i].isSelect=false;
					this.lastSocket.HoldSocket[i].setBackground(Color.white);
				}
				
				
			}
			
			if(e.getSource()==this.myFileTree)
			{
				if(e.getClickCount()==2)
				{
					this.isOpen=true;
				}
			}

		}
	
	//文本框
	class myFrame extends JFrame{
		
		public JMenuBar TextControl= new JMenuBar();
		public JMenu file = new JMenu("文件");
		public JMenuItem save =  new JMenuItem("保存");
		public JMenuItem saveTo= new JMenuItem("保存到磁盘");
		public String temp =""; ;
		
		public JTextArea TextMessage = new JTextArea (40,50);
		
		public myFrame()
		{
			super(FileName);
			this.setSize(600,500);
			this.setResizable(false);
			this.setLocationRelativeTo(null);
			this.add(this.TextMessage);
			this.setMyMenuBar();
		    this.TextMessage.setText(content);
			//this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setVisible(true);
			
		}
		
		public void setMyMenuBar()
		{
			
			this.TextControl.add(this.file);
			this.file.add(this.save);
			this.file.add(this.saveTo);
			
			
			this.saveTo.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					
					
				this.WriteToFile(TextMessage.getText());
					
				
				JOptionPane.showMessageDialog(null, "" +
						"文件成功保存到"+"D:\\"+FileName+".txt", "保存成功", JOptionPane.INFORMATION_MESSAGE);
					 
					
				}
				
				 void WriteToFile(String str)
				 {
				 	// 1、表示要操作record.txt文件
				 	File f =  new File("D:\\"+FileName+".txt");
				 	OutputStream out = null ;
				 	// 2、通过子类实例化
				 	// 使用FileOutputStream子类
				 	try
				 	{
				 		out = new FileOutputStream(f,true) ;
				 	}
				 	catch (Exception e)
				 	{
				 	}
				 	  OutputStreamWriter osw = new OutputStreamWriter(out);
				 	  BufferedWriter bw = new BufferedWriter(osw);
				 	
				 	 
				 	 try {
				 		bw.write(str);
				 	} catch (IOException e2) {
				 		// TODO Auto-generated catch block
				 		e2.printStackTrace();
				 	}
				 	 try {
				 			bw.flush();
				 		} catch (IOException e) {
				 			// TODO Auto-generated catch block
				 			e.printStackTrace();
				 		}
				 		
				 		
				 		  try {
				 			bw.newLine();
				 		} catch (IOException e1) {
				 			// TODO Auto-generated catch block
				 			e1.printStackTrace();
				 		}     
				 		  try {
				 			bw.close();
				 		} catch (IOException e1) {
				 			// TODO Auto-generated catch block
				 			e1.printStackTrace();
				 		}    
				 		  try {
				 			osw.close();
				 		} catch (IOException e1) {
				 			// TODO Auto-generated catch block
				 			e1.printStackTrace();
				 		}    
				 		  try {
				 			out.close();
				 		} catch (IOException e1) {
				 			// TODO Auto-generated catch block
				 			e1.printStackTrace();
				 		}
				 }
				
			
			
			
			}
			);
			
			
			
			
			
			this.save.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					
					// TODO Auto-generated method stub
					MainPanel.WholeUsedVolumeCount-=FileVolume;
					MainPanel.WholeUnUsedVolumeCount+=FileVolume;
					MainPanel.ZoneLeftVolume[rootNum]+=FileVolume;
					temp=content;
					content = TextMessage.getText();
					FileVolume = content.length();
					if(MainPanel.ZoneLeftVolume[rootNum]-FileVolume<0)
					{
						content =temp;
						FileVolume=content.length();
						MainPanel.ZoneLeftVolume[rootNum]-=FileVolume;
						JOptionPane.showMessageDialog(null, "保存失败，该分区内存已满", "内存溢出", JOptionPane.ERROR_MESSAGE);
						
						
						
					}
					MainPanel.ZoneLeftVolume[rootNum]-=FileVolume;
					MainPanel.WholeUsedVolume.setText("已使用内存："+(MainPanel.WholeUsedVolumeCount+=FileVolume)+"B");
					MainPanel.UnUsedVolume.setText("剩余内存："+(MainPanel.WholeUnUsedVolumeCount-=FileVolume)+"B");
					updateFile();
					 updateAllFile();
				}}
			);
			
				
			
			this.setJMenuBar(this.TextControl);
			
  
		}
	
		
		
	}
	
	public void Rename(String name)
	  {
		  this.FileName=name;
		  this.myFileTree.setUserObject(name);
		  this.FileNameLabel.setText(this.FileName);
		  MainPanel.treeView.updateUI();
	  }
		
	
	//存储改变时，更新所有文件夹的存储信息
	public void updateAllFile()
	{
	   temp= this.lastSocket;
	   if(temp.myFileSocket==null)System.out.println("null");
		while(temp.myFileSocket!=null)
		{
			temp.myFileSocket.updateFileSocket();
			temp=temp.lastSocket;
		}
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public int findZone()
	{
		FilePanel temp=this.lastSocket;
		while(temp.lastSocket!=MainPanel.root)
		{
			temp=temp.lastSocket;
			
		}
		for(int i=0;i<MainPanel.root.socketcount;i++)
		{
			if(temp==MainPanel.root.HoldSocket[i].myFilePanel) return i;
		}
		return 0;
	}
}
