package Managers;

import org.andengine.audio.sound.Sound;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.color.Color;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.util.FloatMath;

import com.cslabs.networkcommand.NetworkCmdActivity;

/**
 * This ResourceManager adds the ability to use a set of lower-quality textures
 * if desired. It also includes methods for determining an accurate Font texture
 * size to prevent wasting valuable texture memory.
 **/
public class ResourceManager extends Object {

	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final ResourceManager INSTANCE = new ResourceManager();
	private static final TextureOptions mNormalTextureOption = TextureOptions.BILINEAR;
	private static final TextureOptions mStretchableBeamTextureOption = new TextureOptions(
			GLES20.GL_LINEAR, GLES20.GL_LINEAR, GLES20.GL_CLAMP_TO_EDGE,
			GLES20.GL_REPEAT, false);;
	private static final TextureOptions mTransparentTextureOption = TextureOptions.BILINEAR;
	private static final TextureOptions mTransparentRepeatingTextureOption = TextureOptions.REPEATING_BILINEAR;

	// ====================================================
	// VARIABLES
	// ====================================================
	// We include these objects in the resource manager for
	// easy accessibility across our project.
	public FixedStepEngine engine;
	public Context context;
	public NetworkCmdActivity activity;
	public float cameraWidth;
	public float cameraHeight;
	public float cameraScaleFactorX;
	public float cameraScaleFactorY;

	// The resource variables listed should be kept public, allowing us easy
	// access
	// to them when creating new Sprite and Text objects and to play sound
	// files.
	// ======================== Game Resources ================= //
	private BuildableBitmapTextureAtlas gameTexturesAtlas;
	
	public static TextureRegion gameTankBodyE100;
	public static TextureRegion gameTankTurretE100;
	public static TextureRegion gameTankBodyKV2;
	public static TextureRegion gameTankTurretKV2;
	public static TextureRegion gameTankBodyM6;
	public static TextureRegion gameTankTurretM6;
	public static TextureRegion gameTankBodyPanzer;
	public static TextureRegion gameTankTurretPanzer;
	public static TextureRegion gameTankBodyT34;
	public static TextureRegion gameTankTurretT34;
	public static TextureRegion gameTankBodyTiger2;
	public static TextureRegion gameTankTurretTiger2;
	public static TextureRegion gameTankBodyVK3061;
	public static TextureRegion gameTankTurretVK3061;

	// Bases (more to be added, now just the basics)
	public static TiledTextureRegion gameCamps;

	// ======================== Menu Resources ================= //
	/*
	public static TextureRegion menuBackgroundTR;
	public static TextureRegion menuMainTitleTR;
	public static TiledTextureRegion menuMainButtonsTTR;

	public static TextureRegion menuLevelIconTR;
	public static TextureRegion menuLevelLockedTR;
	public static TiledTextureRegion menuLevelStarTTR;

	public static TextureRegion menuArrow1TR;
	public static TextureRegion menuArrow2TR;
	*/

	// =================== Shared Game and Menu Resources ====== //
	
	public static Font fontDefault32Bold;
	public static Font fontDefault72Bold;
	public static Font fontMonospace72Bold;
	public static Font fontDefaultMTrakk48;

	// This variable will be used to revert the TextureFactory's default path
	// when we change it.
	private String mPreviousAssetBasePath = "";

	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public ResourceManager() {
	}

	// ====================================================
	// GETTERS & SETTERS
	// ====================================================
	// Retrieves a global instance of the ResourceManager
	public static ResourceManager getInstance() {
		return INSTANCE;
	}

	// ====================================================
	// PUBLIC METHODS
	// ====================================================
	// Setup the ResourceManager
	public static void setup(NetworkCmdActivity pActivity,
			FixedStepEngine pEngine, Context pContext, float pCameraWidth,
			float pCameraHeight, float pCameraScaleX, float pCameraScaleY) {
		getInstance().activity = pActivity;
		getInstance().engine = pEngine;
		getInstance().context = pContext;
		getInstance().cameraWidth = pCameraWidth;
		getInstance().cameraHeight = pCameraHeight;
		getInstance().cameraScaleFactorX = pCameraScaleX;
		getInstance().cameraScaleFactorY = pCameraScaleY;
	}

	// Loads all game resources.
	public static void loadGameResources() {
		getInstance().loadGameTextures();
		getInstance().loadSharedResources();
		//LevelWonLayer.getInstance().onLoadLayer();
		//LevelPauseLayer.getInstance().onLoadLayer();
		//OptionsLayer.getInstance().onLoadLayer();
	}

	// Loads all menu resources
	public static void loadMenuResources() {
		getInstance().loadMenuTextures();
		getInstance().loadSharedResources();
	}

	public static FixedStepEngine getEngine() {
		return getInstance().engine;
	}

	public static Context getContext() {
		return getInstance().context;
	}

	public static NetworkCmdActivity getActivity() {
		return getInstance().activity;
	}

	public static Camera getCamera() {
		return getInstance().engine.getCamera();
	}

	// ====================================================
	// PRIVATE METHODS
	// ====================================================
	// Loads resources used by both the game scenes and menu scenes
	private void loadSharedResources() {
		loadSharedTextures();
		loadFonts();
	}

	public boolean useHighQuality = true;

	public static boolean isUsingHighQualityGraphics() {
		return INSTANCE.useHighQuality;
	}

	// ============================ LOAD TEXTURES (GAME) ================= //
	private void loadGameTextures() {

		// Store the current asset base path to apply it after we've loaded our
		// textures
		mPreviousAssetBasePath = BitmapTextureAtlasTextureRegionFactory
				.getAssetBasePath();
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		gameTexturesAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 512,
				BitmapTextureFormat.RGBA_8888,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		gameTankBodyE100 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTexturesAtlas, activity.getAssets(),
						"e100_body.png");
		gameTankTurretE100 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTexturesAtlas, activity.getAssets(),
						"e100_turret.png");

		gameTankBodyKV2 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTexturesAtlas, activity.getAssets(),
						"KV2_body.png");
		gameTankTurretKV2 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTexturesAtlas, activity.getAssets(),
						"KV2_turret.png");

		
		gameTankBodyM6 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTexturesAtlas, activity.getAssets(),
						"M6_body.png");
		gameTankTurretM6 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTexturesAtlas, activity.getAssets(),
						"M6_turret.png");

		gameTankBodyPanzer = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTexturesAtlas, activity.getAssets(),
						"panzer_body.png");
		gameTankTurretPanzer = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTexturesAtlas, activity.getAssets(),
						"panzer_turret.png");

		gameTankBodyT34 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTexturesAtlas, activity.getAssets(),
						"t34_body.png");
		gameTankTurretT34 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTexturesAtlas, activity.getAssets(),
						"t34_turret.png");

		gameTankBodyTiger2 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTexturesAtlas, activity.getAssets(),
						"tiger2_body.png");
		gameTankTurretTiger2 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTexturesAtlas, activity.getAssets(),
						"tiger2_turret.png");

		
		
		gameCamps = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTexturesAtlas, activity.getAssets(),
						"bases.png", 3, 1);
		try {
			gameTexturesAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							2, 0, 2));
			gameTexturesAtlas.load();

		} catch (final TextureAtlasBuilderException e) {
			throw new RuntimeException("Error while loading game textures", e);
		}

		// Revert the asset path
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath(mPreviousAssetBasePath);

	}

	// ================================ LOAD TEXTURES (MENU)
	// ============================
	private void loadMenuTextures() {
}

	// ============================== LOAD TEXTURES (SHARED)
	// =============================================
	private void loadSharedTextures() {
}

	// ============================== LOAD FONTS
	// ===========================================
	private void loadFonts() {
		// Create the Font objects via FontFactory class
		if (fontDefault32Bold == null) {
			fontDefault32Bold = FontFactory.create(engine.getFontManager(),
					engine.getTextureManager(), 256, 256,
					Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32f,
					true, Color.WHITE_ARGB_PACKED_INT);
			fontDefault32Bold.load();
		}

		if (fontDefault72Bold == null) {
			fontDefault72Bold = FontFactory.create(engine.getFontManager(),
					engine.getTextureManager(), 512, 512,
					Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 72f,
					true, Color.WHITE_ARGB_PACKED_INT);
			fontDefault72Bold.load();
		}

		if (fontMonospace72Bold == null) {
			fontMonospace72Bold = FontFactory.create(engine.getFontManager(),
					engine.getTextureManager(), 512, 512,
					Typeface.create(Typeface.MONOSPACE, Typeface.BOLD), 72f,
					true, Color.WHITE_ARGB_PACKED_INT);
			fontMonospace72Bold.load();
		}

		if (fontDefaultMTrakk48 == null) {
			fontDefaultMTrakk48 = getFont(Typeface.createFromAsset(
					activity.getAssets(), "fonts/X_SCALE_by_Factor_i.ttf"),
					48f, true);
			fontDefaultMTrakk48.load();
		}
	}

	// the following methods load and return a Font using a minimal amount of
	// texture memory
	private static String DEFAULT_CHARS = "ABCDEFGHIJLKMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890~`!@#$%^&*()-_=+[] {};:'\",<.>?/\\";

	public Font getFont(Typeface pTypeface, float pSize, boolean pAntiAlias) {
		return getFont(pTypeface, pSize, pAntiAlias, DEFAULT_CHARS);
	}

	private static float FONT_TEXTURE_PADDING_RATIO = 1.04f;
	private float FontTextureWidth = 0f;
	private float FontTextureHeight = 0f;
	private int FontTextureRows;

	public Font getFont(Typeface pTypeface, float pSize, boolean pAntiAlias,
			String pCharsToUse) {
		updateTextBounds(pTypeface, pSize, pAntiAlias, pCharsToUse);
		FontTextureWidth = (float) (mTextBounds.width() - mTextBounds.left);
		FontTextureHeight = (float) (mTextBounds.height() - mTextBounds.top);
		FontTextureRows = (int) Math.round(FloatMath.sqrt(FontTextureWidth
				/ FontTextureHeight));
		FontTextureWidth = ((FontTextureWidth / FontTextureRows) * FONT_TEXTURE_PADDING_RATIO);
		FontTextureHeight = ((FontTextureHeight * FontTextureRows) * FONT_TEXTURE_PADDING_RATIO);
		return new Font(engine.getFontManager(), new BitmapTextureAtlas(
				engine.getTextureManager(), (int) FontTextureWidth,
				(int) FontTextureHeight, BitmapTextureFormat.RGBA_8888,
				TextureOptions.DEFAULT), pTypeface, pSize, pAntiAlias,
				Color.WHITE_ARGB_PACKED_INT);
	}

	private Paint mPaint;
	private Rect mTextBounds = new Rect();

	private void updateTextBounds(final Typeface pTypeface, final float pSize,
			final boolean pAntiAlias, final String pCharactersAsString) {
		mPaint = new Paint();
		mPaint.setTypeface(pTypeface);
		mPaint.setTextSize(pSize);
		mPaint.setAntiAlias(pAntiAlias);
		mPaint.getTextBounds(pCharactersAsString, 0,
				pCharactersAsString.length(), this.mTextBounds);
	}

}
