public class Output {

    public void print(Task[] tasksF, Task[] tasksB) {
        int totalF = 0;
        int totalB = 0;
        int totalblockedF = 0;
        int totalblockedB = 0;
        System.out.println("	FIFO		BANKER'S");
        for (int i = 1; i < tasksF.length; i++) {
            String output = "Task" + i;
            if (tasksF[i].isAborted()) {
                output += "	 aborted    ";
            } else {
                output += "   " + tasksF[i].getTerminatedCycle() + "  " + tasksF[i].getblockedCycle() + "  " + Math.round((float) tasksF[i].getblockedCycle() / (float) tasksF[i].getTerminatedCycle() * 100) + "%";
                totalF += tasksF[i].getTerminatedCycle();
                totalblockedF += tasksF[i].getblockedCycle();
            }
            output += "  Task" + i;
            if (tasksB[i].isAborted()) {
                output += "  " + "aborted";
            } else {
                output += "   " + tasksB[i].getTerminatedCycle() + "  " + tasksB[i].getblockedCycle() + "  " + Math.round((float) tasksB[i].getblockedCycle() / (float) tasksB[i].getTerminatedCycle() * 100) + "%";
                totalB += tasksB[i].getTerminatedCycle();
                totalblockedB += tasksB[i].getblockedCycle();
            }
            System.out.println(output);
        }
        System.out.println("total   " + totalF + "  " + totalblockedF + "  " + Math.round((float) totalblockedF / (float) totalF * 100) + "%" +
            "  total " + totalB + "  " + totalblockedB + "  " + Math.round((float) totalblockedB / (float) totalB * 100) + "%");

    }
}