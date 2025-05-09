import java.util.*;

class Process {
    int id, arrivalTime, burstTime, remainingTime, completionTime, waitingTime, turnaroundTime;
    
    Process(int id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

class Event implements Comparable<Event> {
    int time;
    String type; 
    Process process;
    
    Event(int time, String type, Process process) {
        this.time = time;
        this.type = type;
        this.process = process;
    }
    
    @Override
    public int compareTo(Event other) {
        return Integer.compare(this.time, other.time);
    }
}

public class SRTF {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        
        Process[] processes = new Process[n];
        PriorityQueue<Event> eventQueue = new PriorityQueue<>();
        
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time for P" + (i + 1) + ": ");
            int arrivalTime = sc.nextInt();
            System.out.print("Enter Burst Time for P" + (i + 1) + ": ");
            int burstTime = sc.nextInt();
            
            processes[i] = new Process(i + 1, arrivalTime, burstTime);
            eventQueue.add(new Event(arrivalTime, "ARRIVAL", processes[i]));
        }
        
        int currentTime = 0, completedProcesses = 0, totalExecutionTime = 0;
        int totalTurnaroundTime = 0, totalWaitingTime = 0;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.remainingTime));
        Process lastProcess = null;
        
        System.out.println("\nScheduling Algorithm: Shortest Remaining Time First");
        System.out.println("Context Switch: 1 ms");
        System.out.println("Time\tProcess/CS");
        
        while (completedProcesses < n) {
            while (!eventQueue.isEmpty() && eventQueue.peek().time <= currentTime) {
                Event event = eventQueue.poll();
                if ("ARRIVAL".equals(event.type)) {
                    readyQueue.add(event.process);
                }
            }
            
            if (!readyQueue.isEmpty()) {
                Process selectedProcess = readyQueue.poll();
                
                if (lastProcess != null && lastProcess != selectedProcess) {
                    System.out.println(currentTime + "-" + (currentTime + 1) + "\tCS");
                    currentTime++;
                }
                
                int startTime = currentTime;
                lastProcess = selectedProcess;
                
                while (selectedProcess.remainingTime > 0) {
                    selectedProcess.remainingTime--;
                    currentTime++;
                    totalExecutionTime++;
                    
                    while (!eventQueue.isEmpty() && eventQueue.peek().time == currentTime) {
                        Event event = eventQueue.poll();
                        if ("ARRIVAL".equals(event.type)) {
                            readyQueue.add(event.process);
                        }
                    }
                    
                    if (!readyQueue.isEmpty() && readyQueue.peek().remainingTime < selectedProcess.remainingTime) {
                        break;
                    }
                }
                
                System.out.println(startTime + "-" + currentTime + "\tP" + selectedProcess.id);
                
                if (selectedProcess.remainingTime == 0) {
                    selectedProcess.completionTime = currentTime;
                    selectedProcess.turnaroundTime = selectedProcess.completionTime - selectedProcess.arrivalTime;
                    selectedProcess.waitingTime = selectedProcess.turnaroundTime - selectedProcess.burstTime;
                    totalTurnaroundTime += selectedProcess.turnaroundTime;
                    totalWaitingTime += selectedProcess.waitingTime;
                    completedProcesses++;
                } else {
                    readyQueue.add(selectedProcess);
                }
            } else {
                currentTime++;
            }
        }
        
        double avgTurnaroundTime = (double) totalTurnaroundTime / n;
        double avgWaitingTime = (double) totalWaitingTime / n;
        double cpuUtilization = ((double) totalExecutionTime / currentTime) * 100;
        
        System.out.println("\nPerformance Metrics");
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime);
        System.out.printf("Average Waiting Time: %.2f\n", avgWaitingTime);
        System.out.printf("CPU Utilization: %.2f%%\n", cpuUtilization);
    }
}

