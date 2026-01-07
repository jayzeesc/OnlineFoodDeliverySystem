import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

public final class ReceiptPanel extends JPanel {
    private final JTextArea text = new JTextArea();

    public ReceiptPanel(AppNavigator nav) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        text.setEditable(false);
        text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(new JScrollPane(text), BorderLayout.CENTER);

        JButton back = new JButton("Back");
        JButton save = new JButton("Save");
        back.setPreferredSize(UiSizes.BUTTON);
        save.setPreferredSize(UiSizes.BUTTON);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.add(save);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        back.addActionListener(e -> nav.show(AppNavigator.RESTAURANTS));

        save.addActionListener(e -> {
            try {
                File f = new File("receipt_" + System.currentTimeMillis() + ".txt");
                Files.writeString(f.toPath(), text.getText());
                SwingUtil.info(this, "Saved: " + f.getName());
            } catch (Exception ex) {
                SwingUtil.error(this, "Failed to save receipt");
            }
        });
    }

    public void setReceipt(String receipt) {
        text.setText(receipt == null ? "" : receipt);
        text.setCaretPosition(0);
    }
}
