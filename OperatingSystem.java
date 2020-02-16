import java.util.*;
import java.io.*;

public class OperatingSystem {
	public static int RAM = 256 - 16;
	public static int UsedRAM = 0;
	public static int ProcessJobQueue;
	public static int MemoryPointer;
	public static long Clock;
	public static Process process;
	public static ClockThread ct;
	public static LinkedList<Process> JobQueue = new LinkedList<Process>();
	public static LinkedList<Process> SJF = new LinkedList<Process>();
	public static LinkedList<Process> LCFS = new LinkedList<Process>();
	public static LinkedList<Process> FCFS = new LinkedList<Process>();
	public static LinkedList<Process> WaitingQueue = new LinkedList<Process>();
	public static LinkedList<Process> Memory = new LinkedList<Process>();

	static class LongTermScheduler extends Thread {
		public void run() {
			while (ClockThread.running) {
				if (ct.spent() % 200 == 0 && !Memory.isEmpty() && UsedRAM < (RAM / 100) * 80) {
					SJF.add(Memory.removeLast());
					Memory.remove();
				}
			}
		}
	}

	public static void CPU(Process p, int burst) {
		p.TotalCPUTime += burst;
		p.TimesInCPU++;
		p.ProcessState = "Running";
		MemoryPointer = p.MemoryValues.removeFirst();
		if(MemoryPointer+UsedRAM>240) {
			//Kill biggest process
		}
		UsedRAM = UsedRAM + MemoryPointer;
		
		int b = burst;
		int i = 0;
		if (p.QueueLevel == 1)
			while (i != 50) {
				if (b == 0) {
					break;
				}

				i++;
				b--;
			}
		else if (p.QueueLevel == 2)
			while (i != 100) {
				if (b == 0) {
					break;
				}

				i++;
				b--;
			}
		else
			while (b != 0) { //level 3
				b--;
			}
		if (b != 0) {
			p.QueueLevel += 1;
			if(p.QueueLevel==2) {
				LCFS.add(Memory.removeLast());
			}else {
				FCFS.add(Memory.removeLast());
			}
			
				
			// Move to queue 2 or 3
		}
		if (!p.IOBursts.isEmpty())
			IOOperation(p, p.IOBursts.removeFirst()); //after io done return to cpu
	}

	public static void IOOperation(Process p, int burst) {
		WaitingQueue.add(p);
		p.TotalIOTime += burst;
		p.TimesWaitedForMemory++;
		p.TimesInIO++;
		p.ProcessState = "Waiting";
		UsedRAM = UsedRAM + MemoryPointer;
		Memory.add(p);
	}

	public static void Mq() {
		int i = 0;
		while (i != 50) {

			i++;
		}
	}

	static class ClockThread extends Thread {
		static boolean running = false;
		static long finished;

		public void run() {
			running = true;
			while (running)
				Clock++;
		}

		public void end() {
			running = false;
			finished = Clock;
		}

		public long spent() {
			return Clock;
		}
	}

	public static void ShortTermScheduler() {
		while (!SJF.isEmpty()) {
			Process p1 = SJF.getFirst();
			Process p2 = SJF.getLast();

			if (p1.CPUBursts.getFirst() < p2.CPUBursts.getFirst()) {
				process = p1;
			} else if (p1.CPUBursts.getFirst() > p2.CPUBursts.getFirst()) {
				process = p2;
			}
			SJF.remove(process);
			if (!process.CPUBursts.isEmpty())
				CPU(process, process.CPUBursts.removeFirst());
		}
		while(!LCFS.isEmpty()) {
			Process p=LCFS.getLast();
			process=p;
			LCFS.remove(process);
			if (!process.CPUBursts.isEmpty())
				CPU(process, process.CPUBursts.removeFirst());
		}
		while(!FCFS.isEmpty()) {
			Process p=FCFS.getFirst();
			process=p;
			FCFS.remove(process);
			if (!process.CPUBursts.isEmpty())
				CPU(process, process.CPUBursts.removeFirst());
		}
		
		}
	

	public static void Termination(Process p) {
		if (p.ProcessState.equals("Killed"))
			return;
		else {
			for (int i = 0; JobQueue.get(i) != null; i++) {
				if (p.ProcessID == JobQueue.remove().ProcessID) {
					p.TimeKilledOrTerminated = Clock;
					p.ProcessState = "Terminated";
					p.LastState = "Terminated";
					RemoveFromAllQueues(p);
					break;
				}
			}
		}
	}

	public static void RemoveFromAllQueues(Process p) {
		JobQueue.remove(p);
		if (p.QueueLevel == 1) {
			SJF.remove(p);
		} else if (p.QueueLevel == 2) {
			LCFS.remove(p);
		} else
			FCFS.remove(p);
		WaitingQueue.remove(p);
	}
}
