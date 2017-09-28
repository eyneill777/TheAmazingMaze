package main;

import java.util.HashMap;
import java.util.Map;

public enum Direction 
{
	North (0),
	East (1),
	South (2),
	West (3);
	
	public int num;
	private static Map map = new HashMap<>();
	
	Direction(int num)
	{
		this.num = num;
	}
	
	static //Create a hashmap of direction values for converting an int into a Direction
	{
        for (Direction d : Direction.values()) 
        {
            map.put(d.num, d);
        }
    }

    public static Direction valueOf(int num) //Return the direction associated with a given value
    {
        return (Direction) map.get(num);
    }
}
