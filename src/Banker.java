import java.util.LinkedList;

public class Banker {
    private Database database;
    private String error = "";
    private Task[] tasks;
    private LinkedList < Task > order = new LinkedList < > ();
    private LinkedList < Task > blocked = new LinkedList < > ();
    private LinkedList < Task > running = new LinkedList < > ();
    private int[] releaseReserved;

    public Banker(Database db) {
        this.database = db;
        tasks = database.getTasks();
        for (int i = 1; i < database.getTasks().length; i++) {
            database.getTasks()[i].setState("running");
            order.add(database.getTasks()[i]);
        }
        releaseReserved = new int[database.getResourceAvail().length];

    }

    public Task[] run() {
        int cycle = 0;
        while (!order.isEmpty()) {
            for (int i = 0; i < order.size(); i++) {
                Task task = order.get(i);
                Activity activity = task.getActivities().get(task.getIndexAct());
                int resourceNum = activity.getResource();

                //delay
                if (activity.getDelay() > 0) {
                    activity.setDelay(activity.getDelay() - 1);
                    running.add(task);
                    continue;
                }


                //initiate
                if (activity.getActivity().contains("initiate")) {
                    if (activity.getNumber() > database.getResourceTotal()[activity.getResource()]) {
                        error += ("Banker aborts task " + activity.getTaskNum() + " before run begins: " + " claim for resourse " + activity.getResource() + " (" + activity.getNumber() + ")" +
                            " exceeds number of units present (" + database.getResourceTotal()[activity.getResource()]) + ")";
                        error += "\n";
                        task.setState("terminate");
                        task.setAborted(true);
                        continue;
                    }
                    task.setResourceClaim(activity.getResource(), activity.getNumber());
                    running.add(task);
                }
                //request
                else if (activity.getActivity().contains("request")) {
                    int requestedNum = activity.getNumber();
                    if (requestedNum > (task.getResourceClaim()[activity.getResource()] - task.getResourceGranted()[activity.getResource()])) {
                        task.setState("terminate");
                        task.setAborted(true);
                        error += "During cycle " + cycle + "-" + (cycle + 1) + " of Banker's algorithms Task " + activity.getTaskNum() + "'s request exceeds its claim; aborted;";
                        for (int j = 1; j < task.getResourceGranted().length; j++) {
                            releaseReserved[j] += task.getResourceGranted()[j];
                            if (task.getResourceGranted()[j] > 0)
                                error += " resource " + j + "(" + task.getResourceGranted()[j] + ") ";
                        }
                        error += "units available next cycle";
                        error += "\n";
                        continue;
                    }
                    if (checkSafe(task)) {
                        task.setState("running");
                        database.setResourceAvail(resourceNum, database.getResourceAvail()[resourceNum] - requestedNum);
                        task.setResourceGranted(resourceNum, task.getResourceGranted()[resourceNum] + requestedNum);
                        running.add(task);
                    } else {
                        task.setState("blocked");
                        task.IncrementblockedCycle();
                        blocked.add(task);
                    }
                }
                //release
                else if (activity.getActivity().contains("release")) {
                    int releasedNum = activity.getNumber();
                    releaseReserved[resourceNum] += releasedNum;
                    task.setResourceGranted(resourceNum, task.getResourceGranted()[resourceNum] - releasedNum);
                    running.add(task);
                }
                //terminate
                else if (activity.getActivity().contains("terminate")) {
                    for (int j = 1; j < task.getResourceGranted().length; j++) {
                        releaseReserved[j] += task.getResourceGranted()[j];
                    }
                    task.setTerminatedCycle(cycle);
                    task.setState("terminate");
                }
                if (task.getState().contains("running"))
                    task.increment();
            }
            initializeTaskOrder();
            cycle++;
            for (int i = 1; i < releaseReserved.length; i++) {
                database.setResourceAvail(i, database.getResourceAvail()[i] + releaseReserved[i]);
                releaseReserved[i] = 0;
            }
            if (cycle > 200) {
                break;
            }
        }

        return tasks;
    }

    public void initializeTaskOrder() {
        order.clear();
        for (int i = 0; i < blocked.size(); i++) {
            if (!blocked.get(i).getState().contains("terminate")) {
                order.add(blocked.get(i));
            }
        }
        for (int i = 0; i < running.size(); i++) {
            if (!running.get(i).getState().contains("terminate")) {
                order.add(running.get(i));
            }
        }
        running.clear();
        blocked.clear();
    }

    public boolean checkSafe(Task currTask) {
        Activity activity = currTask.getActivities().get(currTask.getIndexAct());

        if (activity.getNumber() > database.getResourceAvail()[activity.getResource()]) {
            return false;
        }
        currTask.increaseResourceGranted(activity.getResource(), activity.getNumber());
        database.decreaseResourceAvail(activity.getResource(), activity.getNumber());
        for (int i = 1; i < tasks.length; i++) {
            if (!tasks[i].getState().contains("terminate")) {
                Task task = tasks[i];
                boolean isSafe = true;
                for (int j = 1; j < task.getResourceClaim().length; j++) {
                    if (database.getResourceAvail()[j] < (task.getResourceClaim()[j] - task.getResourceGranted()[j])) {
                        isSafe = false;
                        break;
                    }
                }
                if (isSafe) {
                    currTask.decreaseResourceGranted(activity.getResource(), activity.getNumber());
                    database.increaseResourceAvail(activity.getResource(), activity.getNumber());
                    return true;
                }
            }
        }
        currTask.decreaseResourceGranted(activity.getResource(), activity.getNumber());
        database.increaseResourceAvail(activity.getResource(), activity.getNumber());
        return false;
    }

    //Getters and Setters

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Task[] getTasks() {
        return tasks;
    }

    public void setTasks(Task[] tasks) {
        this.tasks = tasks;
    }

    public LinkedList < Task > getOrder() {
        return order;
    }

    public void setOrder(LinkedList < Task > order) {
        this.order = order;
    }

    public LinkedList < Task > getBlocked() {
        return blocked;
    }

    public void setBlocked(LinkedList < Task > blocked) {
        this.blocked = blocked;
    }

    public LinkedList < Task > getRunning() {
        return running;
    }

    public void setRunning(LinkedList < Task > running) {
        this.running = running;
    }

    public int[] getReleaseReserved() {
        return releaseReserved;
    }

    public void setReleaseReserved(int[] releaseReserved) {
        this.releaseReserved = releaseReserved;
    }





}