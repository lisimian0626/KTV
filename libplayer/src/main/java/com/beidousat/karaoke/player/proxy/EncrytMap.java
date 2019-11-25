package com.beidousat.karaoke.player.proxy;

import java.util.HashMap;

public class EncrytMap 
{
	private static EncrytMap _this = new EncrytMap();
	private static HashMap map = new HashMap();
	public void add(String key)
	{
		if(!map.containsKey(key))
		map.put(key, key);
	}
	public  boolean check(String key)
	{
		return map.containsKey(key);
	}
	
	public static EncrytMap getInstance()
	{
		return _this;
	}
}
