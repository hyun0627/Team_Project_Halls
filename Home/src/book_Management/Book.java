package book_Management; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

	public class Book {  
	
	private Scanner sc; 
	private Test t1;
	public Connection dbconn; 
	private int id; // index
	private String title; // 책 제목
	private String isbn; // 책 일련번호
	private int price; // 가격
	private String publisher; // 출판사
	private int pubyear; // 출판연도
	private String author; // 저자
	private int qty; // 책의 수량
	private String bqty; // 수량 입력 후 int에 저장 전 유효성 검사를 하기 위한 변수
	
	// Book클래스 생성자
	Book() {
		
		sc = new Scanner(System.in);
		t1 = new Test();
		this.dbconn = null;

	}
	
	// 실행 메소드
	public void run() throws SQLException {
		
		String bms; // BookManagementSelect 약자

		System.out.println("===============================================================");
		System.out.println("책 관리프로그램을 실행합니다.");

		while(true) {
			System.out.println("[ C: 책 정보 추가 || U: 책 정보 수정 || D: 책 정보 삭제 || R: 책 정보 출력 || X or 공백: 종료 ]");
			System.out.print("작업을 선택하세요: ");
			bms = sc.nextLine();
			
			if(bms.equals("x") || bms.equals("X") || t1.strTest(bms) == false) {
				System.out.println("===============================================================");
				System.out.println("[ B: 책 정보 관리 || M: 사람 정보 관리 || R: 책 대여 관리 || X: 종료 ]");
				break;
			} else {
				if(bms.equals("c") || bms.equals("C")) {
					System.out.println("책 정보 추가 프로세스를 실행합니다.");
					addBook();
				} else if(bms.equals("u") || bms.equals("U")) {
					System.out.println("책 정보 수정 프로세스를 실행합니다.");
					updateBook();
				} else if(bms.equals("d") || bms.equals("D")) {
					System.out.println("책 정보 삭제 프로세스를 실행합니다.");
					deleteBook();
				} else if(bms.equals("r") || bms.equals("R")) {
					System.out.println("책 정보 출력 프로세스를 실행합니다.");
					printBook();				
				}
			} // if-else문 종료
			
		} // while문 종료
		
	}
	
	// 책 정보 추가 메소드(addBook)
	public void addBook() throws SQLException {
    	
    	// 책 제목부터 나머지 정보들 입력
		System.out.print("책 제목을 입력하세요 : ");
		title = sc.nextLine();
		if(t1.strTest(title) == false) {return;}
		
		System.out.print("책 일련번호를 입력하세요 : ");
		isbn = sc.nextLine();
		if(t1.strTest(isbn) == false) {return;}
		
		System.out.print("가격을 입력하세요 : ");
		String pr = sc.nextLine();
		if(t1.isNumber(pr) == false) {return;}
		price = Integer.parseInt(pr);
		
		System.out.print("출판사를 입력하세요 : ");
		publisher = sc.nextLine();
		if(t1.strTest(publisher) == false) {return;}
		
		System.out.print("출판연도를 입력하세요 : ");
		String py = sc.nextLine();
		if(t1.isNumber(py) == false) {return;}
		pubyear = Integer.parseInt(py);
		
		System.out.print("저자를 입력하세요 : ");
		author = sc.nextLine();
		if(t1.strTest(author) == false) {return;}
		
		System.out.print("책의 수량을 입력하세요 : ");
		bqty = sc.nextLine();
		if(t1.isNumber(bqty) == false) {return;}
		qty = Integer.parseInt(bqty);
		
		// 입력한 정보를 sql로 전달
		this.setConnection();
		
		Statement st = this.dbconn.createStatement();
    	String selid = "select Max(id) from Book";
    	ResultSet rs = st.executeQuery(selid);
    	rs.next();
    	id = rs.getInt("Max(id)");
    	id++;
    	
    	String add = "insert into Book(id, title, isbn, price, publisher, pubyear, author, qty)"
    			        + " values(" + id + "," + " '" + title + "' ," + " '" + isbn + "' ," + price
    			        + ", '" + publisher + "' ," + pubyear + ", " + "'" + author + "', " + qty + ")";
    	
    	st.executeUpdate(add);
		st.close();
		this.disConnection();
		
	}
	
	// 책 정보 삭제 메소드(deleteBook)
	public void deleteBook() throws SQLException {
		
		String str = ""; 
		String ain = "";
		String index = "";
		
		// 삭제 -> index, 책 일련번호, 책 이름
		System.out.print("index로 삭제하시겠습니까? 다른 걸로 삭제하시겠습니까?");
		str = sc.nextLine();
		if(t1.strTest(str) == false) {return;}
		
		// index로 삭제
		if(str.equals("index") || str.equals("INDEX") || str.equals("인덱스")) {
			System.out.print("삭제할 책의 Index를 입력하십시오 : ");
			index = sc.nextLine();
			if(t1.isNumber(index) == false) {return;}
			id = Integer.parseInt(index);
			
			// DB연결
			this.setConnection();
			String did = "delete from Book where id = " + id; // did = deleteIndex
			ain = "update Book set id = id - 1 where id > " + id;
			Statement dx = this.dbconn.createStatement(); // dx = deleteIndex
			dx.executeUpdate(did);
			dx.executeUpdate(ain);
			dx.close();
			this.disConnection(); // DB연결 종료
			
			// 다른 걸로 삭제
		} else if(str.equals("다른거") || str.equals("딴거") || str.equals("else") || str.equals("select") || str.equals("sl")) {
			System.out.print("일련번호로 삭제하시겠습니까? 이름으로 삭제하시겠습니까?");
			String str2 = sc.nextLine();
			
			if(str2.equals("일련번호") || str2.equals("isbn")) {
				System.out.print("삭제할 책의 일련번호를 입력하세요 : ");
				isbn = sc.nextLine();
				if(t1.strTest(isbn) == false) {return;}
				
				// DB연결 
				this.setConnection();
				String dis = "delete from Book where isbn = '" + isbn + "' ";
				ain = "update Book set id = id - 1 where id > " + id;
				Statement di = this.dbconn.createStatement();
				di.executeUpdate(dis);
				di.executeUpdate(ain);
				di.close();
				this.disConnection(); // DB연결 종료
				
			} else {
				System.out.print("삭제할 책의 이름을 입력하세요 : ");
				title = sc.nextLine();
				if(t1.strTest(title) == false) {return;}
				
				// DB연결
				this.setConnection();
				String dna = "delete from Book where title like '" + title + "%'"
						 		+ "or title like '%" + title + "' "
						 		+ "or title like '%" + title + "%'";
				ain = "update Book set id = id - 1 where id > " + id;
				Statement dn = this.dbconn.createStatement();
				dn.executeUpdate(dna);
				dn.executeUpdate(ain);
				dn.close();
				this.disConnection();
				
			}
		}
		
	}

	// 책 정보 수정 메소드(updateBook)
	public void updateBook() throws SQLException {
		
		System.out.print("찾을 책을 어떻게 찾으시겠습니까? : ");
		String str = sc.nextLine();
		if(t1.strTest(str) == false) {return;}
		
		if(str.equals("이름") || str.equals("name")) {
			System.out.print("찾을 책의 이름을 입력하세요: ");
			title = sc.nextLine();
			if(t1.strTest(title) == false) {return;}
			
			this.setConnection();
			String getin = "select id, title, price, author, isbn from Book where title like '%" + title + "' "
					  		 + " or title like '" + title + "%'" 
					  		 + " or title like '%" + title + "%'";
			Statement st = this.dbconn.createStatement();
			ResultSet rs = st.executeQuery(getin);
			
			System.out.println();
			while(rs.next()) {		
	    		System.out.println(rs.getInt("id") + "." + rs.getString("title") + ",\t" + rs.getInt("price") + ",\t"
	    							  + rs.getString("author") + ",\t" + rs.getString("isbn"));
	    	}
			
			rs.close();
			st.close();
			this.disConnection();
			
			System.out.print("변경하고 싶은 책의 인덱스 번호를 입력하세요: ");
			String index = sc.nextLine();
			if(t1.isNumber(index) == false) {return;}
			id = Integer.parseInt(index);
			
			this.setConnection();
			String tedel = "delete from Book where id = " + id;
			Statement st2 = this.dbconn.createStatement();
			st2.executeUpdate(tedel);
			st2.close();
			this.disConnection();
			
			aBook(id);
				
		} else if(str.equals("저자") || str.equals("author")) {
			System.out.print("찾을 책의 저자를 입력하세요: ");
			author = sc.nextLine(); 
			if(t1.strTest(author) == false) {return;} 
			
			this.setConnection();
			String getin = "select id, title, price, author, isbn from Book where author like '%" + author + "' "
					  		 + " or title like '" + author + "%'" 
					  		 + " or title like '%" + author + "%'";
			Statement st = this.dbconn.createStatement();
			ResultSet rs = st.executeQuery(getin);
			
			System.out.println();
			while(rs.next()) {		
	    		System.out.println(rs.getInt("id") + "." + rs.getString("title") + ",\t" + rs.getInt("price") + ",\t"
	    							  + rs.getString("author") + ",\t" + rs.getString("isbn"));
	    	}
			
			rs.close();
			st.close();
			this.disConnection();
			
			System.out.print("변경하고 싶은 책의 인덱스 번호를 입력하세요: ");
			String index = sc.nextLine();
			if(t1.isNumber(index) == false) {return;}
			id = Integer.parseInt(index);
			
			this.setConnection();
			String tedel = "delete from Book where id = " + id;
			Statement st3 = this.dbconn.createStatement();
			st3.executeUpdate(tedel);
			st3.close();
			this.disConnection();
			
			aBook(id);
			
		}
		
	}
	
	// 책 정보 출력 메소드(printBook)
	public void printBook() throws SQLException { 
		
		System.out.print("책 전체를 출력하시겠습니까? : ");
		String str = sc.nextLine();
		// Book테이블 전체 출력
		if(str.equals("ㅇㅇ") || str.equals("dd")) {
			
			this.setConnection();
			Statement st = this.dbconn.createStatement();
	    	String sql = "select id, title, isbn, price, author from Book order by id";
	    	ResultSet rs = st.executeQuery(sql);
	    	
	    	System.out.println("==================메뉴판 출력==================");
	    	System.out.println("        책이름   " + "    일련번호  " + "    가격    " + "    저자    ");
	    	
	    	while(rs.next()) {		
	    		System.out.println(rs.getInt("id") + "." + rs.getString("title") + ",\t" + rs.getString("isbn") + ",\t"
	    							  + rs.getInt("price") + ",\t" + rs.getString("author"));
	    	}
	  	
	    	rs.close();
	    	st.close();
	    	this.disConnection();
	    	// 책 선택 출력
		} else {
			System.out.print("책을 이름으로 찾으시겠습니까? 저자로 찾으시겠습니까? : ");
			str = sc.nextLine();
			if(t1.strTest(str) == false) {return;}
			
			if(str.equals("이름") || str.equals("name")) {
				System.out.print("찾을 책의 이름을 입력하세요 : ");
				title = sc.nextLine();
				if(t1.strTest(title) == false) {return;}
				
				// DB연결
				this.setConnection();
				String pb = "select title, price, author, qty from Book where title like '" + title + "%'"
						 		+ "or title like '%" + title + "' "
						 		+ "or title like '%" + title + "%'";
				Statement st = this.dbconn.createStatement();
				ResultSet rs = st.executeQuery(pb);
				
				System.out.println("==========메뉴판 출력=============");
		    	System.out.println("   책이름   " + "    가격  " + "    저자    " + "    수량    ");
		    	
		    	while(rs.next()) {		
		    		System.out.println(rs.getString("title") + "\t" + rs.getInt("price") + "원" + "\t" + rs.getString("author")
		    							 + rs.getInt(qty));
		    	}
		    	
		    	rs.close();
		    	st.close();
		    	this.disConnection();
		    	
			} else if(str.equals("저자") || str.equals("author")) {
				System.out.print("찾을 책의 저자를 입력하세요 : ");
				author = sc.nextLine();
				if(t1.strTest(author) == false) {return;}
				
				System.out.println("==========메뉴판 출력=============");
		    	System.out.println("   책이름   " + "    가격  " + "    저자    " + "    수량    ");
				
				this.setConnection();
				String pa = "select title, price, author, qty from Book where author = '" + author + "' ";
				Statement st = this.dbconn.createStatement();
				ResultSet rs = st.executeQuery(pa);
				
				while(rs.next()) {		
		    		System.out.println(rs.getString("title") + "\t" + rs.getInt("price") + "원" + "\t" + rs.getString("author")
		    							  + rs.getInt(qty));
		    	}
		    	
		    	rs.close();
		    	st.close();
		    	this.disConnection();
			}
		}
		
	}
	
	// aBook => addBook(간소화버전)
	public void aBook(int _index) throws SQLException {
		
		// 책 제목부터 나머지 정보들 입력
		System.out.print("책 제목을 입력하세요 : ");
		title = sc.nextLine();
		if(t1.strTest(title) == false) {return;}
				
		System.out.print("책 일련번호를 입력하세요 : ");
		isbn = sc.nextLine();
		if(t1.strTest(isbn) == false) {return;}
				
		System.out.print("가격을 입력하세요 : ");
		String pr = sc.nextLine();
		if(t1.isNumber(pr) == false) {return;}
		price = Integer.parseInt(pr);
				
		System.out.print("출판사를 입력하세요 : ");
		publisher = sc.nextLine();
		if(t1.strTest(publisher) == false) {return;}
				
		System.out.print("출판연도를 입력하세요 : ");
		String py = sc.nextLine();
		if(t1.isNumber(py) == false) {return;}
		pubyear = Integer.parseInt(py);
				
		System.out.print("저자를 입력하세요 : ");
		author = sc.nextLine();
		if(t1.strTest(author) == false) {return;}
		
		System.out.print("책의 수량을 입력하세요 : ");
		bqty = sc.nextLine();
		if(t1.isNumber(bqty) == false) {return;}
		qty = Integer.parseInt(bqty);
		
		this.setConnection();
		Statement st = this.dbconn.createStatement();	
    	String add = "insert into Book(id, title, isbn, price, publisher, pubyear, author, qty)"
    			        + " values(" + _index + "," + " '" + title + "' ," + " '" + isbn + "' ," + price
    			        + ", '" + publisher + "' ," + pubyear + ", " + "'" + author + "' , " + qty + ")";
    	
    	st.executeUpdate(add);
		st.close();
		this.disConnection();
		
	}
	
	// DB커넥션 메소드
	public void setConnection() {
		
	    String dbDriver = "com.mysql.cj.jdbc.Driver";
	    String dbUrl = "jdbc:mysql://127.0.0.1:3306/project";
	    String dbUser = "root";
	    String dbPassword = "himedia";
	    
	    try {
	    	
	    	Class.forName(dbDriver);
	    	this.dbconn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
	    	
	    } catch(SQLException e) {
	    	
	    	System.out.println("DB Connection 실패");
	    	e.printStackTrace();
	    	
	    } catch(ClassNotFoundException e) {
	    	
	    	System.out.println("DB Connection 실패");
	    	e.printStackTrace();
	    	
	    }
	}
	
	// DB커넥션 종료 메소드
	public void disConnection() {
		
		try {
			this.dbconn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}