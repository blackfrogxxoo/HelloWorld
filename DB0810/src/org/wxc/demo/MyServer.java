package org.wxc.demo;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class MyServer implements Runnable {
	/*
	 * 域 serverSocket socket PORT
	 */
	ServerSocket serverSocket = null;
	Socket socket = null;
	private static List<Map<String, Object>> members = null;
	public static final int PORT = 7100;

	private static final String IP = "192.168.1.195";
	private static final int PORT1 = 8888;
	private String sql = null;
	int bufSize = 128*1024 ;
	boolean flag = false;

	String localpath = "/sdcard/";
	String downloadFile = "receive_list";
	private String localFile = localpath + downloadFile;

	/**
	 * 构造方法
	 */
	// 用来创建一个启动线程的实例 执行查询等一系列操作
	public MyServer(String sql) {
		this.sql = sql;
	}

	// 用来创建一个不启动线程的实例 提供members等数据
	public MyServer() {
	}

	/*
	 * 方法一：创建侦听socket (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	private void startServer() throws IOException, ClassNotFoundException {
		serverSocket = new ServerSocket(MyServer.PORT);// 实例化serverSocket
		socket = serverSocket.accept();// 实例化socket
		// 1.读取传入的Object，并用list保存

		ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		byte[] buf = new byte[8 * 1024];
		int len;
		/*ObjectOutputStream out = new ObjectOutputStream();
		while ((len = in.read(buf)) >= 0) {
			out.write(buf, 0, len);
			out.flush();
		}*/
		setMembers((List<Map<String, Object>>) in.readObject());
		System.out.println("startServer(): 接收到服务器的回应！members:" + members);

		// ==========================================
		// 2. 获取socket的输入流并包装成BufferedInputStream
	/*	BufferedInputStream in = new BufferedInputStream(
				socket.getInputStream(),bufSize);

		// 获取与指定本地文件关联的文件输出流
		File file = new File(localFile);

		if (!file.getParentFile().exists()) { // 判断文件是否存在，不存在则创建
			file.getParentFile().mkdirs();
		}

		FileOutputStream out = new FileOutputStream(file);
		byte[] buf = new byte[bufSize]; // 尝试与文件一样大的byte[],104145202,抛出OutOfMemoryError
		// 减小到十分之一，byte[10414520]，无错 // byte[] buf = new byte[10*1024*1024] ;
		int len;
		while ((len = in.read(buf)) >= 0) { 
			out.write(buf, 0, len);
			out.flush();
		}
		*/
		 // ==========================================
		System.out.println("success");
		socket.close();
		serverSocket.close();
	}

	public void setMembers(List<Map<String, Object>> l) {
		this.members = l;
	}

	public List<Map<String, Object>> getMembers() {
		System.out.println("getMembers(): members是：" + members);
		return this.members;
	}

	public void send() {

		try {
			Socket socket = new Socket(IP, PORT1);
			Log.d("客户端", "服务器正在发送信息:'" + sql + "'");

			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())), true);
			out.println(sql);
			Log.d("客户端", "服务器发送消息成功");
			socket.close();
			flag = true;
		} catch (Exception e) {
			Log.e("客户端", "服务器出错", e);
		}
	}

	public void run() {
		send();
		System.out.println(Thread.currentThread().getName() + "运行!");
		if (flag)
			try {
				startServer();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				flag = false;
			}

	}

}
