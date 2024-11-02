package puppy.code;

public abstract class MovableGameObject extends GameObject {
    protected float speed;
    protected float xVel;
    protected float yVel;

    public MovableGameObject(com.badlogic.gdx.graphics.Texture texture, float x, float y, float speed) {
        super(texture, x, y);
        this.speed = speed;
    }

    public void setVelocity(float xVel, float yVel) {
        if (!isPaused) {
            this.xVel = xVel;
            this.yVel = yVel;
        }
    }

    @Override
    public void update(float deltaTime) {
        if (!isPaused && !isDestroyed) {
            updateMovement(deltaTime);
            checkBounds();
        }
    }

    protected abstract void updateMovement(float deltaTime);
    protected abstract void checkBounds();
}
