//package book_Management;
//
//import java.util.Scanner;
//
//public class Main {
//
//	public static void main(String[] args) {
//		Scanner s = new Scanner(System.in);
//		
////		Book bo = new Book();
//		Member mem = new Member();
////		Rent re = new Rent();
//		while(true) {
//			System.out.print("작업선택 (b:책관리,m:멤버관리,r:실적관리,x:종료)");
//			String ins = s.nextLine();
//			
//			if(ins.equals("x")||ins.equals("X"))break;
//			switch(ins) {
//			case "b","B":
//// 				bo.control();
//				break;
//			case "m","M":
//				mem.control();
//				break;
//			case "r","R":
////				re.control();
//				break;
//			}
//		}
//		System.out.println("\n프로그램 종료");
//
//	}
//
//}



package book_Management;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) throws SQLException {
		
		Scanner sc = new Scanner(System.in);
		String select = ""; // 선택 값을 저장하기 위한 변수 
		Book bo = new Book();
		Member mem = new Member();
		System.out.println("===============================================================");
		System.out.println("[ B: 책 관리 || M: 사람 정보 관리 || R: 렌트 정보 관리 || X: 종료 ]");
		
		while(true) {	
			
			System.out.print("작업을 선택하세요 : ");
			select = sc.nextLine(); // 어떤 작업을 선택할건지 입력하는 menuselect(ms) 스캐너
						
			if(select.equals("x") || select.equals("X") || new Test().strTest(select) == false) {
				
				System.out.println("프로그램을 종료합니다.");
				System.out.println("===============================================================");
				break;
				
			} else {
				
				if(select.equals("b") || select.equals("B")) {
					
					bo.run();
					
				} else if(select.equals("m") || select.equals("M")) {
		
					mem.control();
					
				} else if(select.equals("s") || select.equals("S")) {
					
					//Sales sales = new Sales(menu, order);
					//sales.run();
				} 
			}			
		}
		
		sc.close();
	}

}
