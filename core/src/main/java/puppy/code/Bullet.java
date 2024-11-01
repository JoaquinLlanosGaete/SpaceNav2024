package puppy.code;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;

public class Bullet{
    private boolean isPaused;
    private float speed;  // Velocidad de la bala
    private boolean destroyed = false;
    private Sprite spr;

    public Bullet(float x, float y, float speed, Texture tx) {
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        this.speed = speed;  // Velocidad
        this.isPaused = false;
    }

    // Método para actualizar la posición del bullet según la rotación
    public void update(float deltaTime) {
        if (!isPaused && !destroyed) {
            // Obtener la rotación del sprite en radianes
            float angleRad = (float) Math.toRadians(spr.getRotation())+1.5708f;

            // Calcular la dirección basada en el ángulo de rotación
            float xSpeed = (float) Math.cos(angleRad) * speed;
            float ySpeed = (float) Math.sin(angleRad) * speed;

            // Actualizar la posición del sprite
            spr.translate(xSpeed * deltaTime, ySpeed * deltaTime);
        }
    }
    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }
    public boolean checkCollision(Ball2 b2) {
        if(spr.getBoundingRectangle().overlaps(b2.getArea())){
            // Se destruyen ambos
            this.destroyed = true;
            return true;
        }
        return false;
    }
    public boolean isDestroyed() {return destroyed;}
    public void pause() {
        this.isPaused = true;
        // Lógica para pausar el movimiento de la bala
    }

    public void resume() {
        this.isPaused = false;
        // Lógica para reanudar el movimiento de la bala
    }

    public Sprite getSprite() {
        return spr;
    }

    public void dispose() {
        // Liberamos la textura del sprite sheet
        this.destroyed = true;
    }
}
