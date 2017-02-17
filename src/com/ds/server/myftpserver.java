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
		System.out.println("Running FTP server...." );
		System.out.println("Hosting service on Port Number :: " + args[0]);
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
				System.out.println("----- A Client connected to me ----- IP Address :: "+
						conClient.getInetAddress()+" :: Host Name :: "+conClient.getInetAddress().getHostName());
				performFTPfunctions(servSocket,conClient);

			}
		}catch(Exception e){
			System.out.println("Error while making client connection::: "+ e.getMessage());
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
				
				if(command.equals(MyftpServerUtil.QUIT)){
					try{
						sockInp.close();
						sockOutp.close();
						System.out.println("Client connection terminated on request :: Client Name :: " + conClient.getInetAddress().getHostName());
						conClient.close();
						}catch (IOException e){
							System.out.println("Exception in quit command "+e.getMessage());
							//e.printStackTrace();
						}finally{break;}
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
						System.out.println("CD Command excuted :: param :: "+parameters);
						continue;
					case MyftpServerUtil.LS:
						writeOnOutpLine(MyftpServerUtil.executeLS());
						System.out.println("LS Command excuted");
						continue;
					case MyftpServerUtil.MKDIR:
						System.out.println("MKDIR Command excuted :: Directory name :: "+parameters);
						if(MyftpServerUtil.executeMKDIR(parameters)){
							writeOnOutpLine("New directory created");
						}else{
							writeOnOutpLine("Directory name already exist. Failed to create new direcotory with same name");
						}
						continue;
					case MyftpServerUtil.DELETE:
						System.out.println("DELETE Command excuted :: File Name :: "+parameters);
						if(MyftpServerUtil.executeDELETE(parameters)){
							writeOnOutpLine("File deleted successfully");
						}else{
							writeOnOutpLine("File delete unsuccessful");
						}
						continue;
				}
			}
			
		}catch(Exception e){
			System.out.println("Exception in performing FTP functions ::: "+ e.getMessage());
			//e.printStackTrace();
		}
		
	}
	
	private void writeOnOutpLine(String writeMsg) {
		// TODO Auto-generated method stub
		try{
			sockOutp.writeUTF(writeMsg);
		}catch(IOException e){
			System.out.println("Exception in writing message on outline " + e.getMessage());
			//e.printStackTrace();
		}
	}

	public String readFromInpLine() {
		// TODO Auto-generated method stub
    	String IOread = null;
    	try{
    		IOread = sockInp.readUTF();
    	}catch (IOException ioe){
    		//ioe.printStackTrace();
    		System.out.println("Exception in reading from input line " + ioe.getMessage());
    	}
		return IOread;
	}
}
