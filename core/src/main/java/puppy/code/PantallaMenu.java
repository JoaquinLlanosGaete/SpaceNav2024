package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;


public class PantallaMenu implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;
    private Texture fondo;
    private SpriteBatch batch;
    private Music musicaFondo;
    private float cont;

	public PantallaMenu(SpaceNavigation game) {
		this.game = game;
        fondo = new Texture(Gdx.files.internal("fondomenu.jpg"));
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musicaFondo.mp3"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
        musicaFondo.setLooping(true);
        batch = new SpriteBatch();
    }

	@Override
	public void render(float delta) {
		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        batch.begin();
        musicaFondo.play();
        batch.setColor(0,0,0,1-cont/4f);
        batch.draw(fondo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.getBatch().draw(fondo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Configura las fuentes para el título y la descripción
        BitmapFont titulo = game.getFont();
        BitmapFont descripcion = game.getFont();

        // Define los textos
        String textoTitulo = "¡Bienvenido a Space Navigation!";
        String textoDescripcion = "Pincha en cualquier lado o presiona cualquier tecla para comenzar ...";

        // Calcula las posiciones centradas
        titulo.getData().setScale(1f);
        GlyphLayout layoutTitulo = new GlyphLayout(titulo, textoTitulo);
        float xTitulo = (Gdx.graphics.getWidth() - layoutTitulo.width) / 2;
        titulo.setColor(Color.BLACK);
        titulo.draw(game.getBatch(), textoTitulo, xTitulo, 400);
        titulo.setColor(Color.WHITE);
        titulo.draw(game.getBatch(), textoTitulo, xTitulo-4, 404);

        descripcion.getData().setScale(0.5f);
        GlyphLayout layoutDescripcion = new GlyphLayout(descripcion, textoDescripcion);
        float xDescripcion = (Gdx.graphics.getWidth() - layoutDescripcion.width) / 2;
        descripcion.setColor(Color.BLACK);
        descripcion.draw(game.getBatch(), textoDescripcion, xDescripcion, 300);
        descripcion.setColor(Color.WHITE);
        descripcion.draw(game.getBatch(), textoDescripcion, xDescripcion+2, 300+2);
        cont += 1/60f;
        batch.end();
		game.getBatch().end();
        if ((Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) &&  cont > 4) {
            Screen ss = new PantallaJuego(game,1,1,0,0,0,3);
            game.setScreen(ss);
            System.out.println(Gdx.graphics.getWidth()+", "+Gdx.graphics.getHeight());
            dispose();
        }
    }


	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {

		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
        musicaFondo.dispose();
        fondo.dispose();

	}

}
