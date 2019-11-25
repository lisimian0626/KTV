package com.bestarmedia.pcmrecord;


public interface RecordInf {
	final int FRAME_COUNT = 160;
	
	/**
	 * 开始录音
	 * 建议在MediaPlayer的onPrepared回调函数中调用
	 * @param savePath 录音文件保存的路径
	 * @return
	 */
	boolean startRecord(String savePath);
	
	/**
	 * 停止录音
	 */
	void stopRecord();
	
	/**
	 * 重新录音
	 */
	void reRecord();
	
	/**
	 * 丢弃录音，并且把录音文件删除掉
	 */
	void discardRecord();
	
	/**
	 * 是否正在录音
	 * @return
	 */
	boolean isRecording();
	
	/**
	 * 录音是否结束
	 * @return
	 */
	boolean isRecordFinish();
	
	/**
	 * 获取录音时长
	 * @return
	 */
	long getRecordDuration();
	
	/**
	 * 获取录音保存的路径
	 * @return
	 */
	String getSavePath();
	
	/**设置mp3压缩率，压缩率越大，音质越好，mp3文件就越大
	 * 例如设置为192Kbps，则ratio为192。
	 * */
	void setCompressionRatio(int ratio);
	
/*	*//**设置采样率*//*
	void setSampleRate(int rate);*/
	
	/**
	 * 设置录音合成比例
	 * */
	void setComposeScale(float videoScale, float micScale);
}
