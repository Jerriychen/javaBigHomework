package frame;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Denglu extends JFrame{
    JTextField jTextField;            //定义文本框组件
    JPasswordField jPasswordField;    //定义密码框组件
    JPanel jp1;							//面板，用来装组件的
    JButton jb1,jb2,jb3;          //创建按钮
    @SuppressWarnings("rawtypes")
	JComboBox combobox;												//下拉式多选，将显示所有在线客户端，并选择一个与之进行通信
    Dimension faceSize = new Dimension(400, 600);  //设置一个固定大小
    ClientListener clientListener;				//监听对象
    JTextField message;							//写消息区域
    JTextArea messageShow;					//消息面板
    JButton clientMessageButton;
    JScrollPane messageScrollPane;   //滚动面板，将messageShow放入其中
    JButton flush;					//刷新按钮，后面将对次按钮添加监听
    
    public JScrollPane getJscrollpane() {
    	return messageScrollPane;
    }
    
    @SuppressWarnings("rawtypes")
	public JComboBox getJCombobox() {
    	return combobox;
    }
    
    public JTextField getJTextField() {
    	return message;
    }
    
    public JTextArea getJTextArea() {
    	return messageShow;
    }
    
    public JButton getJButton1() {
		return jb1;
    }
    
    public JButton getJButton2() {
    	return jb2;
    }
    
    public JButton getJButton3() {
    	return jb3;
    }
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void init() {
    	Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
        jTextField = new JTextField(15);
        jTextField.setToolTipText("用户名");
        jPasswordField = new JPasswordField(15);
        jPasswordField.setToolTipText("密码");
        jb1 = new JButton("登陆");
        jb2 = new JButton("注册");
        jb3 = new JButton("退出");
        jp1 = new JPanel(new GridLayout(1, 5));
        jp1.add(jTextField);
        jp1.add(jPasswordField);
        jp1.add(jb1);
        jp1.add(jb2);
        jp1.add(jb3);
        contentPane.add(jp1, BorderLayout.NORTH);
        
        messageShow = new JTextArea();
        messageShow.setFont(new   Font( "标楷体 ",Font.BOLD,16)); 
        messageShow.setLineWrap(true);//激活自动换行功能 
        messageShow.setWrapStyleWord(true);//激活断行不断字功能 
		messageShow.setEditable(false);
		//添加滚动条
		messageScrollPane = new JScrollPane(messageShow,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		messageScrollPane.setPreferredSize(new Dimension(400,400));
		messageScrollPane.revalidate();
		messageScrollPane.getViewport().setViewPosition(
				new Point(0, messageScrollPane.getVerticalScrollBar().getMaximum()));
		contentPane.add(messageScrollPane, BorderLayout.CENTER);
		
		
		message = new JTextField(20);
		clientMessageButton = new JButton("发送");
		JLabel sendToLabel = new JLabel("发送至:");
		combobox = new JComboBox();
		combobox.insertItemAt("所有人",0);
		combobox.setSelectedIndex(0);
		flush = new JButton("刷新");
		
		
		JPanel downPanel = new JPanel();
		downPanel.setLayout(new GridLayout(2,1));
		JPanel p1 = new JPanel();
		p1.add(sendToLabel);
		p1.add(combobox);
		p1.add(flush);
		JPanel p2 = new JPanel();
		p2.add(message);
		p2.add(clientMessageButton);
		downPanel.add(p1);
		downPanel.add(p2);
		contentPane.add(downPanel, BorderLayout.SOUTH);
		clientListener = new ClientListener(this);
		addWindowListener(clientListener);
    }
    
    
	public Denglu(){
    	init();
        this.pack();
        this.setSize(faceSize);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("局域网聊天");
    }
	
    
	
    public static void main(String[] args){
        new Denglu();
    }
    
    
}

