package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Ball2 extends MovableGameObject {

    public Ball2(float x, float y, float size, float xSpeed, float ySpeed, Texture texture) {
        super(texture, x, y, (float)Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed));
        sprite.setSize(size, size);
        this.xVel = xSpeed;
        this.yVel = ySpeed;
    }

    @Override
    protected void updateMovement(float deltaTime) {
        if (!isPaused && !isDestroyed) {
            sprite.translate(xVel, yVel);
        }
    }

    @Override
    protected void checkBounds() {
        // Rebote en los bordes de la pantalla
        if (sprite.getX() <= 0 || sprite.getX() + sprite.getWidth() >= Gdx.graphics.getWidth()-40) {
            xVel = -xVel;
        }
        if (sprite.getY() <= 0 || sprite.getY() + sprite.getHeight() >= Gdx.graphics.getHeight()) {
            yVel = -yVel;
        }
    }

    public void checkCollision(Ball2 other) {
        if (super.checkCollision(other)) {
            // Intercambiar velocidades para simular rebote
            float tempXVel = xVel;
            float tempYVel = yVel;
            xVel = other.xVel;
            yVel = other.yVel;
            other.xVel = tempXVel;
            other.yVel = tempYVel;
        }
    }

    public void setXSpeed(float xSpeed) {
        this.xVel = xSpeed;
    }

    public void setySpeed(float ySpeed) {
        this.yVel = ySpeed;
    }

    public float getXSpeed() {
        return xVel;
    }

    public float getySpeed() {
        return yVel;
    }

    public Rectangle getArea() {
        return sprite.getBoundingRectangle();
    }
}
