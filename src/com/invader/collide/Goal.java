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

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
//This class is responsible for the goal
public class Goal {
	Sprite sprite;
	Body body;
	public Goal(float X,float Y)
	{
		FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f,0.1f);
		sprite = new Sprite(X, Y,Constants.goal_sprite_texture , Constants.vbom);
		body=PhysicsFactory.createCircleBody(Constants.physicsWorld, sprite, BodyType.KinematicBody,objectFixtureDef);
		body.setUserData("GOAL");
		Constants.LevelScreen.attachChild(sprite);
		Constants.physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
	}
}
