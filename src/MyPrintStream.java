import java.io.OutputStream;

public class MyPrintStream extends java.io.PrintStream{
	
	public MyPrintStream(OutputStream out){
		super(out);
	}
	
	public void println(String s){
		if(!s.contains("[INFO] RiTa.WordNet.version [033]"))
			super.println(s);
	}
	
}
