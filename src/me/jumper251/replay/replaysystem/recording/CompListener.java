package me.jumper251.replay.replaysystem.recording;


import me.jumper251.replay.listener.AbstractListener;

public class CompListener extends AbstractListener{

	@SuppressWarnings("unused")
	private PacketRecorder packetRecorder;
	
	public CompListener(PacketRecorder packetRecorder) {
		super();
		
		this.packetRecorder = packetRecorder;
	}
}
