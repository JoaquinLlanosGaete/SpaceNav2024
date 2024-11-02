package puppy.code;

public interface Collidable {
    boolean checkCollision(Collidable other);
    com.badlogic.gdx.math.Rectangle getCollisionBounds();
}
