package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;


public class Nave4 {

    private boolean destruida = false;
    private boolean isPause = false;
    private int vidas = 3;
    private float xVel = 0;
    private float vx = 0;
    private float vy = 0;
    private float yVel = 0;
    private float rotacion = 0;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax = 0;
    private int tiempoHerido;

    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.soundBala.setVolume(0, 0.7f);
        this.txBala = txBala;
        spr = new Sprite(tx);
        spr.setRotation(rotacion);
        spr.setPosition(x, y);
        spr.setOrigin(spr.getWidth() / 2, spr.getHeight() / 2);

        spr.setBounds(x, y, 45, 45);

    }

    public void draw(SpriteBatch batch, PantallaJuego juego) {
        float x = spr.getX();
        float y = spr.getY();
        if (!herido && !isPause) {
            // Obtener la posición del ratón en coordenadas de pantalla
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            float naveCenterX = spr.getX() + spr.getWidth() / 2;
            float naveCenterY = spr.getY() + spr.getHeight() / 2;
            float dx = mouseX - naveCenterX;
            float dy = mouseY - naveCenterY;

            float angle = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;
            while (angle < 0) angle += 360;
            while (angle >= 360) angle -= 360;

            spr.setRotation(angle - 90);


            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && !isPause) xVel--;
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && !isPause) xVel++;
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && !isPause) yVel--;
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && !isPause) yVel++;

            // que se mantenga dentro de los bordes de la ventana
            if (x + xVel < 0 || x + xVel + spr.getWidth() > Gdx.graphics.getWidth())
                xVel *= -1;
            if (y + yVel < 0 || y + yVel + spr.getHeight() > Gdx.graphics.getHeight())
                yVel *= -1;

            spr.setPosition(x + xVel, y + yVel);

            spr.draw(batch);
        } else if (!isPause) {
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        } else {
            spr.draw(batch);
        }
        // disparo
        if (Gdx.input.justTouched() && !isPause) {
            // Posición inicial de la bala (centro del sprite principal)
            float balaX = spr.getX();
            float balaY = spr.getY();

            // Crear la bala con la dirección basada en la rotación del sprite principal
            Bullet bala = new Bullet(balaX, balaY, 400, txBala);  // 200 es la velocidad de la bala

            // Establecer la rotación de la bala según la del sprite principal
            bala.getSprite().setRotation(spr.getRotation());

            // Agregar la bala al juego
            juego.agregarBala(bala);
            soundBala.play();
        }


    }

    public boolean checkCollision(Ball2 b) {
        if (!herido && b.getArea().overlaps(spr.getBoundingRectangle())) {
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
            while (b.getArea().overlaps(spr.getBoundingRectangle()) && cont < xVel) {
                spr.setX(spr.getX() + Math.signum(xVel));
            }
            //actualizar vidas y herir
            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
            if (vidas <= 0)
                destruida = true;
            return true;
        }
        return false;
    }

    public boolean estaDestruido() {
        return !herido && destruida;
    }

    public boolean estaHerido() {
        return herido;
    }

    public int getVidas() {
        return vidas;
    }

    public boolean isDestruida() {
        return destruida;
    }

    public int getX() {
        return (int) spr.getX();
    }

    public int getY() {
        return (int) spr.getY();
    }

    public void setVidas(int vidas2) {
        vidas = vidas2;
    }

    public void pause() {
        isPause = true;
        this.vy = this.yVel;
        this.vx = this.xVel;
        this.xVel = 0;
        this.yVel = 0;
    }

    public void resume() {
        isPause = false;
    }
}
