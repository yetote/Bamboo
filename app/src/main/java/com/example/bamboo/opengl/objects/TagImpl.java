package com.example.bamboo.opengl.objects;


import android.util.Log;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import static com.example.bamboo.util.CoordinateTransformation.SIDE_TYPE.SIDE_HEIGHT;
import static com.example.bamboo.util.CoordinateTransformation.SIDE_TYPE.SIDE_WIDTH;
import static com.example.bamboo.util.CoordinateTransformation.dpHeight;
import static com.example.bamboo.util.CoordinateTransformation.dpToPX;
import static com.example.bamboo.util.CoordinateTransformation.dpWidth;
import static com.example.bamboo.util.CoordinateTransformation.openGLToAndroid;


/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.opengl.objects
 * @class jBox2d刚体
 * @time 2018/11/13 18:32
 * @change
 * @chang time
 * @class describe
 */
public class TagImpl {

    private static final String TAG = "JBoxImpl";
    private World world;

    private float friction = 0.3f, density = 0.5f, restitution = 0.7f, ratio = 50;
    private SelectTag[] tag;
    private float[] lx, ly;
    private float radius;

    public TagImpl(SelectTag[] tag) {
        this.tag = tag;
        lx = new float[10];
        ly = new float[10];
    }

    public void onDraw() {
        int velocityIterations = 3;
        int positionIterations = 10;
        float dt = 1f / 60f;
        world.step(dt, velocityIterations, positionIterations);
        for (int i = 0; i < 1; i++) {
            Body body = (Body) tag[i].getTag();
            if (body != null) {
                tag[i].setX(metersToPixels(body.getPosition().x - lx[i]));
                lx[i] = body.getPosition().x;
                tag[i].setY(metersToPixels(body.getPosition().y - ly[i]));
                ly[i] = body.getPosition().y;
                tag[i].setCenter(metersToPixels(lx[i]), metersToPixels(ly[i]));
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
        for (int i = 0; i < 1; i++) {
            Body body = (Body) tag[i].getTag();
            if (body == null) {
                createBody(world, i);
            }
        }
    }

    private void createBody(World world, int i) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = (BodyType.DYNAMIC);

        bodyDef.position.set(pixelsToMeters(openGLToAndroid(tag[i].getVertexData()[0], SIDE_WIDTH, dpWidth) - (dpToPX(tag[i].getRadius(), SIDE_WIDTH) / 2)),
                pixelsToMeters(openGLToAndroid(tag[i].getVertexData()[1], SIDE_HEIGHT, dpHeight) - (dpToPX(tag[i].getRadius(), SIDE_WIDTH) / 2)));
        lx[i] = pixelsToMeters(openGLToAndroid(tag[i].getVertexData()[0], SIDE_WIDTH, dpWidth));
        ly[i] = pixelsToMeters(openGLToAndroid(tag[i].getVertexData()[1], SIDE_HEIGHT, dpHeight));

        CircleShape circleShape = new CircleShape();
        circleShape.m_radius = pixelsToMeters(dpToPX(tag[i].getRadius(), SIDE_WIDTH)) / 2;
        radius = circleShape.getRadius();

        FixtureDef fixture = new FixtureDef();
        fixture.shape = (circleShape);
        fixture.friction = friction;
        fixture.restitution = restitution;
        fixture.density = density;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixture);
        tag[i].setTag(body);
        body.setLinearVelocity(new Vec2(lx[i], ly[i]));
    }

    private void createTopAndBottomBounds() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;

        PolygonShape box = new PolygonShape();
        float boxWidth = pixelsToMeters(dpWidth);
        float boxHeight = pixelsToMeters(ratio);
        box.setAsBox(boxWidth, boxHeight);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;

        bodyDef.position.set(0, -boxHeight);
        Body topBody = world.createBody(bodyDef);
        topBody.createFixture(fixtureDef);

        bodyDef.position.set(0, pixelsToMeters(dpHeight) + boxHeight);
        Body bottomBody = world.createBody(bodyDef);
        bottomBody.createFixture(fixtureDef);
    }

    private void createLeftAndRightBounds() {
        float boxWidth = pixelsToMeters(ratio);
        float boxHeight = pixelsToMeters(dpHeight);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;

        PolygonShape box = new PolygonShape();
        box.setAsBox(boxWidth, boxHeight);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;

        bodyDef.position.set(-boxWidth, boxHeight);
        Body leftBody = world.createBody(bodyDef);
        leftBody.createFixture(fixtureDef);

        bodyDef.position.set(pixelsToMeters(dpWidth) + boxWidth, 0);
        Body rightBody = world.createBody(bodyDef);
        rightBody.createFixture(fixtureDef);
    }


    private float metersToPixels(float meters) {
        return meters * ratio;
    }

    private float pixelsToMeters(float pixels) {
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

    /**
     * @param select 点击的标签的id
     * @param scale  缩放倍数
     */
    public void onChanged(int select, float scale) {
        Body body = (Body) tag[select].getTag();
        boolean isSelect = tag[select].getIsSelect();
        Shape shape = body.getFixtureList().getShape();
        if (isSelect) {
            shape.m_radius = radius * scale;
        } else {
            shape.m_radius = radius;
        }
    }
}
