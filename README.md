# Banker-s-Algorithm

implement resource allocation using both an optimistic resource manager and the banker’s
algorithm of Dijkstra. The optimistic resource manager is simple: Satisfy a request if possible, if not make
the task wait; when a release occurs, try to satisfy pending requests in a FIFO manner.

### input
The program takes a text file input with  values T, the number of tasks, and R, the number of resource types, followed by
R additional values, the number of units present of each resource type. Then come multiple inputs, each representing the next activity of a specific task in the following format.

activity task-number delay resource-type initial-claim

### output
At the end of the run prints, for each task, the time taken, the waiting time, and the percentage of time
spent waiting. Also prints the total time for all tasks, the total waiting time, and the overall percentage of
time spent waiting.


### Compiling
```
javac Main.java
```

### Running
```
Java Main ../input --(01-13).txt
```
