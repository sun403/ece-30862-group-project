import java.io.*;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.util.Scanner;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.*;

public class Sounds
{
	IntBuffer buffer = BufferUtils.createIntBuffer(1);
	IntBuffer source = BufferUtils.createIntBuffer(1);
	FloatBuffer sourcePos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	FloatBuffer sourceVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	FloatBuffer listenerPos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	FloatBuffer listenerVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	FloatBuffer listenerOri = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();


	public Sounds()
	{
		try {
			AL.create();
		} catch (LWJGLException le) {
			le.printStackTrace();
			return;
		}
		AL10.alGetError();

		// Load the wav data.
		if(loadALData() == AL10.AL_FALSE) {
			System.out.println("Error loading data.");
			return;
		}

		setListenerValues();
	}

	int loadALData()
	{
		AL10.alGenBuffers(buffer);

		if(AL10.alGetError() != AL10.AL_NO_ERROR) {
		  return AL10.AL_FALSE;
		}

		//Loads the wave file from your file system
		/*
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream("C:\\shoot.wav");
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		  	return AL10.AL_FALSE;
		}

		WaveData waveFile = WaveData.create(fileIn);

		try {
			fileIn.close();
		}
		catch(IOException ex) {}
		*/

		//Loads the wave file from this class's package in your classpath
		WaveData waveFile = WaveData.create("shoot.wav");

		AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();

		AL10.alGenSources(source);

		if (AL10.alGetError() != AL10.AL_NO_ERROR) {
			return AL10.AL_FALSE;
		}

		AL10.alSourcei(source.get(0), AL10.AL_BUFFER,   buffer.get(0));
		AL10.alSourcef(source.get(0), AL10.AL_PITCH,    1.0f         );
		AL10.alSourcef(source.get(0), AL10.AL_GAIN,     1.0f         );
		AL10.alSource (source.get(0), AL10.AL_POSITION, sourcePos    );
		AL10.alSource (source.get(0), AL10.AL_VELOCITY, sourceVel    );

		if (AL10.alGetError() == AL10.AL_NO_ERROR) {
			return AL10.AL_TRUE;
		}

		return AL10.AL_FALSE;
	}

	void setListenerValues()
	{
		AL10.alListener(AL10.AL_POSITION,    listenerPos);
		AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
		AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
	}

	void killALData()
	{
		AL10.alDeleteSources(source);
		AL10.alDeleteBuffers(buffer);
	}

	public void play(int toPlay)
	{
		switch(toPlay) {
			case 0:
				AL10.alSourcePlay(source.get(0));
				break;
		}


		//AL10.alSourcePause(source.get(0));
		//AL10.alSourceStop(source.get(0));
	}

	protected void finalize() throws Throwable
	{
		super.finalize();
		killALData();
		AL.destroy();
	}
}