import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;


public class FtpServer  {

	private JFrame frame;
	static InputStream in;
	static OutputStream out;
	static ServerSocket serverSocket;
	static Socket socket;
	
	static JLabel label_1;
	static JLabel label_2;
	static JLabel label_3;
	

	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FtpServer window = new FtpServer();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
		// config server
		serverSocket = new ServerSocket(4002);	// insert port as integer

		socket = serverSocket.accept();
		label_2.setText("new Client accepted");	// print text in label when Client accepted
		label_2.setForeground(Color.ORANGE);

		
				
		in = socket.getInputStream(); 			// open channel and accept data from client side
		out = new FileOutputStream("test.txt"); // receive the date in the test.txt (without content)
		
		// get the content from the received file
		byte[] b = new byte[16 * 1024];
		
		int count;
		while ( (count = in.read(b) ) > 0 ) {
			out.write(b, 0, count);  			// print the date in test.txt
			label_3.setText("new file received !");	// print in the label when file received
			label_3.setForeground(Color.ORANGE);
		}
		
		

	}
	
	
	

	/**
	 * Create the application.
	 */
	public FtpServer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setBackground(UIManager.getColor("Button.highlight"));
		frame.setBounds(100, 100, 481, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblStatus = new JLabel("Status :");
		lblStatus.setFont(new Font("Dialog", Font.BOLD, 16));
		lblStatus.setForeground(Color.WHITE);
		lblStatus.setBounds(46, 104, 70, 15);
		frame.getContentPane().add(lblStatus);
		
		JLabel lblClientConnection = new JLabel("Client Connection :");
		lblClientConnection.setForeground(Color.WHITE);
		lblClientConnection.setFont(new Font("Dialog", Font.BOLD, 16));
		lblClientConnection.setBounds(46, 148, 194, 15);
		frame.getContentPane().add(lblClientConnection);
		
		JLabel lblRecievedFiles = new JLabel("Recieved Files :");
		lblRecievedFiles.setForeground(Color.WHITE);
		lblRecievedFiles.setFont(new Font("Dialog", Font.BOLD, 16));
		lblRecievedFiles.setBounds(46, 196, 171, 15);
		frame.getContentPane().add(lblRecievedFiles);
		
		Panel panel = new Panel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(20);
		panel.setBackground(Color.DARK_GRAY);
		panel.setBounds(0, 0, 481, 69);
		frame.getContentPane().add(panel);
		
		JLabel label = new JLabel("Server");
		panel.add(label);
		label.setForeground(Color.WHITE);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Dialog", Font.BOLD, 24));
		
		
		label_1 = new JLabel("server running ...");
		label_1.setFont(new Font("Dialog", Font.BOLD, 16));
		label_1.setForeground(new Color(50, 205, 50));
		label_1.setBounds(231, 104, 207, 15);
		frame.getContentPane().add(label_1);
		
		label_2 = new JLabel("no new Client");
		label_2.setFont(new Font("Dialog", Font.BOLD, 16));
		label_2.setBounds(231, 148, 207, 15);
		frame.getContentPane().add(label_2);
		
		label_3 = new JLabel("no files received");
		label_3.setFont(new Font("Dialog", Font.BOLD, 16));
		label_3.setBounds(231, 196, 207, 15);
		frame.getContentPane().add(label_3);
		
	
	}
}
