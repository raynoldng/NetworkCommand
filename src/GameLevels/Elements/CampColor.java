package GameLevels.Elements;

public enum CampColor {
	NEUTRAL(0), BLUE(1), RED(2); // more to be added
	
	
	public int mTileIndex;
	CampColor(int pTileIndex) {
		this.mTileIndex = pTileIndex;
	}
}
