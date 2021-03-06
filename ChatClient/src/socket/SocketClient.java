package socket;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import frame.Denglu;

public class SocketClient extends Thread{
	
	Socket socket;
	Denglu myframe;
	JTextArea textarea;
	PrintWriter printWriter;
    BufferedReader bufferedReader; 
	
    public SocketClient(Denglu fram) throws Exception  {
    	myframe=fram;
    	textarea = fram.getJTextArea();
		try {
				socket = new Socket("127.0.0.1", 2013);
				printWriter = new PrintWriter(socket.getOutputStream());
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		Thread thread = new Thread(this);
		thread.start();
		
	}
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void run() {
    	
    	JComboBox box = myframe.getJCombobox();
    	
    	while(true) {
    		String tmp="";
        	try {
        		while(socket.getInputStream().available() <= 0) {
        			try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
        		}
        		
				tmp=bufferedReader.readLine();
				String[] mail = tmp.split(" ");
				
				if(mail.length>1) {
					
					if("signin".equals(mail[0])&&"succeed".equals(mail[1])) {
							textarea.setText("登陆成功!\n");
							myframe.getJButton1().setEnabled(false);			//将登陆按钮设置成不可按状态
							myframe.getJButton2().setEnabled(false);			//将注册按钮设置成不可按状态
					}
					
					else if("signin".equals(mail[0])&&"failed".equals(mail[1])) {
						JOptionPane.showMessageDialog(null, "用户名或密码错误", "消息", JOptionPane.ERROR_MESSAGE);
//						textarea.setText("登陆失败!\n");
					}
					
					else if("signup".equals(mail[0])&&"succeed".equals(mail[1])) {
						textarea.setText("注册成功\n");
						myframe.getJButton2().setEnabled(false);				//将注册按钮设置成不可按状态
					}
					
					else if("signup".equals(mail[0])&&"failed".equals(mail[1])) {
						JOptionPane.showMessageDialog(null, "该用户名已存在", "消息", JOptionPane.ERROR_MESSAGE);
//						textarea.setText("注册失败\n");
					}
					
					else if("flush".equals(mail[0])) {
						box.removeAllItems();
						for(int i=1;i<mail.length;i++) {
							box.addItem(mail[i]);
						}
						box.addItem("所有人");
						 box.setSelectedIndex(0);
					}
					
					else {
						String str = textarea.getText();
						str  = str+"\n\n"+mail[0]+"对我说:\n"+mail[1];
					    textarea.setText(str);
					    myframe.getJscrollpane().getViewport().setViewPosition(new Point(0, 
					    		myframe.getJscrollpane().getVerticalScrollBar().getMaximum()));
					}
				}
    		} catch (IOException e) {}
    	}
    }
    

    public void Signin(String name,String passwd) {
        printWriter.println("signin "+name+" "+passwd);
		printWriter.flush();
    }
    
    
    
    public void Signup(String name,String passwd) {
			printWriter.println("signup "+name+" "+passwd);
			printWriter.flush();
    }
    
    

	public void send() throws IOException  {
		String name = myframe.getJCombobox().getSelectedItem().toString();
		String neirong = myframe.getJTextField().getText();
		String message = name+" "+neirong.replaceAll(" ", "");  //需要发送的消息
		String str = textarea.getText();
		str = str + "\n\n我对" + name+"说:\n"+neirong+"\n";
		textarea.setText(str);
        printWriter.println(message);
        printWriter.flush();
        myframe.getJTextField().setText("");
	}
	
	
	
	public void flush() {
			printWriter.println("flush");
			printWriter.flush();
	}
	
	
	public void exit() throws Exception {
		printWriter.println("exit");
		printWriter.flush();
		printWriter.close();
		bufferedReader.close();
		socket.close();
	}
}

