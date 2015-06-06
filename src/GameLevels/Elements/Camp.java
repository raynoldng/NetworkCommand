package GameLevels.Elements;

import java.util.ArrayList;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;

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
	private Text mTextCampSize;
	
	
	//=====================================================
	// CONSTRUCTOR
	//=====================================================
	public Camp(int pX, int pY, int pSize, CampColor color, GameLevel pGameLevel) {
		this.mX = pX;
		this.mY = pY;
		this.mSize = pSize;
		this.mColor = color;
		this.mGameLevel = pGameLevel;
		
		this.mStrength = this.mSize;
		
		mIncidentEdges = new ArrayList<Edge>();
		
		// draw camp
		mTiledCamp = new TiledSprite(mX, mY, ResourceManager.gameCamps, ResourceManager.getActivity().getVertexBufferObjectManager());
		mTiledCamp.setCurrentTileIndex(color.mTileIndex);
		
		// draw text
		mTextCampSize = new Text(mTiledCamp.getWidth()/2, mTiledCamp.getHeight()/2, ResourceManager.fontDefault32Bold, String.valueOf(mSize), ResourceManager.getActivity().getVertexBufferObjectManager());
		
		mTiledCamp.attachChild(mTextCampSize);
		mTiledCamp.setZIndex(2);
		
		this.mGameLevel.attachChild(mTiledCamp);
	}
	
	//=====================================================
	// METHODS
	//=====================================================
	public void addEdge(Edge e) {
		this.mIncidentEdges.add(e);
	}
	
	public TankDetail sendTankDetail(int pDetailStrength, Edge pEdge) {
		
		if(mStrength < pDetailStrength) return null;
		
		TankDetail tankDetail = new TankDetail(this, pEdge, pDetailStrength, mPlayer, mGameLevel);
		mStrength -= pDetailStrength;
		updateText();
		
		return tankDetail;
	}
	
	/**
	 * @param tankDetail Tank detail in bound to camp
	 * updates the camp strength and color if necessary
	 */
	public void recieveTankDetail(TankDetail tankDetail) {
		
		// update the strength and change camp color if strength goes negative
		if(mColor == tankDetail.getTankDetailColor()) {
			mStrength += tankDetail.getmDetailStength();
		} else {
			mStrength -= tankDetail.getmDetailStength();
			if(mStrength < 0) {
				// change sides
				mStrength = Math.abs(mStrength);
				changeColor(tankDetail.getTankDetailColor());
			}
		}
		
		updateText();
	}
	
	private void changeColor(CampColor tankDetailColor) {
		// TODO: hook for animation
		mTiledCamp.setCurrentTileIndex(tankDetailColor.mTileIndex);
	}

	/**
	 * called when the number of tanks in the camp change
	 */
	private void updateText() {
		mTextCampSize.setText(String.valueOf(mTextCampSize));
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
