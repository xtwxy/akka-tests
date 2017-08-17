package resttest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

/**
 *
 * @author wangxy
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        labelUrl = new javax.swing.JLabel();
        url = new javax.swing.JTextField();
        labelMethod = new javax.swing.JLabel();
        method = new javax.swing.JComboBox();
        labelContentType = new javax.swing.JLabel();
        contentType = new javax.swing.JComboBox();
        labelHeaders = new javax.swing.JLabel();
        headersPane = new javax.swing.JScrollPane();
        headers = new javax.swing.JTextArea();
        labelBody = new javax.swing.JLabel();
        bodyPane = new javax.swing.JScrollPane();
        body = new javax.swing.JTextArea();
        sendButton = new javax.swing.JButton();
        labelResponse = new javax.swing.JLabel();
        responsePane = new javax.swing.JScrollPane();
        response = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RESTful WebService Test");
        setName("RESTful WebService Test");
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {0, 5, 0, 5, 0, 5, 0};
        layout.rowHeights = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0};
        getContentPane().setLayout(layout);

        labelUrl.setDisplayedMnemonic('U');
        labelUrl.setText("URL:");
        labelUrl.setName("labelUrl");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(labelUrl, gridBagConstraints);

        url.setText("http://192.168.0.162:9999/resources/tables/SESSION");
        url.setName("url");
        url.setPreferredSize(new java.awt.Dimension(350, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(url, gridBagConstraints);

        labelMethod.setDisplayedMnemonic('M');
        labelMethod.setText("请求方法(M):");
        labelMethod.setName("labelMethod");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(labelMethod, gridBagConstraints);

        method.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "TRACE" }));
        method.setName("method");
        method.setPreferredSize(new java.awt.Dimension(120, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(method, gridBagConstraints);

        labelContentType.setDisplayedMnemonic('C');
        labelContentType.setText("数据格式(C):");
        labelContentType.setName("labelContentType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(labelContentType, gridBagConstraints);

        contentType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "application/json", "application/xml", "text/html", "text/plain" }));
        contentType.setPreferredSize(new java.awt.Dimension(160, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(contentType, gridBagConstraints);

        labelHeaders.setDisplayedMnemonic('H');
        labelHeaders.setText("请求头(H):");
        labelHeaders.setToolTipText("");
        labelHeaders.setName("labelHeaders");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(labelHeaders, gridBagConstraints);

        headersPane.setName("headers");

        headers.setColumns(20);
        headers.setRows(5);
        headersPane.setViewportView(headers);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(headersPane, gridBagConstraints);

        labelBody.setDisplayedMnemonic('B');
        labelBody.setText("请求体(B):");
        labelBody.setName("labelBody");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(labelBody, gridBagConstraints);

        bodyPane.setName("");

        body.setColumns(20);
        body.setRows(5);
        bodyPane.setViewportView(body);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(bodyPane, gridBagConstraints);

        sendButton.setMnemonic('S');
        sendButton.setText("发送(S)请求");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(sendButton, gridBagConstraints);

        labelResponse.setDisplayedMnemonic('R');
        labelResponse.setText("响应(R)体:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        getContentPane().add(labelResponse, gridBagConstraints);

        response.setColumns(20);
        response.setRows(10);
        responsePane.setViewportView(response);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(responsePane, gridBagConstraints);

        pack();
    }

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {
        doLayout();
    }

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	HashMap<String, String> headersMap = new HashMap<String, String>();
    	BufferedReader br = new BufferedReader(new StringReader(headers.getText()));
    	try {
	    	while(true) {
	    		String line = br.readLine();
	    		if(line == null) {
	    			break;
	    		}
	    		String[] header = line.split(":");
	    		if(header.length == 2) {
	    			headersMap.put(header[0], header[1]);
	    		} else {
	    			// invalid header.
	    			response.setText("请求头无效。");
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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration
    private javax.swing.JTextArea body;
    private javax.swing.JScrollPane bodyPane;
    private javax.swing.JComboBox contentType;
    private javax.swing.JTextArea headers;
    private javax.swing.JScrollPane headersPane;
    private javax.swing.JLabel labelBody;
    private javax.swing.JLabel labelContentType;
    private javax.swing.JLabel labelHeaders;
    private javax.swing.JLabel labelMethod;
    private javax.swing.JLabel labelResponse;
    private javax.swing.JLabel labelUrl;
    private javax.swing.JComboBox method;
    private javax.swing.JTextArea response;
    private javax.swing.JScrollPane responsePane;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextField url;
    // End of variables declaration
}
