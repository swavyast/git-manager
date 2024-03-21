package com.ml.gitmanager.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GitManagerWindow {

    private JFrame frame;
    private JTextField urlTextField;
    private JTextField pathTextField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GitManagerWindow window = new GitManagerWindow();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GitManagerWindow() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblUrl = new JLabel("URL:");
        lblUrl.setBounds(50, 50, 61, 16);
        frame.getContentPane().add(lblUrl);

        urlTextField = new JTextField();
        urlTextField.setBounds(120, 45, 400, 26);
        frame.getContentPane().add(urlTextField);
        urlTextField.setColumns(10);

        JLabel lblPath = new JLabel("Path:");
        lblPath.setBounds(50, 100, 61, 16);
        frame.getContentPane().add(lblPath);

        pathTextField = new JTextField();
        pathTextField.setBounds(120, 95, 320, 26);
        frame.getContentPane().add(pathTextField);
        pathTextField.setColumns(10);

        JButton btnBrowsePath = new JButton("Browse");
        btnBrowsePath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(frame);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    pathTextField.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        btnBrowsePath.setBounds(450, 95, 100, 26);
        frame.getContentPane().add(btnBrowsePath);

        JButton btnClone = new JButton("Clone");
        btnClone.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String url = urlTextField.getText();
                String path = pathTextField.getText();
                try {
                    cloneRepository(url, path);
                } catch (IOException | InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        btnClone.setBounds(250, 200, 117, 29);
        frame.getContentPane().add(btnClone);
    }

    private static void cloneRepository(String url, String path) throws IOException, InterruptedException {
        // Command to clone the repository
        String[] cmd = { "git", "clone", url, path };

        // Setting up ProcessBuilder
        ProcessBuilder builder = new ProcessBuilder(cmd);
        builder.redirectErrorStream(true);

        // Starting the process
        Process process = builder.start();

        // Reading the output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // Waiting for the process to finish
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("Command execution failed with exit code " + exitCode);
        }
    }
}
