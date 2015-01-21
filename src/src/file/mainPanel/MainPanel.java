package file.mainPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import file.afilesocket.AFileSocket;
import file.filepanel.FilePanel;
import file.filetree.FileTree;
import file.textfile.TextFile;
import file.zonepanel.ZonePanel;

public class MainPanel extends JPanel  implements MouseListener,ActionListener,Runnable{
	
	public static final int WHOLEVOLUME = 6400;
	public static final int EVERYZONEVOLUME = 6400/4;
	
	
	//记录整个系统的使用情况
	
	public static int WholeUsedVolumeCount= 0;
	public static int WholeUnUsedVolumeCount = WHOLEVOLUME;
	public static int WholeFileNumCount = 0;
	public static int WholeSocketNumCount = 0;

	public static int ZoneLeftVolume[] = {EVERYZONEVOLUME,EVERYZONEVOLUME,EVERYZONEVOLUME,EVERYZONEVOLUME} ;
	
	
	
	public String SortString = null;
	public JPanel LeftPanel = new JPanel(); //左边的信息描述panel
	public JPanel LeftHintPanel =new JPanel();
	public static JTextArea ShowFileMessage =new JTextArea();
	//public FileTree SortPanel = null;

	
	//设置一个搜索导引框
	public JTextField Introduction = new JTextField("\\\\root",300);
	public Icon ImgIcon= new ImageIcon(getClass().getResource("/file/filesocket.png"));
	public Icon MoveIcon= new ImageIcon(getClass().getResource("/file/moveto.png"));
	public JButton Sort = new JButton(MoveIcon);
	
	//中心区域
	public JPanel CenterPanel = new JPanel();
	public JButton VolumeButton  = new JButton("大小 ");
	public JButton FileNumButtton = new JButton("文件数");
	public JButton FileNameButton = new JButton("文件名");
	public JPanel ButtonsPanel = new JPanel();
	public static JScrollPane treeView = null;
	public static JTree rootTree = null;
	
	//是否创建文件夹和文件
	public boolean isCreate =false;
	public boolean isCreateFile =false;
	public boolean isDelete = false;
	public static FilePanel root =null;
	public boolean isSort= false;
	public boolean  isRename=false;
	public boolean  isCopy =false;
	public boolean  isPaste = false;
	public boolean isFormate =false;
	
	//控制栏
	public JButton Create = new  JButton("新建文件夹");
	public JButton CreateFile = new JButton("新文件");
	public JButton Delete  = new JButton("删除");
	public JButton Copy  = new JButton ("复制");
	public  JButton Paste = new JButton("粘贴");
	public JButton  Rename = new JButton("重命名");	
	public JButton Formate= new JButton("格式化");
	
	//复制文件的缓冲量
	public static AFileSocket CopySocketBuffer = null;
	public static TextFile   CopyFileButter = null;
	
	//文件中心区域
	//public FilePanel myFilePanel = new FilePanel();
	public FilePanel myFilePanel= null;
	public FilePanel newPanel = null;
    
	//显示内存使用情况
	public static JLabel  WholeFileNum = new JLabel("文件总数：0"); 
	public static JLabel  WholeFileSocketNum = new JLabel("文件夹总数：0 ");
	public static JLabel  WholeUsedVolume = new JLabel ("已使用内存：0");
	public static JLabel  UnUsedVolume = new JLabel("剩余内存： "+WholeUnUsedVolumeCount+"B");
	public static JLabel  WholeVolume = new JLabel("总内存："+WHOLEVOLUME+"B");
	
	//前进后退按钮
	
	public Icon backward= new ImageIcon(getClass().getResource("/file/backward.png"));
	public Icon foreward= new ImageIcon(getClass().getResource("/file/foreward.png"));
	public JButton BackControl = new JButton(this.backward);
	public JButton ForeControl = new JButton(this.foreward);
//	final Image img=Toolkit.getDefaultToolkit().createImage("back.png");
	
//	public AFileSocket  ZoneA = new AFileSocket ('A');
//	public AFileSocket  ZoneB = new AFileSocket ('B');
//	public AFileSocket  ZoneC = new AFileSocket ('C');
//	public AFileSocket  ZoneD = new AFileSocket ('E');
	
	
	
	public MainPanel()
	{
		
		myFilePanel = new FilePanel();
		root = myFilePanel;
		this.setLayout(null);
		//this.setFileTree();
		this.setLeftPanel();
		//this.myFilePanel.setFileTree();
		this.setCenterPanel();
		this.setControlPanel();
		this.setUpControlPanel();
		this.setBackground(Color.white);
		
		
		
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			
			this.listenToCreate();
			this.listenToOpen();
			this.listenToCreateFile();
			this.listenToDelete();
			this.listenTOsort();
			this.listenToRename();
			this.listenToCopy();
			this.listenToPaste();
			this.listenToFormate();
			
//			this.updateFileMessage();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//this.CenterPanel.remove(this.myFilePanel);
			//this.CenterPanel.repaint();
			
		}
		
	}
	//是否打开文件
	public void listenToOpen ()
	{
		for(int i=0;i<this.myFilePanel.socketcount;i++)
		{
			if(this.myFilePanel.HoldSocket[i].isOpen)
			{
				this.myFilePanel.HoldSocket[i].isOpen=false;
				this.CenterPanel.remove(this.myFilePanel);
				//this.myFilePanel=null;
				this.myFilePanel=this.myFilePanel.HoldSocket[i].myFilePanel;
				this.CenterPanel.add(this.myFilePanel,BorderLayout.CENTER);
				this.CenterPanel.repaint();
				this.myFilePanel.repaint();
				
				
				WholeVolume.setText("总内存："+(WHOLEVOLUME-1)+"B");
				WholeVolume.setText("总内存："+WHOLEVOLUME+"B");
				
			
			}
		}
	}
	
	public void listenToFormate()
	{
		
		if(isFormate)
		{
		
			this.Formate();
			
		}
		isFormate=false;
	}
	
  public void Formate()
  {
	 
	  for(int i=0;i<root.socketcount;i++)
	  {
		  if(root.HoldSocket[i].isSelect)
		  {
			MainPanel.WholeFileNum.setText("文件总数："+(MainPanel.WholeFileNumCount=MainPanel.WholeFileNumCount-root.getWholeFileNum(root.HoldSocket[i])));
			  MainPanel.WholeFileSocketNum.setText("文件夹总数："+(MainPanel.WholeSocketNumCount=MainPanel.WholeSocketNumCount-root.getWholeSocketNum(root.HoldSocket[i])));
			 MainPanel.UnUsedVolume.setText("剩余内存："+(MainPanel.WholeUnUsedVolumeCount+=root.HoldSocket[i].FileVolume)+"B");
			 MainPanel.WholeUsedVolume.setText("已使用内存："+(MainPanel.WholeUsedVolumeCount-=root.HoldSocket[i].FileVolume)+"B");
			  root.HoldSocket[i].myFilePanel=new FilePanel();
		      root.HoldSocket[i].myFilePanel.lastSocket=root;
		      root.HoldSocket[i].myFilePanel.Instruction=    root.HoldSocket[i].myFilePanel.lastSocket.Instruction+"\\\\"+  root.HoldSocket[i].SocketName;
		      root.HoldSocket[i].updateFileSocket();
		      root.HoldSocket[i].flushAll();
		      
		  }   
		      
		  }
	  
	



	  
  }
	
	
	
	//是否查找转到
	public void listenTOsort()
	{
		if(isSort)
		{
			this.SortFileSocket(root);
			if(this.newPanel!=null)
			{
				this.changeFilePanel(this.newPanel);
			 Introduction.setText(this.myFilePanel.Instruction);
			 this.newPanel=null;
			}
		}
		isSort= false;
	}
	//添加一个新文件夹
    synchronized public void listenToCreate()
    {
    	if(isCreate)
		{
		   this.myFilePanel.addAFileSocket();
		  
		  
		}
    	
    	 isCreate=false;
    
    }
    
    public void listenToRename()
    {
    	if(isRename)
    	{
    		this.Rename();
    	}
    	isRename=false;
    }
    
    //删除
    public void listenToDelete()
    {
    	if(this.isDelete)
    	{
    		this.delete();
    	}
    	this.isDelete=false;
    }
    
    //删除文件或者文件夹
    public void delete()
    {
    	
    		for(int i = 0;i<this.myFilePanel.filecount;i++)
    		{
    			if(this.myFilePanel.HoldFile[i].isSelect)
    			{
    				this.myFilePanel.HoldFile[i].isDelete=true;
    				this.myFilePanel.deleteFile();
    				
    			}
    			
    		}
    		for(int i = 0;i<this.myFilePanel.socketcount;i++)
    		{
    			if(this.myFilePanel.HoldSocket[i].isSelect)
    			{
    				this.myFilePanel.HoldSocket[i].isDelete=true;
    				this.myFilePanel.deleteSocket();
    			}
    			
    			
    			
		}
    		this.myFilePanel.repaint();
    	
    		
    }
  
  
    //添加一个新文件
    synchronized public void listenToCreateFile()
    {
    	if(isCreateFile)
		{
		   this.myFilePanel.addAFile();
		  
		  
		}
    	
    	 isCreateFile=false;
    
    }
    //copy按钮实现
    
    public void Copy()
    {

		for(int i = 0;i<this.myFilePanel.filecount;i++)
		{
			if(this.myFilePanel.HoldFile[i].isSelect)
			{
				CopySocketBuffer=null;
				CopyFileButter=this.myFilePanel.HoldFile[i].getACopy();
				
			}
			
		}
		for(int i = 0;i<this.myFilePanel.socketcount;i++)
		{
			if(this.myFilePanel.HoldSocket[i].isSelect)
			{
				CopyFileButter=null;
				CopySocketBuffer=this.myFilePanel.HoldSocket[i].getACopy();
			}
		}
    }
    
    
    public void listenToCopy()
    {
    	if(isCopy)
    	{
    		this.Copy();
    	}
    	isCopy=false;
    }
    
    //粘贴的实现
    public void Paste()
    {
    	if(MainPanel.CopyFileButter!=null)
    	{
    	  this.myFilePanel.pasteAFile();

    	}
    	if(MainPanel.CopySocketBuffer!=null)
    	{
    		this.myFilePanel.pasteAFileSocket();
    
    	}
    	
    }
    
    public void listenToPaste()
    {
    	if(isPaste)
    	{
    		this.Paste();
    	}
    	isPaste=false;
    	
    }
    
    
    //设置一个最上边的控制栏,包括前进和后退和搜索栏
    public void setUpControlPanel()
    {
    	
    
    	JPanel UpControl = new JPanel();
    	 Border b = BorderFactory.createEtchedBorder(); //边框
 		UpControl.setBorder(b);
 		UpControl.setBackground(Color.pink);
 		UpControl.setBounds(0,0,1000,50);
 		this.add(UpControl);
 		UpControl.setLayout(null);
 		BackControl.setBounds(10,0,40,40);
 		ForeControl.setBounds(60,0,40,40);
 		Introduction.setBounds(200, 10, 600, 20);
 		Introduction.setBorder(b);
 		this.Sort.setBounds(820,5,30,30);
 		Introduction.setBackground(Color.WHITE);
 		this.BackControl.setBackground(Color.pink);
 		this.ForeControl.setBackground(Color.pink);
 		this.Sort.setBackground(Color.pink);
 		UpControl.add(BackControl);
 		UpControl.add(ForeControl);
 		UpControl.add(Sort);
 		JLabel socket = new JLabel(ImgIcon);
 		socket.setBounds(170, 10, 20, 20);
 		UpControl.add(socket);
 		UpControl.add(Introduction);
 		
 	    this.BackControl.addActionListener(this);
 	    this.ForeControl.addActionListener(this);
		this.Sort.addActionListener(this);
 	    
 			
 		
 		
 		
 		
 		
    }

	//设置控制栏
	public void setControlPanel()
	{
		 Border b = BorderFactory.createEtchedBorder(); //边框
		 
		JPanel control = new JPanel();
		control.setBorder(b);
		control.setLayout(null);
		control.setBackground(Color.white);
		control.setBounds(0, 43, 1000, 25);
		
		this.Create.setBounds(0,0,100,24);
		this.CreateFile.setBounds(150, 0, 100, 24);
		this.Delete.setBounds(300,0,100,24);
		this.Rename.setBounds(450,0, 100,24);
		this.Copy.setBounds(600,0, 100,24);
		this.Paste.setBounds(750,0, 100,24);
		this.Formate.setBounds(900,0,100,24);
		
		this.Formate.setBackground(Color.yellow);
		this.CreateFile.setBackground(Color.yellow);
	    this.Create.setBackground(Color.yellow);
	    this.Delete.setBackground(Color.YELLOW);
	    this.Rename.setBackground(Color.YELLOW);
	    this.Copy.setBackground(Color.YELLOW);
	    this.Paste.setBackground(Color.YELLOW);
	    
	    //this.Create.addActionListener(this);
	    this.Create.addActionListener(this);
	    this.CreateFile.addActionListener(this);
	    this.Delete.addActionListener(this);
	    this.Rename.addActionListener(this);
	    this.Copy.addActionListener(this);
	    this.Paste.addActionListener(this);
	    this.Formate.addActionListener(this);
	    
		control.add(this.Create);
		control.add(this.CreateFile);
		control.add(this.Delete);
		control.add(this.Rename);
		control.add(this.Copy);
		control.add(this.Paste);
		control.add(this.Formate);
		
		this.add(control);
	}
	
	 
	 public void setLeftPanel()
	 {
		 JPanel sortAllPanel= new JPanel();
		 JPanel leftAllPanel= new JPanel();
		 leftAllPanel.setLayout(new GridLayout(2,1));
		 
		 JLabel hint =new JLabel("文件信息");
		 JLabel hintFile =new JLabel("文件夹");
		 
		// leftAllPanel.setBackground(Color.GREEN);   
		 leftAllPanel.setBounds(1, 73, 300, 600);
		 this.add( leftAllPanel);
		 sortAllPanel.setBackground(Color.GREEN);
		 this.LeftHintPanel.setBackground(Color.GREEN);
		
		 this.LeftHintPanel.setLayout(new BorderLayout());
		 this.LeftHintPanel.add(hint,BorderLayout.NORTH);
		
		 
		 
			
					
		root.myFileTree = new DefaultMutableTreeNode("root");
	    rootTree = new JTree(root.myFileTree);
	    treeView = new JScrollPane(rootTree);
	    rootTree.setEditable(false);
	    
	    
	    rootTree.addTreeSelectionListener(
	    		   new TreeSelectionListener()
	    		   {
	    		      public void valueChanged(TreeSelectionEvent e){
	    		    	  
	    		    	  
	    		    	  
	    		    	  DefaultMutableTreeNode myFileTree = (DefaultMutableTreeNode)rootTree.getSelectionPath().getLastPathComponent();
	    		    	  if(myFileTree==root.myFileTree) changeFilePanel(root);
	    		    	  else{
	    		    	  FilePanel temp =getTreeNode(myFileTree,root);
	    		    	  changeFilePanel(temp);
	    		    	  }
	    		    		

	    		      }
	    		   }
	    		  );//增加选中节点的监听器
					    
					    
			
		
		// this.LeftPanel.setBackground(Color.red);
		 this.LeftHintPanel.add(this.LeftPanel,BorderLayout.CENTER);
		 
		 sortAllPanel.setLayout(new BorderLayout());
		 sortAllPanel.add(this.treeView,BorderLayout.CENTER);
		 sortAllPanel.add(hintFile,BorderLayout.NORTH);
		 
		 leftAllPanel.add(sortAllPanel);
		 leftAllPanel.add(this.LeftHintPanel);
		 
		 Border b = BorderFactory.createEtchedBorder(); //边框
		this.setBorder(b); //为这个显示区加上边框
		hint.setBorder(b);
		this.LeftPanel.setBorder(b);
		
		sortAllPanel.setBorder(b);
		hintFile.setBorder(b);
		
		
		
		this.LeftPanel.setBackground(Color.WHITE);
		this.LeftPanel.setLayout(new BorderLayout());
	     ShowFileMessage.setEnabled(false);
	     ShowFileMessage.setForeground(Color.black);
		ShowFileMessage.setBackground(Color.RED);
		//this.ShowFileMessage.setBounds(0, 0, 298,300);
		this.LeftPanel.add(ShowFileMessage);
		
	 }
	 
	 
  //通过跟节点找到文件树的节点
	 public FilePanel getTreeNode(DefaultMutableTreeNode myFileTree,FilePanel subroot)
	 {
		 if(subroot!=null)
		 {
			 for(int j=0;j<subroot.socketcount;j++)
			 {
				 if(subroot.HoldSocket[j].myFilePanel.myFileTree==myFileTree)
				 {
					 
					 subroot.HoldSocket[j].setBackground(Color.GREEN);
					 subroot.HoldSocket[j].isSelect=true;
						//其他文件取消选择
						for(int i=0;i<subroot.socketcount;i++)
						{
							if(i!=j)
							{
								subroot.HoldSocket[i].isSelect=false;
								subroot.HoldSocket[i].setBackground(Color.white);
							}
							
						}
						
						
						//文件夹取消选择
						for(int i=0;i<subroot.filecount;i++)
						{
							subroot.HoldFile[i].isSelect=false;
							subroot.HoldFile[i].setBackground(Color.white);
						}
						
					 return subroot.HoldSocket[j].myFilePanel.lastSocket;
				 }
				
				 
				 
				 
			 }
			 
			 if(subroot!=this.root)
			 {
			 for(int  j = 0;j<subroot.filecount;j++)
			 {
				 if(subroot.HoldFile[j].myFileTree==myFileTree)
				 {
					 subroot.HoldFile[j].setBackground(Color.GREEN);
					 subroot.HoldFile[j].isSelect=true;
						//其他文件取消选择
						for(int i=0;i<subroot.filecount;i++)
						{
							if(i!=j)
							{
								subroot.HoldFile[i].isSelect=false;
								subroot.HoldFile[i].setBackground(Color.white);
							}
							
						}
						
						
						//文件夹取消选择
						for(int i=0;i<subroot.socketcount;i++)
						{
							subroot.HoldSocket[i].isSelect=false;
							subroot.HoldSocket[i].setBackground(Color.white);
						}
						
					 return subroot.HoldFile[j].lastSocket;
				 }
				 
			 }
			 }
			 for(int i=0;i<subroot.socketcount;i++)
			 {
				
				 if(getTreeNode(myFileTree,subroot.HoldSocket[i].myFilePanel)!=null)
					 return getTreeNode(myFileTree,subroot.HoldSocket[i].myFilePanel);
				
				 
			 }
			 return null;
		 }
		 return null;
	 }
	 
	


	//设置文件显示区域的panel
	 
	 public void  setCenterPanel()
	 {
		   JPanel RootMessage  = new JPanel();
		   RootMessage.setLayout(new GridLayout(1,5));
		   RootMessage.setBackground(Color.yellow);
		   RootMessage.setFont(new Font("Serif",Font.ROMAN_BASELINE,18));
		   RootMessage.add(WholeFileNum);
		   RootMessage.add(WholeFileSocketNum);
		   RootMessage.add(WholeUsedVolume);
		   RootMessage.add(UnUsedVolume);
		   RootMessage.add(WholeVolume);
			  
			  
			  
			  
		  
	        GridBagLayout gridbag = new GridBagLayout();
	        GridBagConstraints c = new GridBagConstraints();
	        this.ButtonsPanel.setFont(new Font("SansSerif", Font.PLAIN, 13));
	        this.ButtonsPanel.setLayout(gridbag);
	        
	        

	       // GridBagLayout gridbag = new GridBagLayout();
	        //GridBagConstraints c = new GridBagConstraints();
	      
	        c.fill = GridBagConstraints.BOTH;
	        this.setOneLabel(0.5, new JButton(""), gridbag, c);
	        this.setOneLabel(4, this.FileNameButton, gridbag, c);
	        this.setOneLabel(4, this.FileNumButtton, gridbag, c);

	        
	        c.gridwidth = GridBagConstraints.REMAINDER; //end row
	        gridbag.setConstraints(this.VolumeButton, c);
	        this.ButtonsPanel.add(this.VolumeButton);
	        this.VolumeButton.setBackground(Color.lightGray);
	        
	        this.CenterPanel.setLayout(new BorderLayout());
	        this.CenterPanel.add(this.ButtonsPanel,BorderLayout.NORTH);
	        this.CenterPanel.add(this.myFilePanel,BorderLayout.CENTER);
	        this.CenterPanel.setBackground(Color.white);
	        this.CenterPanel.add(RootMessage,BorderLayout.SOUTH);
	        
	       this.CenterPanel.setBounds(302,72,690,600);
	        this.add(this.CenterPanel);

	        
	        root.HoldSocket[0]=new AFileSocket('A',0);
	        root.HoldSocket[1]=new AFileSocket('B',1);
	        root.HoldSocket[2]=new AFileSocket('C',2);
	        root.HoldSocket[3]=new AFileSocket('D',3);
	        root.HoldSocket[0]. setBounds(75,200, 100, 100);
	        root.HoldSocket[1]. setBounds(225,200, 100, 100);
	        root.HoldSocket[2]. setBounds(375,200, 100, 100);
	        root.HoldSocket[3]. setBounds(525,200, 100, 100);
	        root.socketcount=4;
	        root.allcount=4;
	      
	        for(int i=0;i<4;i++)
	        {
	        	root.HoldSocket[i].myFilePanel.lastSocket=root;
	        	root.add(root.HoldSocket[i]);
	        	
	        	root.HoldSocket[i].myFilePanel.Instruction=root.HoldSocket[i].myFilePanel.lastSocket.Instruction+"\\\\"+root.HoldSocket[i].SocketName;
	        	root.HoldSocket[i].myFilePanel.myFileTree = new DefaultMutableTreeNode ("分区"+(char)('A'+i));
	        	root.myFileTree.add(root.HoldSocket[i].myFilePanel.myFileTree);
	        }
	        
	        MainPanel.rootTree.updateUI();
	       
	 }
	 
	 
	 //设置一个button
	 public void setOneLabel(double weight,JButton button, GridBagLayout gridbag, GridBagConstraints c)
		{
			c.weightx=weight;
			gridbag.setConstraints(button, c);
			button.setBackground(Color.lightGray);
	        this.ButtonsPanel.add(button);

		}
		
//进入文件夹 
	 public void changeFilePanel(FilePanel NewPanel)
	   {
		    this.CenterPanel.remove(this.myFilePanel);
		    this.CenterPanel.repaint();
			//this.myFilePanel=null;
			this.myFilePanel=NewPanel;
			this.CenterPanel.add(this.myFilePanel,BorderLayout.CENTER);
			this.myFilePanel.repaint();
		
			Introduction.setText(this.myFilePanel.Instruction);
			
	   }
	 
	   //重命名函数
	   public void Rename()
	   {
		   
		   for(int i = 0;i<this.myFilePanel.filecount;i++)
   		{
   			if(this.myFilePanel.HoldFile[i].isSelect)
   			{
   			    String inputValue = JOptionPane.showInputDialog("请输入文件名：");
   			 if(this.myFilePanel.hasSameNameFile(inputValue,i))
				{
					JOptionPane.showMessageDialog(null, "命名重复：已存在\""+inputValue+"\"的文件", "重复命名", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
   			 if(inputValue!=null&&!inputValue.equals(""))
   			    {
   				this.myFilePanel.HoldFile[i].Rename(inputValue);
   			    }
   			}
   			}
   			
   		}
   		for(int i = 0;i<this.myFilePanel.socketcount;i++)
   		{
   			if(this.myFilePanel.HoldSocket[i].isSelect)
   			{
   				
   				String inputValue = JOptionPane.showInputDialog("请输入文件名：");
   				if(inputValue!=null&&!inputValue.equals(""))
   				{
   					if(this.myFilePanel.hasSameNameSocket(inputValue,i))
   					{
   						JOptionPane.showMessageDialog(null, "命名重复：已存在\""+inputValue+"\"的文件", "重复命名", JOptionPane.ERROR_MESSAGE);
   					}
   					else
   					{
   				     this.myFilePanel.HoldSocket[i].Rename(inputValue);
   					}
   				}
   			}
   			
   			
   			
		}
   		this.myFilePanel.repaint();
	   }
	 //查找文件
		public void SortFileSocket(FilePanel temp)
		{
			
			this.SortString=this.Introduction.getText();
					if(temp!=null)
					{
						  if(temp.Instruction.equals(SortString))
							  this.newPanel=temp;
						  else{
					     for(int i=0;i<temp.socketcount;i++)
					     { if(temp.HoldSocket[i]!=null)
					     {
					         if(temp.HoldSocket[i].myFilePanel.Instruction.equals(this.SortString))
					        		 {
					        	 
					        	           this.newPanel =temp.HoldSocket[i].myFilePanel;
					        	           break;
					        		 }
					         else
					         {
					        	  SortFileSocket(temp.HoldSocket[i].myFilePanel);
					         }
					     }
					     }

					}
					}
					
		}
	 


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	
	
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
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==this.Create)
			{
			if(this.myFilePanel!=root)
			this.isCreate=true;
		
			
			}
		if(e.getSource()==this.CreateFile)
		{
			if(this.myFilePanel!=root)
			this.isCreateFile=true;
			
			
		}
		if(e.getSource()==this.Delete)
		{
			if(this.myFilePanel!=root)
			this.isDelete=true;
		}
		
		if(e.getSource()==this.BackControl&&this.myFilePanel.lastSocket!=null)
		{
			this.myFilePanel.lastSocket.foreSocket=this.myFilePanel;
			this.changeFilePanel(this.myFilePanel.lastSocket);
			Introduction.setText(this.myFilePanel.Instruction);
		}
		
		
		if(e.getSource()==this.ForeControl&&this.myFilePanel.foreSocket!=null)
		{
			this.changeFilePanel(this.myFilePanel.foreSocket);
			Introduction.setText(this.myFilePanel.Instruction);
		}
		if(e.getSource()==this.Sort)
		{
			this.isSort= true;
			
		}
		if(e.getSource()==this.Rename)
		{
			this.isRename=true;
		}
		if(e.getSource()==this.Copy)
		{
			if(this.myFilePanel!=root)
			this.isCopy=true;
		}
		if(e.getSource()==this.Paste)
		{
			if(this.myFilePanel!=root)
			this.isPaste=true;
		}
		if(e.getSource()==this.Formate)
		{
			if(this.myFilePanel==root)
			{
				this.isFormate= true;
			}
		}
	}
	
	
	
	
	

}
