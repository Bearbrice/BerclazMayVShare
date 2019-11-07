package Client;

import java.io.File;
import java.io.IOException;

public class Test {

	public static void main(String[] args) {
		//File x = new File("C:\\temp\\received.txt");
		
		File fileToSend = new File("C:\\temp\\IntroductionTB.pdf");
		String name=null;
		int length=0;
		
		if(fileToSend.exists()) {
			name = fileToSend.getName();
			length=(int) fileToSend.length();
		}
		else {
			System.out.println("File does not exists!");
		}
		
		
		
		
		
		System.out.println(name);
		System.out.println(length);
		
		
		
/*		System.out.println(x.getAbsoluteFile());
		System.out.println(x.getAbsolutePath());
		try {
			System.out.println(x.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println(x.getCanonicalFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(x.getName());*/
		
		
		
		

	}

}
