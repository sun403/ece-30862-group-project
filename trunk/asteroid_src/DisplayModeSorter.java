import org.lwjgl.opengl.DisplayMode;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class DisplayModeSorter
{
    private final static int SORT_BY_HEIGHT = 0;
    private final static int SORT_BY_WIDTH = 1;
    private final static int SORT_BY_FREQUENCY = 2;
    private final static int SORT_BY_BITS_PER_PIXEL = 3;

    private ArrayList<DisplayMode> modeList;

    //HashMap<Integer, ArrayList<DisplayMode>> 

    public DisplayModeSorter(DisplayMode[] modeArray) { 
        modeList = new ArrayList<DisplayMode>(Arrays.asList(modeArray));
    }

    public DisplayMode getBestDisplayMode()
    {

        HashMap<Integer, ArrayList<DisplayMode>> heightMap = createDisplayModeMap(modeList, SORT_BY_HEIGHT);
        HashMap<Integer, ArrayList<DisplayMode>> widthMap;
        HashMap<Integer, ArrayList<DisplayMode>> frequencyMap;
        HashMap<Integer, ArrayList<DisplayMode>> bitsPerPixelMap;

        //Actually returns the second largest DisplayMode in modeArray
        //Why?
        //Because the largest, is usually the entire screen. However, because
        //of different windowing systems, sometimes there is an unavoidable menu bar at the top of the window
        //Thus, some of the bottom of the window will be cut off and not visible.
        heightMap.remove(getBiggestKey(heightMap));

        //Making modeList now the biggest (remaining) heights displaymodes
        modeList = heightMap.get(getBiggestKey(heightMap));

        widthMap = createDisplayModeMap(modeList, SORT_BY_WIDTH);
        modeList = widthMap.get(getBiggestKey(widthMap));

        frequencyMap = createDisplayModeMap(modeList, SORT_BY_FREQUENCY);
        modeList = frequencyMap.get(getBiggestKey(frequencyMap));
        
        bitsPerPixelMap = createDisplayModeMap(modeList, SORT_BY_BITS_PER_PIXEL);
        modeList = bitsPerPixelMap.get(getBiggestKey(bitsPerPixelMap));

        return modeList.get(0);
    }

    //Gets the keyed value from a given mode
    private int getValue(DisplayMode mode, int keyBy)
    {
        switch(keyBy)
        {
            case SORT_BY_HEIGHT:
                return mode.getHeight();
            case SORT_BY_WIDTH:
                return mode.getWidth();
            case SORT_BY_FREQUENCY:
                return mode.getFrequency();
            case SORT_BY_BITS_PER_PIXEL:
                return mode.getBitsPerPixel();
            default:
                return -1;
        }
    }

    //Takes a List of DisplayModes, and creates a HashMap
    //keyable by either height, width, frequency, or bits per pixel.
    private HashMap<Integer, ArrayList<DisplayMode>> 
        createDisplayModeMap(ArrayList<DisplayMode> modes, int keyBy)
    {
        HashMap<Integer, ArrayList<DisplayMode>> toReturn = new 
            HashMap<Integer, ArrayList<DisplayMode>>();

        for(DisplayMode mode : modes)
        {
            int value = getValue(mode, keyBy);

            if(toReturn.get(value) == null) {
                toReturn.put(value, new ArrayList<DisplayMode>());
            }

            toReturn.get(value).add(mode);
        }

        return toReturn;
    }

    private <T> int getBiggestKey(HashMap<Integer, T> map)
    {
        Integer[] keyArray = new Integer[0];
        Set<Integer> keySet = map.keySet();

        keyArray = keySet.toArray(keyArray);
        Arrays.sort(keyArray);

        if(keyArray.length == 0) {
            return -1;
        }
        else if(keyArray.length == 1) {
            return keyArray[0];
        }
        else {
            return keyArray[keyArray.length - 1];
        }
    }
}
