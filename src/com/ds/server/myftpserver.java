package com.ds.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class myftpserver {

	DataInputStream sockInp = null;
	DataOutputStream sockOutp = null;
	
	public static void main(String args[]){
		System.out.println("Running FTP server....");
		myftpserver ftpserver = new myftpserver();
		ftpserver.makeConnection(args[0]);
	}

	private void makeConnection(String portNo) {
		// TODO Auto-generated method stub
		ServerSocket servSocket = null;
		int servPort = Integer.parseInt(portNo);
		try{
			
			servSocket = new ServerSocket(servPort);
			while(true){
				
				Socket conClient = servSocket.accept();
				System.out.println("----- A Client connected to me ----- "+
						conClient.getInetAddress()+" :: "+conClient.getInetAddress().getHostName());
				performFTPfunctions(servSocket,conClient);

			}
		}catch(Exception e){
			System.out.println("Error while making client connection::: "+ e);
		}
	}
	
	private void performFTPfunctions(ServerSocket servSocket, Socket conClient) {
		// TODO Auto-generated method stub
		try{
			
			sockInp = new DataInputStream(conClient.getInputStream());
			sockOutp = new DataOutputStream(conClient.getOutputStream());
			
			String clientReq = null;
			
			
			/*Class[] noparams = {};
			Class[] stringparam = {String.class};
			Class utilityClass = Class.forName("com.ds.server.util.MyftpServerUtil");
			MyftpServerUtil m = (MyftpServerUtil) utilityClass.newInstance();
			if(noParamCmdList.contains(clientReq))
				Method runCommand = utilityClass.getDeclaredMethod(clientReq, stringparam);
				runCommand.invoke(m, null);
			*/
			
			
			while(true){
				//Read Client request from InputStream.
				clientReq = sockInp.readUTF();
				String command = null, parameters = null;
				
				if(clientReq.contains("#")){
					command = clientReq.split("#")[0];
					parameters = clientReq.split("#")[1];
					//System.out.println(command+"::"+parameters);
				}else{
					command = clientReq;
				}
				
				if(command.equals("quit")){
					try{
						sockInp.close();
						sockOutp.close();
						System.out.println("Client connection terminated on request " + conClient.getInetAddress().getHostName());
							conClient.close();
						}catch (IOException e){
							System.out.println("Exception in quit command" );
							e.printStackTrace();
					}finally{
						break;
					}
				}

				switch (command){
					
					case MyftpServerUtil.GET:
						MyftpServerUtil.executeGET(sockInp,sockOutp);
						continue;
					case MyftpServerUtil.PUT:
						MyftpServerUtil.executePUT(sockInp,sockOutp);
						continue;
					case MyftpServerUtil.PWD:
						writeOnOutpLine(MyftpServerUtil.executePWD().toString());
						System.out.println("PWD Command excuted");
						continue;
					case MyftpServerUtil.CD:
						writeOnOutpLine(MyftpServerUtil.executeCD(parameters));
						System.out.println("CD Command excuted :: "+parameters);
						continue;
					case MyftpServerUtil.LS:
						writeOnOutpLine(MyftpServerUtil.executeLS());
						System.out.println("LS Command excuted");
						continue;
					case MyftpServerUtil.MKDIR:
						MyftpServerUtil.executeMKDIR(parameters);
						writeOnOutpLine("New Directory made");
						System.out.println("MKDIR Command excuted :: "+parameters);
						continue;
					case MyftpServerUtil.DELETE:
						MyftpServerUtil.executeDELETE(parameters);
						writeOnOutpLine("Directory deleted");
						System.out.println("DELETE Command excuted :: "+parameters);
						continue;
				}
				
			}
			
		}catch(Exception e){
			System.out.println("Excception in performing FTP functions ::: "+ e);
			e.printStackTrace();
		}
		finally{
			
		}
	}
	
	private void writeOnOutpLine(String writeMsg) {
		// TODO Auto-generated method stub
		try{
			sockOutp.writeUTF(writeMsg);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
