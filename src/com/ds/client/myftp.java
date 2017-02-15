package com.ds.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Scanner;

public class myftp {
	static final String PROMPT = "myftp> ";
	Socket clientSocket =null;
	DataInputStream clientSocketInp = null;
	DataOutputStream clientSocketOut = null;
	Scanner sc = null;
	
	public static void main (String args[]){
		
		System.out.println("Running FTP Client...");
        /*ClientCommands command = new ClientCommands(args[0],Integer.parseInt(args[1]));
		//ClientCommands command = new ClientCommands("10.2.84.195",1234);
		//ClientCommands command = new ClientCommands("192.168.1.37",1240);
        command.cmdInputs();
        */
        myftp ftpClient = new myftp();
        ftpClient.connectServer(args[0],args[1]);
        System.out.println(":::::::::Connected to server successfully:::::::::");
        ftpClient.performClientFTPFunctions();
	}


	private boolean connectServer(String hostAddress, String portNo) {
		// TODO Auto-generated method stub
		boolean connectionStatus = false;
		try{
			
			clientSocket = new Socket(hostAddress, Integer.parseInt(portNo));
			clientSocketInp = new DataInputStream(clientSocket.getInputStream());
			clientSocketOut = new DataOutputStream(clientSocket.getOutputStream());
			
			sc = new Scanner(System.in);
			
		}catch(Exception e){
			System.out.println("Exception in making connection" );
			e.printStackTrace();
		}
		return connectionStatus;
	}

	private void performClientFTPFunctions() {
		// TODO Auto-generated method stub
		ClientCommands cmdRead = new ClientCommands(clientSocketInp, clientSocketOut);
		try{
			while(true){
				
				System.out.print(PROMPT);
				String cmdRequest[] = sc.nextLine().split(" ", 2);
				//if quit then release resources and exit
				if(cmdRequest[0].equals("quit")){
                	
					cmdRead.writeOnOutpLine(cmdRequest[0]);
                	//dOutput.writeUTF(cmdRequest[0]);
					cmdRead.releaseClientResources();
                	System.out.println("Client signing off . . . "); 
                    break;
                }
				else{
					/*Class cmd = Class.forName("com.ds.client.ClientCommands");
					Constructor c = cmd.getConstructor(new Class [] {DataInputStream.class,DataOutputStream.class});
					ClientCommands cmdObject = (ClientCommands) c.newInstance(new Object[] {clientSocketInp,clientSocketOut});
					
					try{
						
						Method m = cmd.getDeclaredMethod(cmdRequest[0], new Class[]{String.class});
						m.invoke(cmdObject, cmdRequest[0]+"#"+cmdRequest[1]);
					}catch(NoSuchMethodException me){
						System.out.println("No such method");
						me.printStackTrace();
						continue;
					}*/
					
					switch (cmdRequest[0]) {
	                    case "get":
	                    	cmdRead.get(cmdRequest[1]);
	                        break;
	                    case "put":
	                    	cmdRead.put(cmdRequest[1]);
	                        break;
	                    case "delete":
	                    	cmdRead.delete(cmdRequest[0]+"#"+cmdRequest[1]);
	                        break;
	                    case "ls":
	                    	cmdRead.ls(cmdRequest[0]);
	                        break;
	                    case "cd":
	                    	cmdRead.cd(cmdRequest[0]+"#"+cmdRequest[1]);
	                        break;
	                    case "mkdir":   
	                    	cmdRead.mkdir(cmdRequest[0]+"#"+cmdRequest[1]);
	                        break;
	                    case "pwd":     
	                    	cmdRead.pwd(cmdRequest[0]);
	                        break;
	                    default:        
	                    	System.out.println("Invalid FTP command. Please enter correct command.");
	                    	break;
					}
					
				}
				
			}
		}catch(Exception e){
			System.out.println("excception in perform client FTP functions" );
			 e.printStackTrace();
		}
		
	}	
	
}
