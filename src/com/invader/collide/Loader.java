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

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Loader implements IAsyncCallback {
    Semaphore loader_sem=new Semaphore(1);
	void menuLoader()
	{       
		    //Create and load sprites for menu scene
		    //Loads all sprite and creates scene for Main Menu
			Constants.MenuAtlas=new BitmapTextureAtlas(Constants.tm,336,58);
			TextureRegion playbut_text=(TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(Constants.MenuAtlas, Constants.context, "Play.png",0,0);
			TextureRegion exitbut_text=(TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(Constants.MenuAtlas, Constants.context, "exit_but.png",113,0);
			TextureRegion help_but_texture=(TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(Constants.MenuAtlas, Constants.context, "help_but.png",167,0);
			TextureRegion www_texture=(TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(Constants.MenuAtlas, Constants.context, "link.png",227,0);
			TextureRegion fb_texture=(TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(Constants.MenuAtlas, Constants.context, "fb.png",287,0);
			Constants.MenuAtlas.load();
			Sprite playbut=new Sprite(348.5f,266,playbut_text,Constants.vbom);
			Constants.MenuScreen=new Scene();
		    Sprite bg=new Sprite(0, 0,Constants.commonBG,Constants.vbom);
		    Sprite exit=new Sprite(Constants.CAMERA_WIDTH-54,Constants.CAMERA_HEIGHT-54,exitbut_text,Constants.vbom);
		    Sprite help=new Sprite(24,Constants.CAMERA_HEIGHT-54,help_but_texture,Constants.vbom);
		    Sprite www=new Sprite(405-24,Constants.CAMERA_HEIGHT-54,www_texture,Constants.vbom);
		    Sprite fb=new Sprite(5,5,fb_texture,Constants.vbom);
		    Constants.MenuScreen.attachChild(bg);
		    Constants.MenuScreen.attachChild(playbut);
	        Constants.MenuScreen.attachChild(exit);
	        Constants.MenuScreen.attachChild(help);
	        //Constants.MenuScreen.attachChild(www);
	        //Constants.MenuScreen.attachChild(fb);
	   	Constants.load_inprogress=2;
	}
    void levelLoader()
    {
    	//Create and load sprites for level
    	//Dispose Previously used global vars
    	Constants.paused_game=0;
    	Constants.hole.clear();
    	Constants.goal.clear();
    	Constants.coin.clear();
    	Constants.wall.clear();
    	
    	//STart initialisation
    	Constants.LevelScreen=new Scene();
    	Constants.LevelbgAtlas=new BitmapTextureAtlas(Constants.tm, 256, 256,TextureOptions.REPEATING_BILINEAR);
    	Constants.background_grass=(TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(Constants.LevelbgAtlas,Constants.context, "ground.png", 0, 0);
    	Constants.background_grass.setTextureHeight(Constants.CAMERA_HEIGHT);
        Constants.background_grass.setTextureWidth(Constants.CAMERA_WIDTH);
        Constants.LevelbgAtlas.load();
        
        Constants.LevelAtlas=new BitmapTextureAtlas(Constants.tm,650,165);
        Constants.comp_sprite_texture = (TiledTextureRegion) BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Constants.LevelAtlas, Constants.context, "comp_striker.png",0,27,2,1);    //96*48
        Constants.user_sprite_texture = (TiledTextureRegion) BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Constants.LevelAtlas, Constants.context, "user_striker.png",54,27,2,1);   //96x48
        Constants.hole_sprite_texture = (TiledTextureRegion) BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Constants.LevelAtlas, Constants.context, "hole.png",0,0,8,1);          //750x50
        Constants.wall_sprite_texture = (TiledTextureRegion) BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Constants.LevelAtlas, Constants.context, "wall.png",108,27,1,1);
        Constants.goal_sprite_texture = (TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(Constants.LevelAtlas,Constants.context, "goal.png", 135, 27);
        Constants.menu_sprite_texture = (TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(Constants.LevelAtlas,Constants.context, "menu_but.png", 216, 0);
        Constants.pause_sprite_texture = (TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(Constants.LevelAtlas,Constants.context, "pause_but.png",270, 0);
        Constants.play_sprite_texture = (TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(Constants.LevelAtlas,Constants.context, "play_but.png", 324, 0);
        Constants.reset_sprite_texture = (TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(Constants.LevelAtlas,Constants.context, "reset.png", 459, 0);
        Constants.next_sprite_texture = (TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(Constants.LevelAtlas,Constants.context, "next_but.png", 514, 0);
        Constants.prev_sprite_texture = (TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(Constants.LevelAtlas,Constants.context, "prev_but.png", 395, 0);
        Constants.comp_disappear_texture=(TiledTextureRegion) BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Constants.LevelAtlas, Constants.context, "comp_disappear.png",0,108,12,1);
        Constants.user_disappear_texture=(TiledTextureRegion) BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Constants.LevelAtlas, Constants.context, "user_disappear.png",0,54,12,1);
        Constants.LevelAtlas.load();
        
        SpriteBackground background_sprite=new SpriteBackground(new Sprite(0,0,Constants.background_grass,Constants.vbom));
        Constants.LevelScreen.setBackground(background_sprite);
        Constants.physicsWorld=new PhysicsWorld(new Vector2(0,0), false);;
        Constants.physicsWorld.setContactListener(contactListener());
        Constants.Enable_Listener=1;
        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
        final Rectangle ground = new Rectangle(-2, Constants.CAMERA_HEIGHT, Constants.CAMERA_WIDTH+4, 2,Constants.vbom);
        final Rectangle roof = new Rectangle(-2, -2, Constants.CAMERA_WIDTH+4, 2,Constants.vbom);
        final Rectangle left = new Rectangle(-2, -2, 2, Constants.CAMERA_HEIGHT+4,Constants.vbom);
        final Rectangle right = new Rectangle(Constants.CAMERA_WIDTH , -2, 2,Constants.CAMERA_HEIGHT+4,Constants.vbom);
        Body b_ground=PhysicsFactory.createBoxBody(Constants.physicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
        Body b_roof=PhysicsFactory.createBoxBody(Constants.physicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
        Body b_left=PhysicsFactory.createBoxBody(Constants.physicsWorld, left, BodyType.StaticBody, wallFixtureDef);
        Body b_right=PhysicsFactory.createBoxBody(Constants.physicsWorld, right, BodyType.StaticBody, wallFixtureDef);
        b_ground.setUserData("GROUNDWALL");
        b_roof.setUserData("ROOFWALL");
        b_left.setUserData("LEFTWALL");
        b_right.setUserData("RIGHTWALL");
        Constants.LevelScreen.attachChild(ground);
        Constants.LevelScreen.attachChild(roof);
        Constants.LevelScreen.attachChild(left);
        Constants.LevelScreen.attachChild(right);
        Constants.menu=new Sprite(0,0,Constants.menu_sprite_texture,Constants.vbom);
        Constants.resume=new Sprite(Constants.CAMERA_WIDTH-54,0,Constants.play_sprite_texture,Constants.vbom);
        Constants.pause=new Sprite(Constants.CAMERA_WIDTH-54,0,Constants.pause_sprite_texture,Constants.vbom);
        Constants.reset=new Sprite(381,0,Constants.reset_sprite_texture,Constants.vbom);
        Constants.next = new Sprite(543,0,Constants.next_sprite_texture,Constants.vbom);
        Constants.prev=new Sprite(219,0,Constants.prev_sprite_texture,Constants.vbom);
        Constants.pause.setAlpha(0.7f);
        Constants.menu.setAlpha(0.7f);
        Constants.resume.setAlpha(0.7f);
        Constants.reset.setAlpha(0.7f);
        Constants.next.setAlpha(0.7f);
        Constants.prev.setAlpha(0.7f);
        Constants.NO_USER=0;
        //Load Level
        try {
        	Scanner in;
        	Log.e("LOADING",String.valueOf(Constants.CUR_LEVEL));
            try{
			  in = new Scanner (new InputStreamReader(Constants.context.getAssets().open("level/"+Constants.CUR_LEVEL)));
            }catch (Exception e)
        	  {in = new Scanner  (new InputStreamReader(Constants.context.getAssets().open("level/finallevel")));}
			for(int i=0;i<20;i++)
				for(int j=0;j<30;j++)
				{
					String inp=in.next();
					if(inp.compareTo("enemy")==0)
						Constants.coin.add(new Coin("COMP",j*27,24*i,false));
					else if(inp.compareTo("user")==0)
						{Constants.coin.add(new Coin("USER",j*27,24*i,false));Constants.NO_USER++;}
					else if(inp.compareTo("wall")==0)
						Constants.wall.add(new Wall(j*27,24*i));
					else if(inp.compareTo("hole")==0)
						Constants.hole.add(new Hole(j*27,i*24));
					else if(inp.compareTo("goal")==0)
						Constants.goal.add(new Goal(j*27,i*24));
					else if(inp.compareTo("stat")==0)
						Constants.coin.add(new Coin("COMP",j*27,24*i,true));
						
			//Log.e("READ",inp+" "+i+" "+j);		
				}
			
		} catch (IOException e1) {
			e1.printStackTrace();
			
		}
        Constants.LevelScreen.attachChild(Constants.menu);
        Constants.LevelScreen.attachChild(Constants.pause);
        Constants.LevelScreen.attachChild(Constants.prev);
        Constants.LevelScreen.attachChild(Constants.next);
        Constants.LevelScreen.attachChild(Constants.reset);
        Constants.resume.setVisible(false);
        if(Constants.CUR_LEVEL==Constants.getLevel())
         Constants.next.setVisible(false);
        if(Constants.CUR_LEVEL==0)
         Constants.prev.setVisible(false);
        Constants.LevelScreen.attachChild(Constants.resume);
    	Constants.load_inprogress=2;
       }
    public void helploader()
    {
    	//Load scene and sprites for help scene
    	BitmapTextureAtlas atlas=new BitmapTextureAtlas(Constants.tm,820,490);
		TextureRegion help_text=(TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, Constants.context, "help.png",0,0);
		atlas.load();
		Sprite help=new Sprite(0,0,help_text,Constants.vbom);
		Constants.HelpScreen=new Scene();
	    Sprite bg=new Sprite(0, 0,help_text,Constants.vbom);
    	Constants.HelpScreen.attachChild(help);
    	Constants.load_inprogress=2;
    }
	public void workToDo() {
		//Load the respective scene
		try {loader_sem.acquire();} catch (InterruptedException e) {e.printStackTrace();Log.e("ERROR","Unable to Lock loader_sem");}
	    if(Constants.toLoad.compareTo("MENU")==0)
	           menuLoader();
	    else if(Constants.toLoad.compareTo("LEVEL")==0)
	           levelLoader();
	    else if(Constants.toLoad.compareTo("HELP")==0)
	    	   helploader();
	    loader_sem.release();
	}

	public void onComplete() {
		// TODO Auto-generated method stub
		
	}
	 private ContactListener contactListener()
	    {
		        //On collision check what collided and do the respective
	            ContactListener contactListener = new ContactListener()
	            {
	            	    Boolean Collided(Contact contact,Object str_a,Object str_b)
	            	    {
	            	    	String A=(String) contact.getFixtureA().getBody().getUserData();
						    String B=(String) contact.getFixtureB().getBody().getUserData();
						    if((A.contains(str_a.toString())&&B.contains(str_b.toString()))||(A.contains(str_b.toString())&&B.contains(str_a.toString())))
						        return true;
						    else
						    	return false;	    
	            	    }
						public void beginContact(Contact contact) {
						
							if(Constants.Enable_Listener==0)
									return;
						
							if(Collided(contact,"HOLE","USER")||Collided(contact,"USER","COMP"))
							{
								Constants.Enable_Listener=0;
								Vibrator v = (Vibrator) Constants.context.getSystemService(Context.VIBRATOR_SERVICE);
								long vib[]={50,100,50,100,50,100};
								v.vibrate(vib, -1);
								Constants.toLoad="LEVEL";
								Constants.sceneManager.needed_scene=Constants.SCENE.LOAD;
							}
							else if(Collided(contact,"COMP","HOLE"))
							{
								int toremove;
								if(contact.getFixtureA().getBody().getUserData().toString().contains("COMP"))
									toremove=Integer.valueOf(contact.getFixtureA().getBody().getUserData().toString().replaceAll("[^0-9]",""));
								else
									toremove=Integer.valueOf(contact.getFixtureB().getBody().getUserData().toString().replaceAll("[^0-9]",""));
								Constants.sprite_Queue.add(toremove);
							}
							else if(Collided(contact,"USER","GOAL"))
							{
								int toremove;
								if(contact.getFixtureA().getBody().getUserData().toString().contains("USER"))
									toremove=Integer.valueOf(contact.getFixtureA().getBody().getUserData().toString().replaceAll("[^0-9]",""));
								else
									toremove=Integer.valueOf(contact.getFixtureB().getBody().getUserData().toString().replaceAll("[^0-9]",""));
								Constants.sprite_Queue.add(toremove);
							}
														
						}

						public void endContact(Contact contact) {
							
						}

						public void preSolve(Contact contact, Manifold oldManifold) {
							
						}

						public void postSolve(Contact contact,
								ContactImpulse impulse) {
							
						}
	            };
	            return contactListener;
	    }
}
