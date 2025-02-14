package book_Management;
public class Test {
	
	// String 유효성 검사
	public boolean strTest(String _str) { 
				
		// 공백이 아니여야 하고 !isNumber()이 모두 참이라면
		if(_str == null || _str.isEmpty()) {
			return false;
		}			
		return true;
	}
			
	// Integer 유효성 검사
	public boolean isNumber(String _str) {
				
		// null이거나 비어있으면 false
		if(_str == null || _str.isEmpty() || Integer.parseInt(_str) <= 0) {
			return false;
		}			
		return _str.matches("-?\\d*(\\.\\d+)?");
	}
}