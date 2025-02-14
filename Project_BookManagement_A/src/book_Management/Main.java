package book_Management;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
	
		Book bo = new Book();
		
		while(true) {
			System.out.print("작업 선택 [b:book, m:member, r:rent, x:종료] : ");
			String ins = s.nextLine();
			if(ins.equals("x") || ins.equals("X")) break;
			
		
			switch(ins) {
			case "b", "B":
				bo.control();
				break;
			case "m", "M":
				break;
			case "r", "R":	
					break;

			}
		}
	}
}
