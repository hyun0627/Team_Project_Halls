package book_Management;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
 
public class Rent {

	private Scanner sc; // 스캐너 
	private Test t1; // 유효성 검사를 위해 사용할 변수
	public Connection dbconn; // DB 연결을 위한 변수
	
	Book bo; // Book 클래스의 메소드 + 값들을 가져오기 위한 변수
	Member mem; // Member클래스의 메소드 + 값들을 가져오기 위한 변수
	
	private int index; // id값을 저장할 변수
	private String id; // id값을 저장하기 전에 유효성 검사를 진행하기 위해 id값을 임시로 저장해놓을 변수
	private String title; // 책 이름을 입력받기 위한 변수
	private String member_id; // member_id(userid)
	
	// 생성자
	Rent(Book _bo, Member _mem) {
		
		sc = new Scanner(System.in);
		t1 = new Test();
		dbconn = null;
		this.bo = _bo;
		this.mem = _mem;
		
	}
	
	// 실행 메소드
	public void run() throws SQLException {
		
		String rms; // RentManagementSelect 약자

		System.out.println("===============================================================");
		System.out.println("책 대여 관리 프로그램을 실행합니다.");
		
		while(true) {
			System.out.println("[ C: 책 대여 || U: 대여 연장 || D: 책 반납 || R: 대여 정보 출력 || X or 공백: 종료 ]");
			System.out.print("작업을 선택하세요: ");
			rms = sc.nextLine();
			
			if(rms.equals("x") || rms.equals("X") || t1.strTest(rms) == false) {
				System.out.println("===============================================================");
				System.out.println("[ B: 책 정보 관리 || M: 사람 정보 관리 || R: 책 대여 관리 || X: 종료 ]");
				break;
			} else {
				if(rms.equals("c") || rms.equals("C")) {
					System.out.println("책 대여 프로세스를 실행합니다.");
					addBookRent();
				} else if(rms.equals("u") || rms.equals("U")) {
					System.out.println("대여 연장 프로세스를 실행합니다.");
					updateRent();
				} else if(rms.equals("d") || rms.equals("D")) {
					System.out.println("책 반납 프로세스를 실행합니다.");
					deleteRent();
				} else if(rms.equals("r") || rms.equals("R")) {
					System.out.println("대여 정보 출력 프로세스를 실행합니다.");
					//printBook();				
				}
			} // if-else문 종료
			
		} // while문 종료
		
	} // 실행 메소드(run메소드) 종료
	
	// 책 대여 메소드
	public void addBookRent() throws SQLException {
		
		/* 책 대여 프로세스
		 *  초안: userId를 입력 -> 만약 해당 유저가 있으면 책 이름 입력 없으면 다시 처음으로
		 *  						-> 입력한 책 이름에 해당하는 isbn 번호를 찾아서 isbn 번호와 userid를 insert
		 *  while문 써서 한번에 한 책을 insert
		 *  1. userid 입력
		 *  2. 그 아이디가 있는지 확인 -> 있으면 빌릴 책 이름 입력
		 */
		
		System.out.print("아이디를 입력하세요: ");
		member_id = sc.nextLine();
		if(t1.strTest(member_id) == false) {return;}
		
		// 입력한 id가 table내에 존재 하는지 확인하는 코드
		String si = "select userid from Member where userid = '" + member_id + "' "; // si -> SearchId
		bo.setConnection();
		Statement stsi = this.bo.dbconn.createStatement();
		ResultSet rs = stsi.executeQuery(si);

		// 만약 rs 값이 null이면 프로세스 종료
		if(rs == null) {
			System.out.println("해당 아이디가 없습니다. 대출 프로세스를 종료합니다.");
			rs.close();
			stsi.close();
			bo.disConnection();
			return;
		}
	
		stsi.close();
		rs.close();
		bo.disConnection();
		
		// 책 이름 입력 -> 해당하는 책 리스트 출력 -> 그 중 빌릴 책 선택
		while(true) {
			System.out.print("빌릴 책 이름을 입력하세요 : ");
			title = sc.nextLine();
			if(t1.strTest(title) == false) {break;}
			// 입력한 이름에 해당하는 책들의 리스트 출력
			bo.setConnection();
			// sopon -> SqlOfPrintOfName
			String sopon = "select id, title, isbn, price, publisher, author from Book where title like '" + title + "%' "
							  + "or title like '%" + title + "' "
							  + "or title like '%" + title + "%'"; 
			Statement pon = this.bo.dbconn.createStatement(); // pon -> PrintOfName
			ResultSet rpon = pon.executeQuery(sopon); // rpon -> ResultPrintOfName
			
			// 책들의 리스트 출력
			while(rpon.next()) {
				System.out.println(rpon.getInt("id") + "." + rpon.getString("title") + "\t" + rpon.getString("isbn") + "\t" 
									  + rpon.getInt("price") + "\t" + rpon.getString("publisher") + "\t" + rpon.getString("author"));
			}
			
			rpon.close();
			pon.close();
			bo.disConnection();
			
			// 리스트들의 책들 중 원하는 책 선택
			System.out.print("빌릴 책의 index번호를 입력하세요: ");
			id = sc.nextLine();
			if(t1.isNumber(id) == false) {return;}
			index = Integer.parseInt(id);
			
			String sar = "insert into Rent set book_id = (select isbn from Book where id = " + index + "), "
						 + "member_id = '" + member_id + "' ,"
						 + "expirydate = date_add(current_timestamp(), INTERVAL 2 WEEK)"; // sar -> StringAddRent
			
			bo.setConnection();
			Statement soa = this.bo.dbconn.createStatement(); // soa -> StatementOfAdd
			soa.executeUpdate(sar); // sar SQL문 실행
			
			soa.close();
			bo.disConnection();
			
		
		} // while문 종료
		
	}

	// 대여 연장 메소드
	public void updateRent() throws SQLException{
		
		String str = ""; // sys문 답변을 저장하기 위한 변수
		
		System.out.print("대출 기한을 연장하시겠습니까? : ");
		str = sc.nextLine();
		if(t1.strTest(str) == false) {return;}
		
		if(str.equals("ㅇㅇ") || str.equals("네") || str.equals("dd")) {
			
			System.out.print("아이디를 입력하세요: ");
			member_id = sc.nextLine();
			if(t1.strTest(member_id) == false) {return;}
			
			// 입력한 id가 table내에 존재 하는지 확인하는 코드
			String si = "select count(*) from Member where userid = '" + member_id + "' "; // si -> SearchId
			bo.setConnection();
			Statement stsi = this.bo.dbconn.createStatement();
			ResultSet rs = stsi.executeQuery(si);
			rs.next();
			int count = rs.getInt("count(*)");
			if(count == 0) {
				System.out.println("해당 아이디가 없습니다. 대출 프로세스를 종료합니다.");
				rs.close();
				stsi.close();
				bo.disConnection();
				return;
			}
		
			stsi.close();
			rs.close();
			bo.disConnection();
			
			// System.out.println("대출 연장 기간은 연장을 신청한 시점으로부터 2주입니다.");
			// 1. 입력한 아이디에 해당하는 데이터 모두 출력
			// 2. 그 책들 중 연장할 책 선택

			String spl = "select Rent.id, Book.title, Rent.book_id, Member.pname, Rent.member_id, Rent.created, Rent.expirydate"
						+ " from Rent Inner join Book on Rent.book_id = Book.isbn "
						+ " Inner join Member on Rent.member_id = Member.userid"
						+ " where Rent.member_id = '" + member_id + "' ";
			bo.setConnection();
			Statement test = this.bo.dbconn.createStatement();
			ResultSet te = test.executeQuery(spl);
			
			while(te.next()) {
				System.out.println(te.getInt("id") + "." +te.getString("Book.title") + ", " + te.getString("Rent.book_id") + ", " + te.getString("Member.pname")
									 + ", " + te.getString("Rent.member_id") + ", " + te.getTimestamp("Rent.created") + ", " 
									 + te.getTimestamp("Rent.expirydate"));
			}
			
			te.close();
			test.close();
			bo.disConnection();
			
			// 일괄 연장 기능시작
			System.out.print("일괄연장 하시겠습니까? : ");
			str = sc.nextLine();
			if(t1.strTest(str) == false) {return;}
			
			// 일괄 연장 구현 시작
			if(str.equals("dd") || str.equals("ㅇㅇ") || str.equals("네")) {
				
				// update Rent set expirydate = date_add(expirydate, INTERVAL 2 WEEK) where member_id = '1';
				// bx -> Bulk Extension
				String bx = "update Rent set expirydate = date_add(expirydate, INTERVAL 2 WEEK) where member_id = '" + member_id + "' ";
				bo.setConnection();
				Statement sbx = this.bo.dbconn.createStatement(); // sbx -> StatementBulkExtension
				sbx.executeUpdate(bx);
				
				sbx.close();
				bo.disConnection();
				
				// 한권만 연장하는 경우
			} else {
				
				// 출력한 책들 중 어떤 책을 연장할건지 선택(선택은 id로)
				System.out.print("연장할 책의 번호를 입력하세요: ");
				id = sc.nextLine();
				if(t1.isNumber(id) == false) {return;}
				index = Integer.parseInt(id);
				
				// update Rent set expirydate = date_add(expirydate, INTERVAL 2 WEEK) where id = 1;
				String ud = "update Rent set expirydate = date_add(expirydate, INTERVAL 2 WEEK) where id = " + id;
				bo.setConnection();
				Statement sud = this.bo.dbconn.createStatement();
				sud.executeUpdate(ud);
				
				sud.close();
				bo.disConnection();
				
			} // if-else문 종료
			
		} // 대출 연장 할건지 안할건지 물어보는 if문 종료
		
	} // 대여 메소드 종료
	
	// 대출 반납 메소드
	public void deleteRent() throws SQLException {
		
		// user_id 입력 -> 리스트 출력 -> 거기서 반납할 책 번호 선택 -> 그 번호에 해당하는 데이터 삭제
		// 1.일괄 삭제, 2.한권씩 삭제 기능
		String str = ""; // sys문 답변을 저장하기 위한 변수
		
		System.out.print("책을 반납하시겠습니까? : ");
		str = sc.nextLine();
		if(t1.strTest(str) == false) {return;}
		
		if(str.equals("ㅇㅇ") || str.equals("네") || str.equals("dd")) {
			
			System.out.print("아이디를 입력하세요: ");
			member_id = sc.nextLine();
			if(t1.strTest(member_id) == false) {return;}
			
			// 입력한 id가 table내에 존재 하는지 확인하는 코드
			String si = "select count(*) from Member where userid = '" + member_id + "' "; // si -> SearchId
			bo.setConnection();
			Statement stsi = this.bo.dbconn.createStatement();
			ResultSet rs = stsi.executeQuery(si);
			rs.next();
			int count = rs.getInt("count(*)");
			if(count == 0) {
				System.out.println("해당 아이디가 없습니다. 대출 프로세스를 종료합니다.");
				rs.close();
				stsi.close();
				bo.disConnection();
				return;
			}
		
			stsi.close();
			rs.close();
			bo.disConnection();
			
			// 1. 입력한 아이디에 해당하는 데이터 모두 출력
			// 2. 그 책들 중 반납할 책 선택

			String spl = "select Rent.id, Book.title, Rent.book_id, Member.pname, Rent.member_id, Rent.created, Rent.expirydate"
						+ " from Rent Inner join Book on Rent.book_id = Book.isbn "
						+ " Inner join Member on Rent.member_id = Member.userid"
						+ " where Rent.member_id = '" + member_id + "' ";
			bo.setConnection();
			Statement test = this.bo.dbconn.createStatement();
			ResultSet te = test.executeQuery(spl);
			
			while(te.next()) {
				System.out.println(te.getInt("id") + "." +te.getString("Book.title") + ", " + te.getString("Rent.book_id") + ", " + te.getString("Member.pname")
									 + ", " + te.getString("Rent.member_id") + ", " + te.getTimestamp("Rent.created") + ", " 
									 + te.getTimestamp("Rent.expirydate"));
			}
			
			te.close();
			test.close();
			bo.disConnection();
			
			// 일괄 반납 기능시작
			System.out.print("일괄반납 하시겠습니까? : "); 
			str = sc.nextLine();
			if(t1.strTest(str) == false) {return;} 
			
			// 일괄 반납 구현 시작
			if(str.equals("dd") || str.equals("ㅇㅇ") || str.equals("네")) {
				
				// delete from Rent where member_id = 'id1';
				String bx = "update Rent set expirydate = date_add(expirydate, INTERVAL 2 WEEK) where member_id = '" + member_id + "' ";
				bo.setConnection();
				Statement sbx = this.bo.dbconn.createStatement(); // sbx -> StatementBulkExtension
				sbx.executeUpdate(bx);
				
				sbx.close();
				bo.disConnection();
				
				// 한권만 연장하는 경우
			} else {
				
				// 출력한 책들 중 어떤 책을 연장할건지 선택(선택은 id로)
				System.out.print("연장할 책의 번호를 입력하세요: ");
				id = sc.nextLine();
				if(t1.isNumber(id) == false) {return;}
				index = Integer.parseInt(id);
				
				// update Rent set expirydate = date_add(expirydate, INTERVAL 2 WEEK) where id = 1;
				String ud = "update Rent set expirydate = date_add(expirydate, INTERVAL 2 WEEK) where id = " + id;
				bo.setConnection();
				Statement sud = this.bo.dbconn.createStatement();
				sud.executeUpdate(ud);
				
				sud.close();
				bo.disConnection();
				
			} // if-else문 종료
			
		} // 대출 연장 할건지 안할건지 물어보는 if문 종료
	}
	
	// 책 대출 내역 출력 메소드
	
}