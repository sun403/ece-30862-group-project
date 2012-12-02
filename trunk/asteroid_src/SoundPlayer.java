import java.io.*;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.util.Scanner;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.*;

public class SoundPlayer
{
    private final String[] soundFileNames = { "shoot.wav", "explosion.wav", "ufo.wav" };
	private final IntBuffer buffer = BufferUtils.createIntBuffer(soundFileNames.length);
	private final IntBuffer source = BufferUtils.createIntBuffer(soundFileNames.length);

	private final FloatBuffer sourcePos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	private final FloatBuffer sourceVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	private final FloatBuffer listenerPos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	private final FloatBuffer listenerVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	private final FloatBuffer listenerOri = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();

	public SoundPlayer()
	{
		try {
			AL.create();
		} 
        catch(LWJGLException l)
        {
            System.out.println(l);
            System.exit(-1);
		}
		AL10.alGetError();

		// Load the wav data.
		if(loadALData() == AL10.AL_FALSE)
        {
			System.out.println("Error loading data.");
            System.exit(-1);
		}

		setListenerValues();
	}

	private int loadALData()
	{
		AL10.alGenBuffers(buffer);

		if(AL10.alGetError() != AL10.AL_NO_ERROR) {
		  return AL10.AL_FALSE;
		}

		//Loads the wave files from this class's package in your classpath

        WaveData soundFile;

        for(int i = 0; i < soundFileNames.length; i++)
        {
            soundFile = WaveData.create(soundFileNames[i]);
            AL10.alBufferData(buffer.get(i), soundFile.format, soundFile.data, soundFile.samplerate);
            soundFile.dispose();
        }

		AL10.alGenSources(source);

		if (AL10.alGetError() != AL10.AL_NO_ERROR) {
			return AL10.AL_FALSE;
		}


        for(int i = 0; i < soundFileNames.length; i++)
        {
            AL10.alSourcei(source.get(i), AL10.AL_BUFFER,   buffer.get(i));
            AL10.alSourcef(source.get(i), AL10.AL_PITCH,    1.0f         );
            AL10.alSourcef(source.get(i), AL10.AL_GAIN,     1.0f         );
            AL10.alSource (source.get(i), AL10.AL_POSITION, sourcePos    );
            AL10.alSource (source.get(i), AL10.AL_VELOCITY, sourceVel    );
        }

		if (AL10.alGetError() == AL10.AL_NO_ERROR) {
			return AL10.AL_TRUE;
		}

		return AL10.AL_FALSE;
	}

	private void setListenerValues()
	{
		AL10.alListener(AL10.AL_POSITION,    listenerPos);
		AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
		AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
	}

	private void killALData()
	{
		AL10.alDeleteSources(source);
		AL10.alDeleteBuffers(buffer);
	}

	public void play(int toPlay) {
        AL10.alSourcePlay(source.get(toPlay));
	}
    
    public void stopPlay(int toStop) {
        AL10.alSourceStop(source.get(toStop));
    }

	protected void finalize() throws Throwable
	{
		super.finalize();
		killALData();
		AL.destroy();
	}
}
