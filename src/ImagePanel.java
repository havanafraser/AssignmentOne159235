/*
  JPanel class to read and display an image. When using IntelliJ this
  will be in a separate class file that you create.
*/

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics;

// Class to read and display an image. Images are displayed in a
// JPanel, so we make our class extend one.
public class ImagePanel extends JPanel implements MouseMotionListener, MouseListener {

    private ImageViewer imageViewer;
    private BufferedImage image;
    private AffineTransform transformed;
    private AffineTransform allTransforms = new AffineTransform();

    public ImagePanel(ImageViewer imageViewer) {

        this.imageViewer = imageViewer;
        // For debugging - this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(800, 600));
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    // A method to read an image from a file handle object. Note here
    // that we MUST handle the IOException
    public void getImage(File file) {
        try {
            image = ImageIO.read(file);
            this.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This gets called by the JVM whenever it needs to do so. For
    // example, when rendering a JFrame, this method will be called
    // for all JPanels that have been added to the JFrame. Here we
    // override the paintComponent() method in the parent class (which
    // actually does nothing)
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            // Call the appropriate g2 methods;
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(image,0,0,this);
        }
    }

    // Image is returned to original orientation
    public void originalOrientation() {
        // check if there is an image loaded
        if (image == null) {
            nullImage();
        }
        try {
            //inverses all transforms
            AffineTransform inverseTransform = allTransforms.createInverse();
            //apply inverse to image
            AffineTransformOp transformed = new AffineTransformOp(inverseTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = transformed.filter((BufferedImage) image, null);
            //reset alltransforms to record the next set of transformations on the image
            allTransforms = new AffineTransform();
            //repaint
            this.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //flips image by X axis and repaints
    public void flipX() {
        if (image == null) {
            nullImage();
        } else {
            // transformed = scales x by -1 to flip the image horizontally
            transformed = AffineTransform.getScaleInstance(-1, 1);
            //transformed = move image back to original position
            transformed.translate(-image.getWidth(null),0);
            // keep track of all transformations
            allTransforms.concatenate(transformed);
            // apply transformed to the image
            AffineTransformOp op = new AffineTransformOp(transformed, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);
            // repaint
            this.repaint();
        }
    }

    //flips image by Y axis and repaints
    public void flipY() {
        if (image == null) {
            nullImage();
        } else {
            // transformed = scales y by -1 to flip the image vertically
            transformed = AffineTransform.getScaleInstance(1, -1);
            //transformed = move image back to original position
            transformed.translate(0,-image.getHeight(null));
            // keep track of all transformations
            allTransforms.concatenate(transformed);
            // apply transformed to the image
            AffineTransformOp op = new AffineTransformOp(transformed, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);
            //repaint
            this.repaint();
        }
    }

    //flips image by X & Y axis and repaints
    public void flipXY() {
        // Checks there is an image loaded
        if (image == null) {
            nullImage();
        } else {
            // transformed = scales by -1 x and y to flip the image vertically and horizontally
            transformed = AffineTransform.getScaleInstance(-1, -1);
            // transformed = move image back to original position
            transformed.translate(-image.getWidth(null), -image.getHeight(null));
            // keep track of all transformations
            allTransforms.concatenate(transformed);
            // Apply transformed to the image
            AffineTransformOp op = new AffineTransformOp(transformed, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);
            // Repaint
            this.repaint();
        }
    }

    // Method to check if there is an image loaded
    // or not when the negate button is clicked
    public void negateImage() {
        if (image == null) {
            nullImage();
        } else {
            argbPixelChanger();
        }
    }

    //For both negating the image and then returning it to colour, pixel by pixel
    public void argbPixelChanger() {

        for (int y = 0; y < image.getHeight(); y++) {

            for (int x = 0; x < image.getWidth(); x++) {

                // current pixel and it's argb values using bit shifting
                // and 0xff mask
                int argb = image.getRGB(x, y);
                int a = (argb >> 24) & 0xff;
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = (argb >> 0) & 0xff;

                r = 255 - r; // new r value
                g = 255 - g; // new g value
                b = 255 - b; // new b value

                //new pixel value
                argb = (a << 24) | (r << 16) | (g << 8) | b;

                //setting image's new pixel value
                image.setRGB(x, y, argb);
            }
        }

        //repainting image with new argb values
        this.repaint();
    }

    // Prints a string to the console if there is no image loaded
    public void nullImage() {
        System.out.println("There is no image loaded! Please load in an image via File >> Open :)");
    }

    // Mouse moved event action handler
    @Override
    public void mouseMoved(MouseEvent e) {
        if (image != null) {
            int mouseX = e.getX();
            int mouseY = e.getY();

            // checks if the mouse is within the bounds of the image
            if (mouseX >= 0 && mouseX < image.getWidth() && mouseY >= 0 && mouseY < image.getHeight()) {

                // Get current pixels argb values
                int argb = image.getRGB(mouseX,mouseY);

                // Get individual argb values using relevant bit shifting
                // and 0xff mask
                int a = (argb >> 24) & 0xff;
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = (argb >> 0) & 0xff;

                // Send the argb values and current mouse x/y coords
                // to the text fields
                imageViewer.updateTextFields(mouseX,mouseY, r, g, b, a);

            } else {
                // If the mouse has left the bounds of the image, clear the text fields
                imageViewer.clearTextFields();
            }
        }
    }

    // If the mouse leaves the image panel
    // clear the text fields too
    @Override
    public void mouseExited(MouseEvent e) {
        imageViewer.clearTextFields();
    }

    // --- Unused methods --- //
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
}