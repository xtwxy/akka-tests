package resttest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * @author wangxy
 */
public class MainFrame extends JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        bundle = ResourceBundle.getBundle("Bundle");
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        labelUrl = new JLabel();
        url = new JTextField();
        labelMethod = new JLabel();
        method = new JComboBox();
        labelContentType = new JLabel();
        labelAcceptContentType = new JLabel();
        contentType = new JComboBox();
        acceptContentType = new JComboBox();
        labelHeaders = new JLabel();
        headersPane = new JScrollPane();
        headers = new JTextArea();
        labelBody = new JLabel();
        bodyPane = new JScrollPane();
        body = new JTextArea();
        sendButton = new JButton();
        labelResponse = new JLabel();
        responsePane = new JScrollPane();
        response = new JTextArea();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(bundle.getString("app"));
        setName(bundle.getString("app"));
        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });
        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0};
        layout.rowHeights = new int[]{0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0};
        getContentPane().setLayout(layout);

        labelUrl.setDisplayedMnemonic('U');
        labelUrl.setText(bundle.getString("url"));
        labelUrl.setName("labelUrl");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        getContentPane().add(labelUrl, gridBagConstraints);

        url.setText(bundle.getString("url.default"));
        url.setName("url");
        url.setPreferredSize(new Dimension(350, 23));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        getContentPane().add(url, gridBagConstraints);

        labelMethod.setDisplayedMnemonic('M');
        labelMethod.setText(bundle.getString("request.method"));
        labelMethod.setName("labelMethod");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        getContentPane().add(labelMethod, gridBagConstraints);

        method.setModel(new DefaultComboBoxModel(new String[]{"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "TRACE"}));
        method.setName("method");
        method.setPreferredSize(new Dimension(120, 23));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        getContentPane().add(method, gridBagConstraints);

        labelContentType.setDisplayedMnemonic('C');
        labelContentType.setText(bundle.getString("content.type"));
        labelContentType.setName("labelContentType");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        getContentPane().add(labelContentType, gridBagConstraints);

        contentType.setModel(new DefaultComboBoxModel(new String[]{"application/json", "application/xml", "text/html", "text/plain"}));
        contentType.setPreferredSize(new Dimension(160, 23));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        getContentPane().add(contentType, gridBagConstraints);
        labelAcceptContentType.setDisplayedMnemonic('A');
        labelAcceptContentType.setText(bundle.getString("accept.content"));
        labelAcceptContentType.setName("labelAcceptContentType");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        getContentPane().add(labelAcceptContentType, gridBagConstraints);

        acceptContentType.setModel(new DefaultComboBoxModel(new String[]{"application/json", "application/xml", "text/html", "text/plain"}));
        acceptContentType.setPreferredSize(new Dimension(160, 23));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        getContentPane().add(acceptContentType, gridBagConstraints);

        labelHeaders.setDisplayedMnemonic('H');
        labelHeaders.setText(bundle.getString("request.headers"));
        labelHeaders.setToolTipText("");
        labelHeaders.setName("labelHeaders");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        getContentPane().add(labelHeaders, gridBagConstraints);

        headersPane.setName("headers");

        headers.setColumns(20);
        headers.setRows(5);
        headersPane.setViewportView(headers);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        getContentPane().add(headersPane, gridBagConstraints);

        labelBody.setDisplayedMnemonic('B');
        labelBody.setText(bundle.getString("request.body"));
        labelBody.setName("labelBody");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        getContentPane().add(labelBody, gridBagConstraints);

        bodyPane.setName("body");

        body.setColumns(20);
        body.setRows(5);
        bodyPane.setViewportView(body);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        getContentPane().add(bodyPane, gridBagConstraints);

        sendButton.setMnemonic('S');
        sendButton.setText(bundle.getString("send.request"));
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        getContentPane().add(sendButton, gridBagConstraints);

        labelResponse.setDisplayedMnemonic('R');
        labelResponse.setText(bundle.getString("response.body"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
        getContentPane().add(labelResponse, gridBagConstraints);

        response.setColumns(20);
        response.setRows(10);
        responsePane.setViewportView(response);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(responsePane, gridBagConstraints);

        pack();
    }

    private void formWindowStateChanged(WindowEvent evt) {
        doLayout();
    }

    private void sendButtonActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        HashMap<String, String> headersMap = new HashMap<String, String>();
        BufferedReader br = new BufferedReader(new StringReader(headers.getText()));
        try {
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                String[] header = line.split(":");
                if (header.length == 2) {
                    headersMap.put(header[0], header[1]);
                } else {
                    // invalid header.
                    response.setText(bundle.getString("invalid.headers"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setText(e.toString());
        }

        try {
            response.setText(HttpRequest.send(url.getText(),
                    method.getSelectedItem().toString(),
                    contentType.getSelectedItem().toString(),
                    acceptContentType.getSelectedItem().toString(),
                    headersMap,
                    body.getText()
            ).toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setText(e.toString());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /*
         * Create and display the form
         */
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration
    private JTextArea body;
    private JScrollPane bodyPane;
    private JComboBox contentType;
    private JComboBox acceptContentType;
    private JTextArea headers;
    private JScrollPane headersPane;
    private JLabel labelBody;
    private JLabel labelContentType;
    private JLabel labelAcceptContentType;
    private JLabel labelHeaders;
    private JLabel labelMethod;
    private JLabel labelResponse;
    private JLabel labelUrl;
    private JComboBox method;
    private JTextArea response;
    private JScrollPane responsePane;
    private JButton sendButton;
    private JTextField url;
    private ResourceBundle bundle;
    // End of variables declaration
}
