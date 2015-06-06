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
	
	private IUpdateHandler mITankMovement = new IUpdateHandler() {
		
		@Override
		public void reset() {}

		@Override
		public void onUpdate(float pSecondsElapsed) {
			float m = (float) mEdge.getGradient();
			float dX = (float) (mMOVEMENT_SPEED * pSecondsElapsed / Math.sqrt(1 + m * m));
			float dY = dX * m;
			
			float nX = mTankBody.getX() + dX;
			float nY = mTankBody.getY() + dY;
			
			mTankBody.setPosition(nX, nY);
		}
	};
	
	private IUpdateHandler mITankTurretRotation = new IUpdateHandler() {

		@Override
		public void onUpdate(float pSecondsElapsed) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void reset() {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
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
	
	public CampColor getTankDetailColor() {
		return this.mPlayer.getmColor();
	}

	public int getmDetailStength() {
		return mDetailStength;
	}
	
}
