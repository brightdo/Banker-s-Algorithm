public class Activity {
    private String activity;
    private int taskNum;
    private int delay;
    private int resource;
    private int number;

    public Activity(String act, int task, int delay, int r, int n) {
        this.activity = act;
        this.delay = delay;
        this.taskNum = task;
        this.resource = r;
        this.number = n;
    }


    public String toString() {
        return activity + " " + taskNum + " " + delay + " " + resource + " " + number;
    }


    //Getters and Setters
    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


}