package file.filetree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import file.filepanel.FilePanel;
import file.mainPanel.MainPanel;

public class FileTree  extends JPanel{
	
	public JTree  myFileTree = null;
	//public MainPanel myMainPanel  = null;
	
	public FileTree()
	{
		
		DefaultMutableTreeNode top =
	        new DefaultMutableTreeNode("root");
		//createNodes(top);
	    myFileTree = new JTree(top);
	   
	    JScrollPane treeView = new JScrollPane(myFileTree);
	    this.add(treeView);
	    
	    
	    
	   treeView.setBounds(0,0, 400, 400);

	}
	

}
