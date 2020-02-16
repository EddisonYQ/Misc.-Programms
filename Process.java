import java.util.*;
import java.io.*;

public class Process implements Serializable {
	public int ProcessName, ProcessID, ProcessSize, TimesInCPU, TotalCPUTime, TimesInIO, TotalIOTime,
			TimesWaitedForMemory,QueueLevel;
	public long TimeLoadedIntoRQ, TimeKilledOrTerminated;
	public String ProcessState, LastState;
	public double CPUUtilization;
	public LinkedList<Integer> CPUBursts = new LinkedList<Integer>();
	public LinkedList<Integer> MemoryValues = new LinkedList<Integer>();
	public LinkedList<Integer> IOBursts = new LinkedList<Integer>();

	public Process(int name, int ID) {
		ProcessName = name;
		ProcessID = ID;
		QueueLevel=1;
	}

	static void read() {
		try {
			String s = "";
			File f = new File("");	//Enter file to read from
			FileInputStream fi = new FileInputStream(f);
			ObjectInputStream oi = new ObjectInputStream(fi);
			oi.readObject(); // Gets rid of first line which is "PID CPU/MEM/IO"
			while (true) {
				int i = 0; // To keep track of where to get the burst
				s = (String) oi.readObject(); // Read process
				Process p = new Process(Character.getNumericValue(s.charAt(i)), Character.getNumericValue(s.charAt(i))); // Create process
				i += 2; // Move from PID to CPU burst
				while (i < s.length()) {
					p.CPUBursts.add(Character.getNumericValue(s.charAt(i))); // get CPU burst
					i += 2; // Move from CPU to MEM
					p.MemoryValues.add(Character.getNumericValue(s.charAt(i)));
					i += 2; // Move from MEM to IO
					if (i + 2 > s.length()) { // Check to see if it is End of process
						p.CPUBursts.add(Character.getNumericValue(s.charAt(i))); // Writes -1 To CPUBURST to indicate
																					// end of process
						OperatingSystem.JobQueue.add(p); // Add process to the Job Queue
						break; // End Burst reading for the process
					} else { // It has an IO burst
						p.IOBursts.add(Character.getNumericValue(s.charAt(i)));
						i += 2; // Move from IO to CPU
					}

				}
			}
		} catch (Exception e) {

		}
	}
}
