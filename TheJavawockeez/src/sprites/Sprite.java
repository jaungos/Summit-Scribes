package sprites;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

public class Sprite {
    //****************     ATTRIBUTES      *********************//
    public Image img;
    public double xPos, yPos;
    public double width;
    public double height;

    //****************     CONSTURCTOR      *********************//
    public Sprite(double xPos, double yPos, Image image) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.img = image;
        this.setSize();
    }

    //****************     METHODS      *********************//
    // getting bounds of sprite
    private Rectangle2D getBounds() {
        return new Rectangle2D(this.xPos, this.yPos, this.width, this.height);
    }

    // method to set the object's width and height properties
    private void setSize() {
        this.width = this.img.getWidth();
        this.height = this.img.getHeight();
    }

    // method as a checker whether a sprite collides with another sprite
    protected boolean collidesWith(Sprite sprite2) {
        Rectangle2D rect1 = this.getBounds();
        Rectangle2D rect2 = sprite2.getBounds();

        return rect1.intersects(rect2);
    }

    // method to set the object's image
    public void setImage(Image image) {
        this.img = image;
        this.setSize();
    }

    // method to render the image using GraphicsContext
    public void render(GraphicsContext gc) {
        if (this.img != null) {
            gc.drawImage(this.img, this.xPos, this.yPos);
        }
    }

    // GETTERS
    public double getXPos() {
        return this.xPos;
    }

    public double getYPos() {
        return this.yPos;
    }

    // SETTERS
    public void setDX(double PLAYER_SPEED) {
        // this.dx = PLAYER_SPEED; // If needed in the future
    }

    public void setDY(double PLAYER_SPEED) {
        // this.dy = PLAYER_SPEED; // If needed in the future
    }

    public void vanish() {
        // this.visible = false; // If needed in the future
    }
}
