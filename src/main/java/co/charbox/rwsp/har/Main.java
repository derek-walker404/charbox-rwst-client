package co.charbox.rwsp.har;


public class Main {
	
	public static void main(String[] args) {
		// TODO: not ideal, use config file.
		HarDriver hd = new HarDriver();
		for (String s : args) {
			hd.run(s);
		}
    }
}
