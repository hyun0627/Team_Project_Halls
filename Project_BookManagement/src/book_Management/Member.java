package book_Management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Member {
	private Test t1 = new Test(); 
	public Book bo;
	public Connection dbconn;
	Member(){
		this.dbconn = null;
		bo = new Book();
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
public void control() throws SQLException {
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
public void addMemeber() throws SQLException {
	Scanner s1 = new Scanner(System.in);
	
	int id = 0; // 현재 몇명의 데이터가 들어왔는지 확인하는 방법
	
	while(true) {
	System.out.println("이름을 입력하세요   ['   ':뒤로가기]");
	String name = s1.nextLine();
	if(name.equals("")) break;
	System.out.println("유저 아이디를 입력하세요   ['  ':뒤로가기]");
	String userid = s1.nextLine();
	
	// 여기서 부터 보세요!
	if(id != 0) {
		bo.setConnection();
		String ts = "select pname from Member where pname like '" + name + "%' "
										+ "or pname like '%" + name + "' "
										+ "or pname like '%" + name + "%'";
		
		Statement test = this.bo.dbconn.createStatement();
		ResultSet rs = test.executeQuery(ts);
		
		if(rs.getString("pname") != null) {		
			System.out.println("똑같은 아이디가 존재합니다. 처음부터 다시 하십시오");
			break;
		} 
		rs.close();
		bo.disConnection();
	}	
	
	if(userid.equals("select userid from member"))break;
	System.out.println("성별을 입력하세요   ['   ':뒤로가기]");
	String gender = s1.nextLine();
	if(gender.equals(""))break;
	System.out.println("모바일번호를 입력하세요 ex)010-1234-5678   ['   ':뒤로가기]");
	String mobile = s1.nextLine();
	if(t1.strTest(mobile))break;
	System.out.println("비밀번호를 입력하세요 ['   ':뒤로가기]");
	String pw = s1.nextLine();
	if(pw.equals(""))break;
	
	String sql = "insert into Member set pname='"+name+"',userid='"+userid+
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
	id++;
	} //while문 종료
	
	}
//멤버 출력 메소드
public void printMember() {
	Scanner s1 = new Scanner(System.in);
	
	while(true) {
	this.setConnection();
	try {
	
	System.out.println("찾고 싶은 유저의 유저 아이디를 입력하세요   ['   ':뒤로가기]");
	String a = s1.nextLine();
	if(a.equals(""))break;
	Statement st = dbconn.createStatement();
	String sql = "select id,pname,userid,gender,mobile,pw,created,updated from member where userid='"+a+"'";
	ResultSet rs = st.executeQuery(sql);
	
	while (rs.next()) {
		System.out.println(rs.getString("pname")+", "+rs.getString("userid")+", "+rs.getString("gender")+
				", "+rs.getString("mobile")+", "+rs.getString("pw")+", "+rs.getString("created")+", "+rs.getString("updated"));//rs.getInt("id")+", "+
	}//while문 종료
	rs.close(); st.close();
	}catch(SQLException e) {
		System.out.println(e.getMessage()); 
	}
	this.disConnection();
}	//while문 종료
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
			String sql ="update member set pname='"+updateName+"',gender='"+updateGender+"',mobile='"+updateMobile+
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
	}	//while문 종료
  }
}
