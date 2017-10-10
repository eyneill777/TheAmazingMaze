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
    
    /**
     *  Get the opposite direction
     * @return the integer number of the opposite directionss
     */
    public int opposite() {
    	switch(num) {
    	case 0:
    		return 2;
		case 1:
			return 3;
		case 2:
			return 0;
		case 3:
			return 1;
		default:
			return num;
    	}
    }
}
