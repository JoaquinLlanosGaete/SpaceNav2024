package puppy.code;

public abstract class GameObject implements Drawable, Collidable, Pausable, Destructible {
    protected com.badlogic.gdx.graphics.g2d.Sprite sprite;
    protected boolean isPaused;
    protected boolean isDestroyed;

    public GameObject(com.badlogic.gdx.graphics.Texture texture, float x, float y) {
        this.sprite = new com.badlogic.gdx.graphics.g2d.Sprite(texture);
        this.sprite.setPosition(x, y);
        this.isPaused = false;
        this.isDestroyed = false;
    }

    @Override
    public void draw(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        if (!isDestroyed) {
            sprite.draw(batch);
        }
    }

    @Override
    public boolean checkCollision(Collidable other) {
        return !isDestroyed && !isPaused && getCollisionBounds().overlaps(other.getCollisionBounds());
    }

    @Override
    public com.badlogic.gdx.math.Rectangle getCollisionBounds() {
        return sprite.getBoundingRectangle();
    }

    @Override
    public void pause() {
        this.isPaused = true;
    }

    @Override
    public void resume() {
        this.isPaused = false;
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    public void destroy() {
        this.isDestroyed = true;
    }

    // Método abstracto para actualizar el estado del objeto
    public abstract void update(float deltaTime);
}
