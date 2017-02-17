package com.ds.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ClientCommands {

	static final String PROMPT = "myftp> ";
	
    DataInputStream dInput;
    DataOutputStream dOutput;
    Scanner sc;
    
    public ClientCommands(DataInputStream dInput, DataOutputStream dOutput) {
		this.dInput = dInput;
		this.dOutput = dOutput;
	}

	void get(String param) throws Exception {
		writeOnOutpLine("get");
		writeOnOutpLine(param);
        //write command + param to server
        String serverStatus = readFromInpLine();
        if (serverStatus.equals("File Not Found")) {
            System.out.println("File " + param + " doesn't exist on server");
            return;
        }
        else if(serverStatus.equals("READY")) {
            System.out.println("Receiving file...");
            File fileIn = new File(getFileName(param));
            if (fileIn.exists()) {
                System.out.println("File already exists on path "+fileIn.getAbsolutePath()+" Do you wish to overwrite (y or n)");
                if (sc.next()=="n") {
                    dOutput.flush();
                    return;
                }
            }
            //----copy recieving 
            downloadFile(fileIn);
            System.out.println("Client: File downloaded successfully..");
        }
    }
    
  

	void put(String param) throws Exception {
		writeOnOutpLine("put");
		writeOnOutpLine(param);
        File fileOut = new File(param);
        String status = readFromInpLine();
        //--- check on system for existence of files
        if (!fileOut.exists()) {
            System.out.println("File does not exist");
            return;
        }
        //---- if file already exists on the server
        if (status.equals("File Already Exists")){
            System.out.println(status+" Do you wish to overwrite (y or n)");
            //dOutput.writeUTF(sc.next());
            if(sc.next()=="n") {
                return;
            }
        }
        System.out.println("Sending file to server...");
        
        uploadFile(fileOut);
        
    }
	
    private void uploadFile(File fileOut) {
		// TODO Auto-generated method stub
		try{
			FileInputStream fin = new FileInputStream(fileOut);
	        int ch;
	        do {
	            ch = fin.read();
	            dOutput.writeUTF(String.valueOf(ch));
	        }while(ch != -1);
	        fin.close();
	        System.err.println(readFromInpLine());
		}catch (IOException ioe){
			System.out.println(" Exception in upload file method :: "+ ioe.getMessage());
			//ioe.printStackTrace();
		}
	}


	void writeOnOutpLine(String writeMsg) {
		// TODO Auto-generated method stub
		try{
			dOutput.writeUTF(writeMsg);
		}catch(IOException e){
			System.out.println("Exception in write on outputline method :: "+e.getMessage());
			//e.printStackTrace();
		}
	}

	void releaseClientResources() {
		// TODO Auto-generated method stub
        try{
        	
        	//this.sc.close();
        	this.dInput.close();
            this.dOutput.close();
            //this.socket.close();
            
        }catch(Exception e){
        	System.out.println("Error while releasing client resourcess :::"+ e.getMessage());
        	//e.printStackTrace();
        }
	}
	
		public String readFromInpLine() {
			// TODO Auto-generated method stub
	    	String IOread = null;
	    	try{
	    		IOread = dInput.readUTF();
	    	}catch (IOException ioe){
	    		System.out.println("Exception in reading from input line :: "+ ioe.getMessage());
	    		//ioe.printStackTrace();
	    	}
			return IOread;
		}

		private void downloadFile(File fileName) {
			// TODO Auto-generated method stub
			try{
				FileOutputStream fout = new FileOutputStream(fileName);
	            int ch;
	            do {
	                ch = Integer.parseInt(readFromInpLine());
	                if (ch != -1) {
	                    fout.write(ch);
	                }
	            }while(ch != -1);
	            fout.close();
	            System.err.println(readFromInpLine());
	            
			}catch (IOException ioe){
				System.out.println(" Exception in downloading file ::: " + ioe.getMessage()) ;
				//ioe.printStackTrace();
			}
		}


		private String getFileName(String param) {
			// TODO Auto-generated method stub
			String retFileName = null;
			try{
				if(param.contains(File.separator)){
					String p[] = param.split(File.separator);
					retFileName = p[p.length - 1];
				}else{
					retFileName = param;
				}
			}catch (Exception e){
				System.out.println("Exception in getfileName :: " + e.getMessage());
			}
	    	return retFileName;
		}


		public void pwd(String command) {
			// TODO Auto-generated method stub
			try{
				writeOnOutpLine(command);
				System.err.println(dInput.readUTF());
			}catch(IOException ioe){
				System.out.println("Exception in delete function:: "+ ioe.getMessage());
			}
		}


		public void mkdir(String command) {
			// TODO Auto-generated method stub
			try{
				writeOnOutpLine(command);
				System.err.println(dInput.readUTF());
			}catch(IOException ioe){
				System.out.println("Exception in delete function:: "+ ioe.getMessage());
			}
		}


		public void cd(String command) {
			// TODO Auto-generated method stub
			try{
				writeOnOutpLine(command);
				System.err.println(dInput.readUTF());
			}catch(IOException ioe){
				System.out.println("Exception in delete function:: "+ ioe.getMessage());
			}
		}


		public void ls(String command) {
			// TODO Auto-generated method stub
			try{
				writeOnOutpLine(command);
				System.err.println(readFromInpLine());
			}catch(Exception ioe){
				System.out.println("Exception in delete function:: "+ ioe.getMessage());
			}
		}


		public void delete(String command) {
			// TODO Auto-generated method stub
			try{
				writeOnOutpLine(command);
				System.err.println(readFromInpLine());
			}catch(Exception ioe){
				System.out.println("Exception in delete function:: "+ ioe.getMessage());
			}
		}

}
