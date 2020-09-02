import java.util.LinkedList;

public class Optimistic {
    private Database database;
    private Task[] tasks;
    private LinkedList < Task > order = new LinkedList < > ();
    private LinkedList < Task > blocked = new LinkedList < > ();
    private LinkedList < Task > running = new LinkedList < > ();
    private int[] releaseReserved;
    public Optimistic(Database db) {
        this.database = db;
        tasks = database.getTasks();
        for (int i = 1; i < database.getTasks().length; i++) {
            database.getTasks()[i].setState("running");
            order.add(database.getTasks()[i]);
        }
        releaseReserved = new int[database.getResourceAvail().length];
        //		run();
    }


    public Task[] run() {
        int cycle = 0;
        while (!order.isEmpty()) {
            for (int i = 0; i < order.size(); i++) {
                Task task = order.get(i);
                Activity activity = task.getActivities().get(task.getIndexAct());
                int resourceNum = activity.getResource();


                if (task.getState().contains("terminate")) {
                    continue;
                }


                //delay
                if (activity.getDelay() > 0) {
                    activity.setDelay(activity.getDelay() - 1);
                    running.add(task);
                    continue;
                }



                //initiate
                if (activity.getActivity().contains("initiate")) {
                    task.setResourceClaim(activity.getResource(), activity.getNumber());
                    running.add(task);
                }
                // request
                else if (activity.getActivity().contains("request")) {
                    int requestedNum = activity.getNumber();
                    if (database.getResourceAvail()[resourceNum] >= requestedNum) {
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
                    task.setTerminatedCycle(cycle);
                    task.setState("terminate");
                    for (int j = 1; j < task.getResourceGranted().length; j++) {
                        releaseReserved[j] += task.getResourceGranted()[j];
                    }
                }
                if (task.getState().contains("running"))
                    task.increment();
            }
            while (deadlock()) {
                resolveDeadlock();
            }
            initializeTaskOrder();
            cycle++;
            for (int i = 1; i < releaseReserved.length; i++) {
                database.setResourceAvail(i, database.getResourceAvail()[i] + releaseReserved[i]);
                releaseReserved[i] = 0;
            }
        }
        return tasks;
    }



    public boolean deadlock() {
        boolean isDeadlock = false;
        for (int i = 1; i < tasks.length; i++) {
            if (!tasks[i].getState().contains("terminate")) {
                if (tasks[i].getState().contains("blocked"))
                    isDeadlock = true;
                else {
                    return false;
                }
            }
        }
        return isDeadlock;
    }
    public void resolveDeadlock() {
        for (int i = 1; i < tasks.length; i++) {
            if (tasks[i].getState().contains("blocked")) {
                for (int j = 1; j < tasks[i].getResourceGranted().length; j++) {
                    database.setResourceAvail(j, database.getResourceAvail()[j] + tasks[i].getResourceGranted()[j]);
                }
                tasks[i].setAborted(true);
                tasks[i].setState("terminate");
                break;
            }
        }
        for (int i = 1; i < tasks.length; i++) {
            if (tasks[i].getState().contains("blocked")) {
                Task task = tasks[i];
                int resourceNum = tasks[i].getActivities().get(task.getIndexAct()).getResource();
                if (task.getActivities().get(task.getIndexAct()).getNumber() <= database.getResourceAvail()[resourceNum]) {
                    tasks[i].setState("running");
                }
            }
        }
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


    public Database getDatabase() {
        return database;
    }


    public void setDatabase(Database database) {
        this.database = database;
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

    //Getters and Setters

}