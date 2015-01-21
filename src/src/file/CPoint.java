package file;

import java.awt.Point;

public class CPoint {
	
	public int Cx=0;
	public int Cy=0;
	public  CPoint()
	{
		
	}
	public CPoint(int x,int y)
	{
		this.Cx=x;
		this.Cy=y;
	}
	public CPoint(Point a)
	{
		
	}
	
	public static CPoint changeFromPointToCPoint(Point a)
	{
		CPoint temp = new CPoint ();
		temp.Cy= (int)a.y/20;
		temp.Cx=0;
		return temp;
	}
	
	
	public static Point changeFromCPointToPoint(CPoint a)
	{
		Point temp =new Point();
		
		temp.y=a.Cx*20;
		temp.x=0;
		return temp;
	}
	
	public static Point getFilePoint(int i)
	{
		return CPoint.changeFromCPointToPoint(new CPoint(i,0));
	}

}
