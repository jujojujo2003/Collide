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

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
//THis class is responsible for the vortexes
public class Hole {
	AnimatedSprite sprite;
	Body body;
	public Hole(float X,float Y)
	{
		FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f,0.1f);
		sprite = new AnimatedSprite(X, Y,Constants.hole_sprite_texture , Constants.vbom);
		body=PhysicsFactory.createCircleBody(Constants.physicsWorld, sprite, BodyType.KinematicBody,objectFixtureDef);
		body.setUserData("HOLE");
		sprite.animate(60);
		Constants.LevelScreen.attachChild(sprite);
		Constants.physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
	}
}
