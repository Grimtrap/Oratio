import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * InputPanel.java
 * input panel class which holds all the components in the input panel
 * @author Kyle To
 * created 2018-11-20
 * last modified 2018-11-21
 */

public class InputPanel extends JPanel{
    private TitledBorder title;
    private GridBagConstraints c;
    private JButton animateButton;
    private JTextField textField;
    private JLabel output;
    private JButton reAnimateButton;
    private JButton resetButton;
    private String text;
    private String phoneticSpelling;
    private OratioDisplay display;

    // Constructor
    public InputPanel(OratioDisplay display, GridBagConstraints constraints){
        super(new GridBagLayout());
        c = constraints;

        this.display = display;

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 0.05;
        c.anchor = GridBagConstraints.PAGE_END;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        c.gridheight = 1;
        title = BorderFactory.createTitledBorder("Input"); //creates titled border
        this.setBorder(title);

        display.getContentPane().add(this, c);

        // Creates initial animate button
        animateButton = createAnimateButton(this);
        this.add(animateButton, c);

        // Creates initial text field
        textField = createTextField(this);
        this.add(textField, c);
    }

    // Method for creating the text label for output
    private JLabel createTextLabel(JPanel panel){
        output = new JLabel();
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        return output;
    }

    // Method that creates animate button
    private JButton createAnimateButton(JPanel panel){
        animateButton = new JButton("Animate");
        c.weightx = 0.5;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        animateButton.addActionListener(display.getLauncher().getInputPanelListener());
        return animateButton;
    }

    // method that switches input panel. this method is called when the animateButton is pressed.
    public void switchInputPanel(String[] phoneticSpellings) {
        text = textField.getText();

        // Translates the split words into their phonetic spelling and recombines them into one string
        phoneticSpelling = "";
        for (String s : phoneticSpellings){
            phoneticSpelling = phoneticSpelling + " " + s;
        }

        // Creates text label with both input and phonetic spelling
        output = createTextLabel(this);
        output.setText(text + " " + phoneticSpelling + "  ");
        this.add(output);

        // remove old components
        this.remove(animateButton);
        this.remove(textField);

        reAnimateButton = createReAnimateButton(this);
        this.add(reAnimateButton,c);

        resetButton = createResetButton(this);
        this.add(resetButton, c);
        this.validate();
        this.repaint();
    }

    // Method that creates text field
    private JTextField createTextField(JPanel panel){
        textField = new JTextField();
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        textField.addActionListener(display.getLauncher().getInputPanelListener());
        return textField;
    }

    // Method that creates reanimate button
    private JButton createReAnimateButton(JPanel panel){
        reAnimateButton = new JButton("Reanimate");
        c.weightx = 0.5;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        return reAnimateButton;
    }

    // Method that creates reset button
    private JButton createResetButton(JPanel panel){
        resetButton = new JButton("Reset");
        c.weightx = 0.5;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;

        resetButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent E){
                panel.remove(output);
                panel.remove(reAnimateButton);
                panel.remove(resetButton);
                textField = createTextField(panel);
                panel.add(textField,c);
                animateButton = createAnimateButton(panel);
                panel.add(animateButton, c);
                panel.validate();
                panel.repaint();
            }
        });
        return resetButton;
    }

    // Method for getting the text field
    public JTextField getTextField() {
        return textField;
    }

    public JButton getAnimateButton() {
        return animateButton;
    }
}
