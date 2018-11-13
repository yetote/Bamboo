package com.example.bamboo.opengl.objects;

import android.view.View;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import java.util.Random;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.opengl.objects
 * @class 使用jBox2d实现的标签接口
 * @time 2018/11/13 16:49
 * @change
 * @chang time
 * @class describe
 */
public class TagImpl {
    private World world;
    /**
     * 密度
     */
    private int density;
    /**
     * 摩擦力
     */
    private float friction = 0.5f;
    /**
     * 弹力系数
     */
    private float restitution = 0.5f;
    private int ratio = 10;
    private int width, height;
    Random random = new Random();

    void createWorld() {
        //设定重力方向与重力加速度
        world = new World(new Vec2(0, 100f));
        createVerticalRounds();
        createHorizontalRounds();
    }

    void onSizeChange(int w, int h) {
        this.width = w;
        this.height = h;
    }

    void startWorld() {
        float dt = 1f / 60f;
        //计算速度时间
        int velocityIterations = 3;
        //计算位置时间
        int positionIterations = 10;
        world.step(dt, velocityIterations, positionIterations);
    }

    void createBody(int radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(trueToImitate(radius));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setLinearVelocity(new Vec2(random.nextFloat(), random.nextFloat()));

    }

    /**
     * 创建水平方向的边界(top&bottom)
     */
    private void createHorizontalRounds() {
        float boxWight = trueToImitate(width);
        float boxHeight = trueToImitate(ratio);

        //创建刚体描述
        BodyDef bodyDef = new BodyDef();
        //刚体为静态
        bodyDef.type = BodyType.STATIC;

        //屏幕左侧边界的刚体，为多边形刚体
        PolygonShape box = new PolygonShape();
        //将多边形设置为矩形
        box.setAsBox(boxWight, boxHeight);

        //增加物理属性
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution / 2;

        //描述刚体摆放位置为上方
        bodyDef.position.set(0, -boxHeight);
        Body topBody = world.createBody(bodyDef);
        topBody.createFixture(fixtureDef);

        //描述刚体摆放位置为下方
        bodyDef.position.set(0, trueToImitate(height) + boxHeight);
        Body bottomBody = world.createBody(bodyDef);
        bottomBody.createFixture(fixtureDef);

    }

    /**
     * 创建垂直方向的边界(left&right)
     */
    private void createVerticalRounds() {
        //真实转模拟
        float boxWight = trueToImitate(ratio);
        float boxHeight = trueToImitate(height);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(boxWight, boxHeight);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.restitution = restitution / 2;
        fixtureDef.density = density;

        //描述刚体摆放位置为左侧
        bodyDef.position.set(-boxWight, boxHeight);
        Body leftBody = world.createBody(bodyDef);
        leftBody.createFixture(fixtureDef);

        //描述刚体摆放位置为右侧
        bodyDef.position.set(trueToImitate(width) + boxWight, 0);
        Body rightBody = world.createBody(bodyDef);
        rightBody.createFixture(fixtureDef);
    }

    /**
     * 模拟世界转化为真实世界
     *
     * @param imitate 模拟世界数据
     * @return 真实世界数据
     */
    private float imitateToTrue(float imitate) {
        return imitate * ratio;
    }

    /**
     * 真实世界转化模拟世界
     *
     * @param reality 真实世界数据
     * @return 模拟世界数据
     */
    private float trueToImitate(float reality) {
        return reality / ratio;
    }
}
