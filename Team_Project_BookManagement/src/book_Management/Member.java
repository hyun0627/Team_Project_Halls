package book_Management;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Member {
	
	private Test t1;
	private Scanner sc;
	public Connection dbconn;
	
	Book bo;
	
	private int id;
	private String name;
	private String userid;
	private String gender;
	private String mobile;
	private String pw;
	
	Member(Book _bo){
		
		t1 = new Test();
		sc = new Scanner(System.in);
		bo = _bo;
		
	}
	
	//실행메소드
	public void run() throws SQLException {
		
		System.out.println("===============================================================");
		System.out.println("사람 정보 관리 프로그램을 실행합니다.");
		
		while(true) {
			System.out.print("[ C: 사람 정보 추가 || R: 사람 정보 출력 || U: 사람 정보 수정 || D: 사람 정보 삭제|| X or 공백: 종료 ]");
			String ins = sc.nextLine();
			
			if(ins.equals("x") || ins.equals("X") || t1.strTest(ins) == false ) {
				System.out.println("===============================================================");
				System.out.println("[ B: 책 정보 관리 || M: 사람 정보 관리 || R: 책 대여 관리 || X: 종료 ]");
				break;
			}
			
			switch(ins) {
				case "c","C":
					System.out.println("사람 정보 추가 프로세스를 실행합니다.");
					addMemeber();
					break;
				case "r","R":
					System.out.println("사람 정보 출력 프로세스를 실행합니다.");
					printMember();
					break;
				case "u","U":
					System.out.println("사람 정보 수정 프로세스를 실행합니다.");
					updateMember();
					break;
				case "d","D":
					System.out.println("사람 정보 삭제 프로세스를 실행합니다.");
					deleteMember();
					break;
			}
		}
		
	}
	//멤버추가 메소드
	public void addMemeber() throws SQLException {
		
		while(true) {
			
			String ssi = "select Max(id) from Member";
			String ssc = "select Count(*)  from Member";
			int di = 0;
			
			bo.setConnection();
			Statement si = bo.dbconn.createStatement();
			Statement si2 = bo.dbconn.createStatement();
			ResultSet rs2 = si2.executeQuery(ssc);
			ResultSet rsi = si.executeQuery(ssi);
			rs2.next();
			di = rs2.getInt("Count(*)");
			
			if(di == 0) {
				id = 0;
			} else {
				rsi.next();
				id = rsi.getInt("Max(id)");
				id++;
			}
			rsi.close();
			rs2.close();
			si2.close();
			si.close();		
			bo.disConnection();
			
			System.out.print("이름을 입력하세요 : ");
			name = sc.nextLine();
			if(t1.strTest(name) == false) { break;}
			
			System.out.print("유저 아이디를 입력하세요 : ");
			userid = sc.nextLine();
			if(t1.strTest(userid) == false) { break;}
			
			//userid에 입력한값이 멤버에 일치하는게 있는지 검색
			String ts = "select userid from Member where userid like '" + userid + "%' "
											+ "or pname like '%" + userid + "' "
											+ "or pname like '%" + userid + "%'";
			
			bo.setConnection();
			try {
				Statement test = bo.dbconn.createStatement();
				ResultSet rs1 = test.executeQuery(ts);
				//userid에 입력한값이 일치한게 있으면 아래 문장 실행하고 빠져나가기
				if(rs1.next()) {		
					System.out.println("똑같은 아이디가 존재합니다. 처음부터 다시 하십시오.\n");
					break;
				}
				rs1.close();
				test.close();
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			bo.disConnection();
			
			System.out.print("성별을 입력하세요 : ");
			gender = sc.nextLine();
			if(t1.strTest(gender) == false) { break;}
			
			System.out.print("모바일번호를 입력하세요 : ");
			mobile = sc.nextLine();
			if(t1.strTest(mobile) == false) { break;}
			
			//mobile에 입력한 값이 테이블 Member에 일치한 값이 있는지 검색
			String t = "select mobile from Member where mobile like '" + mobile + "%' "
												   + "or pname like '%" + mobile + "' "
												   + "or pname like '%" + mobile + "%'";
			
			bo.setConnection();
			try {
				Statement M1 = bo.dbconn.createStatement();
				ResultSet M2 = M1.executeQuery(t);
				//mobile에 입력한 값에 일치한게 있으면 아래 문장 실행하고 빠져나가기
				if(M2.next()) {
					System.out.println("똑같은 모바일 번호가 존재합니다. 처음부터 다시 하십시오.\n");
					break;
				}
				M2.close();
				M1.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			bo.disConnection();
			
			System.out.print("비밀번호를 입력하세요 : ");
			pw = sc.nextLine();
			if(t1.strTest(pw) == false) { break;}
			
			//입력한 값을 데이터 베이스에 저장
			String sql2 = "insert into member set id = " + id + ", pname = '"+name+"' ,userid = '"+ userid +
					"',gender= '"+gender+ "',mobile= '"+mobile+"',pw='"+pw+"',created=current_timestamp,updated=current_timestamp";
			
			bo.setConnection();
			try {
				Statement st2 = bo.dbconn.createStatement();
				st2.executeUpdate(sql2);
				bo.dbconn.commit();
				st2.close();
			}	catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			bo.disConnection();
		}//while문 종료
		
	}
	
	//멤버 출력 메소드
	public void printMember() {
		
		while(true) {

			System.out.print("멤버를 찾는 방법를 고르시오 [이름/유저아이디/모바일번호] : ");
			String a =sc.nextLine();
			if(t1.strTest(a) == false) { break;}
			
			//이름으로 검색할때 실행 메소드
			if(a.equals("이름")) {
				System.out.print("찾고 싶은 이름을 입력하시오 : ");
				name = sc.nextLine();
				if(t1.strTest(name) == false) { break;}
				
				bo.setConnection();
				try {
					//이름을 쳤을때 나오는 동명이인 검색
					String sn = "select id, pname,userid,gender,mobile,pw,created,updated from member where pname='"+name+"'";
					Statement st3 = bo.dbconn.createStatement();
					ResultSet rs3 = st3.executeQuery(sn);
					while(rs3.next()) {
						//출력
						System.out.println(rs3.getInt("id") + "."+ " 이름: "+rs3.getString("pname")+", 유저 아이디: "+rs3.getString("userid")+", 성별: "+rs3.getString("gender")+
								", 모바일 번호: "+rs3.getString("mobile")+", 비밀 번호: "+rs3.getString("pw")+
								", 만든 날짜"+rs3.getString("created")+", 수정된 날짜: "+rs3.getString("updated"));
					}
					bo.dbconn.commit();
					rs3.close();
					st3.close();
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
				bo.disConnection();
				
			}
			
			// 유저아이디로 검색할떄 실행 메소드
			if(a.equals("유저아이디")||a.equals("유저 아이디")) {
				System.out.print("찾고 싶은 유저아이디를 입력하시오 : ");
				userid = sc.nextLine();
				if(t1.strTest(userid) == false) { break;}
				
				bo.setConnection();
				try {
					//유저 아이디을 쳤을때 나오는 사람 검색
					String su = "select id, pname,userid,gender,mobile,pw,created,updated from member where userid='"+userid+"'";
					Statement st4 = bo.dbconn.createStatement();
					ResultSet rs4 = st4.executeQuery(su);
					while(rs4.next()) {
						//출력
						System.out.println(rs4.getInt("id") + "." + " 이름: "+rs4.getString("pname")+", 유저 아이디: "+rs4.getString("userid")+", 성별: "+rs4.getString("gender")+
								", 모바일 번호: "+rs4.getString("mobile")+", 비밀 번호: "+rs4.getString("pw")+
								", 만든 날짜"+rs4.getString("created")+", 수정된 날짜: "+rs4.getString("updated"));
					}
					rs4.close();
					st4.close();
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
				bo.disConnection();
	
			}
			
			if(a.equals("모바일번호")||a.equals("모바일 번호")||a.equals("모바일")||a.equals("번호")) {
				System.out.print("찾고 싶은 유저의 모바일번호를 입력하시오 : ");
				mobile = sc.nextLine();//찾고 싶은 모바일번호 검색
				if(t1.strTest(mobile) == false) { break;}
				
				bo.setConnection();
				try {
					//모바일을 쳤을때 나오는 동명이인 목록 검색
					String sm = "select id,pname,userid,gender,mobile,pw,created,updated from member where mobile='"+mobile+"'";
					Statement st5 = bo.dbconn.createStatement();
					ResultSet rs5 = st5.executeQuery(sm);
					while(rs5.next()) {
						//출력
						System.out.println(rs5.getInt("id") + "." + "이름: "+rs5.getString("pname")+", 유저 아이디: "+rs5.getString("userid")+", 성별: "
								+rs5.getString("gender") + ", 모바일 번호: "+rs5.getString("mobile")+", 비밀 번호: "+rs5.getString("pw")+
								", 만든 날짜"+rs5.getString("created")+", 수정된 날짜: "+rs5.getString("updated"));
					}
					rs5.close();
					st5.close();
					
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
				bo.disConnection();
			}
		}
		
	}
	
	//멤버 수정
	public void updateMember() {
		
		while(true) {
			System.out.print("수정하고 싶은  멤버를 찾는 방법를 고르시오 [이름/유저아이디/모바일번호] : ");
			String str = sc.nextLine(); //이름,유저,아이디,모바일번호 입력
			if(t1.strTest(str) == false ) {break;}
			
			//찾는 방법이 이름일때 실행메소드
			if(str.equals("이름")) {
				System.out.print("찾고 싶은 멤버의 이름의 입력하시오 : ");
				name = sc.nextLine();//찾고 싶은 멤버 이름 검색
				if(t1.strTest(name) == false ) {break;}
				
				bo.setConnection();		
				try {
					//이름을 쳤을때 나오는 동명이인 검색
					String sql3= "select pname,userid,gender,mobile,pw,created,updated from member where pname='"+name+"'";
					Statement st6 = bo.dbconn.createStatement();
					ResultSet rs6 = st6.executeQuery(sql3);
					
					while(rs6.next()) {
						//출력
						System.out.println("이름: "+rs6.getString("pname")+", 유저 아이디: "+rs6.getString("userid")+", 성별: "
								+rs6.getString("gender")+ ", 모바일 번호: "+rs6.getString("mobile")+", 비밀 번호: "+rs6.getString("pw")+
								", 만든 날짜"+rs6.getString("created")+", 수정된 날짜: "+rs6.getString("updated"));
					}
					
					rs6.close();
					st6.close();
				} catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
				
				bo.disConnection();
				
				//멤버 수정 메소드
				upAdd();
					
				}
				//찾는 방법이 유저아이디 일 때 실행메소드
				if(str.equals("유저아이디") || str.equals("유저 아이디") || str.equals("아이디")) {
					System.out.print("아이디를 입력하세요 : ");
					userid = sc.nextLine();//찾고 싶은 멤버 이름 검색
					if(t1.strTest(userid) == false) {break;}
					
					String sq4 = "select pname,userid,gender,mobile,pw,created,updated from member where userid='"+userid+"'";
					
					bo.setConnection();
					try {
						Statement st7 = bo.dbconn.createStatement();
						ResultSet rs7 = st7.executeQuery(sq4);
						while(rs7.next()) {
							//출력
							System.out.println("이름: "+rs7.getString("pname")+", 유저 아이디: "+rs7.getString("userid")+", 성별: "+rs7.getString("gender")+
												", 모바일 번호: "+rs7.getString("mobile")+", 비밀 번호: "+rs7.getString("pw")+
												", 만든 날짜"+rs7.getString("created")+", 수정된 날짜: "+rs7.getString("updated"));
						}
						rs7.close();
						st7.close();
						
					}catch(SQLException e) { 
					System.out.println((e.getMessage()));
					} 
					bo.disConnection();
					//멤버 수정 메소드
					upAdd();			
				
				}
				
				//찾는 방법이 모바일번호일때 실행메소드
				if(str.equals("모바일번호")||str.equals("모바일 번호")) {
					System.out.print("찾고 싶은 멤버의 모바일 번호 입력하시오 : ");
					mobile = sc.nextLine();
					if(t1.strTest(mobile) == false) { break;}
					
					bo.setConnection();
					try {
						//데이터 베이스에 멤버 검색
						String sq5 = "select pname,userid,gender,mobile,pw,created,updated from member where mobile='"+mobile+"'";
						Statement st8 = bo.dbconn.createStatement();
						ResultSet rs8 = st8.executeQuery(sq5);
						while(rs8.next()) {
							//출력
							System.out.println("이름: "+rs8.getString("pname")+", 유저 아이디: "+rs8.getString("userid")+", 성별: "+rs8.getString("gender")+
									", 모바일 번호: "+rs8.getString("mobile")+", 비밀 번호: "+rs8.getString("pw")+
									", 만든 날짜"+rs8.getString("created")+", 수정된 날짜: "+rs8.getString("updated"));
							}
						rs8.close();
						st8.close();
					}catch(SQLException e) {
						System.out.println((e.getMessage()));
					}
					bo.disConnection();
					//멤버 수정 메소드
					upAdd();
				}
			}
		
	}
	
	//멤버 삭제 메소드
	public void deleteMember() {
		
		while(true) {
		//멤버 찾는 메소드
			System.out.print("삭제하고 싶은 멤버찾기[유저/이름/모바일번호] : ");
			String a= sc.nextLine();
			if(t1.strTest(a) == false) {break;}
			
			if(a.equals("이름")) {
				System.out.print("찾고있는 멤버 이름입력 : ");
				name = sc.nextLine();
				if(t1.strTest(name) == false) {break;}
				
				bo.setConnection();
				try {
					//이름에 해당하는 멤버 검색
					String sq6 = "select pname,userid,gender,mobile,pw,created,updated from member where pname='"+name+"'";
					Statement st9 = bo.dbconn.createStatement();
					ResultSet rs9 = st9.executeQuery(sq6);
					while(rs9.next()) {
						//출력
						System.out.println("이름: "+rs9.getString("pname")+", 유저 아이디: "+rs9.getString("userid")+", 성별: "+rs9.getString("gender")+
								", 모바일 번호: "+rs9.getString("mobile")+", 비밀 번호: "+rs9.getString("pw")+
								", 만든 날짜"+rs9.getString("created")+", 수정된 날짜: "+rs9.getString("updated"));
					}
					rs9.close();
					st9.close();
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}			
				bo.disConnection();
				
				bo.setConnection();
				try {
					//삭제
					String sq7 ="delete from member where pname='"+name+"'";
					Statement st10 = bo.dbconn.createStatement();
					st10.executeUpdate(sq7);
					bo.dbconn.commit();
					st10.close();
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
				bo.disConnection();		
			}
			
			//찾는 방법이 유저아이디일때 실행 메소드
			if(a.equals("유저아이디")||a.equals("유저 아이디") || a.equals("유저")) {
				
				System.out.print("찾고있는 멤버의 유저아이디 입력 : ");
				userid = sc.nextLine(); //멤버의 유저아이디입력
				if(t1.strTest(userid) == false) {break;}
				
				bo.setConnection();
				try {
					//입력한 유저아이디에 해당하는 멤버 검색
					String sq8 = "select pname,userid,gender,mobile,pw,created,updated from member where userid='"+userid+"'";
					Statement st11 = bo.dbconn.createStatement();
					ResultSet rs11 = st11.executeQuery(sq8);
					while(rs11.next()) {
						//출력
						System.out.println("이름: "+rs11.getString("pname")+", 유저 아이디: "+rs11.getString("userid")+", 성별: "+rs11.getString("gender")+
								", 모바일 번호: "+rs11.getString("mobile")+", 비밀 번호: "+rs11.getString("pw")+
								", 만든 날짜"+rs11.getString("created")+", 수정된 날짜: "+rs11.getString("updated"));
					}
					rs11.close();
					st11.close();
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
				bo.disConnection();

				bo.setConnection();
				try {
					//삭제
					String sq9 ="delete from member where userid='"+userid+"'";
					Statement st12 = bo.dbconn.createStatement();
					st12.executeUpdate(sq9);
					bo.dbconn.commit();
					st12.close();
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
				bo.disConnection();
				
			}
			//찾는 방법이 모바일 번호일떄 실행 메소드
			if(a.equals("모바일번호")||a.equals("모바일 번호") || a.equals("모바일")) {
				
				System.out.print("찾고 있는 멤버의 모바일 번호 입력 : ");
				mobile = sc.nextLine(); //찾고 있는 멤버의 모바일 번호 입력
				if(t1.strTest(mobile) == false) {break;}
				
				bo.setConnection();
				try {
					//입력한 모바일 번호에 해당하는 멤버 검색
					String sq10 = "select pname,userid,gender,mobile,pw,created,updated from member where mobile='"+mobile+"'";
					Statement st13 = bo.dbconn.createStatement();
					ResultSet rs13 = st13.executeQuery(sq10);
					while(rs13.next()) {
						//삭제
						System.out.println("이름: "+rs13.getString("pname")+", 유저 아이디: "+rs13.getString("userid")+", 성별: "+rs13.getString("gender")+
								", 모바일 번호: "+rs13.getString("mobile")+", 비밀 번호: "+rs13.getString("pw")+
								", 만든 날짜"+rs13.getString("created")+", 수정된 날짜: "+rs13.getString("updated"));
					}
					
					rs13.close();
					st13.close();
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
				bo.disConnection();
				
				bo.setConnection();
				try {
					//삭제
					String sq11 ="delete from member where userid='"+userid+"'";
					Statement st14 = bo.dbconn.createStatement();
					st14.executeUpdate(sq11);
					bo.dbconn.commit();
					st14.close();
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
			
			System.out.print("변경할 유저의 id를 입력하세요 : ");
			userid = sc.nextLine();
			if(t1.strTest(userid) == false) { break;}
			
			System.out.print("이름을 그대로 사용하시겠습니까? [ Y: 그대로 ]");
			String a = sc.nextLine();
			if(t1.strTest(a) == false) {break;}
			
			if(a.equals("Y")||a.equals("y")) {
				bo.setConnection();
				try {
					String sq12= "select pname from member where userid='"+userid+"'";
					Statement st15 = bo.dbconn.createStatement();
					ResultSet rs15 =st15.executeQuery(sq12);
					rs15.next();
					name = rs15.getString("pname");
					rs15.close();
					st15.close();
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
				bo.disConnection();
			} else {
				System.out.print("새 이름을 입력하세요 : ");
				String newName = sc.nextLine();
				if(t1.strTest(newName) == false) {break;}
				name = newName;
			}
			
			System.out.print("성별을 그대로 사용하시겠습니까? [Y:그대로 사용하기] : ");
			a = sc.nextLine();
			if(t1.strTest(a) == false) {break;}
			
			if(a.equals("Y")||a.equals("y")) {
				
				bo.setConnection();
				try {
					String sq13 ="select gender from member where userid='"+userid+"'";
					Statement st16 = bo.dbconn.createStatement();
					ResultSet rs16 = st16.executeQuery(sq13);
					rs16.next();
					gender = rs16.getString("gender");
					rs16.close();
					st16.close();
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
				bo.disConnection();
			}else {
				System.out.print("성별을 입력하세요 : ");
				String newGender = sc.nextLine();
				if(t1.strTest(newGender) == false) {break;}
				gender = newGender;
			}
			
			System.out.print("모바일 번호를 그대로 사용하시겠습니까? [Y:그대로 사용하기] : ");
			a= sc.nextLine();
			if(t1.strTest(a) == false ) {break;}
			
			if(a.equals("Y")||a.equals("y")) {
				
				bo.setConnection();
				try {
					String sq14 ="select mobile from member where userid ='"+userid+"'";
					Statement st17 = bo.dbconn.createStatement();
					ResultSet rs17 =st17.executeQuery(sq14);
					rs17.next();
					mobile = rs17.getString("mobile");
					rs17.close();
					st17.close();
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
				bo.disConnection();
				
			}else{
				System.out.print("새 모바일 번호를 입력하세요 : ");
				String newMobile = sc.nextLine();
				if(t1.strTest(newMobile) == false) {break;}
				mobile = newMobile;
				
				String t = "select mobile from Member where mobile like '" + mobile + "%' "
											  	 		+ "or pname like '%" + mobile + "' "
											  	   		+ "or pname like '%" + mobile + "%'";		
				
				bo.setConnection();
				try {
					Statement M3 = bo.dbconn.createStatement();
					ResultSet M4 = M3.executeQuery(t);
				
					if(M4.next()) {
						System.out.println("똑같은 모바일 번호가 존재합니다. 처음부터 다시 하십시오.\n");
						break;
					}
					M4.close();
					M3.close();
				}catch(SQLException e) {
					System.out.println(e.getMessage());
				}
				bo.disConnection();
			}
			
			System.out.print("비밀번호를 그대로 사용하시겠습니까? [Y:그대로 사용하기] : ");
			a = sc.nextLine();
			if(t1.strTest(a) == false) {break;}
			
			if(a.equals("Y")||a.equals("y")) {
				
				bo.setConnection();
				try {
					String sq15 ="select pw from member where userid='"+userid+"'";
					Statement st18 = bo.dbconn.createStatement();
					ResultSet rs18 =st18.executeQuery(sq15);
					rs18.next();
					pw = rs18.getString("pw");
					rs18.close();
					st18.close();
				}catch(SQLException e) {
					System.out.println((e.getMessage()));
				}
				bo.disConnection();
			} else {
				System.out.print("새 비밀번호를 입력하세요 : ");
				String newPw = sc.nextLine();
				if(t1.strTest(newPw) == false) {break;}
				pw = newPw;
				
			}
			
			bo.setConnection();
			try {
				//데이터베이스에 수정된 멤버 추가
				String sq16 ="update member set pname='"+name+"',gender='"+gender+"',mobile='"+mobile+
						"', pw='"+pw+"',updated=current_timestamp where userid= '"+userid+"'";
				Statement st19 = bo.dbconn.createStatement();
				st19.executeUpdate(sq16);
				bo.dbconn.commit();
				st19.close();
			}catch(SQLException e) {
				System.out.println((e.getMessage()));
			}
			bo.disConnection();
			
		}
	}
	
}