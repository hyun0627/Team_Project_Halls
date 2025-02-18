package book_Management; 

import java.sql.SQLException;
import java.util.Scanner;
 
public class Main {    
	
	public static void main(String[] args) throws SQLException {
		
		Scanner sc = new Scanner(System.in);
		String select = ""; // 선택 값을 저장하기 위한 변수  
		Book bo = new Book();
		Member mem = new Member(bo);
		System.out.println("===============================================================");
		System.out.println("[ B: 책 정보 관리 || M: 사람 정보 관리 || R: 책 대여 관리 || X: 종료 ]");
		
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
		
					mem.run();
					
				} else if(select.equals("r") || select.equals("R")) {
					
					Rent re = new Rent(bo, mem);
					re.run();
				} 
			}			
		}
		
		sc.close();
	}

}