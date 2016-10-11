package object;



public class DeviceObj {
		
	private String id;
	private int number;
	private double detical;
	//private List<String> list;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public double getDetical() {
		return detical;
	}
	public void setDetical(double detical) {
		this.detical = detical;
	}
	@Override
	public String toString() {
		
		return id+" "+number+" "+detical;
	}
	
}
