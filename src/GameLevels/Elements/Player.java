package GameLevels.Elements;

import org.andengine.opengl.texture.region.TextureRegion;

import Managers.ResourceManager;

public class Player {
	
	//==================================================
	// CONSTANTS
	//==================================================
	
	
	private final int mPlayerNo;
	
	private CampColor mColor;
	private TankOptions mTankOptions;
	
	
	public Player(int pPlayerNo, CampColor pColor, TankOptions pTankOptions) {
		this.mPlayerNo = pPlayerNo;
		this.setmColor(pColor);
		this.mTankOptions = pTankOptions;
	}
	
	
	
	public TankOptions getTankOptions() {
		return this.mTankOptions;
	}
	
	
	public CampColor getmColor() {
		return mColor;
	}



	public void setmColor(CampColor mColor) {
		this.mColor = mColor;
	}


	// INNER CLASSES
	public enum TankOptions {
		E100 (ResourceManager.gameTankBodyE100, ResourceManager.gameTankTurretE100),
		KV2 (ResourceManager.gameTankBodyKV2, ResourceManager.gameTankTurretKV2),
		M6 (ResourceManager.gameTankBodyM6, ResourceManager.gameTankTurretM6), 
		PANZER (ResourceManager.gameTankBodyPanzer, ResourceManager.gameTankTurretPanzer),
		T34 (ResourceManager.gameTankBodyT34, ResourceManager.gameTankTurretT34), 
		TIGER2 (ResourceManager.gameTankBodyTiger2, ResourceManager.gameTankTurretTiger2),
		VK3061 (ResourceManager.gameTankBodyVK3061, ResourceManager.gameTankTurretVK3061);
	
	
	private final TextureRegion mTankBody, mTankTurret;
	
	public TextureRegion getTankBodyTR() {return this.mTankBody;}
	public TextureRegion getTankTurretTR() {return this.mTankTurret;}
	
	TankOptions(TextureRegion pTankBody, TextureRegion pTankTurret) {
		this.mTankBody = pTankBody;
		this.mTankTurret = pTankTurret;
	}
	}
}
