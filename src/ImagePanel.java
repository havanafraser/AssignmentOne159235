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
public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener {

    private BufferedImage image;
    private AffineTransform transformed;
    private AffineTransform allTransforms = new AffineTransform();
    private int imageX = 0;
    private int imageY = 0;
    int imageWidth = 0;
    int imageHeight = 0;

    private JTextField xField, yField, aField, rField, gField, bField;
    public ImagePanel() {

        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(200, 200));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    // A method to read an image from a file handle object. Note here
    // that we MUST handle the IOException
    public void getImage(File file) {
        try {
            image = ImageIO.read(file);
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();
            this.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateImageCoordinates() {
        imageX = (getWidth() - imageWidth) / 2;
        imageY = (getHeight() - imageHeight) / 2;
        this.repaint();
    }

    // This gets called by the JVM whenever it needs to do so. For
    // example, when rendering a JFrame, this method will be called
    // for all JPanels that have been added to the JFrame. Here we
    // override the paintComponent() method in the parent class (which
    // actually does nothing)
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // If there is an image to draw, then draw it!
        if (image != null) {
            // Call the appropriate g2 methods;
            imageX = (getWidth() - image.getWidth()) / 2;
            imageY = (getHeight() - image.getHeight()) / 2;
            super.paintComponent(g2);
            g2.drawImage(image,0,0,this);
            this.revalidate();
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
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent event) { if (image == null) {

    } else {
        //public void mouseMotionListener(new MouseAdapter() {
        //public void mouseMoved(MouseEvent e) {
        //int packedInt = image.getRGB(e.getX(), e.getY());
        //Color color = new;
        //}
        //});
        //System.out.println("I have entered the image");
        }
    }
    @Override
    public void mouseExited(MouseEvent event) {
        if (image == null) {

        } else {
            //System.out.println("I have left the image");
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (image != null) {
            Point currentPixel = e.getPoint();
            if(overImage(currentPixel)) {
                int tempX = currentPixel.x - imageX;
                int tempY = currentPixel.y - imageY;

                int argb = image.getRGB(tempX,tempY);
                int a = (argb >> 24) & 0xff;
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = (argb >> 0) & 0xff;

                //System.out.println("A: " + a);
                //System.out.println("R: " + r);
                //System.out.println("G: " + g);
                //System.out.println("B: " + b);
                //System.out.println("X: " + tempX);
                //System.out.println("Y: " + tempY);

            }
        } else {

        }
    }

    public boolean overImage(Point currentPixel) {
        return currentPixel.x >= imageX && currentPixel.x < imageX + imageWidth && currentPixel.y >= imageY && currentPixel.y < imageY + imageHeight;
    }
}