package main;

public enum Direction 
{
	North (0),
	East (1),
	South (2),
	West (3);
	
	public int num;
	Direction(int num)
	{
		this.num = num;
	}
}
