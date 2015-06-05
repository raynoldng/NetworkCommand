package GameLevels.Elements;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;

import GameLevels.GameLevel;
import Managers.ResourceManager;

public class TankDetail {

	//=====================================
	// CONSTANTS
	//=====================================
	private static final float mMOVEMENT_SPEED = 5f;
	private static final float mTURRET_DEGREES_PER_SECOND= 5f;
	
	//=====================================
	// VARIABLES
	//=====================================
	private final GameLevel mGameLevel;
	
	private final Camp mStartingCamp;	// where the detail starts
	private final Camp mEndCamp;		// where the detail ends
	private final Edge mEdge;			// edge detail traveling on
	
	private int mDetailStength;			// number of tanks in detail
	private Player mPlayer;				// Player who controls the detail
	
	private final Sprite mTankBody;
	private final Sprite mTankTurret;
	
	private TankDetail mTargetedEnemy;	// engaged tank detail if any
	
	private IUpdateHandler mITankMovement, mITankTurretRotation;
	
	//======================================
	// CONSTUCTOR
	//======================================
	public TankDetail(Camp pStartingCamp, Edge pEdge, int pDetailStrength, Player pPlayer, GameLevel pGameLevel) {
		this.mStartingCamp = pStartingCamp;
		this.mEdge = pEdge;
		this.mEndCamp = this.mEdge.other(mStartingCamp);
		
		this.mDetailStength = pDetailStrength;
		this.mPlayer = pPlayer;
		this.mGameLevel = pGameLevel;
		
		mTankBody = new Sprite(mStartingCamp.getmX(), mStartingCamp.getmY(), mPlayer.getTankOptions().getTankBodyTR(), ResourceManager.getActivity().getVertexBufferObjectManager());
		mTankTurret = new Sprite(0, 0, mPlayer.getTankOptions().getTankTurretTR(), ResourceManager.getActivity().getVertexBufferObjectManager());
		
		mTankBody.registerUpdateHandler(mITankMovement);	// NOT INITIALISED YET
		mTankTurret.registerUpdateHandler(mITankTurretRotation);
		
		mTankTurret.setAnchorCenterY(0.5f); // random value 
		mTankBody.attachChild(mTankTurret);
		
		this.mGameLevel.attachChild(mTankBody);
		this.mTankBody.setScale(0.3f); // random value
	}
	
	//=================================================
	// METHODS
	//=================================================
	
	
}
