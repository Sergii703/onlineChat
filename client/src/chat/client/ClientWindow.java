package chat.client;

import chat.network.TCPConnection;
import chat.network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener{

    private static final String IP_ADDR = "";
    private static final int PORT = 1;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private final JTextArea log = new JTextArea();
    private final JTextField fieldNick = new JTextField("WOW");
    private final JTextField fieldInput = new JTextField();
    private TCPConnection connection;


    private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        log.setEditable(false);
        log.setLineWrap(true);
        fieldInput.addActionListener(this);
        add(log, BorderLayout.CENTER);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNick, BorderLayout.NORTH);
        setVisible(true);
        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (Exception e) {
            printMessage("Connection exeption: " + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = fieldInput.getText();
        if(message.equals("")) return;
        fieldInput.setText(null);
        connection.sendString(fieldNick.getText() + ": " + message);
    }

    @Override
    public void onConnectionready(TCPConnection tcpConnection) {
        printMessage("Connection ready: ");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMessage(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMessage("Connection close: ");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMessage("Connection exeption: " + e);
    }

    private synchronized void printMessage(String message){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                log.append(message + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
