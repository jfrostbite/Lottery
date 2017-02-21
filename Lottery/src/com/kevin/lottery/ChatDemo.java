/**
 * 
 */
package com.kevin.lottery;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Kevin
 *java网络编程，传输协议：UDP和TCP
 *UDP特点：面向无连接，封包数据限制（64k），因为面向无连接所以不安全，速度快
 *TCP特点：面向有连接（通过三次握手）简历数据传输通道，封包数据量大，因为面向连接所以安全，速度慢
 *
 *UDP连接，用到Socket是DatagramSocket,封包数据用DatagramPacket
 *
 *步骤：
 *1、创建Socket服务
 *2、提供数据并封包
 *3、发送/接受数据
 *4、关闭
 *
 *需求：写一个简单的UDP聊天程序,该程序需要发送数据，和接受数据，用多线程分别创建发送接受线程
 *步骤
 *1、创建发送端和接收端线程
 *2、复写run方法，实现发送，接收代码
 *3、运行程序
 */

//创建聊天发送线程,实现Runnable，复写run方法
class Send implements Runnable{
	//该线程建立接受一个Socket服务
	private DatagramSocket ds;
	private DatagramPacket dp;
	public Send(DatagramSocket ds,DatagramPacket dp){
		this.ds = ds;
		this.dp = dp;
	}
	//run方法复写发送代码
	public void run() {
		//写到图形界面，就是一次发送一个数据包
		try {
			//发送封包数据
			ds.send(dp);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("发送失败");
		}
	}
	
//	//对外提供接收要发送的ip的方法
//	public void setIP(String ip){
//		this.ip = ip;
//	}
//	//对外提供接收要发送的端口的方法
//	public void setData(int port){
//		this.port = port;
//	}
//	//对外提供接收要发送的数据的方法
//	public void setData(String data){
//		this.data = data;
//	}
}

//创建一个接受信息端
class Receive implements Runnable{
	//初始化Socket服务
	private DatagramSocket ds;
	//接收的数据传给编辑框
	private TextArea ta;
	public Receive(DatagramSocket ds,TextArea ta){
		this.ds = ds;
		this.ta = ta;
	}
	//用于接受数据的缓冲区
	//创建数据包

	@Override
	public void run() {
		byte[] buf = new byte[1024*64];
		//获取一个跨平台换行符
		DatagramPacket dp = new DatagramPacket(buf,buf.length);
		//循环接受数据
		while(true){
			try {
				//阻塞式接收
				ds.receive(dp);
				String ip = dp.getAddress().getHostAddress();
				String data = new String(dp.getData(),0,dp.getLength());
				//数据添加到文本区域
				ta.append(ip+"----"+ChatDemo.getTime()+ChatDemo.newLine);
				ta.append(data+ChatDemo.newLine);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("数据接收失败");
			}
			
		}
	}
	
}

public class ChatDemo extends Frame{

	/**
	 * 
	 */
	public static String newLine = System.getProperty("line.separator");
	private static final long serialVersionUID = 1L;
	/**
	 * @param args
	 */
	//创建窗口组件成员变量
	private Label ipLab,toPortLab,portLab;
	private TextField ipTf,toPortTf,portTf;
	private TextArea recTa,sendTa;
	private Button conBut,sendBut;
	private DatagramSocket ds;
	//初始化创建窗体
	public ChatDemo(){
		super("聊天程序");
		init();
	}
	//窗体组件初始化方法
	public void init(){
		//设置窗体属性
		this.setBounds(300,150,480,250);
		this.setLayout(new FlowLayout());
		//所有组件统一初始化
		ipLab = new Label("IP:");
		toPortLab = new Label("ToPort");
		portLab = new Label("MyPort");
		ipTf = new TextField(10);
		toPortTf = new TextField(5);
		portTf = new TextField(5);
		recTa = new TextArea(6,45);
		sendTa = new TextArea(3,37);
		conBut = new Button("连接");
		sendBut = new Button("发送");
		//添加组件到窗体
		this.add(ipLab);
		this.add(ipTf);
		this.add(toPortLab);
		this.add(toPortTf);
		this.add(portLab);
		this.add(portTf);
		this.add(conBut);
		this.add(recTa);
		this.add(sendTa);
		this.add(sendBut);
		//给组件添加事件代码
		event();
		//现实窗体
		this.setVisible(true);
	}
	//窗体组件事件
	private void event(){
		//窗口默认初始化端口事件
		this.addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent we){
				portTf.setText("10086");
				//窗口一打开就启动接受端
				try {
					ds = new DatagramSocket(Integer.parseInt(portTf.getText()));
					new Thread(new Receive(ds,recTa)).start();
				} catch (NumberFormatException | SocketException e) {					
					e.printStackTrace();
					recTa.append("连接失败"+newLine);
				}
			}
		});
		//用按钮来连接，不要一启动就连接，
		conBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				//先判断ds是否存在，存在就先关闭掉
				if(ds!=null)
					return;
				//窗口一打开就启动接受端
				try {
					ds = new DatagramSocket(Integer.parseInt(portTf.getText()));
					new Thread(new Receive(ds,recTa)).start();
				} catch (NumberFormatException | SocketException e) {					
					e.printStackTrace();
					recTa.append("连接失败"+newLine);
				}
			}
		});
		//关闭事件
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		});
		
		//发送按钮事件
		sendBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				DatagramSocket ds;
				DatagramPacket dp;
				try {
					ds = new DatagramSocket();
					byte[] buf = sendTa.getText().getBytes();
					try {
						recTa.append(InetAddress.getLocalHost().getHostName()+"----"+getTime()+newLine+sendTa.getText()+newLine);
						dp = new DatagramPacket(buf,buf.length,InetAddress.getByName(ipTf.getText()),Integer.parseInt(toPortTf.getText()));
						new Thread(new Send(ds,dp)).start();
					} catch (UnknownHostException e) {
						
						e.printStackTrace();
					}
				} catch (NumberFormatException | SocketException e) {					
					e.printStackTrace();
					recTa.append("<"+sendTa.getText()+">发送失败"+newLine);
				}
				
			}
		});
	}
	public static String getTime(){
		Date d = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
		return sd.format(d);
	}
	public static void main(String[] args) {
		new ChatDemo();
	}

}
