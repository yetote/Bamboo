package com.example.bamboo.opengl.objects;


import android.util.Log;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import java.util.Random;

import static com.example.bamboo.opengl.utils.CoordinateTransformation.openGLToAndroidX;
import static com.example.bamboo.opengl.utils.CoordinateTransformation.openGLToAndroidY;

/**
 * Created by kimi on 2017/7/8 0008.
 * Email: 24750@163.com
 */

public class TagImpl {

    private static final String TAG = "JBoxImpl";
    private World world;
    private float dt = 1f / 60f;
    private int velocityIterations = 3;
    private int positionIterations = 10;
    private float friction = 0.3f, density = 0.5f, restitution = 0.7f, ratio = 50;
    private int width = 1080, height = 1800;
    private boolean enable = true;
    private final Random random = new Random();
    private SelectTag[] tag;
    float[] lx, ly;

    public TagImpl(SelectTag[] tag) {
        this.tag = tag;
        density = 0.5f;
        lx = new float[10];
        ly = new float[10];
    }

    public void onSizeChanged(int width, int height) {
        this.width = 1080;
        this.height = 1800;
    }

    public void onDraw() {
        world.step(dt, velocityIterations, positionIterations);
        for (int i = 0; i < 10; i++) {
            Body body = (Body) tag[i].getTag();
            if (body != null) {
                tag[i].setX(metersToPixels(body.getPosition().x - lx[i]));
                lx[i] = body.getPosition().x;
                tag[i].setY(metersToPixels(body.getPosition().y - ly[i]));
                ly[i] = body.getPosition().y;
                Log.e(TAG, "onDraw: x    " + body.getPosition().x +
                                "y:   " + body.getPosition().y
               );
            }
        }
    }

    public void onLayout() {
        createWorld();
    }

    private void createWorld() {
        if (world == null) {
            world = new World(new Vec2(0.0f, 0.0f));
            createTopAndBottomBounds();
            createLeftAndRightBounds();
        }
        for (int i = 0; i < 10; i++) {
            Body body = (Body) tag[i].getTag();
            if (body == null) {
                createBody(world, i);
            }
        }
    }

    private void createBody(World world, int i) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = (BodyType.DYNAMIC);

        bodyDef.position.set(pixelsToMeters(openGLToAndroidX(tag[i].getVertexData()[0], width)-(tag[i].getRadius() / 1080 * 1680) / 2),
                pixelsToMeters(openGLToAndroidY(tag[i].getVertexData()[1], height)-(tag[i].getRadius() / 1080 * 1680) / 2));
        lx[i] = pixelsToMeters(openGLToAndroidX(tag[i].getVertexData()[0], width));
        ly[i] = pixelsToMeters(openGLToAndroidY(tag[i].getVertexData()[1], height));
        CircleShape circleShape = new CircleShape();
//        circleShape.setRadius(pixelsToMeters((tag[i].getRadius() / 1080 * 1680) / 2));
        circleShape.m_radius=3.36f;
        Log.e(TAG, "radius " + circleShape.getRadius());
        FixtureDef fixture = new FixtureDef();
        fixture.shape = (circleShape);
        fixture.friction = friction;
        fixture.restitution = restitution;
        fixture.density = density;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixture);
        tag[i].setTag(body);
        body.setLinearVelocity(new Vec2(random.nextFloat(), random.nextFloat()));
    }

    private void createTopAndBottomBounds() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;

        PolygonShape box = new PolygonShape();
        float boxWidth = pixelsToMeters(width);
        float boxHeight = pixelsToMeters(ratio);
        box.setAsBox(boxWidth, boxHeight);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.7f;

        bodyDef.position.set(0, -boxHeight);
        Body topBody = world.createBody(bodyDef);
        topBody.createFixture(fixtureDef);

        bodyDef.position.set(0, pixelsToMeters(height) + boxHeight);
        Body bottomBody = world.createBody(bodyDef);
        bottomBody.createFixture(fixtureDef);
    }

    private void createLeftAndRightBounds() {
        float boxWidth = pixelsToMeters(ratio);
        float boxHeight = pixelsToMeters(height);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;

        PolygonShape box = new PolygonShape();
        box.setAsBox(boxWidth, boxHeight);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.7f;
        fixtureDef.restitution = 0.5f;

        bodyDef.position.set(-boxWidth, boxHeight);
        Body leftBody = world.createBody(bodyDef);
        leftBody.createFixture(fixtureDef);

        bodyDef.position.set(pixelsToMeters(width) + boxWidth, 0);
        Body rightBody = world.createBody(bodyDef);
        rightBody.createFixture(fixtureDef);
    }


    public float metersToPixels(float meters) {
        return meters * ratio;
    }

    public float pixelsToMeters(float pixels) {
        return pixels / ratio;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        if (density >= 0) {
            this.density = density;
        }
    }
}
