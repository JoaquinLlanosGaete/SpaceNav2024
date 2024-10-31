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
    private static final float FRAME_TIME = 1;
    private float elapsed_time;
    private Animation<TextureRegion> run;
    private SpriteBatch batch2;

    public Bullet(float x, float y, float speed, Texture tx) {
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        this.speed = speed;  // Velocidad
        this.isPaused = false;
        TextureAtlas charset = new TextureAtlas(Gdx.files.internal("run.atlas"));
        run = new Animation<>(FRAME_TIME, charset.findRegions("run"));
        batch2 = new SpriteBatch(1000);
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
    public void animacion(float delta, Bullet b) {
        // Reinicia el tiempo de estado solo si la animación acaba de comenzar
        if (elapsed_time == 0) {
            elapsed_time += delta;
        }

        TextureRegion currentFrame = run.getKeyFrame(elapsed_time, true); // No repetimos la animación
        batch2.begin();
        batch2.draw(currentFrame, b.getSprite().getX(), b.getSprite().getY());
        batch2.end();

        // Detén la animación si ya se completó
        if (run.isAnimationFinished(elapsed_time)) {
            elapsed_time = 0; // Resetea el tiempo para la próxima explosión
            // Realiza cualquier acción que necesites al finalizar la animación
        }
    }

    public Sprite getSprite() {
        return spr;
    }

    public void dispose() {// Liberamos la textura del sprite sheet
    }
}
