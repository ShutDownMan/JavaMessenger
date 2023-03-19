/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package novofrontsd;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import message.Message;
import message.MessageType;
import message.MessageEncryption;

/**
 *
 * @author laril
 */
public class Chat extends javax.swing.JFrame {

    private ArrayList<panelChat> tabs = new ArrayList<>();

    private class ListaUsersCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof ItemUser) {
                ItemUser usuarioNovo = (ItemUser) value;
                ImageIcon icon = new ImageIcon(usuarioNovo.getCaminho());
                Image scaledIconImage = icon.getImage();
                ImageIcon scaledIcon = new ImageIcon(scaledIconImage);
                setText("  " + usuarioNovo.getNome());
                setIcon(scaledIcon);
                setFont(new Font("Sansserif", 1, 14));
                setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                setVerticalAlignment(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.LEFT);
            }
            return this;
        }
    }

    private ItemUser currentUser;
    private TCPClient client;
    private ItemUser broadcast;

    /**
     * Creates new form Chat
     */
    public Chat(ItemUser currentUser, TCPClient client) {
        initComponents();
        this.client = client;
        this.currentUser = currentUser;

        this.listaUsers.setCellRenderer(new ListaUsersCellRenderer());
        ArrayList<ItemUser> users = new ArrayList<ItemUser>();
        this.broadcast = new ItemUser("Chat Geral");
        users.add(broadcast);

        this.updateList(users);

        // send a request to the server to get the list of active users
        try {
            client.sendMessage(new Message(MessageType.LIST_USERS, null, this.currentUser.getNome(), null));
        } catch (EOFException e) {
            e.printStackTrace();
        }

        Function <Message, Void> handleText = (Message m) -> {
            String sender = m.getSender();
            ArrayList<String> recipients = m.getRecipients();
            String text = (String) m.getPayload();

            if (recipients.contains(this.currentUser.getNome())) {
                for (panelChat tab : tabs) {
                    if (tab.getNome().equals(sender)) {
                        // decrypt text before appending to chat
                        String decryptedText = MessageEncryption.decrypt(text, "KEY");

                        // append text to chat
                        tab.addChat(sender, decryptedText);
                        return null;
                    }
                }

                // panelChat newTab = new panelChat(this.currentUser, new ItemUser(sender), this.client);
                // newTab.addMessage(sender, text);
                // this.tabs.add(newTab);
                // this.jTabbedPane1.addTab(sender, newTab);
            }

            return null;

        };
        // add a callback to the client to handle the list of users
        Function<Message, Void> listUsersCallback = (Message m) -> {
            ArrayList<String> loadedUsers = (ArrayList<String>) m.getPayload();
            System.out.println("Loaded users: " + loadedUsers);
            for (String user : loadedUsers) {
                boolean found = false;
                for (ItemUser item : users) {
                    if (item.getNome().equals(user)) {
                        found = true;
                        break;
                    }
                }

                if (!found)
                    users.add(new ItemUser(user));
            }

            this.updateList(users);

            return null;
        };
        Function<Message, Void> connectCallback = (Message m) -> {
            try {
                client.sendMessage(new Message(MessageType.LIST_USERS, null, this.currentUser.getNome(), null));
            } catch (EOFException e) {
                e.printStackTrace();
            }

            return null;
        };
        // generate an event when the list is received
        client.addOn(MessageType.TEXT, handleText);
        client.addOn(MessageType.LIST_OF_USERS, listUsersCallback);
        client.addOn(MessageType.CONNECTED, connectCallback);
    }

    // Update the news list.
    private void updateList(ArrayList<ItemUser> users) {
        // Cleaning the list.
        DefaultListModel model = new DefaultListModel();
        model.clear();
        this.listaUsers.setModel(model);
        // Creating the new model.
        model = new DefaultListModel();
        int size = users.size();
        for (int i = 0; i < size; i++) {
            model.addElement(users.get(i));
        }
        this.listaUsers.setModel(model);
    }

    public void setUserName(String name) {
        userName.setText(name);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMainUser = new javax.swing.JPanel();
        userImage = new javax.swing.JLabel();
        userName = new javax.swing.JLabel();
        panelChatArea = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        chatField = new javax.swing.JTextArea();
        iconSend = new javax.swing.JLabel();
        tabbedPaneChat = new javax.swing.JTabbedPane();
        panelUsers = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaUsers = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panelMainUser.setBackground(new java.awt.Color(34, 34, 59));

        userImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon2.png"))); // NOI18N

        userName.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        userName.setForeground(new java.awt.Color(242, 233, 228));
        userName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        userName.setText("Lari");

        javax.swing.GroupLayout panelMainUserLayout = new javax.swing.GroupLayout(panelMainUser);
        panelMainUser.setLayout(panelMainUserLayout);
        panelMainUserLayout.setHorizontalGroup(
            panelMainUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainUserLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userImage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(userName)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelMainUserLayout.setVerticalGroup(
            panelMainUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainUserLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelMainUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMainUserLayout.createSequentialGroup()
                        .addComponent(userImage)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMainUserLayout.createSequentialGroup()
                        .addComponent(userName)
                        .addGap(23, 23, 23))))
        );

        panelChatArea.setBackground(new java.awt.Color(34, 34, 59));

        chatField.setBackground(new java.awt.Color(127, 131, 160));
        chatField.setColumns(20);
        chatField.setLineWrap(true);
        chatField.setRows(5);
        chatField.setPreferredSize(new java.awt.Dimension(234, 92));
        jScrollPane4.setViewportView(chatField);

        iconSend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/send2 (1).png"))); // NOI18N
        iconSend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconSendMouseClicked(evt);
            }
        });

        tabbedPaneChat.setBackground(new java.awt.Color(34, 34, 59));
        tabbedPaneChat.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        javax.swing.GroupLayout panelChatAreaLayout = new javax.swing.GroupLayout(panelChatArea);
        panelChatArea.setLayout(panelChatAreaLayout);
        panelChatAreaLayout.setHorizontalGroup(
            panelChatAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChatAreaLayout.createSequentialGroup()
                .addGroup(panelChatAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(tabbedPaneChat)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelChatAreaLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iconSend)))
                .addGap(0, 10, Short.MAX_VALUE))
        );
        panelChatAreaLayout.setVerticalGroup(
            panelChatAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChatAreaLayout.createSequentialGroup()
                .addComponent(tabbedPaneChat)
                .addGroup(panelChatAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelChatAreaLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(iconSend))
                    .addGroup(panelChatAreaLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        panelUsers.setBackground(new java.awt.Color(34, 34, 59));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(262, 412));
        jScrollPane1.setWheelScrollingEnabled(false);

        listaUsers.setBackground(new java.awt.Color(51, 51, 89));
        listaUsers.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        listaUsers.setForeground(new java.awt.Color(255, 255, 255));
        listaUsers.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listaUsers.setPreferredSize(new java.awt.Dimension(262, 412));
        listaUsers.setSelectionBackground(new java.awt.Color(119, 130, 209));
        listaUsers.setSelectionForeground(new java.awt.Color(0, 0, 0));
        listaUsers.setValueIsAdjusting(true);
        listaUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaUsersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(listaUsers);

        javax.swing.GroupLayout panelUsersLayout = new javax.swing.GroupLayout(panelUsers);
        panelUsers.setLayout(panelUsersLayout);
        panelUsersLayout.setHorizontalGroup(
            panelUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelUsersLayout.setVerticalGroup(
            panelUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUsersLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelUsers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelMainUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelChatArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelChatArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelUsers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panelMainUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // close the server connection
        client.stop();
    }//GEN-LAST:event_formWindowClosed

    private void iconSendMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_iconSendMouseClicked
        // Enviar a mensagem para o user selecionado no tabbed pane
        // Atualizar o panelChat
        int index = this.tabbedPaneChat.getSelectedIndex();
        String title = this.tabbedPaneChat.getTitleAt(index);
        panelChat panel = encontrarPanelUser(title);
        // panel.setChat(this.chatField.getText());

        // recipients
        ArrayList<String> recipients = new ArrayList<>();

        // add each one selected
        for (int i = 0; i < this.listaUsers.getSelectedValuesList().size(); i++) {
            recipients.add(this.listaUsers.getSelectedValuesList().get(i).getNome());
        }

        // encrypt the message before sending
        String encryptedMessage = MessageEncryption.encrypt(this.chatField.getText(), "KEY");

        // Criar a mensagem
        Message msg = new Message(MessageType.TEXT, encryptedMessage, this.currentUser.getNome(), recipients);

        // Enviar a mensagem para o servidor
        try {
            client.sendMessage(msg);
        } catch (Exception e) {
            System.out.println("Erro ao enviar a mensagem");
        }

        // Limpar o campo de texto
        this.chatField.setText("");

    }// GEN-LAST:event_iconSendMouseClicked

    private panelChat encontrarPanelUser(String nome) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).getNome() == nome) {
                return tabs.get(i);
            }
        }
        return null;
    }

    private void listaUsersMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_listaUsersMouseClicked
        String nome = listaUsers.getSelectedValue().getNome();
        if (encontrarPanelUser(nome) == null) {
            panelChat novoPanel = new panelChat(nome);
            tabs.add(novoPanel);
            this.tabbedPaneChat.add(nome, novoPanel);
            // click on new tab
            this.tabbedPaneChat.setSelectedIndex(this.tabbedPaneChat.indexOfTab(nome));
            // add onclick event to the tab
            this.tabbedPaneChat.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int index = tabbedPaneChat.getSelectedIndex();
                    String title = tabbedPaneChat.getTitleAt(index);
                    panelChat panel = encontrarPanelUser(title);
                }
            });
        } else {
            this.tabbedPaneChat.setSelectedIndex(this.tabbedPaneChat.indexOfTab(nome));
        }
    }// GEN-LAST:event_listaUsersMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatField;
    private javax.swing.JLabel iconSend;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList<ItemUser> listaUsers;
    private javax.swing.JPanel panelChatArea;
    private javax.swing.JPanel panelMainUser;
    private javax.swing.JPanel panelUsers;
    private javax.swing.JTabbedPane tabbedPaneChat;
    private javax.swing.JLabel userImage;
    private javax.swing.JLabel userName;
    // End of variables declaration//GEN-END:variables
}
