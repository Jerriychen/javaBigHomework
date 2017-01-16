package frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import socket.SocketClient;

public class ClientListener extends WindowAdapter implements ActionListener  {
	
	Denglu frame;
	SocketClient socketclient;
	
	public ClientListener(Denglu frame) {
		this.frame=frame;
		frame.jb1.addActionListener(this);
		frame.jb2.addActionListener(this);
		frame.jb3.addActionListener(this);
		frame.combobox.addActionListener(this);
		frame.clientMessageButton.addActionListener(this);
		frame.flush.addActionListener(this);
		try {
			socketclient = new SocketClient(frame);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void windowClosing(WindowEvent e)  //关闭窗口时同时断开socket连接
    {
		try {
			socketclient.exit();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        System.exit(0);
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if("注册".equals(e.getActionCommand())) {
			String name = frame.jTextField.getText();
			String passwd = String.valueOf(frame.jPasswordField.getPassword());
			socketclient.Signup(name, passwd);
		}
		
		
		 if("登陆".equals(e.getActionCommand())) {
			 String name = frame.jTextField.getText();
			 String passwd = String.valueOf(frame.jPasswordField.getPassword());
			 socketclient.Signin(name, passwd);
		}
		 
		 
		 if("刷新".equals(e.getActionCommand())) {
				 socketclient.flush();
		 }
		 
		 
		 if("发送".equals(e.getActionCommand())) {
			 try {
				 socketclient.send();
			 } catch (Exception e1) {
				 e1.printStackTrace();
			 }
		 }
		 
		 
		 if("退出".equals(e.getActionCommand())) {
			try {
				socketclient.exit();
				System.exit(0);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		 
	}
	
}