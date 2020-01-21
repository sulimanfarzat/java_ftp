import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;


public class FtpClient {

	private JFrame frame;
	private JTextField textField;
	static Socket socket;
	String strLink; 		// for file chooser
	static InputStream in;
	static OutputStream out;
	
	private JLabel lblDragDrop;
	String strLinkDragDrop; 		// for file chooser
	InputStream inputStream;
	

	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FtpClient window = new FtpClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		// client
		socket = new Socket("127.0.0.1", 4002);
		
		
	}

	
	
	
	
	
	
	/**
	 * Create the application.
	 */
	public FtpClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(UIManager.getColor("Button.highlight"));
		frame.setBounds(100, 100, 703, 347);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblSelectFile = new JLabel("Select file :");
		lblSelectFile.setForeground(UIManager.getColor("Label.foreground"));
		lblSelectFile.setFont(new Font("Dialog", Font.BOLD, 16));
		lblSelectFile.setBounds(12, 24, 110, 30);
		frame.getContentPane().add(lblSelectFile);
		
		textField = new JTextField();
		textField.setBounds(118, 24, 349, 30);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		
		
		// Browse button 
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBorder(null);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(btnBrowse); 						// connect file browse with browse button
				strLink = fileChooser.getSelectedFile().getAbsolutePath();	// convert path to string
				textField.setText(strLink); 								// get path in the input box textField
				
			}
		});
		btnBrowse.setFont(new Font("Dialog", Font.BOLD, 16));
		btnBrowse.setForeground(Color.ORANGE);
		btnBrowse.setBackground(Color.DARK_GRAY);
		btnBrowse.setBounds(479, 24, 201, 30);
		frame.getContentPane().add(btnBrowse);
		
		
		
		// sent button
		JButton btnSend = new JButton("Send");
		btnSend.setBorder(null);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				File file = new File(strLink);								// get the file from textField
				byte[] b = new byte[16 * 1024];								// send the content from the file to the server as byte
				
				try {
					in = new FileInputStream(file);							// in to get content 
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
				try {
					out = socket.getOutputStream();							// out to send to server
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
				try {
					int count;
					while ( (count = in.read(b) )  > 0 ) {
						out.write(b, 0, count);								// print the date in test.txt
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				

				/* to send data from Drag and Drop Box from TransferHandler */
				inputStream = new ByteArrayInputStream(strLinkDragDrop.getBytes());
				//or//StringBuilder textBuilder = new StringBuilder();
				
			    try (Reader reader = new BufferedReader(new InputStreamReader
			      (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
			        int c = 0;
			        while ((c = reader.read()) != -1) {
			        	//or//textBuilder.append((char) c);
			            out.write(c);										// out to send to server
			        }
			    } catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    
			    // System.out.println(textBuilder.toString() + " " +  strLinkDragDrop);
			    /* End */

				
				
			}
		});
		
		
		
		btnSend.setForeground(Color.DARK_GRAY);
		btnSend.setFont(new Font("Dialog", Font.BOLD, 24));
		btnSend.setBackground(Color.ORANGE);
		btnSend.setBounds(54, 215, 584, 64);
		frame.getContentPane().add(btnSend);
		
		lblDragDrop = new JLabel("");
		lblDragDrop.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblDragDrop.setBackground(Color.WHITE);
		lblDragDrop.setBorder(new LineBorder(Color.DARK_GRAY));
		lblDragDrop.setBounds(118, 66, 349, 113);
		frame.getContentPane().add(lblDragDrop);
		
		JButton btnDragDrop = new JButton("allwo Drag and Drop");
		btnDragDrop.setLocation(new Point(20, 20));
		btnDragDrop.setHorizontalTextPosition(SwingConstants.LEADING);
		btnDragDrop.setBorder(null);
		btnDragDrop.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnDragDrop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				TransferHandler th = new TransferHandler() {
					
					@Override
					public boolean canImport(JComponent compo, DataFlavor[] arg1) {
						return true;
					};
					
					@Override
					public boolean importData(JComponent comp, Transferable t) {
						
						try {
							@SuppressWarnings("unchecked")
							List<File> files = (List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);
							for (File file : files) {
								System.out.println(file.getAbsoluteFile());
							}
							
							List<String> texts = Files.readAllLines(files.get(0).toPath());
							for (String text : texts) {
								lblDragDrop.setText(lblDragDrop.getText() + text + " ; ");
								strLinkDragDrop = text ;
							}
							
						} catch (Exception e) {
							// TODO: handle exception
						}

						return true;
					}
					
				};
				
				lblDragDrop.setTransferHandler(th);
				
			}
		});
		btnDragDrop.setForeground(Color.ORANGE);
		btnDragDrop.setFont(new Font("Dialog", Font.BOLD, 16));
		btnDragDrop.setBackground(Color.DARK_GRAY);
		btnDragDrop.setBounds(479, 68, 201, 111);
		frame.getContentPane().add(btnDragDrop);
		
		JLabel lblOrDragDrop = new JLabel("Or ...");
		lblOrDragDrop.setFont(new Font("Dialog", Font.BOLD, 16));
		lblOrDragDrop.setBounds(12, 114, 110, 15);
		frame.getContentPane().add(lblOrDragDrop);
		
	}
}
