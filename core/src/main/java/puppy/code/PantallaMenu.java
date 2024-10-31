package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;


public class PantallaMenu implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;

	public PantallaMenu(SpaceNavigation game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
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
        titulo.draw(game.getBatch(), textoTitulo, xTitulo, 400);

        descripcion.getData().setScale(0.5f);
        GlyphLayout layoutDescripcion = new GlyphLayout(descripcion, textoDescripcion);
        float xDescripcion = (Gdx.graphics.getWidth() - layoutDescripcion.width) / 2;
        descripcion.draw(game.getBatch(), textoDescripcion, xDescripcion, 300);

		game.getBatch().end();

		if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
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

	}

}
