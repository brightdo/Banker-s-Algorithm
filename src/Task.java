import java.util.ArrayList;

public class Task {
    private int taskNum;
    private int[] resourceClaim;
    private int[] resourceGranted;
    private int indexAct = 0;
    private int terminatedCycle = 0;
    private int blockedCycle = 0;
    private String state;
    private boolean aborted = false;
    private ArrayList < Activity > activities;

    public Task(int num, int resourceCount) {
        this.taskNum = num;
        activities = new ArrayList < > ();
        resourceClaim = new int[resourceCount + 1];
        resourceGranted = new int[resourceCount + 1];
    }

    public void increment() {
        indexAct++;
    }

    public void add(Activity activity) {
        activities.add(activity);
    }



    //Getters and Setters

    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }

    public ArrayList < Activity > getActivities() {
        return activities;
    }

    public void setActivities(ArrayList < Activity > activities) {
        this.activities = activities;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int[] getResourceClaim() {
        return resourceClaim;
    }

    public void setResourceClaim(int resourceNum, int val) {
        this.resourceClaim[resourceNum] = val;
    }

    public int getIndexAct() {
        return indexAct;
    }

    public void setIndexAct(int indexAct) {
        this.indexAct = indexAct;
    }

    public int[] getResourceGranted() {
        return resourceGranted;
    }

    public void increaseResourceGranted(int resource, int val) {
        this.resourceGranted[resource] += val;
    }

    public void decreaseResourceGranted(int resource, int val) {
        this.resourceGranted[resource] -= val;
    }


    public void setResourceGranted(int resource, int val) {
        this.resourceGranted[resource] = val;
    }

    public int getTerminatedCycle() {
        return terminatedCycle;
    }

    public void setTerminatedCycle(int terminatedCycle) {
        this.terminatedCycle = terminatedCycle;
    }

    public int getblockedCycle() {
        return blockedCycle;
    }

    public void setblockedCycle(int pendingCycle) {
        this.blockedCycle = pendingCycle;
    }

    public void IncrementblockedCycle() {
        this.blockedCycle++;
    }

    public boolean isAborted() {
        return aborted;
    }

    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }



}