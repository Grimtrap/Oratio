import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Launcher.java
 * main class which ties everything together
 * @author Joey Chik
 * @author Michael Tatsiopoulos
 * @author Angelina Zhang
 * @author Eric Ke
 * created 2018-11-15
 * last modified 2018-11-30
 */

public class Launcher {
    private final static String FILE_LOADING_ERR_MSG = "There was a problem loading the graphics.";

   private PhoneticTranslator phoneticTranslator;

    // GUI
    private OratioDisplay display;
    private String[] phoneticSpellings;
    private String[] words;
    private String preset;
    private MouthShape avatar;
    private JPanel errorMsg;
    // data structures
    private OratioTree<MouthShape> tree;
    private OratioDEQueue<MouthShape> previewQueue;
    private OratioDEQueue<MouthShape> galleryQueue;

    /**
     * main method
     * @param args idk what this actually does
     */
    public static void main(String[] args) {
        new Launcher();
    }

    /**
     * Launcher
     */
    private Launcher() {
        this.preset = "Default-Female";
        DisplayContent(null);
    }

    /**
     * DisplayContent
     * @param preset
     */
    private void DisplayContent(String preset) {
        if (preset!=null) {
            this.preset = preset;
        }
        // generate data structures
        try {
            String filePath = "resources\\Graphics\\" + this.preset + "\\meta.json"; //FIX THIS LATER!!!!
            OratioTreeGenerator treeGenerator = new OratioTreeGenerator();
            this.tree = treeGenerator.generateTreeFromJson(filePath);
            this.avatar = treeGenerator.getAvatar();
        } catch (IOException e) {
            System.err.println(FILE_LOADING_ERR_MSG);
            System.exit(1);
        }

        this.display = new OratioDisplay(this);
        OratioMenuBar addListenerMenu = this.display.getOratioMenuBar();
        JMenu presetMenu = addListenerMenu.getMenu(0);
        for(int i =0; i<presetMenu.getMenuComponentCount();i++){
            java.awt.Component comp = presetMenu.getMenuComponent(i);
            if(comp instanceof JMenuItem){
                JMenuItem item = (JMenuItem) comp;
                item.addActionListener(e -> {
                    String name = item.getText();
                    setPresetName(name);
                });
            }
        }
        this.display.setOratioMenuBar(addListenerMenu);
        this.phoneticTranslator = new PhoneticTranslator();
    }

    private void setPresetName(String name){
        this.display.dispose();
        DisplayContent(name);
    }

    private class InputPanelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                generatePhoneticSpelling();
            } catch (IOException exception) {
                return;
            }
            display.getInputPanel().switchInputPanel(phoneticSpellings);
            previewQueue = assembleAnimationQueue();
            galleryQueue = assembleAnimationQueue();
            display.getPreviewPanel().setQueue(previewQueue);
            display.getPreviewPanel().animate(previewQueue);
            display.getGalleryPanel().setQueue(galleryQueue);
            display.getGalleryPanel().showFrames(display.getGalleryPanel());
        }
    }

    public InputPanelListener getInputPanelListener() {
        return new InputPanelListener();
    }

    public MouthShape getAvatar() {
        return avatar;
    }

    private void generatePhoneticSpelling() throws IOException{
        String phrase = display.getInputPanel().getTextField().getText();

        words = phrase.split("\\s+|\\p{Punct}+"); // splits the string at any whitespace or punctuation

        for (String s : words) {
            System.out.println(s);
        }


        try {
            phoneticSpellings = new String[words.length];
            for (int i = 0; i < words.length; i++) {
                phoneticSpellings[i] = PhoneticTranslator.getPronounce(words[i]);
            }
        }catch (Exception e){
            errorMsg = new JPanel();
            JOptionPane.showMessageDialog(errorMsg, "Error getting word.", "Error", JOptionPane.ERROR_MESSAGE);

        }
}


    private OratioDEQueue<MouthShape> assembleAnimationQueue() {
        OratioDEQueue<MouthShape> queue = new OratioDEQueue<>();

        for (String phoneticSpelling : phoneticSpellings) {
            // splits phonetic spelling of word into individual phonemes
            String[] phonemes = phoneticSpelling.split("[^A-Z]+");
            for (String phoneme : phonemes) {
                queue.addLast(tree.get(phoneme));
            }
            queue.addLast(avatar);
        }
        return queue;
    }
}
