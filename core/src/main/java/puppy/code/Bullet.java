package puppy.code;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Bullet{
    private boolean isPaused;
    private float speed;  // Velocidad de la bala
    private boolean destroyed = false;
    private Sprite spr;
    private SpriteBatch batch;  // Para dibujar texturas
    private Texture spriteSheet;  // La imagen que contiene los fotogramas de la animación
    private Animation<TextureRegion> animation;  // Animación
    private float stateTime;

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

    public void create() {
        batch = new SpriteBatch();
        spriteSheet = new Texture("animacion.png");
        TextureRegion[][] tmpFrames = TextureRegion.split(spriteSheet, 64, 64);
        // Crear un array para guardar los fotogramas
        Array<TextureRegion> animationFrames = new Array<TextureRegion>();

        for (int i = 0; i < 4; i++) {    // Número de filas
            for (int j = 0; j < 4; j++) {  // Número de columnas
                animationFrames.add(tmpFrames[i][j]);
            }
        }

        animation = new Animation<>(0.1f, animationFrames);
        stateTime = 0;
    }
    public void render(float x, float y) {
        // Actualizamos el tiempo de la animación
        stateTime += Gdx.graphics.getDeltaTime();  // DeltaTime es el tiempo entre cada frame

        // Obtener el fotograma actual basado en el tiempo
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);  // true para que la animación se repita

        // Dibujar el fotograma actual
        batch.begin();  // Comenzamos a dibujar
        batch.draw(currentFrame, x,y);  // Dibujamos el fotograma en la posición (50, 50)
        batch.end();  // Terminamos de dibujar
    }

    public void setAnimation(float x, float y){
        create();
        render(x,y);
        dispose();
    }

    public void resume() {
        this.isPaused = false;
        // Lógica para reanudar el movimiento de la bala
    }

    public Sprite getSprite() {
        return spr;
    }

    public void dispose() {
        batch.dispose();  // Liberamos el SpriteBatch
        spriteSheet.dispose();  // Liberamos la textura del sprite sheet
    }
}
