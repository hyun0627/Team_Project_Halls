package book_Management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Book {
	
	public Scanner sc; // 스캐너 변수
	private Test t1; // 유효성 검사를 하기 위한 Test클래스를 불러오기 위한 변수
	public Connection dbconn; // DB연결하기 위한 변수
	private int id; // book index
	private String index; 
	private String title; // 책 제목
	private String isbn; // 책 일련번호
	private int price; // 가격
	private String pr; // 가격 유효성 검사를 위한 변수
	private String publisher; // 출판사
	private int pubyear; // 출판연도(PublisherYear)
	private String author; // 저자
	private int qty; // 책의 수량
	private String bqty; // 수량 입력 후 int에 저장 전 유효성 검사를 하기 위한 변수
	private String str; // 잡다한 것들 저장하기 위한 변수
	
	Book(){
		
		this.sc = new Scanner(System.in);
		this.dbconn = null;
		this.t1 = new Test();
		
	}
	
	// 작업선택 하는 메서드	
	public void control() throws SQLException {
		
		while(true) {
			System.out.print("작업 선택 [c:책 등록하기, r:책 리스트 보기, u:책 자료 수정하기, d:책 폐기하기, x:종료하기] : ");
			String str = sc.nextLine();
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
	
	private void addBook() throws SQLException {
		// 책 정보 추가 메소드
		this.setConnection();		
		try {
			String idsql = "select Max(id) from book";
			Statement st = this.dbconn.createStatement();
			ResultSet rs = st.executeQuery(idsql);
			rs.next();
			id = rs.getInt("Max(id)");
			id++;
			rs.close();
			st.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			this.disConnection();		
					
		while(true) {
			System.out.print("등록할 책의 제목을 입력해주세요 [''입력시 종료]: ");			
			title = sc.nextLine();
			if(t1.strTest(title) == false) break;
			
			System.out.print("isbn을 입력해주세요 [''입력시 종료]: ");
			isbn = sc.nextLine();
			if(t1.strTest(isbn) == false) break;
			
			System.out.print("가격을 입력해주세요 [''입력시 종료]:");
			pr = sc.nextLine();
			if(t1.isNumber(pr) == false) break;
			price = Integer.parseInt(pr);
			
			System.out.print("출판사를 입력해주세요 [''입력시 종료]: ");
			publisher = sc.nextLine();
			if(t1.strTest(publisher) == false) break;
			
			System.out.print("출판연도를 입력해주세요 [''입력시 종료]: ");
			String py = sc.nextLine();
			if(t1.isNumber(py) == false) break;
			pubyear = Integer.parseInt(py);
			
			System.out.print("저자를 입력해주세요 [''입력시 종료]: ");
			author = sc.nextLine();
			if(t1.strTest(author) == false) break;
			
			System.out.print("책의 수량을 입력하세요 [''입력시 종료]: ");
			bqty = sc.nextLine();
			if(t1.isNumber(bqty) == false) break;
			qty = Integer.parseInt(bqty);
			
			this.setConnection();
			try {
				String sql = "insert into book set id="+id+",title='"+title+"',isbn='"+isbn+
							 "', price="+price+",publisher='"+publisher+"',pubyear="+pubyear+
							 ",author='"+author+"',qty="+qty;
				Statement st = this.dbconn.createStatement();
				st.executeUpdate(sql);
				st.close();
				System.out.println("성공적으로 책 등록이 완료되었습니다.");
			}catch(SQLException e) {
				System.out.println("책 추가 중 오류 발생"+e.getMessage());
			}
			this.disConnection();
		} //while문 종료
		
	}	
	//검색을 통해 책 리스트를 보게 하는 메서드
	public void printBook() throws SQLException {

		while(true) {
			System.out.print("작업 선택 [e: 전체 보기 t:제목으로 찾기, a:저자명으로 찾기, p:출판사로 찾기, x:종료]: ");
			String str = sc.nextLine();
			if(str.equals("x") || str.equals("X")) break;
			
			switch(str) {
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
	public void updateBook() throws SQLException {
		
		while(true) {			
			System.out.print("  t : 수정할 책 제목으로 검색 \n  a : 수정할 책 저자명으로 검색 "
					       + "\n  p : 수정할 책 출판사로 검색하기 \n  x : 돌아가기\n작업선택  ");
			String str = sc.nextLine();
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
	public void deleteBook() throws SQLException {
		
		while(true) {
			System.out.print("  t : 삭제할 책 제목으로 검색 \n  a : 삭제할 책 저자명으로 검색 "
						   + "\n  p : 삭제할 책 출판사로 검색하기 \n  x : 돌아가기\n작업선택  ");
			String str = sc.nextLine();
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
	public void upAdd() {
		
		while(true) {
			System.out.print("수정할 책의 id를 입력해주세요 ['x'입력시 종료]: ");
			String ID = sc.nextLine();
			if(ID.equals("x") || ID.equals("X")) break;
			
			if(t1.isNumber(ID) == false) {
				System.out.println("올바른 id를 입력해주세요.");
				continue;
			}
			id = Integer.parseInt(ID);
						
			//기존 데이터 조회
			this.setConnection();
			String oldT = "", oldPs = "", oldA ="";
			int oldPr = 0, oldPy =0, oldQ =0;
			
			try {
				String sql = "select title,price,publisher,pubyear,author,"+
							 "qty from book where id="+id;
				Statement st = this.dbconn.createStatement();
				ResultSet rs = st.executeQuery(sql);
				if(rs.next()) {
					oldT = rs.getString("title");
					oldPr = rs.getInt("price");
					oldPs = rs.getString("publisher");
					oldPy = rs.getInt("pubyear");
					oldA = rs.getString("author");
					oldQ = rs.getInt("qty");					
				} else {
					System.out.println("해당 id의 책을 찾을 수 없습니다");
					rs.close();
					st.close();
					continue;
				}
				rs.close();
				st.close();
			}catch(SQLException e) {
				System.out.println("데이터 조회 중 오류 발생: "+e.getMessage());
				this.disConnection();
				continue;
			}
			this.disConnection();
			
			// 수정할 내용 입력
			System.out.print("제목 ['' 취소 / 'u' 기존 값 유지 / 'n'새 값 입력]: ");
			str = sc.nextLine();
			
			if(t1.strTest(str) == false) {return;}	
			
			while(true) {
				
				if (str.equals("u") || str.equals("U")) { 			
					title = oldT;
					break;
				}else if(str.equals("n") || str.equals("N")){
					System.out.println("새 제목을 입력해주세요.");
					title = sc.nextLine();
					if(t1.strTest(title) == false) {break;}
					break;
				}else {
					System.out.println("잘못 입력하셨습니다.");
					str = sc.nextLine();
				}
			}
			
			// 가격 유효성 검사 추가	
			while(true) {
				System.out.print("가격 ['' 취소 / 'u' 기존 값 유지 / 'n'새 값 입력]: ");
				str = sc.nextLine();
				if(t1.strTest(str) == false) return;
				
				if(str.equals("n") || str.equals("N")) {
					System.out.println("새 가격을 입력해주세요");
					pr = sc.nextLine();
					if(t1.isNumber(pr) == false) {
						System.out.println("0(원) 이상의 올바른 가격을 입력해주세요.");
						continue;
					}
					price=Integer.parseInt(pr);
					break;
					
				} else if (str.equals("u") || str.equals("U")) { 			
					price = oldPr;
					break;
				}	
				
			}		
			
			// 출판사 입력
			while(true) {
				System.out.print("출판사 ['' 취소 / 'u' 기존 값 유지 / 'n'새 값 입력]: ");
				str = sc.nextLine();
				
				if(t1.strTest(str) == false) return;	
				
				if (publisher.equals("u") || publisher.equals("U")) { 			
					publisher = oldPs;
					
				} else if(publisher.equals("n") || publisher.equals("N")){
					System.out.print("새 출판연도를 입력해주세요: ");
					publisher = sc.nextLine();
					if(t1.strTest(publisher) == false) break;
					break;
				} else {
					System.out.println("잘못 입력하셨습니다");
				}
			}

			// 출판연도 유효성 검사 추가
			while(true) {
				System.out.print("출판연도 ['' 취소 / 'u' 기존 값 유지 / 'n'새 값 입력]: ");
				// py가 아니라 u나 n을 입력받는 거 변수 하나 만들어서 여기에 입력 받아야함
				str = sc.nextLine();
				if(t1.strTest(str) == false) return;
						
				if (str.equals("u") || str.equals("U")) { 			
					pubyear = oldPy;
					break;
				}else if(str.equals("n") || str.equals("N")){
					// 여기에서 출판연도를 입력받아야 함
					System.out.print("새 출판연도를 입력해주세요 : ");
					String py = sc.nextLine();
					if(t1.isNumber(py) == false) {
						System.out.println("올바른 출판연도를 입력해주세요.");
						continue;
					}	
					pubyear=Integer.parseInt(py);
					break;
				}
			}

			// 저자 입력
			while(true) {
				System.out.print("저자 ['' 취소 / 'u' 기존 값 유지 / 'n'새 값 입력]: ");
				str = sc.nextLine();
				if(t1.strTest(str) == false) {break;}
				
				if (str.equals("u") || str.equals("U")) { 			
					author = oldA;
					break;
				} else if(str.equals("n") || str.equals("N")) {
					System.out.println(" 새 저자를 입력해주세요.");
					author = sc.nextLine();
					if(t1.strTest(author) == false) break;
					break;
				}
			}
			
			// 수량 유효성 검사 추가
			while(true) {
				System.out.print("수량을 입력해주세요 ['' 취소 / 'u' 기존 값 유지 / 'n'새 값 입력]: ");
				str = sc.nextLine();
				
				if(t1.isNumber(str) == false) {
					System.out.println("올바른 수량을 입력해주세요.");
					continue;
				}
				if (str.equals("u") || str.equals("U")) { 			
					qty = oldQ;
					break;
				}else {
					System.out.print("새 수량을 입력해주세요 : ");
					bqty = sc.nextLine();
					if(t1.isNumber(bqty) == false) {
						System.out.println("올바른 수량을 입력해주세요.");
						continue;
					}	
					qty=Integer.parseInt(bqty);
					break;
				}
			}
			
			this.setConnection();	
			try {
				String sql = "update book set title='"+title+"',price="+price+
						     ",publisher='"+publisher+"',pubyear="+pubyear+",author='"+author+"'"
						     +",qty="+qty+",updated=current_timestamp where id="+id;
				Statement st = this.dbconn.createStatement();
				st.executeUpdate(sql);
				st.close();
				System.out.println("성공적으로 수정이 완료되었습니다.");
			}catch(SQLException e) {
				System.out.println("수정 중 오류 발생"+e.getMessage());
			}
			this.disConnection();
			}//while문 종료
		}
	
	
	
	
	// 폐기할 책 입력 후 실행시키는 메서드
	public void delAdd() {
		
		System.out.print("삭제할 책의 id 번호를 입력해주세요 : ");
		index = sc.nextLine();
		if(t1.isNumber(index) == false) return;
		id = Integer.parseInt(index);
		System.out.print("정말 삭제하시겠습니까[y/x]? : ");
		String decide = sc.nextLine();
		if(decide.equals("x")) {
			System.out.println("삭제가 취소되었습니다.");
			return;
			
		} else if(decide.equals("y")) {
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
		}else {
			System.out.println("잘못된 입력입니다. 다시 입력해주세요");
			return;
		}
		
		
	}
	
	// 책 제목으로 검색하여 리스트를 출력하는 메서드
	public void Tsearch() throws SQLException {
				
				System.out.print("찾으실 책의 제목을 입력해주세요[''입력시 종료]: ");
				String Tsearch = sc.nextLine();
				if(Tsearch.equals("")) return;
			
				this.setConnection();
		
				Statement Tst = dbconn.createStatement();
				String Tsql = "select id,title,isbn,price,publisher,pubyear,author,qty,created,updated"+
								" from book where title like'"+Tsearch+"%' or title like '%"+
								Tsearch+"' or title like '%"+Tsearch+"%'";
				ResultSet Trs = Tst.executeQuery(Tsql);
			
				boolean search = false;  // 검색된 책이 있는지 체크하는 변수
				while(Trs.next()) {
					search = true;
					
					System.out.println("id:"+Trs.getInt("id")+" 제목:"+Trs.getString("title")+" isbn:"+Trs.getString("isbn")
									 +" 가격:"+Trs.getInt("price")+" 출판사:"+Trs.getString("publisher")
									 +" 출판연도:"+Trs.getInt("pubyear")+" 저자:"+Trs.getString("author")+" 수량:"+Trs.getInt("qty")
									 +" / 등록시일:"+Trs.getTimestamp("created")+" _수정시일:"+Trs.getTimestamp("updated"));
				}
				if (!search) {  // 만약 한 번도 검색 결과가 없었다면
		        	System.out.println("해당 책이 존재하지 않습니다.");	
				}
				
				Trs.close();
				Tst.close();

			this.disConnection();		    
	}
	
	// 책 저자명으로 검색하여 리스트를 출력하는 메서드
	public void Asearch() {

			System.out.print("찾으실 책의 저자를 입력해주세요[''입력시 종료]: ");
			String Asearch = sc.nextLine();
			if(Asearch.equals("")) return;
			
			this.setConnection();
			try {
				Statement Ast = dbconn.createStatement();
				String Asql = "select id,title,isbn,price,publisher,pubyear,author,qty,created,updated"+
							  " from book where author like'"+Asearch+"%' or author like '%"+
							  Asearch+"' or author like '%"+Asearch+"%'";
				ResultSet Ars = Ast.executeQuery(Asql);
				
				boolean search = false;  // 검색된 책이 있는지 체크하는 변수
				while(Ars.next()) {
					search = true;
					System.out.println("id:"+Ars.getInt("id")+". 제목:"+Ars.getString("title")+" isbn:"+Ars.getString("isbn")
									 +" 가격:"+Ars.getInt("price")+" 출판사:"+Ars.getString("publisher")
									 +" 출판연도:"+Ars.getInt("pubyear")+" 저자:"+Ars.getString("author")+" 수량:"+Ars.getInt("qty")
									 +" / 등록시일:"+Ars.getTimestamp("created")+" _수정시일:"+Ars.getTimestamp("updated"));
				}
		        if (!search) {  // 만약 한 번도 검색 결과가 없었다면
		        	System.out.println("해당 책이 존재하지 않습니다.");				
		        }
		        
				Ars.close();
				Ast.close();				
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			this.disConnection();		
	}
	
	// 책 출판사로 검색하여 리스트를 출력하는 메서드
	public void Psearch() {

			System.out.print("찾으실 책의 출판사를 입력해주세요[''입력시 종료]: ");
			String Psearch = sc.nextLine();
			if(Psearch.equals("")) return;
			
			this.setConnection();
			try {
				Statement Pst = dbconn.createStatement();
				String Psql = "select id,title,isbn,price,publisher,pubyear,author,qty,created,updated"+
							  " from book where publisher like'"+Psearch+"%' or publisher like '%"+
							  Psearch+"' or publisher like '%"+Psearch+"%'";
				ResultSet Prs = Pst.executeQuery(Psql);
				
				boolean search = false;  // 검색된 책이 있는지 체크하는 변수
				while(Prs.next()) {
					search = true;
					System.out.println("id:"+Prs.getInt("id")+". 제목:"+Prs.getString("title")+" isbn:"+Prs.getString("isbn")
									 +" 가격:"+Prs.getInt("price")+" 출판사:"+Prs.getString("publisher")
									 +" 출판연도:"+Prs.getInt("pubyear")+" 저자:"+Prs.getString("author")+" 수량:"+Prs.getInt("qty")
									 +" / 등록시일:"+Prs.getTimestamp("created")+" _수정시일:"+Prs.getTimestamp("updated"));
				}
				if (!search) {  // 만약 한 번도 검색 결과가 없었다면
		        	System.out.println("해당 책이 존재하지 않습니다.");	
				}
				
				Prs.close();
				Pst.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			this.disConnection();		
	}
	
	// 모든 책 리스트를 출력하는 메서드
	public void Everything() {
		this.setConnection();
		try {
			Statement Est = dbconn.createStatement();
			String Esql = "select id,title,isbn,price,publisher,pubyear,author,qty,created,updated"+
						  " from book order by id";
			ResultSet Ers = Est.executeQuery(Esql);
			
			while(Ers.next())
			{
				System.out.println("id:"+Ers.getInt("id")+". 제목:"+Ers.getString("title")+" isbn:"+Ers.getString("isbn")
								 +" 가격:"+Ers.getInt("price")+" 출판사:"+Ers.getString("publisher")
								 +" 출판연도:"+Ers.getInt("pubyear")+" 저자:"+Ers.getString("author")+" 수량:"+Ers.getInt("qty")
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
	public void setConnection(){
		String dbDriver = "com.mysql.cj.jdbc.Driver";
		String dbUrl = "jdbc:mysql://192.168.0.24:3306/project";
		String dbUser = "gb";
		String dbPassword = "0627";
		
		try {
			Class.forName(dbDriver);
			this.dbconn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
			this.dbconn.setAutoCommit(false);
		    }catch(SQLException e) {
				System.out.println("DB Connection [실패]");
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				System.out.println("DB Connection [실패]");
				e.printStackTrace();
			}	
	}
	
	//DB 연결해제하는 메서드
	public void disConnection() {
		try {
			this.dbconn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}