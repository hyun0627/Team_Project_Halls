package book_Management;

import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Book {
	public Scanner s;  //정수형 스캐너 변수 
	public Scanner s1; //문자열 스캐너 변수
//	private Test t1; // 유효성 검사를 하기 위한 Test클래스를 불러오기 위한 변수 
	public Connection dbconn; // DB연결하기 위한 변수
	private int id; // index
	private String title; // 책 제목
	private String isbn; // 책 일련번호
	private int price; // 가격
	private String publisher; // 출판사
	private int pubyear; // 출판연도(PublisherYear)
	private String author; // 저자
	
	Book(){
		this.s = new Scanner(System.in);
		this.s1 = new Scanner(System.in);
		this.dbconn = null;
	}
		
	public void control() {
		 
		while(true) {
			System.out.print("작업 선택 [c:책 등록하기, r:책 리스트 보기, u:책 자료 수정하기, d:책 폐기하기, x:종료하기] : ");
			String str = s1.nextLine();
			// 입력값이 'x'이면 main 객체로 전환
			if(str.equals("x") || str.equals("X")) break;
			
			switch(str) {
			// 입력값이 'c'이면 책 추가 메소드 실행
			case "c", "C":
				this.addBook(); break; 
			// 입력값이 'r'이면 책 리스트 보기 메소드 실행
			case "r", "R":
				this.printBook(); break;
			// 입력값이 'u'이면 책 자료 수정하기 메소드 실행
			case "u", "U":
				this.updateBook(); break;
			// 입력값이 'd'이면 책 폐기 메소드 실행
			case "d", "D":
				this.deleteBook(); break;
			}
				
		} // while문 종료	 
	}
	
	private void addBook() {
		// 책 정보 추가 메소드
		this.setConnection();
		
		try {
			String idsql = "select Max(id) from book"; 
			Statement st = this.dbconn.createStatement();
			ResultSet rs = st.executeQuery(idsql);
			rs.next();
			id = rs.getInt("Max(id)")+1;
			rs.close(); 
			st.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			this.disConnection();		
					
		while(true) {
			System.out.print("등록할 책의 제목을 입력해주세요 [''입력시 종료]: ");			
			title = s1.nextLine();
			if(title.equals("")) break;
			System.out.print("isbn을 입력해주세요 [''입력시 종료]: ");
			if(title.equals("")) break;
			isbn = s1.nextLine();
			System.out.print("가격을 입력해주세요 [''입력시 종료]:");
			if(title.equals("")) break;
			price = s.nextInt();
			System.out.print("출판사를 입력해주세요 [''입력시 종료]: ");
			if(title.equals("")) break;
			publisher = s1.nextLine();
			System.out.print("출판연도를 입력해주세요 [''입력시 종료]: ");
			if(title.equals("")) break;
			pubyear = s.nextInt();
			System.out.print("저자를 입력해주세요 [''입력시 종료]: ");
			if(title.equals("")) break;
			author = s1.nextLine();
			
			this.setConnection();
			try {
				String sql = "insert into book set id="+id+",title='"+title+"',isbn='"+isbn+
							 "', price="+price+",publisher='"+publisher+"',pubyear="+pubyear+
							 ",author='"+author+"'";
				Statement st = this.dbconn.createStatement();
				st.executeUpdate(sql);
				st.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			this.disConnection();
			} //while문 종료
		
	}
	
	//검색을 통해 책 리스트를 보게 하는 메서드
	public void printBook() {

		while(true) {
			System.out.print("작업 선택 [e: 전체 보기 t:제목으로 찾기, a:저자명으로 찾기, p:출판사로 찾기, x:종료]: ");
			String str = s1.nextLine();
			if(str.equals("x") || str.equals("X")) break;
			
			switch(str) {
			//
			case "e", "E":
				this.Everything(); break; 	// 책 리스트 전체를 보여주는 메서드 호출
			case "t", "T":
				this.Tsearch(); break;		// 제목으로 검색으로 검색하는 메서드 호출
			case "a", "A":
				this.Asearch(); break;		// 저자로 검색 검색하는 메서드 호출
			case "p", "P":
				this.Psearch(); break;		// 출판사로 검색 검색하는 메서드 호출
			}				
		}//while문 종료
	}
	
	// 책 정보를 수정할 책을 선택하는 메서드 -> 어떤 책을 수정할 건지 선택	
	public void updateBook() {
		
		while(true) {			
			System.out.print("  t : 수정할 책 제목으로 검색 \n  a : 수정할 책 저자명으로 검색 "
					       + "\n  p : 수정할 책 출판사로 검색하기 \n  x : 돌아가기\n작업선택  ");
			String str = s1.nextLine();
			if(str.equals("x") || str.equals("X")) break;
				
			switch(str) {
			case "t", "T":
				this.Tsearch(); // 제목으로 검색으로 검색하는 메서드 호출
				this.upAdd();   // 수정 실행시키는 메서드 호출
				break;
			case "a", "A":
				this.Asearch(); // 저자로 검색 검색하는 메서드 호출
				this.upAdd();	// 수정 실행시키는 메서드 호출
				break;
			case "p", "P":
				this.Psearch(); // 출판사로 검색 검색하는 메서드 호출
				this.upAdd();	// 수정 실행시키는 메서드 호출
				break;
			}
		}//while문 종료
	}
	
	// 폐기할 책을 선택하는 메서드 -> 어떤 책을 폐기할 건지 선택
	public void deleteBook() {
		
		while(true) {
			System.out.print("  t : 삭제할 책 제목으로 검색 \n  a : 삭제할 책 저자명으로 검색 "
						   + "\n  p : 삭제할 책 출판사로 검색하기 \n  x : 돌아가기\n작업선택  ");
			String str = s1.nextLine();
			if(str.equals("x") || str.equals("X")) break;
			
			switch(str) {
			case "t", "T":
				this.Tsearch(); // 제목으로 검색으로 검색하는 메서드 호출
				this.delAdd();  // 폐기 실행 메서드 호출
				break;
			case "a", "A":
				this.Asearch(); // 저자로 검색 검색하는 메서드 호출
				this.delAdd();  // 폐기 실행 메서드 호출
				break;
			case "p", "P":
				this.Psearch(); // 출판사로 검색 검색하는 메서드 호출
				this.delAdd();  // 폐기 실행 메서드 호출
				break;
			}			
		}//while문 종료					
	}
	
	// 책 수정내용 입력 후 저장하는 메서드
	// 책 정보를 수정시키는 실행시키는 메서드
	public void upAdd() {	
		System.out.print("수정할 책의 id을 입력해주세요 : ");
		id = s1.nextInt();
		System.out.print("수정할 제목을 입력해주세요 : ");
		title = s1.nextLine();
		System.out.print("수정할 가격을 입력해주세요 :");
	    price = s.nextInt();
		System.out.print("수정할 출판사를 입력해주세요 : ");
		publisher = s1.nextLine();
		System.out.print("수정할 출판연도를 입력해주세요 : ");
		pubyear = s.nextInt();
		System.out.print("수정할 저자를 입력해주세요 : ");
		author = s1.nextLine();
						
		this.setConnection();
		try {
			String sql = "update book set title='"+title+"',price="+price+
					     ",publisher='"+publisher+"',pubyear="+pubyear+
					     ",author='"+author+"',updated=currunt_timestamp where id="+id;
			Statement st = this.dbconn.createStatement();
			st.executeUpdate(sql);
			st.close();
			System.out.println("성공적으로 수정이 완료되었습니다.");
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		this.disConnection();
	}
	
	// 폐기할 책 입력 후 실행시키는 메서드
	// 책 폐기를 실행시키는 메서드
	public void delAdd() {
		System.out.print("삭제할 책의 id 번호를 입력해주세요 : ");
		id = s.nextInt();			
		System.out.print("정말 삭제하시겠습니까[y/x]? : ");
		String decide = s1.nextLine();
		if(decide.equals("x")) {
			System.out.println("삭제가 취소되었습니다.");
			return;
		}
		
		this.setConnection();
		try {
			String sql = "delete from book where id="+id;
			Statement st = this.dbconn.createStatement();
			st.executeUpdate(sql);
			st.close();
			System.out.println("성공적으로 삭제되었습니다.");
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		this.disConnection();
	}
	
	// 제목으로 검색으로 검색하는 메서드 
	// 책 제목으로 검색하여 리스트를 출력하는 메서드
	public void Tsearch() {
		while(true) {
			System.out.print("찾으실 책의 제목을 입력해주세요[''입력시 종료]: ");
			String Tsearch = s1.nextLine();
			if(Tsearch.equals("")) break;
			
			this.setConnection();
			try {
				Statement Tst = dbconn.createStatement();
				String Tsql = "select id,title,isbn,price,publisher,pubyear,author,created,updated"+
							  " from book where title like'"+Tsearch+"%' or title like '%"+
							  Tsearch+"' or title like '%"+Tsearch+"%'";
				ResultSet Trs = Tst.executeQuery(Tsql);
				
				while(Trs.next())
				{
					System.out.println("id:"+Trs.getInt("id")+" 제목:"+Trs.getString("title")+" isbn:"+Trs.getString("isbn")
									 +" 가격:"+Trs.getInt("price")+" 출판사:"+Trs.getString("publisher")
									 +" 출판연도:"+Trs.getInt("pubyear")+" 저자:"+Trs.getString("author")
									 +" / 등록시일:"+Trs.getTimestamp("created")+" _수정시일:"+Trs.getTimestamp("updated"));
				}
				Trs.close(); 
				Tst.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			this.disConnection();		
		}
	}
	
	// 저자로 검색 검색하는 메서드 호출
	// 책 저자명으로 검색하여 리스트를 출력하는 메서드
	public void Asearch() {
		while(true) {
			System.out.print("찾으실 책의 저자를 입력해주세요[''입력시 종료]: ");
			String Asearch = s1.nextLine();
			if(Asearch.equals("")) break;
			
			this.setConnection();
			try {
				Statement Ast = dbconn.createStatement();
				String Asql = "select id,title,isbn,price,publisher,pubyear,author,created,updated"+
							  " from book where author like'"+Asearch+"%' or author like '%"+
							  Asearch+"' or author like '%"+Asearch+"%'";
				ResultSet Ars = Ast.executeQuery(Asql);
				
				while(Ars.next())
				{
					System.out.println("id:"+Ars.getInt("id")+". 제목:"+Ars.getString("title")+" isbn:"+Ars.getString("isbn")
									 +" 가격:"+Ars.getInt("price")+" 출판사:"+Ars.getString("publisher")
									 +" 출판연도:"+Ars.getInt("pubyear")+" 저자:"+Ars.getString("author")
									 +" / 등록시일:"+Ars.getTimestamp("created")+" _수정시일:"+Ars.getTimestamp("updated"));
				}
				Ars.close(); 
				Ast.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			this.disConnection();		
		}
	}
	
	// 출판사로 검색 검색하는 메서드 호출
	// 책 출판사로 검색하여 리스트를 출력하는 메서드
	public void Psearch() {
		while(true) {
			System.out.print("찾으실 책의 출판사를 입력해주세요[''입력시 종료]: ");
			String Psearch = s1.nextLine();
			if(Psearch.equals("")) break;
			
			this.setConnection();
			try {
				Statement Pst = dbconn.createStatement();
				String Psql = "select id,title,isbn,price,publisher,pubyear,author,created,updated"+
							  " from book where publisher like'"+Psearch+"%' or publisher like '%"+
							  Psearch+"' or publisher like '%"+Psearch+"%'";
				ResultSet Prs = Pst.executeQuery(Psql);
				
				while(Prs.next())
				{
					System.out.println("id:"+Prs.getInt("id")+". 제목:"+Prs.getString("title")+" isbn:"+Prs.getString("isbn")
									 +" 가격:"+Prs.getInt("price")+" 출판사:"+Prs.getString("publisher")
									 +" 출판연도:"+Prs.getInt("pubyear")+" 저자:"+Prs.getString("author")
									 +" / 등록시일:"+Prs.getTimestamp("created")+" _수정시일:"+Prs.getTimestamp("updated"));
				}
				Prs.close(); 
				Pst.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			this.disConnection();		
		}
	}
	
	//책 전체 리스트 출력하는 메서드
	// 모든 책 리스트를 출력하는 메서드
	public void Everything() {
		this.setConnection();
		try {
			Statement Est = dbconn.createStatement();
			String Esql = "select id,title,isbn,price,publisher,pubyear,author,created,updated"+
						  " from book order by id";
			ResultSet Ers = Est.executeQuery(Esql);
			
			while(Ers.next())
			{
				System.out.println("id:"+Ers.getInt("id")+". 제목:"+Ers.getString("title")+" isbn:"+Ers.getString("isbn")
								 +" 가격:"+Ers.getInt("price")+" 출판사:"+Ers.getString("publisher")
								 +" 출판연도:"+Ers.getInt("pubyear")+" 저자:"+Ers.getString("author")
								 +" / 등록시일:"+Ers.getTimestamp("created")+" _수정시일:"+Ers.getTimestamp("updated"));
			}
			Ers.close(); 
			Est.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		this.disConnection();		
	}
	
	//DB 연결하는 메서드
	//DB 연결하는 메서드
	public void setConnection(){
		String dbDriver = "com.mysql.cj.jdbc.Driver";
		String dbUrl = "jdbc:mysql://127.0.0.1:3306/project";
		String dbUser = "root";
		String dbPassword = "himedia";
		
		try {
			Class.forName(dbDriver);
			this.dbconn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		    }catch(SQLException e) {
				System.out.println("DB Connection [실패]");
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				System.out.println("DB Connection [실패]");
				e.printStackTrace();
			}	
	}
	
	// DB 연결해제하는 메서드
	//DB 연결해제하는 메서드
	public void disConnection() {
		try {
			this.dbconn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}