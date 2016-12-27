
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server extends ServerSocket {
	
	List<CreateServerThread> threadlist = new ArrayList<CreateServerThread>();
	
    private static final int SERVER_PORT = 2013;

    public Server() throws Exception {
        super(SERVER_PORT);

        try {
            while (true) {
                Socket socket = accept();
                System.out.println("有用户连接到服务器");
                threadlist.add(new CreateServerThread(socket));//当有请求时，启一个线程处理
            }
        } catch (IOException e) {
        } finally {
            close();
        }
    }

    //线程类
    class CreateServerThread extends Thread {
        Socket client;
        BufferedReader bufferedReader;
        PrintWriter printWriter;
        MysqlDemo sqldemo;
        String[] conf;
        String s = null;
        boolean close = false;
        
        public CreateServerThread(Socket s) throws Exception {
            client = s;
            sqldemo = new MysqlDemo();
            printWriter = new PrintWriter(client.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println(client.getInetAddress());
            System.out.println(client.getPort());
            start();
        }

        public void run() {
            try {
                String line;
                while (true) {
                	line="";
                	while(s==null&&client.getInputStream().available() <= 0) {}
//                	while(client.getInputStream().available() <= 0) {
//            			try {
//    						sleep(1000);
//    					} catch (InterruptedException e) {
//    						e.printStackTrace();
//    					}
//            		}
                	if(client.getInputStream().available()>0) {
                		line = bufferedReader.readLine();
	                	if(line.length()>0) {
		                	if(line.startsWith("signin")) {
		                		conf = line.split(" ");
		                    	if(sqldemo.findByNameAndPasswd(conf[1], conf[2])==true) {
		                    		this.setName(conf[1]);
		                    		printWriter.println("signin succeed");
		                    		printWriter.flush();
		                    	}
		                    	else {
		                    		printWriter.println("signin failed");
		                    		printWriter.flush();
		                    	}
		                    }
	                	
	                    
		                	else if(line.startsWith("signup")) {
		                    	conf = line.split(" ");
		                    	sqldemo.insert(conf[1], conf[2]);
		                    	printWriter.println("signup succeed");
		                    	printWriter.flush();
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
		                		return;
		                	}
	                	
	                    
		                    else {
		                    	String[] neirong;
		                    	neirong = line.split(" ");
		                    	for(int i=0; i<threadlist.size();i++) {
		                    		if(neirong[0].equals(threadlist.get(i).getName())) {
		                    			threadlist.get(i).s = this.getName()+" "+neirong[1];
		                    			break;
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
            }
        }
    }
    

    @SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
        new Server();
    }
}
