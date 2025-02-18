package book_Management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Member {
	
	private Test t1;
	public Connection dbconn;
	public String name;
	public String userid;
	public String gender;
	public String mobile;
	public String pw;
	Book bo;
	
	Scanner sc;
	Member(Book _bo){
		t1 = new Test();
		sc = new Scanner(System.in);
		bo = _bo;
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
			bo.dbconn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//실행메소드
	public void control() {
		
		while(true) {
			System.out.println("작업관리[c:멤버추가,r:멤버출력,u:멤버수정,d:멤버삭제,x:뒤로가기]");
			String ins = sc.nextLine();
			
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
		
		while(true) {
			
			System.out.println("이름을 입력하세요.   ['   ':뒤로가기]");
			name = sc.nextLine();
			if(name.equals("")) break;
			System.out.println("유저 아이디를 입력하세요.   ['  ':뒤로가기]");
			userid = sc.nextLine();
			if(userid.equals(""))break;
			//userid에 입력한값이 멤버에 일치하는게 있는지 검색
			String ts = "select userid from Member where userid like '" + userid + "%' "
											+ "or pname like '%" + userid + "' "
											+ "or pname like '%" + userid + "%'";
			bo.setConnection();
			try {
				Statement test = bo.dbconn.createStatement();
				ResultSet rs = test.executeQuery(ts);
				//userid에 입력한값이 일치한게 있으면 아래 문장 실행하고 빠져나가기
				if(rs.next()) {		
					System.out.println("똑같은 아이디가 존재합니다. 처음부터 다시 하십시오.\n");
					break;
				}
				rs.close();
				bo.disConnection();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			
			System.out.println("성별을 입력하세요.   ['   ':뒤로가기]");
			gender = sc.nextLine();
			if(gender.equals(""))break;
			System.out.println("모바일번호를 입력하세요. ex)010-1234-5678   ['   ':뒤로가기]");
			mobile = sc.nextLine();
			if(mobile.equals(""))break;
			//mobile에 입력한 값이 테이블 Member에 일치한 값이 있는지 검색
			String t = "select mobile from Member where mobile like '" + mobile + "%' "
												   + "or pname like '%" + mobile + "' "
												   + "or pname like '%" + mobile + "%'";
			bo.setConnection();
			try {
				Statement Mobile = bo.dbconn.createStatement();
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
			
			System.out.println("비밀번호를 입력하세요. ['   ':뒤로가기]");
			pw = sc.nextLine();
			if(pw.equals(""))break;
			//입력한 값을 데이터 베이스에 저장
			String sql = "insert into member set pname='"+name+"',userid='"+userid+
					"',gender='"+gender+"',mobile='"+mobile+"',pw='"+pw+"',created=current_timestamp,updated=current_timestamp";
			bo.setConnection();
			try {
				Statement st = bo.dbconn.createStatement();
				st.executeUpdate(sql);
				st.close();st.close();
			}	catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			bo.disConnection();
		}//while문 종료
		
	}
	
	//멤버 출력 메소드
	public void printMember() {
		
		while(true) {

			System.out.println("멤버를 찾는 방법를 고르시오.  ex)이름,유저 아이디,모바일번호  ['  ':뒤로가기]");
			String a =sc.nextLine();
			if(a.equals(""))break;
			//이름으로 검색할때 실행 메소드
			if(a.equals("이름")) {
				System.out.println("찾고 싶은 이름을 입력하시오.    ['  ':뒤로가기]");
				a = sc.nextLine();
				if(a.equals(""))break;
				bo.setConnection();
				try {
					//이름을 쳤을때 나오는 동명이인 검색
					String name = "select pname,userid,gender,mobile,pw,created,updated from member where pname='"+a+"'";
					Statement st = bo.dbconn.createStatement();
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
			
			// 유저아이디로 검색할떄 실행 메소드
			if(a.equals("유저아이디")||a.equals("유저 아이디")) {
				System.out.println("찾고 싶은 유저아이디를 입력하시오.   ['  ':뒤로가기]");
				a = sc.nextLine();
				if(a.equals(""))break;
				bo.setConnection();
				try {
					//유저 아이디을 쳤을때 나오는 사람 검색
					String name = "select pname,userid,gender,mobile,pw,created,updated from member where userid='"+a+"'";
					Statement st = bo.dbconn.createStatement();
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
				System.out.println("찾고 싶은 유저의 모바일번호를 입력하시오.   ['  ':뒤로가기]");
				a = sc.nextLine();//찾고 싶은 모바일번호 검색
				if(a.equals(""))break;
				bo.setConnection();
				try {
					//모바일을 쳤을때 나오는 동명이인 목록 검색
					String name = "select pname,userid,gender,mobile,pw,created,updated from member where mobile='"+a+"'";
					Statement st = bo.dbconn.createStatement();
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
		}
	}
	
	//멤버 수정
	public void updateMember() {
		
		while(true) {
			System.out.println("수정하고 싶은  멤버를 찾는 방법를 고르시오.  ex)이름,유저 아이디,모바일번호  ['  ':뒤로가기]");
			String str = sc.nextLine(); //이름,유저,아이디,모바일번호 입력
			String a = "";
			if(str.equals(""))break;
			
			//찾는 방법이 이름일때 실행메소드
			if(str.equals("이름")) {
				System.out.println("찾고 싶은 멤버의 이름의 입력하시오.   ['  ':뒤로가기]");
				String b = sc.nextLine();//찾고 싶은 멤버 이름 검색
				if(b.equals(""))break;
				bo.setConnection();
				
				try {
					//이름을 쳤을때 나오는 동명이인 검색
					String name = "select pname,userid,gender,mobile,pw,created,updated from member where pname='"+b+"'";
					Statement st = bo.dbconn.createStatement();
					ResultSet rs = st.executeQuery(name);
					while(rs.next()) {
						//출력
						System.out.println("이름: "+rs.getString("pname")+", 유저 아이디: "+rs.getString("userid")+", 성별: "+rs.getString("gender")+
								", 모바일 번호: "+rs.getString("mobile")+", 비밀 번호: "+rs.getString("pw")+
								", 만든 날짜"+rs.getString("created")+", 수정된 날짜: "+rs.getString("updated"));
					}
					
					rs.close();
					st.close();
					bo.disConnection();
					
					}catch(SQLException e) {
						System.out.println((e.getMessage()));
					}
				
					//멤버 수정 메소드
					upAdd();
					
				}
				//찾는 방법이 유저아이디 일 때 실행메소드
				if(str.equals("유저아이디")||str.equals("유저 아이디")||str.equals("아이디")) {
					System.out.println("찾고 싶은 멤버의 이름의 입력하시오.   ['  ':뒤로가기]");
					a = sc.nextLine();//찾고 싶은 멤버 이름 검색
					if(a.equals(""))break;
					String userid = "select pname,userid,gender,mobile,pw,created,updated from member where userid='"+a+"'";
					bo.setConnection();
					try {
						Statement st = bo.dbconn.createStatement();
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
					upAdd();			
				
				}
				
				//찾는 방법이 모바일번호일때 실행메소드
				if(str.equals("모바일번호")||str.equals("모바일 번호")) {
					System.out.println("찾고 싶은 멤버의 모바일 번호 입력하시오.   ['  ':뒤로가기]");
					a = sc.nextLine();
					if(a.equals(""))break;
					bo.setConnection();
					try {
						//데이터 베이스에 멤버 검색
						String name = "select pname,userid,gender,mobile,pw,created,updated from member where mobile='"+a+"'";
						Statement st = bo.dbconn.createStatement();
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
					upAdd();
				}
		}
	}
	//멤버 삭제 메소드
	public void deleteMember() {
		while(true) {
		//멤버 찾는 메소드
			System.out.println("삭제하고 싶은 멤버찾기   ex)이름,유저아이디,모바일번호 ['  ':뒤로가기]");
			String a= sc.nextLine();
			if(a.equals(""))break;
			if(a.equals("이름")) {
				System.out.println("찾고있는 멤버 이름입력   ['  ':뒤로가기]");
				a=sc.nextLine();
				if(a.equals(""));
				bo.setConnection();
				try {
					//이름에 해당하는 멤버 검색
					String name = "select pname,userid,gender,mobile,pw,created,updated from member where pname='"+a+"'";
					Statement st = bo.dbconn.createStatement();
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
					System.out.println("삭제하고 싶은 멤버의 유저 아이디 입력   ['  ':뒤로가기]");
					a=sc.nextLine();
					if(a.equals(""))break;
					bo.setConnection();
					try {
						//삭제
						String sql ="delete from member where userid='"+a+"'";
						Statement st = bo.dbconn.createStatement();
						st.executeUpdate(sql);
						st.close();
						}catch(SQLException e) {
							System.out.println((e.getMessage()));
						}
					bo.disConnection();
			}
			
			//찾는 방법이 유저아이디일때 실행 메소드
			if(a.equals("유저아이디")||a.equals("유저 아이디")) {
					System.out.println("찾고있는 멤버의 유저아이디 입력   ['  ':뒤로가기]");
					a = sc.nextLine(); //멤버의 유저아이디입력
					if(a.equals(""))break;
					bo.setConnection();
					try {
						//입력한 유저아이디에 해당하는 멤버 검색
						String name = "select pname,userid,gender,mobile,pw,created,updated from member where userid='"+a+"'";
						Statement st = bo.dbconn.createStatement();
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
					System.out.println("삭제하고 싶은 멤버의 유저 아이디 입력   ['  ':뒤로가기]");
					a=sc.nextLine(); //삭제하고싶은 멤버의 유저아이디 입력
					if(a.equals(""))break;
					//삭제 메소드
					bo.setConnection();
					try {
						//삭제
						String sql ="delete from member where userid='"+a+"'";
						Statement st = bo.dbconn.createStatement();
						st.executeUpdate(sql);
						st.close();
					}catch(SQLException e) {
						System.out.println((e.getMessage()));
					}
					bo.disConnection();
					}
			//찾는 방법이 모바일 번호일떄 실행 메소드
			if(a.equals("모바일번호")||a.equals("모바일 번호")) {
					System.out.println("찾고 있는 멤버의 모바일 번호 입력   ['  ':뒤로가기]");
					a = sc.nextLine(); //찾고 있는 멤버의 모바일 번호 입력
					if(a.equals(""))break;
					bo.setConnection();
					try {
						//입력한 모바일 번호에 해당하는 멤버 검색
						String name = "select pname,userid,gender,mobile,pw,created,updated from member where mobile='"+a+"'";
						Statement st = bo.dbconn.createStatement();
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
					System.out.println("삭제하고 싶은 멤버의 유저 아이디 입력   ['  ':뒤로가기]");
					a=sc.nextLine();//삭제하고 싶은 멤버의 유저 아이디 입력
					if(a.equals(""))break;
					
					//입력한 유저아이디에 해당하는 멤버 삭제
					bo.setConnection();
					try {
						//삭제
						String sql ="delete from member where userid='"+a+"'";
						Statement st = bo.dbconn.createStatement();
						st.executeUpdate(sql);
						st.close();
					}catch(SQLException e) {
						System.out.println((e.getMessage()));
					}
					bo.disConnection();
			}
		}		//while문 종료
	}
	//수정 메소드 
	public void upAdd() {
		while(true) {
			System.out.println("수정하고 싶은 멤버의 유저 아이디를 입력하시오.   ['  ':뒤로가기]");
			userid = sc.nextLine();
			if(userid.equals(""))break;
			System.out.println("이름을 입력하시오.   [Y:그대로 사용하기, '   ':뒤로가기]");
			String a = sc.nextLine();
			if(a.equals(""))break;
			if(a.equals("Y")||a.equals("y")) {
				bo.setConnection();
				try {
					String sql= "select pname from member where userid='"+userid+"'";
					Statement st = bo.dbconn.createStatement();
					st.executeQuery(sql);
					ResultSet rs =st.executeQuery(sql);
					rs.next();
					name = rs.getString("pname");
					rs.close();
					st.close();
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
			}else {
				name=a;
			}
			System.out.println("성별을 입력하시오.   [Y:그대로 사용하기, '   ':뒤로가기]");
			a = sc.nextLine();
			if(a.equals("")) {break;}
			if(a.equals("Y")||a.equals("y")) {
				bo.setConnection();
				try {
					String sql ="select gender from member where userid='"+userid+"'";
					Statement st = bo.dbconn.createStatement();
					st.executeQuery(sql);
					ResultSet rs =st.executeQuery(sql);
					rs.next();
					gender = rs.getString("gender");
					rs.close();
					st.close();
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
			}else {
				gender=a;
			}
			System.out.println("모바일 번호를 입력하시오.  ex)010-1234-5678 [Y:그대로 사용하기,'   ':뒤로가기]");
			a= sc.nextLine();
			if(a.equals(""))break;
			
			if(a.equals("Y")||a.equals("y")) {
				bo.setConnection();
				try {
					String sql ="select mobile from member where userid ='"+userid+"'";
					Statement st = bo.dbconn.createStatement();
					st.executeQuery(sql);
					ResultSet rs =st.executeQuery(sql);
					rs.next();
					mobile = rs.getString("mobile");
					rs.close();
					st.close();
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
				
			}else{
				mobile = a;
				String t = "select mobile from Member where mobile like '" + mobile + "%' "
											  	 		+ "or pname like '%" + mobile + "' "
											  	   		+ "or pname like '%" + mobile + "%'";
				
			bo.setConnection();
			try {
				Statement Mobile = bo.dbconn.createStatement();
				ResultSet Mo = Mobile.executeQuery(t);
				
				if(Mo.next()) {
					System.out.println("똑같은 모바일 번호가 존재합니다. 처음부터 다시 하십시오.\n");
					break;
				}
				
				
				Mo.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			}
			System.out.println("비밀번호를 입력하시오.   [Y:그대로 사용하기, '   ':뒤로가기]");
			a = sc.nextLine();
			if(a.equals(""))break;
			if(a.equals("Y")||a.equals("y")) {
				bo.setConnection();
				try {
					String sql ="select pw from member where userid='"+userid+"'";
					Statement st = bo.dbconn.createStatement();
					st.executeQuery(sql);
					ResultSet rs =st.executeQuery(sql);
					rs.next();
					pw = rs.getString("pw");
					rs.close();
					st.close();
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
			}else {
				pw=a;
			}
			
			bo.setConnection();
			try {
				//데이터베이스에 수정된 멤버 추가
				String sql ="update member set pname='"+name+"',gender='"+gender+"',mobile='"+mobile+
						"', pw='"+pw+"',updated=current_timestamp where userid= '"+userid+"'";
				Statement st = bo.dbconn.createStatement();
				st.executeUpdate(sql);
				st.close();
			}catch(SQLException e) {
				System.out.println((e.getMessage()));
			}
			bo.disConnection();
		}
	}
}
