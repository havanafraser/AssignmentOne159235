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
    private Boolean insideImage = false;

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
        if (image == null) {
            nullImage();
        }
        try {
            //inverses all transforms
            AffineTransform inverseTransform = allTransforms.createInverse();
            AffineTransformOp transformed = new AffineTransformOp(inverseTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = transformed.filter((BufferedImage) image, null);
            //reset alltransforms to record the next set of transformations on the image
            allTransforms = new AffineTransform();

            //repaint the image w/ the original orientaation info
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
            transformed = AffineTransform.getScaleInstance(1, -1);
            transformed.translate(0, -image.getHeight(null));
            allTransforms.concatenate(transformed);
            AffineTransformOp op = new AffineTransformOp(transformed, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);
            this.repaint();
        }
    }

    //flips image by Y axis and repaints
    public void flipY() {
        if (image == null) {
            nullImage();
        } else {
            transformed = AffineTransform.getScaleInstance(-1, 1);
            transformed.translate(-image.getWidth(null), 0);
            allTransforms.concatenate(transformed);
            AffineTransformOp op = new AffineTransformOp(transformed, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);
            this.repaint();
        }
    }

    //flips image by X & Y axis and repaints
    public void flipXY() {
        if (image == null) {
            nullImage();
        } else {
            transformed = AffineTransform.getScaleInstance(-1, -1);
            transformed.translate(-image.getWidth(null), -image.getHeight(null));
            allTransforms.concatenate(transformed);
            AffineTransformOp op = new AffineTransformOp(transformed, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);
            this.repaint();
        }
    }

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

                // current pixel and it's argb values using bitwise and operation with 0xff
                int argb = image.getRGB(x, y);
                int a = (argb >> 24) & 0xff;
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = (argb >> 0) & 0xff;

                r = 255 - r; // new (negative) r value
                g = 255 - g; // new (negative) g value
                b = 255 - b; // new (negative) b value

                //new pixel values
                argb = (a << 24) | (r << 16) | (g << 8) | b;
                //setting images new pixel value
                image.setRGB(x, y, argb);
            }
        }
        //repainting image to display new argb values (negative or coloured depending on boolean)
        this.repaint();
    }

    public void nullImage() {
        System.out.println("There is no image loaded! Please load in an image via File >> Open :)");
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (image != null) {
            int mouseX = e.getX();
            int mouseY = e.getY();

            if (mouseX >= 0 && mouseX < image.getWidth() && mouseY >= 0 && mouseY < image.getHeight()) {
                int argb = image.getRGB(mouseX,mouseY);
                int a = (argb >> 24) & 0xff;
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = (argb >> 0) & 0xff;
                imageViewer.updateTextFields(mouseX,mouseY, r, g, b, a);
            } else {
                imageViewer.clearTextFields();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {
        imageViewer.clearTextFields();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }
}