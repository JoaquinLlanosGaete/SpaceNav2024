package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animacion{

    private Array<TextureRegion> frames; // Almacena las imágenes de la animación
    private float maxFrameTime;          // Tiempo máximo por frame
    private float currentFrameTime;      // Tiempo acumulado en el frame actual
    private int frameCount;              // Cantidad total de frames en la animación
    private int frame;                   // Frame actual en la animación
    private float x, y;                  // Posición de la animación
    private boolean finished;

    public Animacion(float x, float y) {
        this.x = x;
        this.y = y;
        this.finished = false;

        // Cargar la textura de la explosión y dividirla en frames
        Texture explosionTexture = new Texture("explosion.png"); // La textura debe estar en tu proyecto
        int frameWidth = explosionTexture.getWidth() / 12;  // Suponiendo 4 columnas
        int frameHeight = explosionTexture.getHeight(); // Suponiendo 4 filas
        TextureRegion[][] tempFrames = TextureRegion.split(explosionTexture, frameWidth, frameHeight);

        frames = new Array<>();
        for (TextureRegion[] row : tempFrames) {
            for (TextureRegion region : row) {
                frames.add(region);
            }
        }

        this.frameCount = frames.size;
        this.maxFrameTime = 0.1f; // Duración de cada frame en segundos
        this.currentFrameTime = 0;
        this.frame = 0;
    }

    public void update(float delta) {
        if (finished) return;

        currentFrameTime += delta;
        if (currentFrameTime >= maxFrameTime) {
            frame++;
            currentFrameTime = 0;
            if (frame >= frameCount) {
                frame = frameCount - 1;
                finished = true; // Marcar la animación como terminada
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (!finished) {
            batch.draw(frames.get(frame), x, y);
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
