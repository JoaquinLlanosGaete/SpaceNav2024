package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;


public class Ball2 {
    private boolean isPaused;
    private int x;
    private int y;
    private int xSpeed;
    private int vx;
    private int ySpeed;
    private int vy;
    private Sprite spr;

    public Ball2(int x, int y, int size, int xSpeed, int ySpeed, Texture tx) {
        spr = new Sprite(tx);
        this.x = x;

        // Validar que el borde de la esfera no quede fuera
        if (x - size < 0) this.x = x + size;
        if (x + size > Gdx.graphics.getWidth()) this.x = x - size;

        this.y = y;
        // Validar que el borde de la esfera no quede fuera
        if (y - size < 0) this.y = y + size;
        if (y + size > Gdx.graphics.getHeight()) this.y = y - size;

        spr.setPosition(x, y);
        this.setXSpeed(xSpeed);
        this.setySpeed(ySpeed);
        // Inicialización
        this.vx = xSpeed;
        this.vy = ySpeed;
    }
    public void update() {
        x += getXSpeed();
        y += getySpeed();

        if (x + getXSpeed() < 0 || x + getXSpeed() + spr.getWidth() > Gdx.graphics.getWidth()) {
            setXSpeed(getXSpeed() * -1);
        }
        if (y + getySpeed() < 0 || y + getySpeed() + spr.getHeight() > Gdx.graphics.getHeight()) {
            setySpeed(getySpeed() * -1);
        }
        spr.setPosition(x, y);
    }

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public void checkCollision(Ball2 b2) {
        if (spr.getBoundingRectangle().overlaps(b2.spr.getBoundingRectangle())) {
            handleRebound(this, b2, true);  // Eje X
            handleRebound(this, b2, false); // Eje Y
        }
    }

    // Método de ayuda para manejar el rebote
    private void handleRebound(Ball2 b1, Ball2 b2, boolean isXAxis) {
        int speed1 = isXAxis ? b1.getXSpeed() : b1.getySpeed();
        int speed2 = isXAxis ? b2.getXSpeed() : b2.getySpeed();

        if (speed1 == 0) {
            if (isXAxis) {
                b1.setXSpeed(b1.getXSpeed() + b2.getXSpeed() / 2);
            } else {
                b1.setySpeed(b1.getySpeed() + b2.getySpeed() / 2);
            }
        }

        if (speed2 == 0) {
            if (isXAxis) {
                b2.setXSpeed(b2.getXSpeed() + b1.getXSpeed() / 2);
            } else {
                b2.setySpeed(b2.getySpeed() + b1.getySpeed() / 2);
            }
        }

        if (isXAxis) {
            b1.setXSpeed(-b1.getXSpeed());
            b2.setXSpeed(-b2.getXSpeed());
        } else {
            b1.setySpeed(-b1.getySpeed());
            b2.setySpeed(-b2.getySpeed());
        }
    }

    public int getXSpeed() {
        return xSpeed;
    }

    public void setXSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }


    public void pause() {
        if (isPaused) return;  // Si ya está pausado, no hacer nada
        this.isPaused = true;
        this.vy = getySpeed(); // Guardar la velocidad actual en vy
        this.vx = getXSpeed(); // Guardar la velocidad actual en vx
        setySpeed(0); // Pausar el movimiento en y
        setXSpeed(0); // Pausar el movimiento en x
    }

    public void resume() {
        if (!isPaused) return;  // Si no está pausado, no hacer nada
        this.isPaused = false;
        setySpeed(vy); // Restaurar la velocidad en y desde vy
        setXSpeed(vx); // Restaurar la velocidad en x desde vx
    }
}
