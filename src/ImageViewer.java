import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.nio.Buffer;

// Recommended to make your program class a JFrame
public class ImageViewer
        extends JFrame
        implements ActionListener {
    private JMenuItem openItem, quitItem;
    private ImagePanel imagePanel;
    private JTextArea xCoordField, yCoordField, rValueField, gValueField, bValueField, aValueField;
    public ImageViewer() {
        super("Image Manipulator Program Thing");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Instantiate our image panel class and add this panel to the
        // content pane of the JFrame
        imagePanel = new ImagePanel();
        Container content = this.getContentPane();
        content.add(imagePanel);
        // Sets where I want the image to be displayed
        imagePanel.setBounds(20, 200, 400, 400);

        // Sets the size of the window
        this.setSize(800, 800);
        // No layout manager
        this.setLayout(null);

        // Creates JMenuBar and adds this to the JFrame
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        //"Open" Menu item and action listener
        openItem = new JMenuItem("Open");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        //"Quit" Menu item and action listener
        quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(this);
        fileMenu.add(quitItem);


        // Original orienation button
        JButton originalOrientation = new JButton("O");
        originalOrientation.setBounds(50, 5, 80, 40);
        //Add action listener to button
        originalOrientation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imagePanel.originalOrientation();
            }
        });

        // FlipX Button
        JButton flipX = new JButton("flipX");
        flipX.setBounds(155, 5, 80, 40);
        //Add action listener to button
        flipX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imagePanel.flipX();
            }
        });

        // FlipY Button
        JButton flipY = new JButton("flipY");
        flipY.setBounds(255, 5, 80, 40);
        //Add action listener to button
        flipY.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imagePanel.flipY();
            }
        });

        // FlipXY Button
        JButton flipXY = new JButton("flipXY");
        flipXY.setBounds(355, 5, 80, 40);
        //Add action listener to button
        flipXY.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imagePanel.flipXY();
            }
        });

        // Negate button
        JButton negate = new JButton("negate");
        negate.setBounds(455, 5, 80, 40);
        negate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    imagePanel.negateImage();
            }
        });

        // Adding of buttons to JPanel
        this.add(originalOrientation);
        this.add(flipX);
        this.add(flipY);
        this.add(flipXY);
        this.add(negate);


        // X Coordinate label & text area
        JLabel xCoord = new JLabel("X");
        xCoord.setBounds(20,75,100, 100);
        this.add(xCoord);
        xCoordField = new JTextArea();
        this.add(xCoordField);
        xCoordField.setBounds(35, 115, 60,20);

        // Y Coordinate label & text area
        JLabel yCoord = new JLabel("Y");
        yCoord.setBounds(102, 75, 100, 100);
        this.add(yCoord);
        yCoordField = new JTextArea();
        this.add(yCoordField);
        yCoordField.setBounds(115,115, 60, 20);


        // R Value label & text area
        JLabel rValue = new JLabel ("R");
        rValue.setBounds(184, 75, 100, 100);
        this.add(rValue);
        rValueField = new JTextArea();
        this.add(rValueField);
        rValueField.setBounds(195, 115, 60, 20);

        // G Value label & text area
        JLabel gValue = new JLabel ("G");
        gValue.setBounds(266, 75, 100, 100);
        this.add(gValue);
        gValueField = new JTextArea();
        this.add(gValueField);
        gValueField.setBounds(277, 115, 60, 20);

        // B Value label & text area
        JLabel bValue = new JLabel ("B");
        bValue.setBounds(348, 75, 100, 100);
        this.add(bValue);
        bValueField = new JTextArea();
        this.add(bValueField);
        bValueField.setBounds(359, 115, 60, 20);


        // A Value label & text area
        JLabel aValue = new JLabel ("A");
        aValue.setBounds(430, 75, 100, 100);
        this.add(aValue);
        aValueField = new JTextArea();
        this.add(aValueField);
        aValueField.setBounds(441, 115, 60, 20);

        this.setVisible(true);
    }

// File chooser listener for File >> Open event
// Sends chosen file to imagePanel.getImage();
public void actionPerformed(ActionEvent event) {
        JComponent source = (JComponent) event.getSource();
        if (source == openItem) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                imagePanel.getImage(selectedFile);
            }
        } else if (source == quitItem) {
            System.exit(0);
        }
    }


}
