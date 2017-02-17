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
	public static final String GET = "get";
	public static final String PUT = "put";
	public static final String LS = "ls";
	public static final String MKDIR = "mkdir";
	public static final String CD = "cd";
	public static final String PWD = "pwd";
	public static final String DELETE = "delete";
	public static final String QUIT = "quit";
	
	public static void main (String args[]){
		
		System.out.println("Running FTP Client...");
        /*ClientCommands command = new ClientCommands(args[0],Integer.parseInt(args[1]));
		//ClientCommands command = new ClientCommands("10.2.84.195",1234);
		//ClientCommands command = new ClientCommands("192.168.1.37",1240);
        command.cmdInputs();
        */
        myftp ftpClient = new myftp();
        
        if(!ftpClient.connectServer(args[0],args[1])){
        	System.out.println(" :::: Failed to establish connection with the server ::::");
        }
        else{
        	System.out.println(":::::::::Connected to server successfully:::::::::");
        	System.out.println(" Server ip ::"+ args[0] + " Port number :: "+ args [1]);
        	ftpClient.performClientFTPFunctions();
        }
	}


	private boolean connectServer(String hostAddress, String portNo) {
		// TODO Auto-generated method stub
		boolean connectionStatus = false;
		try{
			
			clientSocket = new Socket(hostAddress, Integer.parseInt(portNo));
			clientSocketInp = new DataInputStream(clientSocket.getInputStream());
			clientSocketOut = new DataOutputStream(clientSocket.getOutputStream());
			
			connectionStatus = true;
			sc = new Scanner(System.in);
			
		}catch(Exception e){
			System.out.println("Exception in making connection " + e.getMessage());
			//e.printStackTrace();
			connectionStatus = false;
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
				if(cmdRequest[0].equals(QUIT)){
                	
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
	                    case GET:
	                    	cmdRead.get(cmdRequest[1]);
	                        break;
	                    case PUT:
	                    	cmdRead.put(cmdRequest[1]);
	                        break;
	                    case DELETE:
	                    	cmdRead.delete(cmdRequest[0]+"#"+cmdRequest[1]);
	                        break;
	                    case LS:
	                    	cmdRead.ls(cmdRequest[0]);
	                        break;
	                    case CD:
	                    	cmdRead.cd(cmdRequest[0]+"#"+cmdRequest[1]);
	                        break;
	                    case MKDIR:   
	                    	cmdRead.mkdir(cmdRequest[0]+"#"+cmdRequest[1]);
	                        break;
	                    case PWD:     
	                    	cmdRead.pwd(cmdRequest[0]);
	                        break;
	                    default:        
	                    	System.out.println("Invalid FTP command. Please enter correct command.");
	                    	break;
					}
				}
			}
		}catch(Exception e){
			System.out.println("exception in perform client FTP functions"+e.getMessage());
			 //e.printStackTrace();
		}
		
	}	
	
}
