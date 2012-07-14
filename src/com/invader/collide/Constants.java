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

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import android.util.Log;
//All constants
public class Constants {
	public enum SCENE{
		SPLASH,LOAD,LEVEL,MENU,HELP
	}
	public static int CUR_LEVEL=0;
	public static int NO_USER=0;
	public static float DISP_BUTTONS=0;//To display buttons on the levelScreen
	public static Engine mEngine;
	public static int CAMERA_WIDTH = 810;
	public static int CAMERA_HEIGHT = 480;
	public static int Enable_Listener=0;
	public static VertexBufferObjectManager vbom;
	public static TextureManager tm;
	public static Context context;
	public static SceneManager sceneManager;
	public static String toLoad;
	public static int load_inprogress=0;
	public static Scene SplashScreen,LoadScreen,MenuScreen,LevelScreen,HelpScreen;
    public static List<Coin> coin=new ArrayList();
    public static List<Hole> hole=new ArrayList();
    public static List<Wall> wall=new ArrayList();
    public static List<Goal> goal=new ArrayList();
    public static BitmapTextureAtlas commonAtlas,MenuAtlas,LevelAtlas,LevelbgAtlas;
    public static TextureRegion splashScreen,commonBG,playButton,goal_sprite_texture,menu_sprite_texture,play_sprite_texture,pause_sprite_texture,reset_sprite_texture,next_sprite_texture,prev_sprite_texture;
    public static TiledTextureRegion loading,comp_disappear_texture,user_disappear_texture;
    public static TextureRegion background_grass;
    public static PhysicsWorld physicsWorld;
    public static TiledTextureRegion comp_sprite_texture,user_sprite_texture,hole_sprite_texture,wall_sprite_texture;
    public static int paused_game=0;//0 - not paused
    public static Sprite pause,resume,menu,next,prev,reset;
    public static BlockingQueue<Integer> sprite_Queue=new ArrayBlockingQueue<Integer>(100);//Queue to hold list of sprites to remove
    private static Semaphore level_sem=new Semaphore(1);
    public static int getLevel()
    {
       int level=0;
       try{level_sem.acquire();}catch (Exception e){e.printStackTrace();}
       try{
       FileInputStream fIn = null;
       InputStreamReader isr = null;
       try{fIn = context.openFileInput("levelinfo.dat");}catch (Exception e){e.printStackTrace();}
       isr = new InputStreamReader(fIn);
       Scanner in=new Scanner(isr);
       if(in.hasNextInt())
    	   level=in.nextInt();
       in.close();
       isr.close();
       fIn.close();
       }
       catch(Exception e){}
       level_sem.release();
       return level;
    }
    public static void setLevel(int level)
    {
    	int maxlevel=Math.max(CUR_LEVEL,getLevel());
    	try{level_sem.acquire();}catch (Exception e){}
    	try
    	{
    	
    	FileOutputStream fOut = null;
    	OutputStreamWriter osw = null;
    	fOut = context.openFileOutput("levelinfo.dat", Context.MODE_PRIVATE);
    	osw = new OutputStreamWriter(fOut);
    	osw.write(String.valueOf(maxlevel));
    	osw.close();
    	fOut.close();
    	}
    	catch (Exception e){e.printStackTrace();}
    	level_sem.release();
    }
}
