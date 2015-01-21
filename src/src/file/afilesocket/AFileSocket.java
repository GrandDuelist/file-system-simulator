package file.afilesocket;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import file.CPoint;
import file.filepanel.FilePanel;
import file.mainPanel.MainPanel;
import file.textfile.TextFile;



public class AFileSocket extends JPanel implements MouseListener,ActionListener{
	
	public static final int SocketLength =690;
	public static final int SocketHeight =20;
	
	//public FilePanel myFilePanel = new FilePanel();
	//文件夹内部信息项
	public FilePanel myFilePanel=new FilePanel();
	
	
	//是否删除
	public boolean isDelete =false;
	public int FileNum=0; 
	public int FileVolume =0;
	public String SocketName = "新建文件夹";
	public boolean isSelect = false;
	
	//文件夹图标
	public Icon ImgIcon= new ImageIcon(getClass().getResource("/file/filesocket.png"));
	public Icon zoneIcon = new ImageIcon(getClass().getResource("/file/zone.png"));
	
	
	public  boolean  isZone =false; //是否为分区，如果是，另行处理
	public  boolean  isOpen =false;  //是否打开
	
	//图标
	public JLabel FileSocketLabel = new JLabel(this.ImgIcon);
	//文件名
	public JLabel FileSocketName =new JLabel("新建文件夹");
	//文件数目
	public JLabel FileNumInSocket =new JLabel(""+FileNum);
	//文件大小
	public  JLabel  FileVolumeInSocket = new JLabel(""+this.FileVolume+"B");
	
	public int rootNum =0;
//	 public 	PopupMenu popupMenu1 = new PopupMenu();
//	 
//	 public MenuItem Open = new MenuItem("打开");
//	 public   MenuItem Copy = new MenuItem("复制");
//	 public MenuItem Paste = new MenuItem("粘贴");
//	 public MenuItem Delete = new MenuItem("删除");
//	 public MenuItem Rename = new MenuItem("重命名");


	
	
	public AFileSocket()
	{
		
	
         
		
		this.setLayout(null);
		this.myFilePanel.myFileSocket=this;
		
		this.setFileMessage();
		this.setOpaque(false);
		this.addMouseListener(this);
		
		//this.setPopMenu();
		
		
	}
	
	public AFileSocket(char zone, int i)
	{
		    this.setLayout(null);
			this.rootNum=i;
	
			//this.setFileMessage();
			this.SocketName="分区"+zone;
			this.setZone();
			this.updateFileSocket();
			this.setBackground(Color.WHITE);
			this.addMouseListener(this);
			//this.
		
	}
	
	public void setZone()
	{
		
		this.setLayout(null);
		JLabel icon = new JLabel(zoneIcon);
		icon.setBounds(0, 0, 100,80);
		this.add(icon);
		this.FileSocketName.setBounds(30, 80, 100, 20);
		this.add(this.FileSocketName);
		
		
		
	}
	
	public AFileSocket(String name, int i)
	{
        this.setLayout(null);
		this.rootNum=i;
		this.setFileMessage();
		this.SocketName=name;
		this.updateFileSocket();
		this.setBackground(Color.WHITE);
		this.addMouseListener(this);
		
	}
	
	public void setOneLabel(double weight,JLabel label, GridBagLayout gridbag, GridBagConstraints c)
	{
		c.weightx=weight;
		gridbag.setConstraints(label, c);
        add(label);

	}
	
	public void setFileMessage()
	{
		 
        this.FileNumInSocket.setOpaque(false);
        this.FileSocketName.setOpaque(false);
        this.FileSocketLabel.setOpaque(false);
        this.FileVolumeInSocket.setOpaque(false);
        this.FileNumInSocket.setEnabled(false);
        this.FileVolumeInSocket.setEnabled(false);
       
  
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setFont(new Font("SansSerif", Font.PLAIN, 13));
        setLayout(gridbag);

        c.fill = GridBagConstraints.BOTH;
        this.setOneLabel(0.5, this.FileSocketLabel, gridbag, c);
        this.setOneLabel(4, this.FileSocketName, gridbag, c);
        this.setOneLabel(4, this.FileNumInSocket, gridbag, c);

        
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        gridbag.setConstraints(this.FileVolumeInSocket, c);
        this.add(this.FileVolumeInSocket);
  
        

	}
	//根据上下序号设置初始位置
	public void setFileBountds(Point a)
	{
		this.setBounds(a.x,a.y,SocketLength,SocketHeight);
	}
	
	//根据上下序号设置位置
	public void setFileLocation(int count)
	{
		this.setLocation(CPoint.getFilePoint(count));
	}
	//更新信息
	public void updateFileSocket()
	{
		this.myFilePanel.myFileSocket=this;
		this.FileVolume=0;
		for(int i=0;i<this.myFilePanel.filecount;i++)
		{
			this.FileVolume+=this.myFilePanel.HoldFile[i].FileVolume;
		}
		for(int i=0;i<this.myFilePanel.socketcount;i++)
		{
			this.FileVolume+=this.myFilePanel.HoldSocket[i].FileVolume;
		}
		System.out.println(this.FileNum );
		this.FileNum = this.myFilePanel.allcount;
		//this.FileVolume =this.myFilePanel.volum;
		this.FileSocketName.setText(this.SocketName);
		this.FileNumInSocket.setText(""+this.FileNum);
		this.FileVolumeInSocket.setText(""+this.FileVolume+"B");
		
		
	}
	
	//复制
	public AFileSocket getACopy()
	{

     	AFileSocket temp1 = new AFileSocket(this.SocketName,this.rootNum);
		temp1.FileNum=this.FileNum;
		temp1.FileVolume=this.FileVolume;
		temp1.myFilePanel.Instruction=this.myFilePanel.Instruction;

		for(int i=0;i<this.myFilePanel.socketcount;i++)
		{
		if(temp1.myFilePanel.socketcount<FilePanel.SOCKETMAXNUM)
		{
		AFileSocket temp = this.myFilePanel.HoldSocket[i].getACopy();
		
		//该文件夹下创建文件夹的上一级为该文件夹
		temp.myFilePanel.lastSocket=temp1.myFilePanel;
		temp1.myFilePanel.HoldSocket[temp1.myFilePanel.socketcount++] = temp;
		temp.myFilePanel.setInstruction();
		
		
		temp1.myFilePanel.addNewFileSocket(temp1.myFilePanel.HoldSocket[temp1.myFilePanel.socketcount-1], temp1.myFilePanel.allcount);
		temp1.myFilePanel.Scount++;
		temp1.myFilePanel.allcount++;
		//MainPanel.WholeFileSocketNum.setText("文件夹总数："+(++MainPanel.WholeSocketNumCount));
		if(temp1.myFilePanel.myFileSocket!=null)
			temp1.myFilePanel.myFileSocket.updateFileSocket();
		}
		
		}
		
		for(int i=0;i<this.myFilePanel.filecount;i++)
		{
			if(temp1.myFilePanel.filecount<FilePanel.FILEMAXNUM)
			{
			TextFile temp = this.myFilePanel.HoldFile[i].getACopy();
			
			//该文件夹下创建文件夹的上一级为该文件夹
			  temp.lastSocket=temp1.myFilePanel;
			  temp1.myFilePanel.HoldFile[temp1.myFilePanel.filecount++] = temp;
			//this.HoldSocket[this.socketcount++]  = new AFileSocket("新建文件夹"+(1+this.filecount++));
			
			  temp1.myFilePanel.addNewFile(temp1.myFilePanel.HoldFile[temp1.myFilePanel.filecount-1], temp1.myFilePanel.allcount);
			//System.out.println(212);
			  temp1.myFilePanel.Fcount++;
			  temp1.myFilePanel.allcount++;
			//总文件数增加
			//MainPanel.WholeFileNum.setText("文件总数："+(++MainPanel.WholeFileNumCount));
			if(temp1.myFilePanel.myFileSocket!=null)
				temp1.myFilePanel.myFileSocket.updateFileSocket();
			}
		}
		
		
		return temp1;
	}
	        
//	public void setPopMenu()
//	{
//		this.popupMenu1.add(this.Open);
//		this.popupMenu1.add(this.Rename);
//		this.popupMenu1.add(this.Copy);
//		this.popupMenu1.add(this.Paste);
//		this.popupMenu1.add(this.Delete);
//		
//		this.Open.addActionListener(this);
//		this.Rename.addActionListener(this);
//		this.Copy.addActionListener(this);
//		this.Paste.addActionListener(this);
//		this.Delete.addActionListener(this);
//		
//		this.add(this.popupMenu1);
//	}


	
	
	
  public void Rename(String name)
  {
	  this.SocketName=name;
	  this.FileSocketName.setText(this.SocketName);
	  this.myFilePanel.myFileTree.setUserObject(name);
	  this.myFilePanel.Instruction=this.myFilePanel.lastSocket.Instruction+"\\\\"+SocketName;
	  
	  MainPanel.treeView.updateUI();
  }
	
  
  //粘贴时更新数据-->递归
  public void flushDataWhenPaste(FilePanel root)
  {
  
	  
	  if(root.myFileSocket!=null)
	  {
	  root.myFileSocket.FileNumInSocket.setText(""+root.myFileSocket.FileNum);
	  root.myFileSocket.FileVolumeInSocket.setText(""+root.myFileSocket.FileVolume+"B");
	  

	  
	  
//	  root.myFilePanel.setInstruction();
	  if(root.myFileSocket.myFilePanel.lastSocket!=null)
		  root.Instruction=root.lastSocket.Instruction+"\\\\"+root.myFileSocket.SocketName;
	  for(int i=0;i<root.socketcount;i++)
	  { 
	    
	      flushDataWhenPaste(root.HoldSocket[i].myFilePanel);
	  }
	  }
	  
	 
	  
  }
	
  
	//递归更新文件树
	public void flushFileTree(FilePanel temp)
	{
		if(temp.myFileSocket!=null)
		{
		temp.myFileTree = new DefaultMutableTreeNode(temp.myFileSocket.SocketName);
		//this.myFileTree.add(new DefaultMutableTreeNode(this.HoldSocket[this.socketcount-1].myFilePanel.myFileTree));
		//this.HoldSocket[this.socketcount-1].myFilePanel.myFileTree.isRoot();
	//	if(temp.lastSocket!=null)
		temp.lastSocket.myFileTree.add(temp.myFileTree );
		MainPanel.treeView.repaint();
		//MainPanel.rootTree.updateUI();
		
		for(int j = 0;j<temp.filecount;j++)
		{
			temp.HoldFile[j].myFileTree= new DefaultMutableTreeNode(temp.HoldFile[j].FileName);
		    temp.myFileTree.add(temp.HoldFile[j].myFileTree);
				MainPanel.treeView.repaint();
				MainPanel.rootTree.updateUI();
			
		}
		
		  for(int i=0;i<temp.socketcount;i++)
		  { 
		    
			  flushFileTree(temp.HoldSocket[i].myFilePanel);
		  }
		
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void flushZone()
	{
		MainPanel.ShowFileMessage.append("分区名："+this.SocketName+"\n");
		MainPanel.ShowFileMessage.append("磁盘类型： NTFS"+"\n");
		MainPanel.ShowFileMessage.append("文件个数："+this.FileNum+"\n");
		MainPanel.ShowFileMessage.append("内部文件夹总数："+this.myFilePanel.getWholeSocketNum(this)+"\n");
		MainPanel.ShowFileMessage.append("内部文件总数："+this.myFilePanel.getWholeFileNum(this)+"\n\n");
		MainPanel.ShowFileMessage.append("分区大小："+MainPanel.EVERYZONEVOLUME+"B\n");
		MainPanel.ShowFileMessage.append("已用分区："+this.FileVolume+"B\n");
	
		for(int i=0;i<MainPanel.ZoneLeftVolume.length;i++)
		{
			 MainPanel.ZoneLeftVolume[i]=MainPanel.EVERYZONEVOLUME;
		}
			if(this==MainPanel.root.HoldSocket[0])
				MainPanel.ShowFileMessage.append("剩余分区："+(MainPanel.ZoneLeftVolume[0]-=this.FileVolume)+"B\n");
			if(this==MainPanel.root.HoldSocket[1])
				MainPanel.ShowFileMessage.append("剩余分区："+(MainPanel.ZoneLeftVolume[1]-=this.FileVolume)+"B\n");
			if(this==MainPanel.root.HoldSocket[2])
				MainPanel.ShowFileMessage.append("剩余分区："+(MainPanel.ZoneLeftVolume[2]-=this.FileVolume)+"B\n");
			if(this==MainPanel.root.HoldSocket[3])
				MainPanel.ShowFileMessage.append("剩余分区："+(MainPanel.ZoneLeftVolume[3]-=this.FileVolume)+"B\n");
		
		
	}
	
	public void flushAll()
	{
		MainPanel.ShowFileMessage.setText("");
		if(this.myFilePanel.lastSocket!=MainPanel.root)
		{
		MainPanel.ShowFileMessage.append("文件名："+this.SocketName+"\n");
		MainPanel.ShowFileMessage.append("文件种类： 文件夹"+"\n");
		MainPanel.ShowFileMessage.append("文件大小："+this.FileVolume+"B\n");
		MainPanel.ShowFileMessage.append("文件个数："+this.FileNum+"\n");
		MainPanel.ShowFileMessage.append("内部文件夹总数："+this.myFilePanel.getWholeSocketNum(this)+"\n");
		MainPanel.ShowFileMessage.append("内部文件总数："+this.myFilePanel.getWholeFileNum(this)+"\n");
		}
		
		if(this.myFilePanel.lastSocket==MainPanel.root)
		{
			this.flushZone();
			
		}
	
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

				this.flushAll();
			}
			//两次则进入
			if(!this.isSelect)
			{
				this.setBackground(Color.white);
			    this.isOpen =true;
			
			}
			//其他文件取消选择
			for(int i=0;i<this.myFilePanel.lastSocket.socketcount;i++)
			{
				if(this.myFilePanel.lastSocket.HoldSocket[i]!=this)
				{
				this.myFilePanel.lastSocket.HoldSocket[i].isSelect=false;
				this.myFilePanel.lastSocket.HoldSocket[i].setBackground(Color.white);
				}
			}
			for(int i=0;i<this.myFilePanel.lastSocket.filecount;i++)
			{
				
				this.myFilePanel.lastSocket.HoldFile[i].isSelect=false;
				this.myFilePanel.lastSocket.HoldFile[i].setBackground(Color.white);
				
			}
			
			
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
//		if((e.getModifiers()&InputEvent.BUTTON3_MASK)!=0){
//			//弹出菜单  
//			popupMenu1.show(this.FileSocketLabel,e.getX(),e.getY());
//			//this.myFilePanel.lastSocket.setBackground(Color.GREEN);
//			}
//	

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

	@Override
	public void actionPerformed(ActionEvent e) {

	}

}
