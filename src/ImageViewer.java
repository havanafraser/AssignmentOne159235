import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ImageViewer
        extends JFrame
        implements ActionListener {

    private JMenuItem openItem, quitItem;
    private ImagePanel imagePanel;
    private JTextArea xCoordField, yCoordField, rValueField, gValueField, bValueField, aValueField;

    // Image viewer constructor
    public ImageViewer() {
        super("Image Manipulator Program Thing");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Textfields for x y coordinates and argb values
        xCoordField = new JTextArea();
        yCoordField = new JTextArea();
        rValueField = new JTextArea();
        gValueField = new JTextArea();
        bValueField = new JTextArea();
        aValueField = new JTextArea();

        // Instantiate our image panel class and add this panel to the
        // content pane of the JFrame
        imagePanel = new ImagePanel(this);
        Container content = this.getContentPane();
        content.add(imagePanel);
        // Sets where I want the image to be displayed on the JPanel
        imagePanel.setBounds(20, 200, 400, 400);

        // Sets the size of the window
        this.setSize(800, 800);
        // No layout manager
        this.setLayout(null);

        // Creates JMenuBar menuBar
        JMenuBar menuBar = new JMenuBar();
        // Add menuBar to JFrame
        this.setJMenuBar(menuBar);
        //Create JMenu fileMenu
        JMenu fileMenu = new JMenu("File");
        //Add fileMenu to menuBar
        menuBar.add(fileMenu);

        //Create open menu item, add action listener and add to fileMenu
        openItem = new JMenuItem("Open");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        //Create quit menu item, add action listener and add to fileMenu
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
        xCoord.setBounds(20, 75, 100, 100);
        this.add(xCoord);
        this.add(xCoordField);
        xCoordField.setBounds(35, 115, 60, 20);

        // Y Coordinate label & text area
        JLabel yCoord = new JLabel("Y");
        yCoord.setBounds(102, 75, 100, 100);
        this.add(yCoord);
        this.add(yCoordField);
        yCoordField.setBounds(115, 115, 60, 20);


        // R Value label & text area
        JLabel rValue = new JLabel("R");
        rValue.setBounds(181, 75, 100, 100);
        this.add(rValue);
        this.add(rValueField);
        rValueField.setBounds(195, 115, 60, 20);

        // G Value label & text area
        JLabel gValue = new JLabel("G");
        gValue.setBounds(263, 75, 100, 100);
        this.add(gValue);
        this.add(gValueField);
        gValueField.setBounds(277, 115, 60, 20);

        // B Value label & text area
        JLabel bValue = new JLabel("B");
        bValue.setBounds(345, 75, 100, 100);
        this.add(bValue);
        this.add(bValueField);
        bValueField.setBounds(359, 115, 60, 20);


        // A Value label & text area
        JLabel aValue = new JLabel("A");
        aValue.setBounds(427, 75, 100, 100);
        this.add(aValue);
        this.add(aValueField);
        aValueField.setBounds(441, 115, 60, 20);

        this.setVisible(true);
    }

    public void updateTextFields(int x, int y, int r, int g, int b, int a) {
        xCoordField.setText(String.valueOf(x));
        yCoordField.setText(String.valueOf(y));
        rValueField.setText(String.valueOf(r));
        gValueField.setText(String.valueOf(g));
        bValueField.setText(String.valueOf(b));
        aValueField.setText(String.valueOf(a));
    }

    public void clearTextFields() {
        xCoordField.setText("");
        yCoordField.setText("");
        rValueField.setText("");
        gValueField.setText("");
        bValueField.setText("");
        aValueField.setText("");
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
