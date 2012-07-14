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

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
//This is responsible for the coins(Red , Red static , Blue)
public class Coin {
	AnimatedSprite sprite;
	Body body;
	PhysicsConnector connector;
	public Coin(String type,float X,float Y,boolean stationary)
	{
		FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f,0.1f);
		if(type.compareTo("USER")==0)
			sprite = new AnimatedSprite(X, Y,Constants.user_sprite_texture , Constants.vbom);
		else
			sprite = new AnimatedSprite(X, Y,Constants.comp_sprite_texture , Constants.vbom);
		if(stationary)
		 body=PhysicsFactory.createCircleBody(Constants.physicsWorld, sprite, BodyType.KinematicBody,objectFixtureDef);
		else
		 body=PhysicsFactory.createCircleBody(Constants.physicsWorld, sprite, BodyType.DynamicBody,objectFixtureDef);
		body.setLinearDamping(0.8f);
		body.setAngularDamping(0.9f);
		if (type.compareTo("USER")==0)
		     body.setUserData("USER "+String.valueOf(Constants.coin.size())); 
		else
			 body.setUserData("COMP "+String.valueOf(Constants.coin.size()));
		sprite.animate(500);
		Constants.LevelScreen.attachChild(sprite);
		connector=new PhysicsConnector(sprite, body, true, true);
		Constants.physicsWorld.registerPhysicsConnector(connector);
	}
}
