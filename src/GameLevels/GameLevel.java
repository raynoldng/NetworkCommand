package GameLevels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import GameLevels.Levels.CampDef;
import GameLevels.Levels.EdgeDef;
import GameLevels.Levels.LevelDef;
import GameLevels.Elements.Camp;
import GameLevels.Elements.CampColor;
import GameLevels.Elements.Edge;
import GameLevels.Elements.Player;
import GameLevels.Elements.Player.TankOptions;
import Managers.GameManager;
import Managers.ResourceManager;

public class GameLevel extends ManagedGameScene implements
		IOnSceneTouchListener {

	// ==========================================
	// CONSTANTS
	// ==========================================
	private static final String TAG = "GameLevel";
	// ==========================================
	// VARIABLES
	// ==========================================
	public final LevelDef mLevelDef;

	public volatile List<Camp> mCamps;
	public volatile List<Edge> mEdges;

	public LinkedList<Player> mPlayers;

	// ==========================================
	// UPDATE HANDLERS
	// ==========================================

	// ==========================================
	// OBJECT POOLS
	// ==========================================

	// ==========================================
	// CONSTRUCTOR
	// ==========================================
	public GameLevel(final LevelDef pLevelDef) {
		this.mLevelDef = pLevelDef;

		mCamps = new ArrayList<Camp>();
		mEdges = new ArrayList<Edge>();
	}

	protected void addEdge(EdgeDef curEdge) {
		// helper method needed as we need to reference the two camps at the two
		// ends
		// of edge
		int v = curEdge.v;
		int w = curEdge.w;

		// assert v < mCamps.size() && w < mCamps.size(): "v:" + v + " w:" + w +
		// " nCamps: " + mCamps.size();
		// assert v != w : "Edge must point to two distinct camps!";

		Edge edge = new Edge(mCamps.get(v), mCamps.get(w), this);
		mCamps.get(v).addEdge(edge);
		mCamps.get(w).addEdge(edge);

		mEdges.add(edge);
	}

	@Override
	public void onLoadLevel() {
		GameManager.setGameLevel(this);

		this.addLoadingStep(new LoadingRunnable("Loading game textures", this) {

			@Override
			public void onLoad() {

				// set the zoom factor, dependent on map size and the camera
				// dimension
				SmoothCamera cam = (SmoothCamera) ResourceManager.getCamera();
				cam.setZoomFactorDirect(cam.getCameraSceneWidth() / 800);
				cam.setCenterDirect(400, 240);

				// load camps
				for (final CampDef curCamp : GameLevel.this.mLevelDef.mCamps) {
					mCamps.add(new Camp(curCamp.mX, curCamp.mY, curCamp.mSize,
							curCamp.color, GameLevel.this));
				}

				// add edges
				for (final EdgeDef curEdge : GameLevel.this.mLevelDef.mEdges)
					GameLevel.this.addEdge(curEdge);

			}
		});

		sortChildren(); // so that the camps appear above the lines
						// (edges)
		GameLevel.this.setBackgroundEnabled(true);
		GameLevel.this.setBackground(new Background(Color.WHITE));
		
		this.setOnSceneTouchListener(this);

	}

	@Override
	public void onShowScene() {

		// TEST sending a tank detail

		// set up create players, to be done in a more proper manner
		Player P1 = new Player(1, CampColor.BLUE, TankOptions.E100);
		Debug.i(TAG, "Number of camps:" + mCamps.size());
		Camp test1 = mCamps.get(0);
		test1.setmPlayer(P1);
		test1.sendTankDetail(20, test1.getmIncidentEdges().get(0));

		// END TEST sending a tank detail

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}
}
