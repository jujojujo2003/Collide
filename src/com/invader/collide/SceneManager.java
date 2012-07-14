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

import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

import com.invader.collide.Constants.SCENE;

public class SceneManager implements IUpdateHandler,IOnSceneTouchListener  {
	Engine mEngine;
	SCENE cur_scene;
	SCENE needed_scene;
	float intime=0;
	public SceneManager(Engine engine,VertexBufferObjectManager vbom)
	{
		Constants.vbom=vbom;
		mEngine=engine;
		cur_scene=Constants.SCENE.SPLASH;
		needed_scene=Constants.SCENE.SPLASH;
		//Splash Screen
		Constants.SplashScreen=new Scene();
	    Sprite sprite=new Sprite(0, 0,Constants.splashScreen,vbom);
	    Constants.SplashScreen.attachChild(sprite);
	    //Load Scene
	    Constants.LoadScreen=new Scene();
	    Sprite sprite2=new Sprite(0, 0,Constants.commonBG,vbom);
	    Constants.LoadScreen.attachChild(sprite2);
	    AnimatedSprite sprite3=new AnimatedSprite(290,260,Constants.loading,vbom);
	    sprite3.animate(100);
	    Constants.LoadScreen.attachChild(sprite3);
	}
    public Scene defaultStartScreen()
    {
        Constants.sceneManager.intime=3;
        Constants.toLoad="MENU";
        //Constants.toLoad="LEVEL";
    	Constants.sceneManager.needed_scene=Constants.SCENE.LOAD;
        return Constants.SplashScreen;
    }
    
    public void startLoader()
    {
       needed_scene=Constants.SCENE.LOAD;
    }
    class SpriteRemover implements Runnable
    {   //This class is to create a runnable that removes sprite so that u can run it on updateThread()
        Object obj;
        SpriteRemover(Object o)
        {obj=o;}
		public void run() {
			((Sprite)obj).detachSelf();
		}
    	
    }
	public void onUpdate(float pSecondsElapsed) {
		if(Constants.sceneManager.cur_scene==Constants.SCENE.LEVEL)
		{
			if(Constants.DISP_BUTTONS>0)
			{
				if(Constants.CUR_LEVEL!=0)
					Constants.prev.setVisible(true);
				if(Constants.CUR_LEVEL!=Constants.getLevel())
					Constants.next.setVisible(true);
				Constants.menu.setVisible(true);
				if(Constants.paused_game==0)
					Constants.pause.setVisible(true);
				else
					Constants.resume.setVisible(true);
				Constants.reset.setVisible(true);
				Constants.DISP_BUTTONS-=pSecondsElapsed;
			}
			else
			{
				Constants.reset.setVisible(false);
				Constants.resume.setVisible(false);
				Constants.menu.setVisible(false);
				Constants.pause.setVisible(false);
				Constants.prev.setVisible(false);
				Constants.next.setVisible(false);
			}
		}
		while(!Constants.sprite_Queue.isEmpty())
		{
			//The queue that contains coins to be removed from scene
			int toremove=Constants.sprite_Queue.remove();
			if(Constants.coin.get(toremove).body!=null)
			{
				Constants.physicsWorld.destroyBody(Constants.coin.get(toremove).body);
			    AnimatedSprite anim;
			    if(Constants.coin.get(toremove).body.getUserData().toString().contains("COMP"))
			     {//Enemy coin
			    	anim=new AnimatedSprite(Constants.coin.get(toremove).sprite.getX()-12,Constants.coin.get(toremove).sprite.getY()-12,Constants.comp_disappear_texture,Constants.vbom);
			    	   anim.animate(40, false, new IAnimationListener () {//For the animated fadeout
						public void onAnimationStarted(AnimatedSprite pAnimatedSprite,int pInitialLoopCount) {}
						public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex,int pNewFrameIndex) {}
						public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,int pRemainingLoopCount, int pInitialLoopCount) {}
						public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
					       Constants.mEngine.runOnUpdateThread(new SpriteRemover(pAnimatedSprite));
						}});
	            
			     }
			    else
			     {//User Coin
			    	anim=new AnimatedSprite(Constants.coin.get(toremove).sprite.getX()-12,Constants.coin.get(toremove).sprite.getY()-12,Constants.user_disappear_texture,Constants.vbom);
			    	   anim.animate(40, false, new IAnimationListener () {

						public void onAnimationStarted(AnimatedSprite pAnimatedSprite,int pInitialLoopCount) {}
						public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,int pOldFrameIndex, int pNewFrameIndex) {}
						public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,int pRemainingLoopCount, int pInitialLoopCount) {}
						public void onAnimationFinished(
							   AnimatedSprite pAnimatedSprite) {
							Constants.mEngine.runOnUpdateThread(new SpriteRemover(pAnimatedSprite));
							 
							Constants.NO_USER--;
							if(Constants.NO_USER<=0)
			            	   {
			            		   Constants.CUR_LEVEL++;
			            		   Constants.setLevel(Constants.CUR_LEVEL);
			            		   Constants.toLoad="LEVEL";
			            		   Constants.sceneManager.needed_scene=Constants.SCENE.LOAD;
			            	   }
							
						}//For the animated fadeout
			   
			    	   });
	            
			     }
			    anim.setRotation(Constants.coin.get(toremove).sprite.getRotation());
			    Constants.LevelScreen.attachChild(anim);
			    Constants.coin.get(toremove).sprite.detachSelf();
			    Constants.coin.get(toremove).body=null;
			}
		}
		if(cur_scene==Constants.SCENE.LOAD && Constants.load_inprogress==0)
		{
			Log.e("STARTING LOAD WID ",Constants.toLoad);
			//Loading SCreen Playing and loading is not inprogress , so trigger loading
			  //Before that clear vars
			Constants.load_inprogress=1;
			if(Constants.LevelScreen!=null && Constants.physicsWorld!=null)
			{
			 Constants.mEngine.unregisterUpdateHandler(Constants.physicsWorld);
			 Constants.LevelScreen.clearUpdateHandlers();
			 Constants.physicsWorld.clearPhysicsConnectors();
			 Constants.physicsWorld.clearForces();
			 Constants.physicsWorld.reset();
			 Constants.physicsWorld.setContactListener(null);
			 Constants.physicsWorld.dispose();
			 Constants.physicsWorld=null;
			 Constants.LevelScreen.dispose();
			}
			((Activity)Constants.context).runOnUiThread(new Runnable() {//BUGFIX  TO Ensure that Async task runs on UI thread
                public void run(){
                        new AsyncTaskLoader().execute(new Loader());
                }
        });
	//		.runOnUiThread((new AsyncTaskLoader()).execute(new Loader()));
		}
		else if(cur_scene==Constants.SCENE.LOAD &&  Constants.load_inprogress==1)
		{
			//Loading Screen Playing and loading in progress , so just continue
			return;
		}
		else if(cur_scene==Constants.SCENE.LOAD &&  Constants.load_inprogress==2)
		{
			//LoadingScreen Playing and loading is complete, so set screen
			 Constants.load_inprogress=3;
			 if(Constants.toLoad.compareTo("MENU")==0)
				    needed_scene=Constants.SCENE.MENU;
			 else if(Constants.toLoad.compareTo("LEVEL")==0)
				    needed_scene=Constants.SCENE.LEVEL;
			 else if(Constants.toLoad.compareTo("HELP")==0)
				    needed_scene=Constants.SCENE.HELP;
			 
		}
		if(intime>0)
		{
			//Delay time hasnt yet expired
			intime-=pSecondsElapsed;
		}
		else
		if(needed_scene!=cur_scene)
		{
			if(Constants.load_inprogress==3)
				Constants.load_inprogress=0;
		   intime=0;
		   //Do all things b4 changing screen
		   if(cur_scene==Constants.SCENE.SPLASH)
			   intime=1;
	       	   
		   
		   //Change screen
		   if(needed_scene==Constants.SCENE.LOAD)
			   mEngine.setScene(Constants.LoadScreen);
		   else if (needed_scene==Constants.SCENE.MENU)
			   {
			     mEngine.setScene(Constants.MenuScreen);
			     Constants.MenuScreen.setOnSceneTouchListener(this);
			   }
		   else if(needed_scene==Constants.SCENE.LEVEL)
			     {
			        mEngine.setScene(Constants.LevelScreen);
			        Constants.mEngine.registerUpdateHandler(Constants.physicsWorld);
			        Constants.LevelScreen.setOnSceneTouchListener(this);
			     }
		   else if(needed_scene==Constants.SCENE.HELP)
		   {
			    mEngine.setScene(Constants.HelpScreen);
			    Constants.HelpScreen.setOnSceneTouchListener(this);
		   }
		   cur_scene=needed_scene;
		}
	}
	public void reset() {
	}
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		//HERES where all TOUCH EVENTS COME IRRESPECTIVE OF SCENE
		if(pSceneTouchEvent.getAction()!=MotionEvent.ACTION_DOWN)
			return false;
		if(Constants.DISP_BUTTONS<=0)
			Constants.DISP_BUTTONS=3;
        float X=pSceneTouchEvent.getX();
        float Y=pSceneTouchEvent.getY();
		if(cur_scene==Constants.SCENE.MENU)
		{	
			if(Math.sqrt((376-X)*(376-X)+(291-Y)*(291-Y))<=100)
        	{
                Constants.toLoad="LEVEL";
                Constants.sceneManager.needed_scene=Constants.SCENE.LOAD;
        	}
			if(Math.sqrt((Constants.CAMERA_WIDTH-24-X)*(Constants.CAMERA_WIDTH-24-X)+(Constants.CAMERA_HEIGHT-24-Y)*(Constants.CAMERA_HEIGHT-24-Y))<=70)
			{
				System.exit(0);
			}
			if(Math.sqrt((24-X)*(24-X)+(Constants.CAMERA_HEIGHT-24-Y)*(Constants.CAMERA_HEIGHT-24-Y))<=70)
			{
				Constants.toLoad="HELP";
				Constants.sceneManager.needed_scene=Constants.SCENE.LOAD;
			}
			if(Math.sqrt((405-27-X)*(405-27-X)+(Constants.CAMERA_HEIGHT-27-Y)*(Constants.CAMERA_HEIGHT-27-Y))<=70)
			{
				 //Code Removed
			}
			if(Math.sqrt((5+24-X)*(5+24-X)+(5+24-Y)*(5+24-Y))<=70)
			{
				Log.e("LAUNCHING","FB");
				//Code Removed
			}
		}
		else if(cur_scene==Constants.SCENE.LEVEL)
		{
		  if(Constants.DISP_BUTTONS>0)
			{
			  if(Math.sqrt((Constants.CAMERA_WIDTH-24-X)*(Constants.CAMERA_WIDTH-24-X)+(24-Y)*(24-Y))<=60)
			  {
				//GAME PAUSE AND PLAY
				if(Constants.paused_game==0)
				{
					Constants.pause.setVisible(false);
					Constants.resume.setVisible(true);
					Constants.mEngine.unregisterUpdateHandler(Constants.physicsWorld);
					Constants.paused_game=1;
				}
				else
				{
					Constants.resume.setVisible(false);
					Constants.pause.setVisible(true);
					Constants.mEngine.registerUpdateHandler(Constants.physicsWorld);
					Constants.paused_game=0;
				}
			  }  
			  if(Math.sqrt((24-X)*(24-X)+(24-Y)*(24-Y))<=60)
			  {
				Constants.toLoad="MENU";
				Constants.sceneManager.needed_scene=Constants.SCENE.LOAD;
			  }
			  if(Math.sqrt((243-X)*(243-X)+(24-Y)*(24-Y))<=60)
				  if(Constants.prev.isVisible())
			      {
				     Constants.CUR_LEVEL=Math.max(Constants.CUR_LEVEL-1, 0);
				     Constants.toLoad="LEVEL";
				     Constants.sceneManager.needed_scene=Constants.SCENE.LOAD;
			      }
			  if(Math.sqrt((364-X)*(364-X)+(24-Y)*(24-Y))<=60)
			     {
				     Constants.toLoad="LEVEL";
				     Constants.sceneManager.needed_scene=Constants.SCENE.LOAD;
			     }
			  if(Math.sqrt((567-X)*(567-X)+(24-Y)*(24-Y))<=60)
			    if(Constants.next.isVisible())
			    {
			    	Constants.CUR_LEVEL=Math.min(Constants.CUR_LEVEL+1, Constants.getLevel());
			    	Constants.toLoad="LEVEL";
			    	Constants.sceneManager.needed_scene=Constants.SCENE.LOAD;
			    }
			}
		}
		else if(cur_scene==Constants.SCENE.HELP)
		{
			if(Math.sqrt((45-X)*(45-X)+(Constants.CAMERA_HEIGHT-24-Y)*(Constants.CAMERA_HEIGHT-24-Y))<=80)
			{
				Constants.toLoad="MENU";
				Constants.sceneManager.needed_scene=Constants.SCENE.LOAD;
			}
		}
		return false;
	}
}
