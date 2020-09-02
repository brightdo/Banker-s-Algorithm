import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Database {
    private Task[] tasks;
    private int[] resourceAvail;
    private int[] resourceTotal;


    public Database(Scanner input) {
        initData(input);
    }



    public void initData(Scanner input) {
        ArrayList allData = new ArrayList < String > ();
        String[] indviData = null;

        while (input.hasNext()) {
            String currentLine = input.next();
            indviData = currentLine.split("\\s+");
            for (int i = 0; i < indviData.length; i++) {
                if (!"".equals(indviData[i])) {
                    allData.add(indviData[i]);
                }
            }
        }

        int numOfTask = Integer.parseInt((String) allData.get(0));
        int numOfResource = Integer.parseInt((String) allData.get(1));
        tasks = new Task[numOfTask + 1];
        resourceAvail = new int[numOfResource + 1];
        resourceTotal = new int[numOfResource + 1];

        int resourceNum = 1;
        for (int i = 2; i < Integer.parseInt((String) allData.get(1)) + 2; i++) {
            resourceAvail[resourceNum] = Integer.parseInt((String) allData.get(i));
            resourceTotal[resourceNum] = Integer.parseInt((String) allData.get(i));
            resourceNum++;
        }

        for (int i = 1; i < numOfTask + 1; i++) {
            Task task = new Task(i, numOfResource);
            for (int j = 0; j < allData.size(); j++) {
                if (String.valueOf(allData.get(j)).contains("initiate") || String.valueOf(allData.get(j)).contains("request") ||
                    String.valueOf(allData.get(j)).contains("release") || String.valueOf(allData.get(j)).contains("terminate")) {
                    if (Integer.parseInt((String) allData.get(j + 1)) == i) {
                        Activity activity = new Activity((String) allData.get(j), Integer.parseInt((String) allData.get(j + 1)), Integer.parseInt((String) allData.get(j + 2)),
                            Integer.parseInt((String) allData.get(j + 3)), Integer.parseInt((String) allData.get(j + 4)));
                        task.add(activity);
                    }
                }

            }
            tasks[i] = task;
        }

    }


    //Getters and Setters

    public Task[] getTasks() {
        return tasks;
    }


    public void setTasks(Task[] tasks) {
        this.tasks = tasks;
    }


    public int[] getResourceAvail() {
        return resourceAvail;
    }


    public void setResourceAvail(int resource, int val) {
        this.resourceAvail[resource] = val;
    }



    public int[] getResourceTotal() {
        return resourceTotal;
    }

    public void increaseResourceAvail(int res, int val) {
        this.resourceAvail[res] += val;
    }

    public void decreaseResourceAvail(int res, int val) {
        this.resourceAvail[res] -= val;
    }

    public void setResourceTotal(int[] resourceTotal) {
        this.resourceTotal = resourceTotal;
    }



}