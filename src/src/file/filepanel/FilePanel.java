package file.filepanel;

import java.awt.Color;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;



import file.CPoint;
import file.afilesocket.AFileSocket;
import file.mainPanel.MainPanel;
import file.textfile.TextFile;

public class FilePanel extends JPanel implements MouseListener{
	
	//�����ļ���Ŀ
	public int filecount = 0;
	public int socketcount=0;   //���е��ļ�����Ŀ
	public int allcount =0;      //����Ŀ
	public int volum = 0;      //�ļ��а��������ڴ�
	
	
	public int Scount=0;    //�Խ��������ļ��м���
	public int Fcount=0;    //�Խ������ļ�����
	public int Ccount=0;    //�Ը�����Ϣ����
	
	public AFileSocket myFileSocket=null;     //�ļ���Ŀ¼
	

    public String Instruction="\\\\root";     //�ļ���������Ĭ��Ϊ��Ŀ¼������ʱ�Զ��ı�
	public static final int FILEMAXNUM=10;     //������������ļ���
	public static final int SOCKETMAXNUM =10;  //������������ļ�����
	//AFileSocket temp =null;
	public FilePanel lastSocket =null;            //��¼����һ���ļ���
 	public FilePanel foreSocket =null;             //��¼��һ������򿪵��ļ��У�����ǰ�������
	
	
	public AFileSocket HoldSocket[] =  new AFileSocket[SOCKETMAXNUM];   //�������ļ�������
	public TextFile  HoldFile[]= new TextFile[FILEMAXNUM];              //�������ļ�����
	
	
	//����һ���ļ�tree
	public DefaultMutableTreeNode myFileTree = null;
	public FilePanel()
	{
		   
		  this.setLayout(null);
	      this.setBackground(Color.white);
	      //this.setPopMenu();
	      this.addMouseListener(this);
	      
	     
	      
	     
	}
	
	
	
	public void setInstruction()
	{
		 
	      if(this.lastSocket!=null)
	      Instruction = this.lastSocket.Instruction+"\\\\"+this.myFileSocket.SocketName;
	    
	}
//	//����ļ���
//	public void addAFile()
//	{
//		AFileSocket temp = new AFileSocket();
//		this.HoldSocket[this.socketcount++] = temp;
//		this.allcount++;
//		this.addNewFileSocket(this.HoldSocket[this.socketcount-1], this.socketcount-1);
//     
//		
//	}
	//����ļ���
	synchronized	public void addAFileSocket()
		{
			
		
			if(this.socketcount<SOCKETMAXNUM)
			{
			AFileSocket temp = new AFileSocket("�½��ļ���"+"("+(1+this.Scount++)+")",this.myFileSocket.rootNum);
			
			//���ļ����´����ļ��е���һ��Ϊ���ļ���
			temp.myFilePanel.lastSocket=this;
			this.HoldSocket[this.socketcount++] = temp;
			temp.myFilePanel.setInstruction();
			
			
			this.addNewFileSocket(this.HoldSocket[this.socketcount-1], this.allcount);
		
			this.allcount++;
			MainPanel.WholeFileSocketNum.setText("�ļ���������"+(++MainPanel.WholeSocketNumCount));
			if(this.myFileSocket!=null)
			this.myFileSocket.updateFileSocket();
			
			
			//�����ļ���
			this.HoldSocket[this.socketcount-1].myFilePanel.myFileTree = new DefaultMutableTreeNode(this.HoldSocket[this.socketcount-1].SocketName);
			//this.myFileTree.add(new DefaultMutableTreeNode(this.HoldSocket[this.socketcount-1].myFilePanel.myFileTree));
			//this.HoldSocket[this.socketcount-1].myFilePanel.myFileTree.isRoot();
			this.myFileTree.add(this.HoldSocket[this.socketcount-1].myFilePanel.myFileTree );
			MainPanel.treeView.repaint();
			MainPanel.rootTree.updateUI();
			
			
			
			}
			
			
		}
	
	
	
	
	
	//����ļ�
	public void addAFile()
	{
		if(this.filecount<FILEMAXNUM)
		{
		TextFile temp = new TextFile("���ļ�"+(1+this.Fcount++),this.myFileSocket.rootNum);
		
		//���ļ����´����ļ��е���һ��Ϊ���ļ���
		  temp.lastSocket=this;
		this.HoldFile[this.filecount++] = temp;
		
		//this.HoldSocket[this.socketcount++]  = new AFileSocket("�½��ļ���"+(1+this.filecount++));
		
		this.addNewFile(this.HoldFile[this.filecount-1], this.allcount);
		//System.out.println(212);
		this.allcount++;
		//���ļ�������
		MainPanel.WholeFileNum.setText("�ļ�������"+(++MainPanel.WholeFileNumCount));
		if(this.myFileSocket!=null)
		this.myFileSocket.updateFileSocket();
		
		
		//�����ļ���
		this.HoldFile[this.filecount-1].myFileTree = new  DefaultMutableTreeNode(this.HoldFile[this.filecount-1].FileName);
		this.myFileTree.add(this.HoldFile[this.filecount-1].myFileTree);
		//this.HoldFile[this.filecount-1].myFileTree.getp
		MainPanel.treeView.repaint();
		MainPanel.rootTree.updateUI();
		}
		
	}
	
	
	
	//����һ���ļ���
synchronized	public void  addNewFileSocket(AFileSocket a,int count)
	{
		a.setFileBountds(CPoint.getFilePoint(count));
		this.add(a);
		
	}
//����һ���ļ�
public void addNewFile(TextFile a,int count)
{
	a.setFileBountds(CPoint.getFilePoint(count));
	this.add(a);
	}
	
	
	//ɾ���ļ���
	public void deleteSocket()
	{
		this.reSortSocket();
		this.showAllFile();
		updateAllFile();
	}
	
	
	public void deleteFile()
	{
		this.reSortFile();
		this.showAllFile();
		updateAllFile();
		
	}
	
	public void updateAllFile()
	{
	   FilePanel temp= this;
	   
		while(temp.myFileSocket!=null)
		{
			temp.myFileSocket.updateFileSocket();
			temp=temp.lastSocket;
		}
	}
	
	//ɾ���ļ�������������
	public void reSortSocket()
	{
		for(int c=0;c<this.socketcount;c++)
    	{
			if(this.HoldSocket[c]==null)System.out.println(111);
			else{
			if(this.HoldSocket[c].isDelete==true)
			{
				//�ͷ��ڴ�
				if(this.lastSocket!=null&&this.lastSocket.myFileSocket!=null)
				{
				this.lastSocket.myFileSocket.FileVolume-=HoldSocket[c].FileVolume;
				this.lastSocket.myFileSocket.FileNum--;
				}
				HoldSocket[c].myFilePanel.lastSocket.foreSocket=null;
				MainPanel.WholeFileNum.setText("�ļ�������"+(MainPanel.WholeFileNumCount=MainPanel.WholeFileNumCount-this.getWholeFileNum(HoldSocket[c])));
				//MainPanel.WholeFileSocketNum.setText("�ļ���������"+(MainPanel.WholeSocketNumCount=MainPanel.WholeSocketNumCount-1-HoldSocket[c].myFilePanel.socketcount));
				MainPanel.WholeFileSocketNum.setText("�ļ���������"+(MainPanel.WholeSocketNumCount=MainPanel.WholeSocketNumCount-1-this.getWholeSocketNum(HoldSocket[c])));
				MainPanel.ZoneLeftVolume[this.myFileSocket.rootNum]+=HoldSocket[c].FileVolume;
				MainPanel.UnUsedVolume.setText("ʣ���ڴ棺"+(MainPanel.WholeUnUsedVolumeCount+=HoldSocket[c].FileVolume)+"B");
				MainPanel.WholeUsedVolume.setText("��ʹ���ڴ棺"+(MainPanel.WholeUsedVolumeCount-=HoldSocket[c].FileVolume)+"B");
				this.HoldSocket[c].isDelete=false;
				
				this.myFileTree.remove(this.HoldSocket[c].myFilePanel.myFileTree);
				MainPanel.rootTree.updateUI();
				this.remove(this.HoldSocket[c]);
				this.repaint();
				
			
				for(int j=c;j<socketcount-1;j++)
				{
					System.out.println(j+1);
					this.HoldSocket[j]=this.HoldSocket[j+1];
				}
			
				this.socketcount--;
				this.allcount--;
				this.HoldSocket[this.socketcount]=null;
			}
		}
		}
	
		
	}
	
	//ɾ���ļ�����������
	public void  reSortFile()
	{
		for(int c=0;c<this.filecount;c++)
		{
			if(this.HoldFile[c]==null)System.out.println(111);
			else{
			if(this.HoldFile[c].isDelete==true)
			{
				this.HoldFile[c].isDelete=false;
				
				//���Ǹ�Ŀ¼�������
				if(this.lastSocket!=null&&this.lastSocket.myFileSocket!=null)
				{
				this.lastSocket.myFileSocket.FileVolume-=HoldFile[c].FileVolume;
				this.lastSocket.myFileSocket.FileNum--;
				}
				//MainPanel.WholeFileNum.setText("�ļ�������"+(MainPanel.WholeFileNumCount=MainPanel.WholeFileNumCount-1));
				MainPanel.WholeFileNum.setText("�ļ�������"+(MainPanel.WholeFileNumCount=MainPanel.WholeFileNumCount-1));
				MainPanel.ZoneLeftVolume[this.myFileSocket.rootNum]+=HoldFile[c].FileVolume;
				MainPanel.UnUsedVolume.setText("ʣ���ڴ棺"+(MainPanel.WholeUnUsedVolumeCount+=HoldFile[c].FileVolume)+"B");
				MainPanel.WholeUsedVolume.setText("��ʹ���ڴ棺"+(MainPanel.WholeUsedVolumeCount-=HoldFile[c].FileVolume)+"B");
				//ɾ���ļ����Ľڵ�
			
				this.HoldFile[c].myFileTree.removeFromParent();
				
				MainPanel.rootTree.updateUI();
				
				
				this.remove(this.HoldFile[c]);
				this.repaint();
			
				if(c!=filecount)
				{
				for(int j=c;j<filecount-1;j++)
				{
					this.HoldFile[j]=this.HoldFile[j+1];
				}
				}
				this.filecount--;
				this.allcount--;
				this.HoldFile[this.filecount]=null;
			}
		}
		}
		
	}
	
	
	
	
	//��ʾ�����ļ����ļ���
	public void showAllFile()
	{
            int i=0;
		
		for(;i<this.socketcount;i++)
		{
			this.HoldSocket[i].setFileLocation(i);
		
		}
		for(int j=0;j<this.filecount;j++,i++)
		{
			this.HoldFile[j].setFileLocation(i);
		
		}
		
 
		
	}
	
	
	//ճ���ļ���
	public void pasteAFileSocket()
	{
		
	

	if(this.socketcount<SOCKETMAXNUM)
	{
		
	
		
	AFileSocket temp =MainPanel.CopySocketBuffer.getACopy();
	if(MainPanel.ZoneLeftVolume[this.myFileSocket.rootNum]-temp.FileVolume<0)
	{
		JOptionPane.showMessageDialog(null, "ճ��ʧ�ܣ��÷����ڴ�����", "�ڴ����", JOptionPane.ERROR_MESSAGE);
		
	}
	else
	{
	temp.rootNum=this.myFileSocket.rootNum;
	//���ļ����´����ļ��е���һ��Ϊ���ļ���
	temp.myFilePanel.lastSocket=this;
	this.HoldSocket[this.socketcount++] = temp;
	
	
	if(this.hasSameNameSocket(temp.SocketName,this.socketcount-1))
	{
		this.HoldSocket[this.socketcount-1].SocketName+=("_����_"+Ccount++);
		this.HoldSocket[this.socketcount-1].FileSocketName.setText(this.HoldSocket[this.socketcount-1].SocketName);
	}
	
//	//�����ļ���
//	this.HoldSocket[this.socketcount-1].myFilePanel.myFileTree = new DefaultMutableTreeNode(this.HoldSocket[this.socketcount-1].SocketName);
//	//this.myFileTree.add(new DefaultMutableTreeNode(this.HoldSocket[this.socketcount-1].myFilePanel.myFileTree));
//	//this.HoldSocket[this.socketcount-1].myFilePanel.myFileTree.isRoot();
//	this.myFileTree.add(this.HoldSocket[this.socketcount-1].myFilePanel.myFileTree );
//	MainPanel.treeView.repaint();
//	MainPanel.rootTree.updateUI();
//	temp.myFilePanel.setInstruction();

	
	this.addNewFileSocket(this.HoldSocket[this.socketcount-1], this.allcount);

	this.allcount++;
	MainPanel.WholeFileSocketNum.setText("�ļ���������"+
	(MainPanel.WholeSocketNumCount+=(this.getWholeSocketNum(this.HoldSocket[this.socketcount-1])+1)));
	
	
	MainPanel.WholeFileNum.setText("�ļ�������"+(MainPanel.WholeFileNumCount+=this.getWholeFileNum(this.HoldSocket[this.socketcount-1])));
	MainPanel.ZoneLeftVolume[this.myFileSocket.rootNum]-=this.HoldSocket[this.socketcount-1].FileVolume;
	MainPanel.WholeUsedVolume.setText("��ʹ���ڴ棺"+(MainPanel.WholeUsedVolumeCount+=this.HoldSocket[this.socketcount-1].FileVolume)+"B");
	MainPanel.UnUsedVolume.setText("ʣ���ڴ棺"+(MainPanel.WholeUnUsedVolumeCount-=this.HoldSocket[this.socketcount-1].FileVolume)+"B");
	if(this.myFileSocket!=null)
	this.myFileSocket.updateFileSocket();
	}
	if(this.myFileSocket!=null)	
	{
	this.myFileSocket.flushDataWhenPaste(this);
	this.myFileSocket.flushFileTree(this.HoldSocket[this.socketcount-1].myFilePanel);
	}
	else 
		this.HoldSocket[this.socketcount-1].flushDataWhenPaste(this.HoldSocket[this.socketcount-1].myFilePanel);
	//else this.HoldSocket[this.socketcount-1].myFilePanel.setInstruction();
	
	
	}
	}
	
	

	//ճ���ļ�
	public void pasteAFile()
	{

		
       
		if(this.filecount<FILEMAXNUM)
		{
		TextFile temp = MainPanel.CopyFileButter.getACopy();
		if(MainPanel.ZoneLeftVolume[this.myFileSocket.rootNum]-temp.FileVolume<0)
		{
			JOptionPane.showMessageDialog(null, "ճ��ʧ�ܣ��÷����ڴ�����", "�ڴ����", JOptionPane.ERROR_MESSAGE);
			
		}
		else
		{
		temp.rootNum=this.myFileSocket.rootNum;
		//���ļ����´����ļ��е���һ��Ϊ���ļ���
		 temp.lastSocket=this;
		this.HoldFile[this.filecount++] = temp;
		if(this.hasSameNameFile(temp.FileName,this.filecount-1))
		{
			this.HoldFile[this.filecount-1].FileName+=("_����_"+Ccount++);
			this.HoldFile[this.filecount-1].FileNameLabel.setText(this.HoldFile[this.filecount-1].FileName);
		}
		this.HoldFile[this.filecount-1].updateFile();
		this.HoldFile[this.filecount-1].updateAllFile();
		MainPanel.WholeUsedVolume.setText("��ʹ���ڴ棺"+(MainPanel.WholeUsedVolumeCount+=this.HoldFile[this.filecount-1].FileVolume)+"B");
		MainPanel.ZoneLeftVolume[this.myFileSocket.rootNum]-=this.HoldFile[this.filecount-1].FileVolume;
		MainPanel.UnUsedVolume.setText("ʣ���ڴ棺"+(MainPanel.WholeUnUsedVolumeCount-=this.HoldFile[this.filecount-1].FileVolume)+"B");
		//this.HoldSocket[this.socketcount++]  = new AFileSocket("�½��ļ���"+(1+this.filecount++));
		
		this.addNewFile(this.HoldFile[this.filecount-1], this.allcount); 
		//System.out.println(212);
		this.allcount++;
		//���ļ�������
		MainPanel.WholeFileNum.setText("�ļ�������"+(++MainPanel.WholeFileNumCount));
		if(this.myFileSocket!=null)
		this.myFileSocket.updateFileSocket();
		
		
		//�����ļ���
		this.HoldFile[this.filecount-1].myFileTree = new  DefaultMutableTreeNode(this.HoldFile[this.filecount-1].FileName);
		this.myFileTree.add(this.HoldFile[this.filecount-1].myFileTree);
		//this.HoldFile[this.filecount-1].myFileTree.getp
		MainPanel.treeView.repaint();
		MainPanel.rootTree.updateUI();
		
		 
		}
		
		}
		
		
	}
	
	//���ļ��Ƿ�����
	
	public boolean hasSameNameSocket(String name,int num)
	{
		for(int i=0;i<this.socketcount;i++)
	 {
		if(this.HoldSocket[i].SocketName.equals(name)&&num!=i)
		{
		
			return true;
		}
		
	}
	return false;
	}
	
	//ͨ�������ҵ��ļ���
	public AFileSocket  findSocketByName(String name)
	{
		
		for(int i=0;i<this.socketcount;i++)
		{
			if(this.HoldSocket[i].SocketName.equals(name))
			{
				return this.HoldSocket[i];
			}
		}
	
		

		return null;
		
	}	  
	
	public boolean hasSameNameFile(String name,int num)
	{
		for(int i=0;i<this.filecount;i++)
		{
			if(this.HoldFile[i].FileName.equals(name)&&num!=i)
			{
				return true;
			}
			
		}
		return false;
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

	//�ݹ�����ļ������ļ�����Ŀ
	public int getWholeSocketNum(AFileSocket a)
	{
		
		int WholeNum=0;
		int temp=0;
		if(a!=null)
		{
		    for(int i=0;i<a.myFilePanel.socketcount;i++)
		    {
		    	temp=temp+getWholeSocketNum(a.myFilePanel.HoldSocket[i]);
		    }
		    WholeNum=a.myFilePanel.socketcount+temp;
		}
		return WholeNum;

	}
  //�ݹ�����ļ������ļ���Ŀ	
	public int getWholeFileNum(AFileSocket a)
	{
		
		int WholeNum=0;
		int temp=0;
		if(a!=null)
		{
		    for(int i=0;i<a.myFilePanel.socketcount;i++)
		    {
		    	temp=temp+getWholeFileNum(a.myFilePanel.HoldSocket[i]);
		    }
		    WholeNum=a.myFilePanel.filecount+temp;
		}
		return WholeNum;

	}
	
	
	
	
    




	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
//		if((e.getModifiers()&InputEvent.BUTTON3_MASK)!=0){
//			//�����˵�  
//			popupMenu1.show(this,e.getX(),e.getY());
//			System.out.println(111);
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

}
