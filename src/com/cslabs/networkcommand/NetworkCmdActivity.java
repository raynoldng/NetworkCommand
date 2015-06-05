package com.cslabs.networkcommand;

import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;
import GameLevels.GameLevel;
import GameLevels.Levels;
import Managers.ResourceManager;
import Managers.SceneManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

/**
 * This activity class builds upon the standard AndEngine BaseGameActivity
 * with the addition of ads, some advanced resolution-scaling performed in
 * the onCreateEngineOptions() method, and shared-preference methods to save
 * and restore options and scores
 * 
 * @author ron
 *
 */

public class NetworkCmdActivity extends BaseGameActivity {
	
	//variables that hold references to the Layout and AdView
	public static FrameLayout mFrameLayout;
	public static AdView adView;
	
	
	//CONSTANTS
	
	//Define a minimum and maximum window resolution (to prevent
	//cramped or overlapping elements)
	static float MIN_WIDTH_PIXELS = 800f, MIN_HEIGHT_PIXELS = 480f;
	static float MAX_WIDTH_PIXELS = 1600f, MAX_HEIGHT_PIXELS = 960f;
	
	//This design device is the Samsung Galaxy Note (SGH-I717)
	//The resolution of the window with which you are developing
	static float DESIGN_WINDOW_WIDTH_PIXELS = 1280f;
	static float DESIGN_WINDOW_HEIGHT_PIXELS = 800f;
	
	//The physical size of the window with which you are developing.
	//calculated by dividing the window's x & y pixels by the x & y DPI.
	static float DESIGN_WINDOW_WIDTH_INCHES = 4.472441f;
	static float DESIGN_WINDOW_HEIGHT_INCHES = 2.805118f;
	
	//VARIABLES
	public float cameraWidth;
	public float cameraHeight;
	public float actualWindowWidthInches;
	public float actualWindowHeightInches;
	public Camera mCamera;
	
	//ENGINE
	
	//create and return a Switchable Fixed-Step Engine
	@Override
	public Engine onCreateEngine(final EngineOptions pEngineOptions) {
		return new FixedStepEngine(pEngineOptions, 50);
	}
	
	//Handle the Back Button
	@Override
	public void onBackPressed() {}
	
	
	/**
	 * Create Engine options
	 */
	@Override
	public EngineOptions onCreateEngineOptions() {
		//Override the onMeasure method of the ResolutionPolicy to set the
		//camera's size. This way usually gives better results compared
		//to the DisplayMetrics.getWidth method because it uses the
		//window instead of the display. This should also be better for if
		//the game is placed in a layout where simply measuring the display
		//would give entirely wrong results
		
		FillResolutionPolicy EngineFillResolutionPolicy = new FillResolutionPolicy() {
			@Override
			public void onMeasure(final IResolutionPolicy.Callback pResolutionPolicyCallback, final int pWidthMeasureSpec, final int pHeightMeasureSpec) {
				super.onMeasure(pResolutionPolicyCallback, pWidthMeasureSpec, pHeightMeasureSpec);
				
				final int measuredWidth = MeasureSpec.getSize(pWidthMeasureSpec);
				final int measuredHeight = MeasureSpec.getSize(pHeightMeasureSpec);
				
				//log the pixel values needed for setting up the design window's values
				Log.v("AndEngine", "Design window width & height (in pixels): " + measuredWidth + ", " + measuredHeight);
				Log.v("AndEngine", "Design window width & height (in inches): " + measuredWidth / getResources().getDisplayMetrics().xdpi + ", " + measuredHeight / getResources().getDisplayMetrics().ydpi);
				
				//determine the device's physical window size
				actualWindowWidthInches = measuredWidth / getResources().getDisplayMetrics().xdpi;
				actualWindowHeightInches = measuredHeight / getResources().getDisplayMetrics().ydpi;
				
				//get an initial width for the camera, and bound it to the minimum or maximum values.
				float actualScaledWidthInPixels = DESIGN_WINDOW_WIDTH_PIXELS * (actualWindowWidthInches / DESIGN_WINDOW_WIDTH_INCHES);
				float boundScaledWidthInPixels = Math.round(Math.max(Math.min(actualScaledWidthInPixels, MAX_WIDTH_PIXELS), MIN_WIDTH_PIXELS));
				
				//get the height for the camera based on the width and the height/width ratio of the device
				float boundScaledHeightInPixels = boundScaledWidthInPixels * (actualWindowHeightInches / actualWindowWidthInches);
				//if the height is outside of the set bounds, scale the width to match it.
				if(boundScaledHeightInPixels > MAX_HEIGHT_PIXELS) {
					float boundAdjustmentRatio = MAX_HEIGHT_PIXELS / boundScaledHeightInPixels;
					boundScaledWidthInPixels *= boundAdjustmentRatio;
					boundScaledHeightInPixels *= boundAdjustmentRatio;
				} else if(boundScaledHeightInPixels < MIN_HEIGHT_PIXELS) {
					float boundAdjustmentRatio = MIN_HEIGHT_PIXELS / boundScaledHeightInPixels;
					boundScaledWidthInPixels *= boundAdjustmentRatio;
					boundScaledHeightInPixels *= boundAdjustmentRatio;
				}
				//set the height and width variables
				cameraHeight = boundScaledHeightInPixels;
				cameraWidth = boundScaledWidthInPixels;
				
				//apply the height and width variables
				mCamera.set(0f, 0f, cameraWidth, cameraHeight);
			}
		};
		
		//create the Camera and EngineOptions
		mCamera = new Camera(0, 0, 320, 240);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, EngineFillResolutionPolicy, mCamera);
		
		//enable sounds
		engineOptions.getAudioOptions().setNeedsSound(true);
		//enable music
		engineOptions.getAudioOptions().setNeedsMusic(true);
		//turn on dithering to smooth texture gradients
		engineOptions.getRenderOptions().setDithering(true);
		//turn on multi sampling to smooth the alias of hard-edged elements
		engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedMultiSampling(true);
		//set the wake lock options to prevent the engine from dumping textures when focus changes
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		
		//return our engineOptions
		return engineOptions;
	}
	
	// ==========================================================
	// ACCESS TO SHARED RESOURCES
	// ==========================================================
	
	//the name of the shared preferences used by this game
	public static final String SHARED_PREFS_MAIN = "MTrakkSettings";
	//the quality (Boolean) setting. True = High Quality
	public static final String SHARED_PREFS_HIGH_QUALITY_GRAPHICS = "quality";
	//how many stars (Integer) the player got in each level
	public static final String SHARED_PREFS_LEVEL_STARS = "level.stars.";
	//The high score (Integer) that the player has gotten for each level
	public static final String SHARED_PREFS_LEVEL_HIGHSCORE = "level.highscore.";
	//the highest level (Integer) reached by the player
	public static final String SHARED_PREFS_LEVEL_MAX_REACHED = "levels.reached.";
	//the number (Integer) of times that the application has been started
	public static final String SHARED_PREFS_ACTIVITY_START_COUNT = "count";
	//the player has rated the game. TRUE = player has agreed to rate it
	public static final String SHARED_PREFS_RATING_SUCCESS = "rating";
	//the muted state of the music. True = muted.
	public static final String SHARED_PREFS_MUSIC_MUTED = "mute.music";
	//the muted state of the sound effects. True = muted.
	public static final String SHARED_PREFS_SOUNDS_MUTED = "mute.sounds";
	
	//the number of times that the application has started, stored as a local value
	public int numTimesActivityOpened;
	
	//methods to read/write Integers in the Shared Preferences.
	public static int writeIntToSharedPreferences(final String pStr, final int pValue) {
		//the apply() method requires API level 9 in the manifest
		ResourceManager.getActivity().getSharedPreferences(SHARED_PREFS_MAIN, 0).edit().putInt(pStr, pValue).apply();
		return pValue;
	}
	
	public static int getIntFromSharedPreferences(final String pStr) {
		return ResourceManager.getActivity().getSharedPreferences(SHARED_PREFS_MAIN, 0).getInt(pStr, 0);
	}
	
	//Methods to write/read Booleans in the Shared Preferences
	public static void writeBooleanToSharedPreferences(final String pStr, final boolean pValue) {
		ResourceManager.getActivity().getSharedPreferences(SHARED_PREFS_MAIN, 0).edit().putBoolean(pStr, pValue).apply();
	}
	
	public static boolean getBooleanFromSharedPreferences(final String pStr) {
		return ResourceManager.getActivity().getSharedPreferences(SHARED_PREFS_MAIN, 0).getBoolean(pStr, false);
	}
	
	//a convenience method for accessing how many stars the player achieved on a certain level
	public static int getLevelStars(final int pLevelNumber) {
		return getIntFromSharedPreferences(SHARED_PREFS_LEVEL_STARS + String.valueOf(pLevelNumber));
	}
	
	// =================================================================================
	// CREATE RESOURCES
	// =================================================================================
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		// setup the ResourceManager
		ResourceManager.setup(this, (FixedStepEngine) this.getEngine(), this.getApplicationContext(), cameraWidth, cameraHeight, cameraWidth / DESIGN_WINDOW_WIDTH_PIXELS, cameraHeight / DESIGN_WINDOW_HEIGHT_PIXELS);
		
		//retrieve and increment the number of times that the application has started
		//numTimesActivityOpened = getIntFromSharedPreferences(SHARED_PREFS_ACTIVITY_START_COUNT) + 1;
		//writeIntToSharedPreferences(SHARED_PREFS_ACTIVITY_START_COUNT, numTimesActivityOpened);
		
		//retrieve the quality level setting
		//final boolean DeviceLimited = getBooleanFromSharedPreferences(SHARED_PREFS_HIGH_QUALITY_GRAPHICS);
		//ResourceManager.getInstance().useHighQuality = DeviceLimited;
		
		//tell the callback that the resources have loaded.
		//we'll be handling the actual loading of resources at specific points throughout the application flow.
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	// ==================================================================================
	// CREATE SCENE
	// ==================================================================================
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		//on every third time that the application is started...
		//if(numTimesActivityOpened % 3 == 0) {}
		
		//load the resources necessary for the Main Menu
		ResourceManager.loadGameResources();
		//tell the SceneManager to show the splash screens
		SceneManager.getInstance().showScene(new GameLevel(Levels.getLevelDef(1)));
		//set the MainMenu to the Engine's scene.
		pOnCreateSceneCallback.onCreateSceneFinished(mEngine.getScene());
		
	}
	
	// ============================================================================
	// POPULATE SCENE
	// ============================================================================
	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) {
		//our SceneManager will handle the population of the scenes, so we do nothing here
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	// ============================================================================
	// LIFECYCLE METHODS
	// ============================================================================
	
	//when the game is already loaded and paused, pause the music
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	//When the game is resumed, tell the system that it should perform a
	//garbage collection to clean up previous applications.
	//If the game is already loaded resume the music
	@Override
	protected void onResume() {
		super.onResume();
		System.gc();
	}
	
	//Some devices do not exit the game when the activity is destroyed.
	//This ensures that the game is closed
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}


}
