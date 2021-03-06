import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * PreviewPanel.java
 * preview panel class which holds all the components in the preview panel
 * @author Kyle To
 * @author Angelina Zhang
 * @author Michael Tatsiopoulos
 * @author Eric Ke
 * created 2018-11-20
 * last modified 2018-12-07
 */

public class PreviewPanel extends JPanel {
    private OratioDisplay display;
    private TitledBorder title;
    private GridBagConstraints c;
    private JLabel imageLabel;
    private OratioDEQueue<MouthShape> previewQueue = new OratioDEQueue<>();
    private MouthShape current;
    private BufferedImage currentFrame;


    // Constructor

    /**
     * PreviewPanel
     * @param display
     * @param constraints
     */
    public PreviewPanel(OratioDisplay display, GridBagConstraints constraints) {
        super(new GridLayout());
        this.display = display;
        c = constraints;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.gridy = 0;
        title = BorderFactory.createTitledBorder("Preview");
        this.setBorder(title);
        imageLabel = new JLabel();
        if (previewQueue.size() == 0) {
            imageLabel.setIcon(display.getLauncher().getAvatar().getImageIcon());
        }
        this.add(imageLabel);
        display.getContentPane().add(this, c);
    }

    // Set the queue for mouth shapes
    public void setQueue(OratioDEQueue<MouthShape> queue) {
        this.previewQueue = queue;
    }


    /**
     * animate
     * @param queue
     */
    public void animate(OratioDEQueue queue) {
        String outputPath = "output\\animation.gif";
        System.out.println(queue.size());
        File file = new File(outputPath);
        if (file.exists()) {
            file.delete();
        }
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(outputPath);
        encoder.setDelay(200);
        encoder.setRepeat(0);
        while(!previewQueue.isEmpty()) {
            current = previewQueue.pollFirst();
            System.out.println(current.getFileName());
            try {
                currentFrame = ImageIO.read(new File(current.getFileName()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            encoder.addFrame(currentFrame);
        }
        encoder.finish();
        imageLabel.setIcon(new ImageIcon(outputPath));
    }
}
