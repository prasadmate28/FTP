package com.ds.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Pattern;

public class MyftpServerUtil {
	
	public static final String GET = "get";
	public static final String PUT = "put";
	public static final String LS = "ls";
	public static final String MKDIR = "mkdir";
	public static final String CD = "cd";
	public static final String PWD = "pwd";
	public static final String DELETE = "delete";
	public static final String QUIT = "quit";
	public static final String ROOT = "user.dir";
	
	public String defaultMsg() {
		// TODO Auto-generated method stub
		return "Invalid FTP command. Please enter correct command";
	}


	public static String executeLS() {
		// TODO Auto-generated method stub
		//ArrayList<String> fileNames = new ArrayList<String>();
		StringBuilder directoryList = new StringBuilder();

		try{
		
			File files = new File(executePWD());
			File[] fList = files.listFiles();
			for (File f: fList)
				directoryList.append(f.getName()+"\t");
				//fileNames.add(f.getName());
	
		}catch(Exception e){
			System.out.println("----Exception in LS command execution -----"+e);
		}
		return directoryList.toString();
	}


	public static String executePWD() {
		// TODO Auto-generated method stub
		String currentDirectory = null;
		try{
			 currentDirectory = System.getProperty(ROOT);
		}catch(Exception e){
			System.out.println("----Exception in PWD command execution -----"+e);
		}
		return currentDirectory;
	}

	public static String executeCD(String dirPath) {
		// TODO Auto-generated method stub
		String retStatus = null;
		try{
			
			if(dirPath.equals("..")){
				File curr = new File(executePWD());
				System.setProperty(ROOT, curr.getAbsoluteFile().getParent());
				retStatus = "Directory changed successfully";
			}else{
				File current = new File(System.getProperty(ROOT) + File.separator +dirPath);
				System.out.println("Inside CD ::" + current.getAbsolutePath());
				if(current.exists() && current.isDirectory()){
					System.setProperty(ROOT, current.getAbsoluteFile().getPath());
					retStatus = "Directory changed successfully";
				}
				else
					retStatus = "Directory does not exist. Enter correct directory name and try again.";
			}
			
		}catch(Exception e){
			System.out.println("----Exception in CD command execution -----"+e);
		}
		
		return retStatus; 
	}


	public static void executeMKDIR(String parameters) {
		// TODO Auto-generated method stub
		try{
			File newDir = new File(executePWD()+File.separator+parameters);
			if(!newDir.exists()){
				newDir.mkdir();
			}else{
				System.out.println("directory already exists");
				return;
			}
			
		}catch(Exception e){
			System.out.println("Exception in mkdir" + e);
		}
		
	}


	public static void executeDELETE(String parameters) {
		// TODO Auto-generated method stub
		try{
			File file = new File(executePWD()+File.separator+parameters);
			if(file.exists()){
				file.delete();
			}
			else{
				System.out.println("file does not exist");
				return;
			}
		}catch(Exception e){
			System.out.println("Exception in execute delete command"+e);
		}
	}


	public static void executeGET(DataInputStream sockInp,DataOutputStream sockOutp) {
		// TODO Auto-generated method stub
		try{
			String fileName = sockInp.readUTF();
			File getFile = null;
			if(fileName.indexOf(File.separator) > -1){
				getFile = new File(fileName);
			}else{
				getFile = new File(executePWD()+File.separator+fileName);
			}
			//File getFile = new File(fileName);
			System.out.println("server path::"+getFile.getAbsolutePath());
			if(getFile.exists()){
				sockOutp.writeUTF("READY");//send status1
				System.out.println("Server ready to send the file...");
				//----begin sending file to client-----
				FileInputStream fin = new FileInputStream(getFile);
				int ch;
		        do {
		            ch = fin.read();
		            sockOutp.writeUTF(String.valueOf(ch));
		        }while(ch != -1);
		        fin.close();
                System.out.println(")
				//-----end----
				sockOutp.writeUTF("Server: File "+fileName+" sent successfully");
			}else{
				sockOutp.writeUTF("File Not Found");//send status2
				System.out.println("GET file does not exist");
			}
			
		}catch(Exception e){
			System.out.println("Exception in Get file function::"+e);
		}
	}


	public static void executePUT(DataInputStream sockInp,DataOutputStream sockOutp) {
		// TODO Auto-generated method stub
		try{
			String fileName = sockInp.readUTF();
			File putFile = new File(executePWD()+File.separator+getFileName(fileName));
			System.out.println("Copied file from::"+putFile.getAbsolutePath());
			if(putFile.exists()){
				sockOutp.writeUTF("File Already Exists");
			}else{
				sockOutp.writeUTF("File does not Exist");
			}
			//----begin sending file to client-----
			FileOutputStream fout = new FileOutputStream(putFile);
	        int ch;
	        do {
	        	ch = Integer.parseInt(sockInp.readUTF());
                if (ch != -1) {
                    fout.write(ch);
                }
	        }while(ch != -1);
	        fout.close();
			//-----end----
	        sockOutp.writeUTF("Server: File "+fileName+" uploaded successfully");
			
		}catch(Exception e){
			
		}
	}


	private static String getFileName(String param) {
		// TODO Auto-generated method stub
		String retFileName = null;
		if(param.indexOf('\\') > -1 ){
			//String splitRegex = Pattern.quote(System.getProperty("file.separator"));
			//String[] p = param.split(splitRegex);
			String[] p = param.split(Pattern.quote("\\"));
			retFileName = p[p.length - 1];
		}
		else if(param.indexOf('/') > -1){
			String[] p = param.split("/");
			retFileName = p[p.length - 1];
		}
		else{
			retFileName = param;
		}
    	return retFileName;
	}
	

}
