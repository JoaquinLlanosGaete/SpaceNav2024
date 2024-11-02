package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Bullet extends MovableGameObject {

    public Bullet(float x, float y, float speed, Texture tx) {
        super(tx, x, y, speed);
    }

    @Override
    protected void updateMovement(float deltaTime) {
        // Obtener la rotación del sprite en radianes
        float angleRad = (float) Math.toRadians(sprite.getRotation()) + 1.5708f;

        // Calcular la dirección basada en el ángulo de rotación
        xVel = (float) Math.cos(angleRad) * speed;
        yVel = (float) Math.sin(angleRad) * speed;

        // Actualizar la posición del sprite
        sprite.translate(xVel * deltaTime, yVel * deltaTime);
    }

    @Override
    protected void checkBounds() {
        if (sprite.getX() < 0 || sprite.getX() > com.badlogic.gdx.Gdx.graphics.getWidth() ||
            sprite.getY() < 0 || sprite.getY() > com.badlogic.gdx.Gdx.graphics.getHeight()) {
            destroy();
        }
    }

    public boolean checkCollision(Ball2 ball) {
        if (super.checkCollision(ball)) {
            destroy();
            return true;
        }
        return false;
    }

    public Sprite getSprite(){
        return sprite;
    }
}
