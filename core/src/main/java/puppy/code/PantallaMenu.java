package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
        BitmapFont titulo = game.getFont(), descripcion = game.getFont();
        titulo.getData().setScale(0.8f);
		game.getBatch().begin();
		titulo.draw(game.getBatch(), "¡Bienvenido a Space Navigation!", (float) Gdx.graphics.getWidth() /4, 400);
        descripcion.getData().setScale(0.5f);
		descripcion.draw(game.getBatch(), "Pincha en cualquier lado o presiona cualquier tecla para comenzar ...", (float) Gdx.graphics.getWidth() /7, 300);

		game.getBatch().end();

		if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
			Screen ss = new PantallaJuego(game,1,5,0,0,0,3);
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
