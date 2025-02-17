package book_Management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Member {
	private Test t1 = new Test();
	public Connection dbconn;
	Member(){
		this.dbconn = null;
	}
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
public void disConnection() {
	try {
		this.dbconn.close();
	}catch (SQLException e) {
		e.printStackTrace();
	}
}
//실행메소드
public void control() {
	Scanner s = new Scanner(System.in);
	
	while(true) {
		System.out.println("작업관리[c:멤버추가,r:멤버출력,u:멤버수정,d:멤버삭제,x:뒤로가기]");
		String ins = s.nextLine();
		
		if(ins.equals("x")||ins.equals("X"))break;
		switch(ins) {
		case "c","C":
			addMemeber();
			break;
		case "r","R":
			printMember();
			break;
		case "u","U":
			updateMember();
			break;
		case "d","D":
			deleteMember();
			break;
		}
	}
	}
//멤버추가 메소드
public void addMemeber() {
	Scanner s1 = new Scanner(System.in);
	while(true) {
	System.out.println("이름을 입력하세요   ['   ':뒤로가기]");
	String name = s1.nextLine();
	if(name.equals("")) break;
	System.out.println("유저 아이디를 입력하세요   ['  ':뒤로가기]");
	String userid = s1.nextLine();
		//userid에 입력한값이 멤버에 일치하는게 있는지 검색
		String ts = "select userid from Member where userid like '" + userid + "%' "
										+ "or pname like '%" + userid + "' "
										+ "or pname like '%" + userid + "%'";
		this.setConnection();
		try {
		Statement test = this.dbconn.createStatement();
		ResultSet rs = test.executeQuery(ts);
		//userid에 입력한값이 일치한게 있으면 아래 문장 실행하고 빠져나가기
		if(rs.next()) {		
			System.out.println("똑같은 아이디가 존재합니다. 처음부터 다시 하십시오.");
			break;
			}
		rs.close();
		this.disConnection();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	System.out.println("성별을 입력하세요   ['   ':뒤로가기]");
	String gender = s1.nextLine();
	if(gender.equals(""))break;
	System.out.println("모바일번호를 입력하세요 ex)010-1234-5678   ['   ':뒤로가기]");
	String mobile = s1.nextLine();
	if(mobile.equals(""))break;
	//mobile에 입력한 값이 테이블 Member에 일치한 값이 있는지 검색
		String t = "select mobile from Member where mobile like '" + mobile + "%' "
											   + "or pname like '%" + mobile + "' "
											   + "or pname like '%" + mobile + "%'";
		this.setConnection();
		try {
			Statement Mobile = this.dbconn.createStatement();
			ResultSet Mo = Mobile.executeQuery(t);
			//mobile에 입력한 값에 일치한게 있으면 아래 문장 실행하고 빠져나가기
			if(Mo.next()) {
				System.out.println("똑같은 모바일 번호가 존재합니다. 처음부터 다시 하십시오.\n");
				break;
			}
			Mo.close();
			
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
	System.out.println("비밀번호를 입력하세요 ['   ':뒤로가기]");
	String pw = s1.nextLine();
	if(pw.equals(""))break;
	//입력한 값을 데이터 베이스에 저장
	String sql = "insert into member set pname='"+name+"',userid='"+userid+
			"',gender='"+gender+"',mobile='"+mobile+"',pw='"+pw+"',created=current_timestamp,updated=current_timestamp";
	this.setConnection();
	try {
	Statement st = this.dbconn.createStatement();
	st.executeUpdate(sql);
	st.close();st.close();
	}	catch(SQLException e) {
		System.out.println(e.getMessage());
	}
	this.disConnection();
	}//while문 종료
	
	}
//멤버 출력 메소드
public void printMember() {
	Scanner s1 = new Scanner(System.in);
	
	while(true) {
		System.out.println("멤버를 찾는 방법를 고르시오.  ex)이름,유저 아이디,모바일번호  ['  ':뒤로가기]");
		String a =s1.nextLine();
		if(a.equals(""))break;
		//이름으로 검색할떄 실행 메소드
		if(a.equals("이름")) {
			System.out.println("찾고 싶은 이름을 입력하시오.");
			a = s1.nextLine();
	this.setConnection();
	try {
		//이름을 쳤을때 나오는 동명이인 검색
		String name = "select pname,userid,gender,mobile,pw,created,updated from member where pname='"+a+"'";
		Statement st = this.dbconn.createStatement();
		ResultSet rs = st.executeQuery(name);
		while(rs.next()) {
			//출력
			System.out.println("이름: "+rs.getString("pname")+", 유저 아이디: "+rs.getString("userid")+", 성별: "+rs.getString("gender")+
					", 모바일 번호: "+rs.getString("mobile")+", 비밀 번호: "+rs.getString("pw")+
					", 만든 날짜"+rs.getString("created")+", 수정된 날짜: "+rs.getString("updated"));
		}
		st.close();
	}catch(SQLException e) {
		System.out.println((e.getMessage()));
		}
	// 유저아이디로 검색할떄 실행 메소드
	if(a.equals("유저아이디")||a.equals("유저 아이디")) {
			System.out.println("찾고 싶은 이름을 입력하시오.");
			a = s1.nextLine();
	this.setConnection();
	try {
		//유저 아이디을 쳤을때 나오는 동명이인 검색
		String name = "select pname,userid,gender,mobile,pw,created,updated from member where pname='"+a+"'";
		Statement st = this.dbconn.createStatement();
		ResultSet rs = st.executeQuery(name);
		while(rs.next()) {
			//출력
			System.out.println("이름: "+rs.getString("pname")+", 유저 아이디: "+rs.getString("userid")+", 성별: "+rs.getString("gender")+
					", 모바일 번호: "+rs.getString("mobile")+", 비밀 번호: "+rs.getString("pw")+
					", 만든 날짜"+rs.getString("created")+", 수정된 날짜: "+rs.getString("updated"));
		}
		st.close();
	}catch(SQLException e) {
		System.out.println((e.getMessage()));
	}
	
		}
	if(a.equals("모바일번호")||a.equals("모바일 번호")||a.equals("모바일")||a.equals("번호")) {
			System.out.println("찾고 싶은 이름을 입력하시오.");
			a = s1.nextLine();//찾고 싶은 이름 검색
	this.setConnection();
	try {
		//이름을 쳤을때 나오는 동명이인 목록 검색
		String name = "select pname,userid,gender,mobile,pw,created,updated from member where pname='"+a+"'";
		Statement st = this.dbconn.createStatement();
		ResultSet rs = st.executeQuery(name);
		while(rs.next()) {
			//출력
			System.out.println("이름: "+rs.getString("pname")+", 유저 아이디: "+rs.getString("userid")+", 성별: "+rs.getString("gender")+
					", 모바일 번호: "+rs.getString("mobile")+", 비밀 번호: "+rs.getString("pw")+
					", 만든 날짜"+rs.getString("created")+", 수정된 날짜: "+rs.getString("updated"));
		}
		st.close();
	}catch(SQLException e) {
		System.out.println((e.getMessage()));
	}
	}}}
}
//멤버 수정
public void updateMember() {
	Scanner s1 = new Scanner(System.in);
	while(true) {
		System.out.println("수정하고 싶은  멤버를 찾는 방법를 고르시오.  ex)이름,유저 아이디,모바일번호  ['  ':뒤로가기]");
		String a =s1.nextLine();//이름,유저,아이디,모바일번호 입력
		if(a.equals(""))break;
		//찾는 방법이 이름일때 실행메소드
		if(a.equals("이름")) {
			System.out.println("찾고 싶은 멤버의 이름의 입력하시오.");
			String b = s1.nextLine();//찾고 싶은 멤버 이름 검색
			this.setConnection();
			try {
				//이름을 쳤을때 나오는 동명이인 검색
				String name = "select pname,userid,gender,mobile,pw,created,updated from member where pname='"+b+"'";
				Statement st = this.dbconn.createStatement();
				ResultSet rs = st.executeQuery(name);
				while(rs.next()) {
					//출력
					System.out.println("이름: "+rs.getString("pname")+", 유저 아이디: "+rs.getString("userid")+", 성별: "+rs.getString("gender")+
							", 모바일 번호: "+rs.getString("mobile")+", 비밀 번호: "+rs.getString("pw")+
							", 만든 날짜"+rs.getString("created")+", 수정된 날짜: "+rs.getString("updated"));
				}
				st.close();
			}catch(SQLException e) {
				System.out.println((e.getMessage()));
			}
				//멤버 수정 메소드
			System.out.println("수정하고 싶은 멤버의 유저 아이디를 입력하시오.");
			String id = s1.nextLine();
			if(id.equals(""))break;
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
				//데이터베이스에 수정된 멤버 추가
				String sql ="update member set pname='"+updateName+"',gender='"+updateGender+"',mobile='"+updateMobile+
						"', pw='"+updatePw+"',updated=current_timestamp where userid= "+id;
				Statement st = this.dbconn.createStatement();
				st.executeUpdate(sql);
				st.close();
			}catch(SQLException e) {
				System.out.println((e.getMessage()));
			}
			this.disConnection();
		}
	//찾는 방법이 유저아이디 일 때 실행메소드
		if(a.equals("유저아이디")||a.equals("유저 아이디")||a.equals("아이디")) {
			System.out.println("찾고 싶은 멤버의 이름의 입력하시오.");
			String b = s1.nextLine();//찾고 싶은 멤버 이름 검색
			String userid = "select pname,userid,gender,mobile,pw,created,updated from member where userid='"+b+"'";
			this.setConnection();
			try {
				Statement st = this.dbconn.createStatement();
				ResultSet rs = st.executeQuery(userid);
				while(rs.next()) {
					//출력
					System.out.println("이름: "+rs.getString("pname")+", 유저 아이디: "+rs.getString("userid")+", 성별: "+rs.getString("gender")+
							", 모바일 번호: "+rs.getString("mobile")+", 비밀 번호: "+rs.getString("pw")+
							", 만든 날짜"+rs.getString("created")+", 수정된 날짜: "+rs.getString("updated"));
				}
				st.close();
			}catch(SQLException e) { 
				System.out.println((e.getMessage()));
			} 
				//멤버 수정 메소드
			System.out.println("수정하고 싶은 멤버의 유저 아이디를 입력하시오.");
			String id = s1.nextLine();
			if(id.equals(""))break;
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
			try{
				//데이터베이스 수정된 멤버 추가 
				String sql ="update member set pname='"+updateName+"',gender='"+updateGender+"',mobile='"+updateMobile+
						"', pw='"+updatePw+"',updated=current_timestamp where userid= "+id;
				Statement st = this.dbconn.createStatement();
				st.executeUpdate(sql);
				st.close();
			}catch(SQLException e) {
				System.out.println((e.getMessage()));
			}
			this.disConnection();
		}
	//찾는 방법이 모바일번호일때 실행메소드
		if(a.equals("모바일번호")||a.equals("모바일 번호")) {
			System.out.println("찾고 싶은 멤버의 모바일 번호 입력하시오.");
			String b = s1.nextLine();
			this.setConnection();
			try {
				//데이터 베이스에 멤버 검색
				String name = "select pname,userid,gender,mobile,pw,created,updated from member where mobile='"+b+"'";
				Statement st = this.dbconn.createStatement();
				ResultSet rs = st.executeQuery(name);
				while(rs.next()) {
					//출력
					System.out.println("이름: "+rs.getString("pname")+", 유저 아이디: "+rs.getString("userid")+", 성별: "+rs.getString("gender")+
							", 모바일 번호: "+rs.getString("mobile")+", 비밀 번호: "+rs.getString("pw")+
							", 만든 날짜"+rs.getString("created")+", 수정된 날짜: "+rs.getString("updated"));
				}
				st.close();
			}catch(SQLException e) {
				System.out.println((e.getMessage()));
			}
				//멤버 수정 메소드
			System.out.println("수정하고 싶은 멤버의 유저 아이디를 입력하시오.");
			String id = s1.nextLine();
			if(id.equals(""))break;
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
				//데이터 베이스에 멤버추가
				String sql ="update member set pname='"+updateName+"',gender='"+updateGender+"',mobile='"+updateMobile+
						"', pw='"+updatePw+"',updated=current_timestamp where userid= "+id;
				Statement st = this.dbconn.createStatement();
				st.executeUpdate(sql);
				st.close();
			}catch(SQLException e) {
				System.out.println((e.getMessage()));
			}
			this.disConnection();
}}}
//멤버 삭제 메소드
public void deleteMember() {
	Scanner s1 = new Scanner(System.in); 
	while(true) {
	//멤버 찾는 메소드
		System.out.println("삭제하고 싶은 멤버찾기   ex)이름,유저아이디,모바일번호 ['  ':뒤로가기]");
		String a= s1.nextLine();
		if(a.equals(""))break;
		if(a.equals("이름")) {
			System.out.println("찾고있는 멤버 이름입력");
			a=s1.nextLine();
			this.setConnection();
			try {
				//이름에 해당하는 멤버 검색
				String name = "select pname,userid,gender,mobile,pw,created,updated from member where pname='"+a+"'";
				Statement st = this.dbconn.createStatement();
				ResultSet rs = st.executeQuery(name);
				while(rs.next()) {
					//출력
					System.out.println("이름: "+rs.getString("pname")+", 유저 아이디: "+rs.getString("userid")+", 성별: "+rs.getString("gender")+
							", 모바일 번호: "+rs.getString("mobile")+", 비밀 번호: "+rs.getString("pw")+
							", 만든 날짜"+rs.getString("created")+", 수정된 날짜: "+rs.getString("updated"));
				}
				st.close();
			}catch(SQLException e) {
				System.out.println((e.getMessage()));
			}
			//삭제하고 싶은 멤버의 아이디입력
			System.out.println("삭제하고 싶은 멤버의 유저 아이디 입력");
			a=s1.nextLine();
			this.setConnection();
			try {
				//삭제
				String sql ="delete from member where userid='"+a+"'";
				Statement st = this.dbconn.createStatement();
				st.executeUpdate(sql);
				st.close();
			}catch(SQLException e) {
				System.out.println((e.getMessage()));
			}
			this.disConnection();
		}
	//찾는 방법이 유저아이디일때 실행 메소드
		if(a.equals("유저아이디")||a.equals("유저 아이디")) {
			System.out.println("찾고있는 멤버의 유저아이디 입력");
			a = s1.nextLine(); //멤버의 유저아이디입력
			this.setConnection();
			try {
				//입력한 유저아이디에 해당하는 멤버 검색
				String name = "select pname,userid,gender,mobile,pw,created,updated from member where userid='"+a+"'";
				Statement st = this.dbconn.createStatement();
				ResultSet rs = st.executeQuery(name);
				while(rs.next()) {
					//출력
					System.out.println("이름: "+rs.getString("pname")+", 유저 아이디: "+rs.getString("userid")+", 성별: "+rs.getString("gender")+
							", 모바일 번호: "+rs.getString("mobile")+", 비밀 번호: "+rs.getString("pw")+
							", 만든 날짜"+rs.getString("created")+", 수정된 날짜: "+rs.getString("updated"));
				}
				st.close();
			}catch(SQLException e) {
				System.out.println((e.getMessage()));
			}
			System.out.println("삭제하고 싶은 멤버의 유저 아이디 입력");
			a=s1.nextLine(); //삭제하고싶은 멤버의 유저아이디 입력
			//삭제 메소드
			this.setConnection();
			try {
				//삭제
				String sql ="delete from member where userid='"+a+"'";
				Statement st = this.dbconn.createStatement();
				st.executeUpdate(sql);
				st.close();
			}catch(SQLException e) {
				System.out.println((e.getMessage()));
			}
			this.disConnection();
		}
	//찾는 방법이 모바일 번호일떄 실행 메소드
		if(a.equals("모바일번호")||a.equals("모바일 번호")) {
			System.out.println("찾고 있는 멤버의 모바일 번호 입력");
			a = s1.nextLine(); //찾고 있는 멤버의 모바일 번호 입력
			this.setConnection();
			try {
				//입력한 모바일 번호에 해당하는 멤버 검색
				String name = "select pname,userid,gender,mobile,pw,created,updated from member where mobile='"+a+"'";
				Statement st = this.dbconn.createStatement();
				ResultSet rs = st.executeQuery(name);
				while(rs.next()) {
					//삭제
					System.out.println("이름: "+rs.getString("pname")+", 유저 아이디: "+rs.getString("userid")+", 성별: "+rs.getString("gender")+
							", 모바일 번호: "+rs.getString("mobile")+", 비밀 번호: "+rs.getString("pw")+
							", 만든 날짜"+rs.getString("created")+", 수정된 날짜: "+rs.getString("updated"));
				}
				st.close();
			}catch(SQLException e) {
				System.out.println((e.getMessage()));
			}
			System.out.println("삭제하고 싶은 멤버의 유저 아이디 입력");
			a=s1.nextLine();//삭제하고 싶은 멤버의 유저 아이디 입력
			
			//입력한 유저아이디에 해당하는 멤버 삭제
			this.setConnection();
			try {
				//삭제
				String sql ="delete from member where userid='"+a+"'";
				Statement st = this.dbconn.createStatement();
				st.executeUpdate(sql);
				st.close();
			}catch(SQLException e) {
				System.out.println((e.getMessage()));
			}
			this.disConnection();
		}
		}	//while문 종료
}
}













