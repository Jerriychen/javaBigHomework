
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server extends ServerSocket {
	
	List<CreateServerThread> threadlist = new ArrayList<CreateServerThread>();
	
    private static final int SERVER_PORT = 2013;
    MysqlDemo sqldemo;

    public Server() throws Exception {
    	
        super(SERVER_PORT);
        sqldemo = new MysqlDemo();

        try {
            while (true) {
                Socket socket = accept();
                threadlist.add(new CreateServerThread(socket));//当有请求时，启一个线程处理
            }
        } catch (IOException e) {
        	e.printStackTrace();
        } 
    }

    //线程类
    class CreateServerThread extends Thread {
        Socket client;
        BufferedReader bufferedReader;
        PrintWriter printWriter;
        String[] conf;
        String s = null;
        int signin=1;
        int signup=1;
        
        public CreateServerThread(Socket s) throws Exception {
            client = s;
            printWriter = new PrintWriter(client.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            start();
        }

        public void run() {
        	
            try {
                while (true) {
                	String line="";
                	
                	
                	while(s==null&&client.getInputStream().available() <= 0) {
                		try {
    						sleep(100);
    					} catch (InterruptedException e) {
    						e.printStackTrace();
    					}
                	}

                	
                	if(client.getInputStream().available()>0) {
                		line = bufferedReader.readLine();
	                	if(line.length()>0) {
		                	if(line.startsWith("signin")&&signin==1) {
		                		conf = line.split(" ");
		                		System.out.println("有用户登陆");
		                		System.out.println("用户名:"+conf[1]);
		                		System.out.println("用户地址："+client.getInetAddress());
		                        System.out.println("用户端口"+client.getPort());
//		                        System.out.println();
		                    	if(sqldemo.findByNameAndPasswd(conf[1], conf[2])==true) {
		                    		this.setName(conf[1]);
		                    		printWriter.println("signin succeed");
		                    		printWriter.flush();
		                    		signin++;
		                    	}
		                    	else {
		                    		printWriter.println("signin failed");
		                    		printWriter.flush();
		                    	}
		                    }
	                	
	                    
		                	else if(line.startsWith("signup")&&signup==1) {
		                    	conf = line.split(" ");
		                    	System.out.println("有用户注册");
		                    	System.out.println("用户名:"+conf[1]);
		                		System.out.println("用户地址："+client.getInetAddress());
		                        System.out.println("用户端口"+client.getPort());
		                    	if(sqldemo.insert(conf[1], conf[2])==true) {
			                    	printWriter.println("signup succeed");
			                    	printWriter.flush();
			                    	signup++;
		                    	}
		                    	if(sqldemo.insert(conf[1], conf[2])==false) {
		                    		printWriter.println("signup failed");
			                    	printWriter.flush();
		                    	}
		                    }
	                	
	                	
		                	else if("flush".equals(line)) {
		                		String a = "flush ";
		                		for(int i=0;i<threadlist.size();i++) {
		                			if(threadlist.get(i).isAlive()) {
		                				a += threadlist.get(i).getName()+" ";
		                			}
		                		}
		                		printWriter.println(a);
		                		printWriter.flush();
		                	}
		                	
		                	else if("exit".equals(line)) {
		                		threadlist.remove(this);
		                		return;
		                	}
	                	
	                    
		                    else {
		                    	String[] neirong;
		                    	neirong = line.split(" ");
		                    	if("所有人".equals(neirong[0])) {
		                    		for(int i=0; i<threadlist.size();i++) {
			                    		if(!(threadlist.get(i).getName().equals(this.getName()))) {
			                    			threadlist.get(i).s = this.getName()+" "+neirong[1];
			                    		}
			                    	}
		                    	}
		                    	else {
		                    		for(int i=0; i<threadlist.size();i++) {
		                    			if(neirong[0].equals(threadlist.get(i).getName())) {
		                    				threadlist.get(i).s = this.getName()+" "+neirong[1];
		                    				break;
		                    			}
		                    		}
		                    	}
		                    }
	                	}
                	}
                	
                	if(s!=null) {
		                printWriter.println(s);
		                printWriter.flush();
                    	s=null;
                	}
                	
                }
            } catch (Exception e) {
            	e.printStackTrace();
            	return;
            }
        }
    }
    

    @SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
        new Server();
    }
    
}
