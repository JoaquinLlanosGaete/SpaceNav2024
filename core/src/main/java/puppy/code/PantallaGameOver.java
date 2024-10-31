package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;


public class PantallaGameOver implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;
    private Music loseSound = Gdx.audio.newMusic(Gdx.files.internal("lose.mp3"));;
    private Texture fondo;

	public PantallaGameOver(SpaceNavigation game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
        fondo = new Texture(Gdx.files.internal("fondo.jpeg")); // Asegúrate de que la ruta sea correcta

        loseSound.play();
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        game.getBatch().draw(fondo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        BitmapFont titulo = game.getFont();
        BitmapFont descripcion = game.getFont();

        // Define los textos
        String textoTitulo = "Haz perdido.";
        String textoDescripcion = "Pincha en cualquier lado o presiona cualquier tecla para volver al inicio...";

        // Calcula las posiciones centradas
        titulo.getData().setScale(1f);
        GlyphLayout layoutTitulo = new GlyphLayout(titulo, textoTitulo);
        float xTitulo = (Gdx.graphics.getWidth() - layoutTitulo.width) / 2;
        titulo.draw(game.getBatch(), textoTitulo, xTitulo, 400);

        descripcion.getData().setScale(0.5f);
        GlyphLayout layoutDescripcion = new GlyphLayout(descripcion, textoDescripcion);
        float xDescripcion = (Gdx.graphics.getWidth() - layoutDescripcion.width) / 2;
        descripcion.draw(game.getBatch(), textoDescripcion, xDescripcion, 300);
        game.getBatch().end();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !loseSound.isPlaying()) {
            game.disposes();
        }
        if ((Gdx.input.isTouched()|| Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) && !loseSound.isPlaying()) {
            Screen ss = new PantallaMenu(game);
            game.setScreen(ss);
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
        this.loseSound.dispose();
        this.fondo.dispose();


	}

}
