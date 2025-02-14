package book_Management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Member {
	public Scanner st;
	public Test t1; // 유효성 검사 
	public Book bo; // book 객체를 사용하기 위한 변수
	public Connection dbconn;
	public int id; // member index
	public String pname;  // 멤버 이름
	public String userid; // 멤버 id
	public String gender; // 멤버 성별
	public String mobile; // 멤버 모바일
	public String pw;	  // 멤버 비밀번호
	
	Member(){		
		this.dbconn = null;		
		bo = new Book();
		t1 = new Test();		
	}	
	
	// 작업 선택하는 메서드
	public void control() throws SQLException {
		Scanner s = new Scanner(System.in);
		
		while(true) {
			System.out.println("작업관리[c:멤버추가,r:멤버출력,u:멤버수정,d:멤버삭제,x:뒤로가기]");
			String ins = s.nextLine();
			
			if(ins.equals("x")||ins.equals("X"))break;
			switch(ins) {
			
			// 입력값이 'c'이면 멤버 추가 메서드 실행
			case "c","C":
				addMemeber(); break;
				
			// 입력값이 'r'이면 멤버 리스트를 출력하는 메서드 실행	
			case "r","R":
				printMember(); break;
				
			// 입력값이 'u'이면 멤버 정보를 수정하는 메서드 실행	
			case "u","U":
				updateMember();	break;
			
			// 입력값이 'd'이면 멤버 정보를 제거하는 메서드 실행
			case "d","D":
				deleteMember(); break;
			}
		}
	}
	
	//멤버를 추가하는 메소드
	public void addMemeber() throws SQLException {
		Scanner s = new Scanner(System.in);
		
		int id = 0; // 현재 몇명의 데이터가 들어왔는지 확인하는 방법
		
		while(true) {
			System.out.println("이름을 입력하세요   ['   ':뒤로가기]");
			pname = st.nextLine();
			if(pname.equals("")) break;
			System.out.println("유저 아이디를 입력하세요   ['  ':뒤로가기]");
			String userid = st.nextLine();
			
			if(id != 0) {
				String ts = "select pname from Member where pname like '" + pname + "%' "
												+ "or pname like '%" + pname + "' "
												+ "or pname like '%" + pname + "%'";
				
				Statement test = this.bo.dbconn.createStatement();
				ResultSet rs = test.executeQuery(ts);
				
				if(rs.getString("pname") != null) {		
					System.out.println("똑같은 아이디가 존재합니다. 처음부터 다시 하십시오");
					break;
				} 
				rs.close();
				this.disConnection();
			}	
			
			System.out.println("비밀번호를 입력하세요 ['   ':뒤로가기]");
			pw = st.nextLine();
			if(pw.equals(""))break;
			if(userid.equals("select userid from member"))break;
			System.out.println("성별을 입력하세요   ['   ':뒤로가기]");
			gender = st.nextLine();
			if(gender.equals(""))break;
			System.out.println("모바일번호를 입력하세요 ex)010-1234-5678   ['   ':뒤로가기]");
			mobile = st.nextLine();
			if(t1.strTest(mobile))break;
						
			String sql = "insert into Member set pname='"+pname+"',userid='"+userid+
					"',gender='"+gender+"',mobile='"+mobile+"',pw='"+pw+"',created=current_timestamp,updated=current_timestamp";
			this.setConnection();
			try {
				Statement st = this.dbconn.createStatement();
				st.executeUpdate(sql);
				st.close();st.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			this.disConnection();
			id++;
			
		} //while문 종료
	}
	
	//멤버 출력 메소드
	public void printMember() {
		Scanner s1 = new Scanner(System.in);
		
		while(true) {
			System.out.print("작업 선택 [e: 전체 보기 t:제목으로 찾기, a:저자명으로 찾기, p:출판사로 찾기, x:종료]: ");
			String str = s1.nextLine();
			if(str.equals("x") || str.equals("X")) break;
			
			switch(str) {
			case "i", "I":
				this.iSearch(); break; 	// 멤버 리스트 전체를 출력하는 메서드 호출
			case "n", "N":
				this.nSearch(); break;	// id로 검색으로 검색하는 메서드 호출
			case "m", "M":
				this.mSearch(); break;	// 이름(pname)으로 검색 검색하는 메서드 호출
			case "a", "A":
				this.allList();; break;	// mobile로 검색 검색하는 메서드 호출
			}				
		}
	}
	
	//멤버 수정 메소드
	public void updateMember() {
		this.printMember();
		Scanner s1 = new Scanner(System.in);
		while(true) {
			System.out.println("수정하고 싶은 유저의 유저아이디를 입력하시오.   ['  ':뒤로가기]");
			String userid = s1.nextLine();
			if(userid.equals(""))break;	
			
			System.out.println("이름을 입력하시오.   ['   ':뒤로가기]");
			String updateName = s1.nextLine();
			if(updateName.equals(""))break;
			
			System.out.println("성별을 입력하시오.   ['   ':뒤로가기]");
			String updateGender = s1.nextLine();
			if(updateGender.equals(""))break;
			
			System.out.println("모바일 번호를 입력하시오.  ex)010-1234-5678 ['   ':뒤로가기]");
			String updateMobile = s1.nextLine();
			if(updateMobile.equals(""))break;
			
			System.out.println("비밀번호를 입력하시오.   ['   ':뒤로가기]");
			String updatePw = s1.nextLine();
			if(updatePw.equals(""))break;
			
			this.setConnection();
			try {
				String sql ="update member set pname='"+updateName+
							"',gender='"+updateGender+"',mobile='"+updateMobile+
							"', pw='"+updatePw+"',updated=current_timestamp where userid= "+userid;
				Statement st = this.dbconn.createStatement();
				st.executeUpdate(sql);
				st.close();
			}catch(SQLException e) {
				System.out.println((e.getMessage()));
			}
			this.disConnection();
		}	//while문 종료		
	}
	
	//멤버 삭제 메소드
	public void deleteMember() {
		Scanner s1 = new Scanner(System.in); 
		while(true) {
			System.out.println("삭제하고 싶은 유저의 유저아이디를 입력하시오.   ['  ':뒤로가기]");
			String userid= s1.nextLine();
			if(userid.equals(""))break;
			
			this.setConnection();
			try {
				String sql ="delete from member where userid='"+userid+"'";
				Statement st = this.dbconn.createStatement();
				st.executeUpdate(sql);
				st.close();
			}catch(SQLException e) {
				System.out.println((e.getMessage()));
			}
			this.disConnection();
		}//while문 종료
	}
	
	// 모든 Member 리스트 출력하는 메서드
	public void allList() {
		
	}
	
	// 이름으로 검색한 Member 리스트 출력하는 메서드
	public void nSearch() {
		while(true) {
			System.out.print("찾으실 책의 제목을 입력해주세요[''입력시 종료]: ");
			pname = st.nextLine();
			if(t1.strTest(pname) == false) break;
			
			this.setConnection();
			try {
				Statement Nst = dbconn.createStatement();
				String Nsql = "select id,pname,userid,gender,mobile,pw,created,updated"+
							  " from Meber where pname='"+pname+"'";
				ResultSet Nrs = Nst.executeQuery(Nsql);
				
				while(Nrs.next())
				{
					System.out.println("id:"+Nrs.getInt("id")+" 이름:"+Nrs.getString("pname")+" uesrid:"+Nrs.getString("uesrid")
									 +" 성별:"+Nrs.getInt("gender")+" 모바일:"+Nrs.getString("mobile")
									 +" 비밀번호:"+Nrs.getInt("pubyear")
									 +" / 등록시일:"+Nrs.getTimestamp("created")+" _수정시일:"+Nrs.getTimestamp("updated"));
				}
				Nrs.close(); 
				Nst.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			this.disConnection();		
		}
	}
	
	// id로 검색한 Member 리스트 출력하는 메서드
	public void iSearch() {
		while(true) {
			System.out.println("찾고 싶은 유저의 유저 아이디를 입력하세요   ['   ':뒤로가기]");
			userid = st.nextLine();
			if(t1.strTest(userid) == false) break;
			
			this.setConnection();
			try {							
				Statement st = this.dbconn.createStatement();
				String sql = "select id,pname,userid,gender,mobile,pw,created,updated from member where userid='"+userid+"'";
				ResultSet rs = st.executeQuery(sql);
				
				while (rs.next()) {
					System.out.println(rs.getString("pname")+", "+rs.getString("userid")+", "+
							  	 	  rs.getString("gender")+", "+rs.getString("mobile")+", "+
							  	 	  rs.getString("pw")+", "+rs.getString("created")+", "+rs.getString("updated"));
				}
				rs.close(); 
				st.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage()); 
			}
			this.disConnection();
		}	//while문 종료
	}
	
	// mobile로 검색한 Member 리스트 출력하는 메서드
	public void mSearch() {
		
	}
	
	// DB를 연결하기 위한 메서드
	public void setConnection() {
		String dbDriver = "com.mysql.cj.jdbc.Driver";
		String dbUrl = "jdbc:mysql://192.168.0.24:3306/Project"; 
		String dbUser = "yc";
		String dbPassword = "0627"; 
		
		
		try {
			Class.forName(dbDriver); 
			dbconn = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
		} catch (SQLException e) {
			System.out.println("DB Connection [실패]");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("DB Connection [실패]");
			e.printStackTrace();
		}
	}
	
	// DB와 연결해제 하기 위한 메서드
	public void disConnection() {
		try {
			this.dbconn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
