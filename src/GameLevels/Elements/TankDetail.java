package GameLevels.Elements;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import GameLevels.GameLevel;
import Managers.ResourceManager;

public class TankDetail {

	//=====================================
	// CONSTANTS
	//=====================================
	private static final float mMOVEMENT_SPEED = 50f;
	private static final float mTURRET_DEGREES_PER_SECOND = 50f;
	private static final float mTIME_EPSILON = 0.25f;
	
	//=====================================
	// VARIABLES
	//=====================================
	private final GameLevel mGameLevel;
	
	private final Camp mStartingCamp;	// where the detail starts
	private final Camp mEndCamp;		// where the detail ends
	private final Edge mEdge;			// edge detail traveling on
	
	private int mDetailStength;			// number of tanks in detail
	private Player mPlayer;				// Player who controls the detail
	
	// movement
	private final float mDestX;				// X coordinate of destination camp
	private final float mDestY;				// Y coordinate of destination camp
	private final float mDegreeToDestFromX;	// angle tank will be pointing towards
	private final float mETA;				// estimate time of arrival
	private float mElapsedTime;				// elapse time since deployment of detail
	
	// turret rotation
	private final float mTurretAngleFromTank;	
	private TankDetail mTargetedEnemy;			// engaged tank detail if any
	
	private final Sprite mTankBody;
	private final Sprite mTankTurret;
	
	
	
	/**
	 * Handles the movement of tank and listener when it reached destination
	 */
	private IUpdateHandler mITankMovement = new IUpdateHandler() {
		
		@Override
		public void reset() {}

		@Override
		public void onUpdate(float pSecondsElapsed) {
			
			// see if we have reached destination
			if(Math.abs(mElapsedTime - mETA) < mTIME_EPSILON) {
				Debug.i("TankDetail" , "Destination reached");	// working
			}
			
			mElapsedTime += pSecondsElapsed;
			
			float x = mTankBody.getX();
			float y = mTankBody.getY();
			
			float dx = (float) (pSecondsElapsed * mMOVEMENT_SPEED * Math.cos(MathUtils.degToRad(mDegreeToDestFromX)));
			float dy = (float) (pSecondsElapsed * mMOVEMENT_SPEED * Math.sin(MathUtils.degToRad(mDegreeToDestFromX)));
			
			mTankBody.setPosition(x + dx, y + dy);
		}
	};
	
	/**
	 * Listener for detection of enemy tanks and handles rotation of turret if enemy spotted
	 */
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
		
		// do the position and angle calculations
		mElapsedTime = 0.0f;
		mDestX = mEndCamp.getmX();
		mDestY = mEndCamp.getmY();
		mDegreeToDestFromX = MathUtils.radToDeg((float) Math.atan2(mDestY - mStartingCamp.getmY(), mDestX - mStartingCamp.getmX()));
		mETA = (float) (pEdge.getWeight() / mMOVEMENT_SPEED);
		
		// initialize graphics
		mTankBody = new Sprite(mStartingCamp.getmX(), mStartingCamp.getmY(), mPlayer.getTankOptions().getTankBodyTR(), ResourceManager.getActivity().getVertexBufferObjectManager());
		mTankTurret = new Sprite(mTankBody.getWidth()/2, mTankBody.getHeight()/2, mPlayer.getTankOptions().getTankTurretTR(), ResourceManager.getActivity().getVertexBufferObjectManager());
		
		mTankBody.registerUpdateHandler(mITankMovement);	
		//mTankTurret.registerUpdateHandler(mITankTurretRotation); // NOT INITIALISED YET
		
		mTankTurret.setAnchorCenterY(0.75f); // preset value
		mTankBody.attachChild(mTankTurret);
		mTankBody.setScale(0.7f);
		
		// rotate the body, initialise pointing downwards
		mTankBody.setRotation(mTankBody.getRotation() + 270 - mDegreeToDestFromX);
		
		
		this.mGameLevel.attachChild(mTankBody);
		//this.mTankBody.setScale(0.3f); // random value
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
