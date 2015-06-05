package GameLevels.Elements;

import java.util.ArrayList;

import org.andengine.entity.sprite.TiledSprite;

import GameLevels.GameLevel;
import Managers.ResourceManager;

public class Camp {
	
	//=====================================================
	// VARIABLES
	//=====================================================
	private final GameLevel mGameLevel;
	
	private final int mX,mY;
	private final int mSize;	// maximum capacity of camp
	private CampColor mColor;
	private final ArrayList<Edge> mIncidentEdges;
	
	private int mStrength; // current number of tanks 
	private Player mPlayer; // current player controlling the camp
	
	private TiledSprite mTiledCamp;
	
	
	//=====================================================
	// CONSTRUCTOR
	//=====================================================
	public Camp(int pX, int pY, int pSize, CampColor color, GameLevel pGameLevel) {
		this.mX = pX;
		this.mY = pY;
		this.mSize = pSize;
		this.mColor = color;
		this.mGameLevel = pGameLevel;
		
		mIncidentEdges = new ArrayList<Edge>();
		
		// draw camp
		mTiledCamp = new TiledSprite(mX, mY, ResourceManager.gameCamps, ResourceManager.getActivity().getVertexBufferObjectManager());
		mTiledCamp.setCurrentTileIndex(0);
		
		this.mGameLevel.attachChild(mTiledCamp);
		this.mTiledCamp.setScale(0.3f); // random value
	}
	
	//=====================================================
	// METHODS
	//=====================================================
	public void addEdge(Edge e) {
		this.mIncidentEdges.add(e);
	}

	
	//=====================================================
	// GETTERS AND SETTERS
	//=====================================================
	public int getmStrength() {
		return mStrength;
	}

	public void setmStrength(int mStrength) {
		this.mStrength = mStrength;
	}

	public Player getmPlayer() {
		return mPlayer;
	}

	public void setmPlayer(Player mPlayer) {
		this.mPlayer = mPlayer;
	}

	public int getmX() {
		return mX;
	}

	public int getmY() {
		return mY;
	}

	public int getmSize() {
		return mSize;
	}

	public ArrayList<Edge> getmIncidentEdges() {
		return mIncidentEdges;
	}
}
