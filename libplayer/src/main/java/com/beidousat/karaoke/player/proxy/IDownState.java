package com.beidousat.karaoke.player.proxy;

public interface IDownState 
{
	enum DownState{
	DOWNING,
	SUCCESS,
	ERROR,
	ERROREXIT
	}
	void onInit(String url, int tatol, boolean isenc);
	void onDown(String url, int len, DownState state, byte[] buf, int bufLen);
}
