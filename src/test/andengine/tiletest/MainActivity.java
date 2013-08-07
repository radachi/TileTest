package test.andengine.tiletest;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.ui.activity.SimpleLayoutGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.debug.Debug;

import test.andengine.tiletest.R;

import android.graphics.Typeface;

public class MainActivity extends SimpleLayoutGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	// 画面サイズを設定
	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 720;
	
	// ===========================================================
	// Fields
	// ===========================================================

	private Font mFont;					// 標準フォントを格納するフィールド
	private TMXTiledMap mTMXTiledMap;	// タイルマップ
	//private BoundCamera mBoundCamera;	// 移動可能カメラ
	
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// カメラクラスに表示サイズを指定
		Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		// ゲームのエンジンを初期化
		EngineOptions eo = new EngineOptions(
				true, 
				ScreenOrientation.PORTRAIT_FIXED, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), 
				camera);
		
		return eo;
	}

	@Override
	protected void onCreateResources() {
		// 標準フォントを用意
		mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		mFont.load();
	}

	@Override
	protected Scene onCreateScene() {
		// シーンを作る
		final Scene scene = new Scene();
		// 背景色を水色に設定（RGB）
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		
		// 文字を表示
		final Text text = new Text(	10, 10, this.mFont, "Hello World !", new TextOptions(HorizontalAlign.RIGHT), getVertexBufferObjectManager());
		scene.attachChild(text);
		
		
		//TMX mapの読み込み
		try{
			final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), this.mEngine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.getVertexBufferObjectManager(), new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {
				
				}
			});
			
			this.mTMXTiledMap = tmxLoader.loadFromAsset("tmx/map01.tmx");
		}catch (final TMXLoadException tmxle) {
			Debug.e(tmxle);
		}
	    
		// レイヤーをSceneに結びつける
		for (int i = 0; i < this.mTMXTiledMap.getTMXLayers().size(); i++){
			TMXLayer layer = this.mTMXTiledMap.getTMXLayers().get(i);
			scene.attachChild(layer);
			layer.setPosition(32, 32);
			layer.setScale(1.5f);
			layer.setAlpha(0.5f);
		}
		

		return scene;
	}

	@Override
	protected int getLayoutID() {
		// ActivityのレイアウトのIDを返す
		return R.layout.activity_main;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		// SceneがセットされるViewのIDを返す
		return R.id.renderview;
	}

}