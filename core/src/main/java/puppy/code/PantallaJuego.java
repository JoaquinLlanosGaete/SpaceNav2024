package puppy.code;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class PantallaJuego implements Screen {

	private SpaceNavigation game;
    private ArrayList<Animacion> explosiones = new ArrayList<>();
	public OrthographicCamera camera;
	private SpriteBatch batch;
    private Texture fondo;
	private Sound explosionSound;
    private Music roundWin;
	private Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));;
	private int score;
	private int ronda;
	private int velXAsteroides;
	private int velYAsteroides;
	private int cantAsteroides;
	private Nave4 nave;
	private  ArrayList<Ball2> balls1 = new ArrayList<>();
	private  ArrayList<Ball2> balls2 = new ArrayList<>();
	private  ArrayList<Bullet> balas = new ArrayList<>();


	public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,
			int velXAsteroides, int velYAsteroides, int cantAsteroides) {
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
		explosionSound.setVolume(1,1);
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        if (!gameMusic.isPlaying()){
            gameMusic.play();
        }


	    // cargar imagen de la nave, 64x64
	    nave = new Nave4(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,new Texture(Gdx.files.internal("MainShip3.png")),
	    				Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
	    				new Texture(Gdx.files.internal("Rocket2.png")),
	    				Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        nave.setVidas(vidas);
        //crear asteroides
        Random r = new Random();
	    for (int i = 0; i < cantAsteroides; i++) {
	        Ball2 bb = new Ball2(50+r.nextInt(Gdx.graphics.getWidth()),50+r.nextInt(Gdx.graphics.getHeight()),
	  	            r.nextInt(40)+10, velXAsteroides+r.nextInt(4), velYAsteroides+r.nextInt(4),
	  	            new Texture(Gdx.files.internal("aGreyMedium4.png")));
            balls1.add(bb);
            balls2.add(bb);
            System.out.println("Se agrego un objeto.");

	  	}
	}
	public void dibujaEncabezado() {
		CharSequence str = "Vidas: "+nave.getVidas()+" Ronda: "+ronda;
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+this.score, Gdx.graphics.getWidth()-150, 30);
		game.getFont().draw(batch, "HighScore:"+game.getHighScore()+" Asteroides:"+balls1.size(), (float) Gdx.graphics.getWidth() /2-190, 30);
	}
	@Override
	public void render(float delta) {
		  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
          batch.begin();
          game.getBatch().draw(fondo,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		  dibujaEncabezado();
	      if (!nave.estaHerido() && gameMusic.isPlaying()) {
		      // colisiones entre balas y asteroides y su destruccion
	    	  for (int i = 0; i < balas.size(); i++) {
		            Bullet b = balas.get(i);
		            b.update(delta);
		            for (int j = 0; j < balls1.size(); j++) {
		              if (b.checkCollision(balls1.get(j))) {
		            	 balls1.remove(j);
		            	 balls2.remove(j);
		            	 j--;
		            	 score +=10;
		              }
		  	        }

                    if (b.isDestroyed()||b.getSprite().getX()>Gdx.graphics.getWidth() || b.getSprite().getX()<0 || b.getSprite().getY()>Gdx.graphics.getHeight() || b.getSprite().getY()<0) {
                        balas.remove(b);
                        explosionSound.play();
                        // Iniciar la animación de explosión en la posición de la bala
                        Animacion explosion = new Animacion(b.getSprite().getX(), b.getSprite().getY());
                        explosiones.add(explosion);
                        i--;
                    }
		      }

              //actualizar movimiento de asteroides dentro del area
		      for (Ball2 ball : balls1) {
		          ball.update();
		      }
		      //colisiones entre asteroides y sus rebotes
		      for (int i=0;i<balls1.size();i++) {
		    	Ball2 ball1 = balls1.get(i);
		        for (int j=0;j<balls2.size();j++) {
		          Ball2 ball2 = balls2.get(j);
		          if (i<j) {
		        	  ball1.checkCollision(ball2);

		          }
		        }
		      }
	      }
        // Actualizar y dibujar animaciones de explosión
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
	      //dibujar balas
	     for (Bullet b : balas) {
	          b.draw(batch);
	      }
	      nave.draw(batch, this);
	      //dibujar asteroides y manejar colision con nave
	      for (int i = 0; i < balls1.size(); i++) {
              Ball2 b = balls1.get(i);
              b.draw(batch);
              //perdió vida o game over
              if (nave.checkCollision(b)) {
                  //asteroide se destruye con el choque
                  balls1.remove(i);
                  balls2.remove(i);
                  i--;
              }
          }

          if(Gdx.input.isKeyJustPressed(46)){
            Screen ss = new PantallaMenu(game);
            game.setScreen(ss);
            dispose();
          }
          if(Gdx.input.isKeyJustPressed(44)){
              pause();
          }
          if(Gdx.input.isKeyJustPressed(43)){
              resume();
          }

	      if (nave.estaDestruido()) {
  			if (score > game.getHighScore())
  				game.setHighScore(score);
	    	Screen ss = new PantallaGameOver(game);
  			game.setScreen(ss);
  			dispose();
  		  }
	      batch.end();
	      //nivel completado
        if (balls1.isEmpty() && nave.getVidas() > 0) {
            dispose();
            roundWin.setOnCompletionListener(music -> {
                Screen ss = new PantallaJuego(game, ronda + 1, nave.getVidas() + 1, score,
                    velXAsteroides + 1, velYAsteroides + 1, cantAsteroides + 1);
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
        // Pausar la música del juego
        if (gameMusic != null && gameMusic.isPlaying()) {
            gameMusic.pause();
        }

        // Pausar el procesamiento de entradas
        // Para desactivar la detección de entradas
        Gdx.input.setInputProcessor(null);


        // Detener el movimiento de balas y bolas
        for (Bullet bala : balas) {
            bala.pause();
        }

        for (Ball2 ball : balls1) {
            ball.pause();
        }

        for (Ball2 ball : balls2) {
            ball.pause();
        }
        nave.pause();
        // Pausar otras lógicas del juego según sea necesario
        // ...

        System.out.println("El juego ha sido pausado.");
    }

    @Override
    public void resume() {
        // Reanudar la música del juego
        if (gameMusic != null && !gameMusic.isPlaying()) {
            gameMusic.play();
        }

        // Reanudar el procesamiento de entradas del usuario
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                // Lógica para gestionar las entradas del usuario
                return true;
            }

            // Otros métodos de entrada...
        });

        // Reanudar el movimiento de balas y bolas
        for (Bullet bala : balas) {
            bala.resume();
        }

        for (Ball2 ball : balls1) {
            ball.resume();
        }

        for (Ball2 ball : balls2) {
            ball.resume();
        }
        nave.resume();
        // Reanudar otras lógicas del juego según sea necesario
        // ...

        System.out.println("El juego ha sido reanudado.");
    }

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		this.explosionSound.dispose();
        this.gameMusic.pause();
        for (Bullet b : balas) {
            Animacion explosion = new Animacion(b.getSprite().getX(), b.getSprite().getY());
            explosiones.add(explosion);
        }
        balas.removeAll(balas);
        nave.pause();
	}

}
