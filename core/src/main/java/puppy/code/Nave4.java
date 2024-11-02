package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Nave4 extends MovableGameObject {
    private int vidas;
    private boolean herido;
    private int tiempoHerido;
    private int tiempoHeridoMax;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;

    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        super(tx, x, y, 5.0f); // velocidad base de la nave
        this.sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        this.vidas = 3;

        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setBounds(x, y, 45, 45);
    }

    @Override
    protected void updateMovement(float deltaTime) {
        if (!isPaused && !isDestroyed && !herido) {
            // Actualizar posición según velocidad
            sprite.translate(xVel, yVel);

            // Actualizar rotación según posición del mouse
            updateRotation();
        }
    }

    private void updateRotation() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        float naveCenterX = sprite.getX() + sprite.getWidth() / 2;
        float naveCenterY = sprite.getY() + sprite.getHeight() / 2;
        float dx = mouseX - naveCenterX;
        float dy = mouseY - naveCenterY;

        float angle = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;
        sprite.setRotation(angle - 90);
    }

    @Override
    protected void checkBounds() {
        // Mantener la nave dentro de los límites de la pantalla
        if (sprite.getX() < 0) {
            sprite.setX(0);
            xVel = 0;
        }
        if (sprite.getX() + sprite.getWidth() > Gdx.graphics.getWidth()) {
            sprite.setX(Gdx.graphics.getWidth() - sprite.getWidth());
            xVel = 0;
        }
        if (sprite.getY() < 0) {
            sprite.setY(0);
            yVel = 0;
        }
        if (sprite.getY() + sprite.getHeight() > Gdx.graphics.getHeight()) {
            sprite.setY(Gdx.graphics.getHeight() - sprite.getHeight());
            yVel = 0;
        }
    }

    public void draw(SpriteBatch batch, PantallaJuego juego) {
        super.draw(batch);

        // Manejar el disparo
        if (Gdx.input.justTouched() && !isPaused) {
            disparar(juego);
        }

        // Efecto de daño
        if (herido) {
            sprite.setX(sprite.getX() + MathUtils.random(-2, 2));
            tiempoHerido--;
            if (tiempoHerido <= 0) {
                herido = false;
            }
        }
    }

    private void disparar(PantallaJuego juego) {
        Bullet bala = new Bullet(sprite.getX(), sprite.getY(), 400, txBala);
        bala.getSprite().setRotation(sprite.getRotation());
        juego.agregarBala(bala);
        soundBala.play();
    }

    public boolean checkCollision(Ball2 b) {
        if (!herido && b.getArea().overlaps(sprite.getBoundingRectangle())) {
            // rebote
            if (xVel == 0) xVel += (float) b.getXSpeed() / 2;
            if (b.getXSpeed() == 0) b.setXSpeed(b.getXSpeed() + (int) xVel / 2);
            xVel = -xVel;
            b.setXSpeed(-b.getXSpeed());

            if (yVel == 0) yVel += (float) b.getySpeed() / 2;
            if (b.getySpeed() == 0) b.setySpeed(b.getySpeed() + (int) yVel / 2);
            yVel = -yVel;
            b.setySpeed(-b.getySpeed());
            // despegar sprites
            int cont = 0;
            while (b.getArea().overlaps(sprite.getBoundingRectangle()) && cont < xVel) {
                sprite.setX(sprite.getX() + Math.signum(xVel));
            }
            //actualizar vidas y herir
            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
            return true;
        }
        return false;
    }
    public void updateInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) xVel -= 0.5f;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) xVel += 0.5f;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) yVel += 0.5f;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) yVel -= 0.5f;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public boolean estaHerido() {
        return herido;
    }
}
