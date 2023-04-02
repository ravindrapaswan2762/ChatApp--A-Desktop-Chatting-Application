package chattingApplication;

import javax.swing.*; // b/c JFrame is under the swing package
import javax.swing.border.*;

import java.awt.*;// for color and all things
import java.awt.event.*; // for ActionListener
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.util.Calendar;
import java.text.*;
import java.net.*;// for socket to connect the client and server
import java.io.*;

public class Client  implements ActionListener{
	
	JTextField text;
	static JPanel mp; // main panel for chat screen
	static Box vertical = Box.createVerticalBox();
	static JFrame t = new JFrame();
	static DataOutputStream dout;
	
	Client(){
		
		t.setLayout(null); // we can close or open manually of layout.
		
		//---------------------- panel
		JPanel p1 = new JPanel();// for top frames in which we can add the all types of icons.
		p1.setBackground(new Color(7, 138, 94));
		p1.setBounds(0, 0, 456, 68);// left, right, width, Height.
		p1.setLayout(null);
		t.add(p1);
		//---------------------- left icon
		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/left33.png"));
		Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
		ImageIcon i3 = new ImageIcon(i2);
		JLabel back = new JLabel(i3);
		back.setBounds(20, 20, 25, 25);
		p1.add(back);
		
		back.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent ae){
				System.exit(0);
			}
		}
		);
		//--------------------- profile 1
		ImageIcon j1 = new ImageIcon(ClassLoader.getSystemResource("icons/p2.png"));
		Image j2 = j1.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
		ImageIcon j3 = new ImageIcon(j2);
		JLabel profile1 = new JLabel(j3);
		profile1.setBounds(50, 1, 70, 70);
		p1.add(profile1);
		//---------------------- video call icon
		ImageIcon x1 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
		Image x2 = x1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
		ImageIcon x3 = new ImageIcon(x2);
		JLabel video = new JLabel(x3);
		video.setBounds(270, 20, 25, 25);
		p1.add(video);
		//-------------------------phone call icon
		ImageIcon y1 = new ImageIcon(ClassLoader.getSystemResource("icons/call.png"));
		Image y2 = y1.getImage().getScaledInstance(23, 23, Image.SCALE_DEFAULT); // icon size
		ImageIcon y3 = new ImageIcon(y2);
		JLabel call = new JLabel(y3);
		call.setBounds(330, 20, 25, 25); //icon location
		p1.add(call);
		//------------------------- more icon
		ImageIcon a1 = new ImageIcon(ClassLoader.getSystemResource("icons/3dot.png"));
		Image a2 = a1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT); // icon size
		ImageIcon a3 = new ImageIcon(a2);
		JLabel more = new JLabel(a3);
		more.setBounds(380, 20, 25, 25); //icon location
		p1.add(more);
		//-------------------------- profile name and status
		JLabel name = new JLabel("Virat");
		name.setBounds(110, 15, 100, 20);
		name.setForeground(Color.WHITE);
		name.setFont(new Font("Tahoma", Font.BOLD, 16));
		p1.add(name);
		
		JLabel status = new JLabel("Online");
		status.setBounds(110, 35, 100, 20);
		status.setForeground(Color.WHITE);
		status.setFont(new Font("Tahoma", Font.BOLD, 12));
		p1.add(status);
		
		//-------------------------------------------------------------- now chat area panel
		mp = new JPanel();
		mp.setBounds(5, 75, 440, 500);// from-left, from-top, Breath, Height 
		t.add(mp);
		
		text = new JTextField();
		text.setBounds(5, 575, 310, 35);
		text.setFont(new Font("Tohima", Font.PLAIN, 14));
		t.add(text);
		
		JButton send = new JButton("Send");
		send.setBounds(320, 575, 125, 34);//320, 575, 125, 34
		send.setBackground(new Color(37, 140, 90));
		send.setForeground(Color.WHITE);
		send.setFont(new Font("Tohima", Font.PLAIN, 14));
		send.addActionListener(this); // invoking actionPerformed function
		t.add(send);
		
		
		
		
		
		
		t.setSize(450, 650); // screen size
		t.setLocation(800, 50);// 800, 70, // location for opening on window
		t.setUndecorated(true);
		t.getContentPane().setBackground(Color.white);
		
		t.setVisible(true);
		
		
	}
	
	public void actionPerformed(ActionEvent ae) {
		try {
			String out = text.getText();
			
			JPanel p2 = formatLabel(out); // for after send clean the text area.
			
			mp.setLayout(new BorderLayout());// setting border on whole screen
			
			//----------------------------------------------adding sent message on right
			JPanel right = new JPanel(new BorderLayout());
			right.add(p2, BorderLayout.LINE_END);
			vertical.add(right);
			vertical.add(Box.createVerticalStrut(15));// in box manner
			mp.add(vertical, BorderLayout.PAGE_START);
			//-----------------------------------------------
			
			dout.writeUTF(out);
			
			text.setText("");
			
			
			t.repaint();
			t.invalidate();
			t.validate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static JPanel formatLabel(String out) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JLabel output = new JLabel("<html><p style=\"width: 150px\">" +out+ "</p></html>");
		output.setFont(new Font("Tahoma", Font.PLAIN, 16));
		output.setBackground(new Color(37, 140, 90));
		output.setOpaque(true);
		output.setBorder(new EmptyBorder(15, 15, 15, 50));
		panel.add(output);
		
		//----------------------------------setting time with each msg
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		JLabel time = new JLabel();
		time.setText(sdf.format(cal.getTime()));
		panel.add(time);
		//-----------------------------------
		
		return panel;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Client();
		
		try {
			Socket s = new Socket("127.0.0.1", 6001);
			DataInputStream din = new DataInputStream(s.getInputStream());
			dout = new DataOutputStream(s.getOutputStream());
			
			while(true) {
				mp.setLayout(new BorderLayout());
				
				String msg = din.readUTF();
				JPanel panel = formatLabel(msg);
				JPanel left = new JPanel(new BorderLayout());
				left.add(panel, BorderLayout.LINE_START);
				vertical.add(left);
				
				vertical.add(Box.createHorizontalStrut(15));
				mp.add(vertical, BorderLayout.PAGE_START);
				t.validate();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
