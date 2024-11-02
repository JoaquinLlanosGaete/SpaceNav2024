package puppy.code;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class PantallaJuego implements Screen, Pausable {

    private SpaceNavigation game;
    private ArrayList<Animacion> explosiones = new ArrayList<>();
    private SpriteBatch batch;
    private Texture fondo;
    private Sound explosionSound;
    private Music roundWin;
    private Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal("musicaJuego.mp3"));
    private int score;
    private int ronda;
    private ArrayList<MovableGameObject> gameObjects;
    private ArrayList<Bullet> balas;
    private int velXAsteroides;
    private int velYAsteroides;
    private int cantAsteroides;
    private Nave4 nave;


    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score, int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;
        fondo = new Texture(Gdx.files.internal("fondoJuego.jpeg"));
        batch = game.getBatch();
        //inicializar assets; musica de fondo y efectos de sonido
        roundWin = Gdx.audio.newMusic(Gdx.files.internal("roundwin.mp3"));
        roundWin.setVolume(0.5f);
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1, 1);
        if (!gameMusic.isPlaying()) {
            gameMusic.play();
        }
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);


        // cargar imagen de la nave, 64x64
        nave = new Nave4(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, new Texture(Gdx.files.internal("MainShip3.png")), Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), new Texture(Gdx.files.internal("Rocket2.png")), Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        nave.setVidas(vidas);
        //crear asteroides
        gameObjects = new ArrayList<>();
        balas = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < cantAsteroides; i++) {
            int x = 50 + r.nextInt(Gdx.graphics.getWidth());
            int y = 50 + r.nextInt(Gdx.graphics.getHeight());
            if (x < 50 || x > Gdx.graphics.getWidth() - 50) {
                x = MathUtils.random(51,Gdx.graphics.getWidth())-51;
            }
            if (y < 50 || y > Gdx.graphics.getHeight() - 50) {
                y = MathUtils.random(51,Gdx.graphics.getHeight())-51;
            }
            Ball2 asteroide = new Ball2(x,y, 49, velXAsteroides + r.nextInt(4), velYAsteroides + r.nextInt(4), new Texture(Gdx.files.internal("aGreyMedium4.png")));

            boolean colision = false;
            for (MovableGameObject obj : gameObjects) {
                if (asteroide.checkCollision(obj)) {
                    colision = true;
                    i--;
                    break;
                }
            }

            if (!colision) {
                gameObjects.add(asteroide);
            }
        }
    }

    public void dibujaEncabezado() {
        CharSequence str = "Vidas: " + nave.getVidas() + " Ronda: " + ronda;
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score:" + this.score, Gdx.graphics.getWidth() - 150, 30);
        game.getFont().draw(batch, "HighScore:" + game.getHighScore() + " Asteroides:" + gameObjects.size(), (float) Gdx.graphics.getWidth() / 2 - 190, 30);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        game.getBatch().draw(fondo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        dibujaEncabezado();

        // Dibuja cada asteroide en el juego
        for (MovableGameObject obj : gameObjects) {
            obj.update(delta);
            obj.draw(batch); // Asegúrate de que el objeto se dibuje
        }

        // Dibuja la nave
        nave.update(delta);
        nave.draw(batch, this);
        nave.updateInput(); // Actualizar velocidad de acuerdo a la entrada

        // Asegúrate de que la nave se dibuje también

        // Actualizar y dibujar balas
        for (Iterator<Bullet> it = balas.iterator(); it.hasNext(); ) {
            Bullet bala = it.next();
            bala.update(delta);
            bala.draw(batch);

            // Comprobar colisiones con asteroides
            for (Iterator<MovableGameObject> asteroidIt = gameObjects.iterator(); asteroidIt.hasNext(); ) {
                MovableGameObject asteroide = asteroidIt.next();
                if (asteroide != null && bala.checkCollision((Ball2) asteroide)) {
                    asteroidIt.remove();
                    score += 10;
                    explosionSound.play();
                }
            }

            if (bala.isDestroyed()) {
                it.remove();
                explosiones.add(new Animacion(bala.getSprite().getX(), bala.getSprite().getY()));
            }
        }

        for (MovableGameObject asteroide : gameObjects) {
            if (nave.checkCollision((Ball2) asteroide)) {
                explosiones.add(new Animacion(nave.sprite.getX(), nave.sprite.getY()));
            }
        }
        for (int i = 0; i < explosiones.size(); i++) {
            Animacion explosion = explosiones.get(i);
            explosion.update(delta);
            explosion.render(batch);

            // Remover la explosión si ha terminado
            if (explosion.isFinished()) {
                explosiones.remove(i);
                i--;
            }
        }


        if (Gdx.input.isKeyJustPressed(46) && gameObjects.size()!=0) {
            Screen ss = new PantallaMenu(game);
            game.setScreen(ss);
            dispose();
        }
        if (Gdx.input.isKeyJustPressed(44)) {
            pause();
        }
        if (Gdx.input.isKeyJustPressed(43)) {
            resume();
        }

        if (nave.getVidas()<1) {
            if (score > game.getHighScore()) game.setHighScore(score);
            Screen ss = new PantallaGameOver(game);
            game.setScreen(ss);
            dispose();
        }
        batch.end();
        //nivel completado
        if (gameObjects.size()==0 && nave.getVidas() > 0) {
            dispose();
            roundWin.setOnCompletionListener(music -> {
                Screen ss = new PantallaJuego(game, ronda + 1, nave.getVidas() + 1, score, velXAsteroides + 1, velYAsteroides + 1, cantAsteroides + 1);
                game.setScreen((ss));
            });
            roundWin.play();
        }

    }

    public void agregarBala(Bullet bb) {
        balas.add(bb);
    }

    @Override
    public void show() {
    }


    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        gameMusic.pause();

        // Pausar todos los objetos del juego
        for (MovableGameObject obj : gameObjects) {
            obj.pause();
        }

        for (Bullet bala : balas) {
            bala.pause();
        }

        nave.pause();
    }

    @Override
    public void resume() {
        gameMusic.play();

        // Reanudar todos los objetos del juego
        for (MovableGameObject obj : gameObjects) {
            obj.resume();
        }

        for (Bullet bala : balas) {
            bala.resume();
        }

        nave.resume();
    }



    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        this.explosionSound.dispose();
        gameMusic.pause();
        for (Bullet b : balas) {
            Animacion explosion = new Animacion(b.getSprite().getX(), b.getSprite().getY());
            explosiones.add(explosion);
        }
        balas.removeAll(balas);
        nave.pause();
    }

}
