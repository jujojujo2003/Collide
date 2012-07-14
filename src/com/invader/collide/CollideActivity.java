//    Copyright 2012 S.Lakshminarayanan (www.s-ln.in)
//    This file is part of Collide.
//
//    Collide is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Collide is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Collide.  If not, see <http://www.gnu.org/licenses/>.
package com.invader.collide;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.math.MathUtils;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.AccelerationSensorOptions;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;

public class CollideActivity extends BaseGameActivity implements IAccelerationListener{
	private static String Tag="Collide";
	private Camera camera;
	private float Acc_Sensitivity=2;
	Scene scene;
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback){
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	public EngineOptions onCreateEngineOptions()
    {
    	//Detect Screen Resolution
    	final Display display = getWindowManager().getDefaultDisplay();
    	//CAMERA_WIDTH=display.getWidth();
    	//CAMERA_HEIGHT=display.getHeight();
    	camera = new Camera(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
        //engineOptions.getTouchOptions().setTouchEventIntervalMilliseconds(100);
        return engineOptions;
    }
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)throws Exception
    {
    	  BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	  Constants.tm=this.getTextureManager();
    	  Constants.context=this;
    	  //Load&SplashScreens
    	  Constants.commonAtlas=new BitmapTextureAtlas(this.getTextureManager(),1037,1024);
    	  Constants.splashScreen=(TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(Constants.commonAtlas, this, "splash.png",0,0);
    	  Constants.commonBG=(TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(Constants.commonAtlas, this, "background.png",0,480);
    	  Constants.loading= (TiledTextureRegion) BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Constants.commonAtlas, this, "loading.png",810,0,1,2);
    	  Constants.commonAtlas.load();
    	  Constants.mEngine=this.mEngine;  
    	  Constants.CUR_LEVEL=Constants.getLevel();
          pOnCreateResourcesCallback.onCreateResourcesFinished();     
    }
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
    {
    	
    	this.mEngine.registerUpdateHandler(new FPSLogger());
        this.enableAccelerationSensor(this);
        //Create physics world
        Constants.sceneManager=new SceneManager(this.mEngine,this.getVertexBufferObjectManager());
        this.mEngine.registerUpdateHandler(Constants.sceneManager);
        pOnCreateSceneCallback.onCreateSceneFinished(Constants.sceneManager.defaultStartScreen());
    }
    
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
	}
	
    public void onAccelerationChanged(AccelerationData pAccelerationData) {
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX()*Acc_Sensitivity, pAccelerationData.getY()*Acc_Sensitivity);
		if(Constants.physicsWorld!=null)
		 Constants.physicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);		
	}
    @Override
    public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {    
    	Constants.DISP_BUTTONS=3;
    	return true;
    }
    @Override public void onGameCreated()
    {
    super.onGameCreated();
    }
    
}