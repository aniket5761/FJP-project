import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class CloudDriveUI {
    private JFrame frame;
    private JButton uploadButton, downloadButton, listButton;
    private JTextArea fileListArea;
    private FileStorage storage;

    public CloudDriveUI() {
        storage = new FileStorage();

        frame = new JFrame("Local Cloud Drive");
        frame.setSize(450, 400);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        uploadButton = new JButton("Upload File");
        downloadButton = new JButton("Download File");
        listButton = new JButton("List Files");
        fileListArea = new JTextArea(10, 35);
        fileListArea.setEditable(false);

        frame.add(uploadButton);
        frame.add(downloadButton);
        frame.add(listButton);
        frame.add(new JScrollPane(fileListArea));

        // Upload button -> open file chooser
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(File.listRoots()[0]); // Start from root
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (storage.uploadFile(selectedFile)) {
                    JOptionPane.showMessageDialog(frame, "Uploaded: " + selectedFile.getName());
                } else {
                    JOptionPane.showMessageDialog(frame, "Upload failed!");
                }
            }
        });

        // Download button -> choose destination folder
        downloadButton.addActionListener(e -> {
            File[] files = storage.listFiles();
            if (files == null || files.length == 0) {
                JOptionPane.showMessageDialog(frame, "No files available to download!");
                return;
            }

            // Let user pick which file to download
            String[] fileNames = new String[files.length];
            for (int i = 0; i < files.length; i++) fileNames[i] = files[i].getName();
            String chosenFile = (String) JOptionPane.showInputDialog(frame, 
                "Select file to download:", "Download", 
                JOptionPane.PLAIN_MESSAGE, null, fileNames, fileNames[0]);

            if (chosenFile == null) return;

            // Choose destination folder
            JFileChooser dirChooser = new JFileChooser(File.listRoots()[0]);
            dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = dirChooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File destDir = dirChooser.getSelectedFile();
                if (storage.downloadFile(chosenFile, destDir)) {
                    JOptionPane.showMessageDialog(frame, "Downloaded to: " + destDir.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(frame, "Download failed!");
                }
            }
        });

        // List files button
        listButton.addActionListener(e -> {
            File[] files = storage.listFiles();
            fileListArea.setText("");
            if (files != null && files.length > 0) {
                for (File file : files) {
                    fileListArea.append(file.getName() + "\n");
                }
            } else {
                fileListArea.setText("No files in cloud storage.");
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CloudDriveUI().show());
    }
}
