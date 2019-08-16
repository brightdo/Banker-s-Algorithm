package lab3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Banker {

	public static Scanner newScanner(String fileName) {
		 try{
			 Scanner input = new Scanner(new BufferedReader(new FileReader(fileName)));
			 return input;
		 }
		 catch(Exception ex) {
			 System.out.printf("Error reading %s\n", fileName);
			 System.exit(0);
		 }
		 return null;
	}
	
	public static void resourceManager(Scanner input) {
int numOfTask;
ArrayList initiated = new ArrayList(); // arrayList to keep track of tasks initiated, released, or aborted
ArrayList sequenceOfEvent = new ArrayList(); // arrayList to see the sequence of events(request,release,terminated) happening
ArrayList sequenceOfLine = new ArrayList();
ArrayList sequenceOfInitiate = new ArrayList();
ArrayList terminating = new ArrayList();
ArrayList terminated = new ArrayList();
ArrayList pending = new ArrayList();
ArrayList computing = new ArrayList();
ArrayList running = new ArrayList();
ArrayList aborted = new ArrayList();
ArrayList abortedBanker = new ArrayList();
ArrayList abortedBankerOutput = new ArrayList();
ArrayList releaseWaiting = new ArrayList();
ArrayList cycleIndex = new ArrayList();
ArrayList terminatedBanker = new ArrayList();


int numOfResourceType;
int numOfUnitPerResource;
int cycle =0;
String[] safe;
int numOfLine;  // variable used to find the number of activity lines in order to put them in the 2D array
String [][] allActivity;  // each line of activity saved as two-dimension array with [each line][ 1 activity + 4 unsigned int]
int[][] request; // each line of request saved as two-dimension array with [task number calling request] [#'th occurance of request] saved result will be the line number of the release being called
int[][] release;// each line of release saved as two-dimension array with [task number calling release] [#'th occurance of release] saved result will be the line number of the release being called
int[][] terminate;// each line of terminate saved as two-dimension array with [task number calling terminate] [0 because its only called once per task ] saved result will be the line number of the terminate being called
int[][] initiate;
String [][] eachActivity;// index of all line of activity of one Task in two-dimension array with [task number]  #'th of particular task] ie; eachActivity[1][0] will be the 0th(first) activity called by task number1
int[] eachNumOfUnitRType; // 2d Array to keep track of unit of resource remained after given to task request; [resource type] = remaining resource amount of that resource type 
int[][] resourceGiven; // 2d Array to keep track of amount of resource granted and returned when terminated by each task; [tasknumber][resource type] saved data will be the amount of resource given
int[] counterForReq; 
int[]counterForRel; 
int[] cycleCount; // array for output of end time
int[] waitResult; // array for output of end waiting time
int[] waitingTime;// time counter that keeps track of how long till end of delay
int[] eachNumOfUnitRTypeBanker;
int[][] resourceGivenBanker;
int[] cycleCountBanker; // array for output of end time
int[] waitResultBanker; // array for output of end waiting time
int[] waitingTimeBanker;
int[][] max; // amount told to be needed from each task
int[][] need;// amount remaining for each task is allowed  to ask for;
//int howManyRequest; // how many request  is called
//int howManyRelease; // how many release is called


		 ArrayList allData = new ArrayList<String>();
		 String[] indviData = null;
		 //Store string input into an array and splits all white spaces and does not include empty lines
		 while (input.hasNext()) 
		 {
			 String currentLine = input.next();
			 indviData = currentLine.split("\\s+");			
			 for(int i = 0; i < indviData.length; i++){
				 if(!"".equals(indviData[i])) {					
					 allData.add(indviData[i]);
				 }
			 }
		 }
		 

		 
		 // initiate all array based on number of Resource  and number of Unit per Resource
		 numOfTask = Integer.parseInt((String) allData.get(0));
		 numOfResourceType = Integer.parseInt((String) allData.get(1));
		 numOfUnitPerResource = Integer.parseInt((String) allData.get(2));
		 numOfLine = (allData.size() - (2 + numOfResourceType))/5;
		 allActivity = new String[numOfLine][5];
		 release = new int[numOfTask+1][100];
		 request = new int[numOfTask+1][100];
		 terminate = new int[numOfTask+1][100];
		 initiate = new int[numOfTask+1][numOfResourceType+1];
		 eachActivity = new String[numOfTask+1][numOfLine];
		 eachNumOfUnitRType = new int[numOfResourceType+1];
		 eachNumOfUnitRTypeBanker = new int[numOfResourceType+1];
		 counterForReq = new int[numOfTask+1];
		 counterForRel = new int[numOfTask+1];
		 resourceGiven = new int[numOfTask+1][numOfResourceType+1];
		 resourceGivenBanker = new int[numOfTask+1][numOfResourceType+1];
		 cycleCount = new int[numOfTask+1];
		 cycleCountBanker = new int[numOfTask+1];
		 waitingTime = new int[numOfTask+1];
		 waitResult = new int[numOfTask+1];
		 waitResultBanker = new int[numOfTask+1];
		 waitingTimeBanker = new int[numOfTask+1];
		 max = new int [numOfTask+1][numOfResourceType+1];
		need = new int[numOfTask+1][numOfResourceType+1];
		safe = new String[numOfTask+1];
		 ArrayList<String>[] lists = (ArrayList<String>[])new ArrayList[numOfTask +1]; 
		 ArrayList<String>[] listsBanker = (ArrayList<String>[])new ArrayList[numOfTask +1];
		 
		 
		 
		 //initiate an array of arraylist for fifo and Banker 
		 for(  int i=0; i<lists.length; i++) {
			 lists[i] = new ArrayList<>();
			 listsBanker[i] = new ArrayList<>();
		 }
		 	 
		 // initiate the 2d  array for the eachNumOfUnitRType
		 for( int i=1; i<(numOfResourceType+1);i++) {
			 for( int j=2; j<= numOfResourceType+1;j++ ) {
			 eachNumOfUnitRType[i]=  Integer.parseInt((String) allData.get(j));
			 eachNumOfUnitRTypeBanker[i]=  Integer.parseInt((String) allData.get(j));
			 }
		 }
		 		 
// initiate the 2d array list for release, request, terminate (dont use 0 because index 0 is a thing) this 2d array will be used to store the line 
for( int i=0; i<numOfTask+1; i++)
		 for( int j=0; j<100; j++) {
	release[i][j] = -100;
	request[i][j] = -100;
	terminate[i][j] = -100;
}

// initiate the 2d array list for each acitivity
for( int i=0; i<numOfTask+1; i++)
	 for( int j=0; j<numOfLine; j++) {
eachActivity[i][j] ="null";
}

int counter = 2 + numOfResourceType;// start of the activity list and all the ones after 		 
		 // loop to put all activity in allActivity 2-D array
		 for ( int i=0; i<numOfLine; i++) {
			 for( int j=0; j<5; j++) {
				 allActivity[i][j] = (String) allData.get(counter);
				 counter++;
			 } 
		 }// end of loop and initializing the 2-D allActivity array
		 


		 
// counters for the next loop
		 int requestCounter=0;
		 int releaseCounter=0;
		 int terminateCounter=0;
		 int taskCounter=1;
		 int eachActCounter=0;
// loop through allActivity and find request, release, or terminate than read the task-number and put it in a 2d Array with [task-number][just count of how many request, release, or terminate] 
// this will allow the code to read request,release,terminate of each task in order  ie: request[1][0] and request[2][0] will have the line number of the first request in task 1 and 2.
// eachActivity is to remember the order of the activities like which activity is first; request or release and how many request before release or how many release before terminate
		 for( int i=1; i<(numOfTask+1);i++) {
			 for (int j=0; j< numOfLine; j++) {
				 if(allActivity[j][0].contains("request")) {
					 if(Integer.parseInt(allActivity[j][1])==taskCounter) {
					 request[taskCounter][requestCounter]=j;
					 eachActivity[taskCounter][eachActCounter]= "request";
					 eachActCounter++;
					 requestCounter++;
					 }
				 }
				 if(allActivity[j][0].contains("release")) {
					 if(Integer.parseInt(allActivity[j][1])==taskCounter) {
					 release[taskCounter][releaseCounter]=j;
					 eachActivity[taskCounter][eachActCounter]= "release";
					 eachActCounter++;
					 releaseCounter++;
					 }
				 }
				 if(allActivity[j][0].contains("terminate")) {
					 if(Integer.parseInt(allActivity[j][1])==taskCounter) {
					 terminate[taskCounter][terminateCounter]=j;
					 eachActivity[taskCounter][eachActCounter]= "terminate";
					 requestCounter=0;
					 releaseCounter=0;
					 eachActCounter=0;
					 }
				 }
			 }
			 taskCounter++;
		 }// end of for loop for making  request,release,terminate 2D array

		 
		 
		 
		 // this goes through all the line of the input data and finds the initiate part
		 // 2d array initiate is to keep track of index of AllActivity that contains the beginning of initiate
		 // sequeceOfInitiate just keeps the initiate + 4 unsigned integer in order without having in order of task-number. ie: it would go through all initiate of task 1 first then task 2.
		 int taskCounterInit=1;
		 for( int i=1; i<(numOfTask+1);i++) {
			 for (int j=0; j< numOfLine; j++) {
				 if(allActivity[j][0].contains("initiate")) {
					 if(Integer.parseInt(allActivity[j][1])==taskCounterInit) {
					 sequenceOfInitiate.add(allActivity[j][0]);
					 sequenceOfInitiate.add(allActivity[j][1]);
					 sequenceOfInitiate.add(allActivity[j][2]);
					 sequenceOfInitiate.add(allActivity[j][3]);
					 sequenceOfInitiate.add(allActivity[j][4]);
					 }
				 }
			 	}
			 taskCounterInit++;
			 }// end of making sequenceOfInitiate 

		 
		 
		 
		// keeping track of tasks not terminated yet that will be used to make sequence of event
			for( int i=0; i< numOfTask; i++) {
				initiated.add(i);
			}
			
			cycle=1;
			int anotherCounter=0; 
// making of sequence of events by going through the whole data and extracting only the activity done
// so sequence of events for input with 2 task would be [first activity of task  1(request/release/terminate), first activity of task 2, second activity of task 1, second activity of task2, etc...
// done by looping through eachActivity(order of activity done by one task number only) and a counter that only increments after we go through each task number; ie: [1][0],[2][0],[1][1],[2][1], etc...

		while (cycle< numOfLine+1) {
			for( int i=0; i<initiated.size(); i++) {
				sequenceOfEvent.add(eachActivity[i+1][anotherCounter]);
			}
		anotherCounter++;
		cycle++;	
		}	
		
		
		
		
		initiated.clear();
		for( int i=0; i< numOfTask; i++) {
			initiated.add(i+1);
		}// end of making of  sequenceOfEvent
		


		int initIndex=0;
		boolean wait = false;
		// we now have sequenceOfEvent that has activity(request/release/terminate) in order( first activity of first task-number, then first activity of second task-number)
		// loop through sequence of tasks, find the line number through request,release,terminate and use allAcrivity[line number],[1],[2],[3],[4] to also save the 4 unsigned integers
		// dont add if eachActivity = null meaning that it has terminated already so we don't need to  care about its activity.
		// null and -1 is used when one task is terminated eariler; initIndex circulates through task number; sequenceOfLines go in order like first line of first task, first line of second task, first line of third task, second line of first task, second line of second task, etc...
		// (1) counterForReq/Rel[initIndex] to find which request or release (2) request/release[(int) initiated.get(initIndex)][counterForReq/Rel]to find the line number of the request/release (3) allActivity[request/release][counterForReq/Rel]to find the activity + 4 unsigned int and save those in arrayList	
		while(terminating.size()!=numOfTask) {
			if(sequenceOfEvent.get(0).equals("request")) {
				sequenceOfLine.add(allActivity[request[(int) initiated.get(initIndex)][counterForReq[initIndex]]][0]);
				sequenceOfLine.add(allActivity[request[(int) initiated.get(initIndex)][counterForReq[initIndex]]][1]);
				sequenceOfLine.add(allActivity[request[(int) initiated.get(initIndex)][counterForReq[initIndex]]][2]);
				sequenceOfLine.add(allActivity[request[(int) initiated.get(initIndex)][counterForReq[initIndex]]][3]);
				sequenceOfLine.add(allActivity[request[(int) initiated.get(initIndex)][counterForReq[initIndex]]][4]);
				counterForReq[initIndex]++;
			}
			else if( sequenceOfEvent.get(0).equals("release")) {
				sequenceOfLine.add(allActivity[release[(int) initiated.get(initIndex)][counterForRel[initIndex]]][0]);
				sequenceOfLine.add(allActivity[release[(int) initiated.get(initIndex)][counterForRel[initIndex]]][1]);
				sequenceOfLine.add(allActivity[release[(int) initiated.get(initIndex)][counterForRel[initIndex]]][2]);
				sequenceOfLine.add(allActivity[release[(int) initiated.get(initIndex)][counterForRel[initIndex]]][3]);
				sequenceOfLine.add(allActivity[release[(int) initiated.get(initIndex)][counterForRel[initIndex]]][4]);
				counterForRel[initIndex]++;
			}
			else if( sequenceOfEvent.get(0).equals("terminate")) {
				sequenceOfLine.add(allActivity[terminate[(int) initiated.get(initIndex)][0]][0]);
				sequenceOfLine.add(allActivity[terminate[(int) initiated.get(initIndex)][0]][1]);
				sequenceOfLine.add(allActivity[terminate[(int) initiated.get(initIndex)][0]][2]);
				sequenceOfLine.add(allActivity[terminate[(int) initiated.get(initIndex)][0]][3]);
				sequenceOfLine.add(allActivity[terminate[(int) initiated.get(initIndex)][0]][4]);
				terminating.add(initiated.get(initIndex));
			}
			else if( sequenceOfEvent.get(0).equals("null")) {
				sequenceOfLine.add("null");
				sequenceOfLine.add(-1);
				sequenceOfLine.add(-1);
				sequenceOfLine.add(-1);
				sequenceOfLine.add(-1);
			}

			initIndex++;
			if((initIndex)>= initiated.size()){
				initIndex=0;
				}
			sequenceOfEvent.remove(0);

		}// end of while loop that creats sequenceOfLine( arrayList that has the activity + 4 unsigned int of each line in order to be looked at)
		
		

		// now use sequencOfLine to create an array of arraylist with list[task-number]. Each array will contain the sequence of activity + 4unsigned int in order of that task 
		//so list[1] will contain all the activity + 4unsigned integer( task-number, delay, resource-type, number-request/release) in order.
		// we do this by making list[i+1( task-number when i is activity(request/release/terminate)] and add the activity + 4 unsigned int, then find the next activity to add into the next task-number.
		for( int i=0; i<sequenceOfLine.size(); i++) {
			if( sequenceOfLine.get(i).equals("request")||sequenceOfLine.get(i).equals("release")|| sequenceOfLine.get(i).equals("terminate")) {
				lists[Integer.parseInt((String) sequenceOfLine.get(i+1))].add((String) sequenceOfLine.get(i));
				lists[Integer.parseInt((String) sequenceOfLine.get(i+1))].add((String) sequenceOfLine.get(i+1));
				lists[Integer.parseInt((String) sequenceOfLine.get(i+1))].add((String) sequenceOfLine.get(i+2));
				lists[Integer.parseInt((String) sequenceOfLine.get(i+1))].add((String) sequenceOfLine.get(i+3));
				lists[Integer.parseInt((String) sequenceOfLine.get(i+1))].add((String) sequenceOfLine.get(i+4));
				listsBanker[Integer.parseInt((String) sequenceOfLine.get(i+1))].add((String) sequenceOfLine.get(i));
				listsBanker[Integer.parseInt((String) sequenceOfLine.get(i+1))].add((String) sequenceOfLine.get(i+1));
				listsBanker[Integer.parseInt((String) sequenceOfLine.get(i+1))].add((String) sequenceOfLine.get(i+2));
				listsBanker[Integer.parseInt((String) sequenceOfLine.get(i+1))].add((String) sequenceOfLine.get(i+3));
				listsBanker[Integer.parseInt((String) sequenceOfLine.get(i+1))].add((String) sequenceOfLine.get(i+4));
			}
		}
		
		// end of making lists and listsBanker 
		
		
		
		
		
		// fifo 
		
		//initiate running and cycleIndex
		//running keeps track of task-number not terminated or aborted yet
		// cycleIndex keep track of task-number to visit before we've gone through all of the running ones atleast once before incrementing the cycle
		for( int i=1; i< numOfTask+1; i++) {
			running.add(i);
			cycleIndex.add(i);
		}


cycle=numOfResourceType;
int relWait = 0;

// keep looping while we still have a task not terminated 
		while(!running.isEmpty()) {
			
			boolean skip = false;
			boolean deadlock = false;
			
			//release actual resource
			// when the release was called and the next cycle  came release the resource
			// when release was called from lists, we put in the release +  4 unsigned integer into releaseWaiting to easily keep track of the release line.
			if(relWait==cycle) {
				for(int i=0; i<releaseWaiting.size(); i++) {
					if( releaseWaiting.get(0).equals("release")) {
						eachNumOfUnitRType[Integer.parseInt((String) releaseWaiting.get(3))]+=Integer.parseInt((String) releaseWaiting.get(4));
						resourceGiven[Integer.parseInt((String) releaseWaiting.get(1))][Integer.parseInt((String) releaseWaiting.get(3))]-= Integer.parseInt((String) releaseWaiting.get(4));
						releaseWaiting.remove(0);
						releaseWaiting.remove(0);
						releaseWaiting.remove(0);
						releaseWaiting.remove(0);
						releaseWaiting.remove(0);
					}
				}
				// refresh relWait
				relWait= 0;
			}
			
			
			
			// delay
			// when there is a delay in an activity, add a individual computing activity with four 0 unsigned integer to make list go through those before performing the actual activity
			// rewrite the delay number to 0 to make sure it is not done again.
			if(Integer.parseInt(lists[(int) cycleIndex.get(0)].get(2))!=0) {
				computing.add(cycleIndex.get(0));
				waitingTime[(int) cycleIndex.get(0)] += Integer.parseInt(lists[(int) cycleIndex.get(0)].get(2));
				int iteration = (Integer.parseInt(lists[(int) cycleIndex.get(0)].get(2)));
				lists[(int) cycleIndex.get(0)].set(2, "0");
				for( int i=0; i<iteration; i++){
				lists[(int)cycleIndex.get(0)].add(0, "0");
				lists[(int)cycleIndex.get(0)].add(0, "0");
				lists[(int)cycleIndex.get(0)].add(0, "0");
				lists[(int)cycleIndex.get(0)].add(0, "0");
				lists[(int)cycleIndex.get(0)].add(0, "computing");
				}
			}
			
			// if there is the same number of pending as running it might mean we're deadlock
			if( pending.size() == running.size()) {
				deadlock = true;
				//go through all pending to see if we have a pending request we can fulfill if we do then we're not deadlock
				for( int i=0; i<pending.size(); i++) {
					if(Integer.parseInt(lists[(int) pending.get(i)].get(4))<= eachNumOfUnitRType[Integer.parseInt(lists[(int) pending.get(i)].get(3))]) {
						deadlock = false;
					}
				}
			}
			
			
			
			// pending requests
			// if we have pending but not deadlocked
			//  for this we have a separate pending arraylist that keeps track of cycleIndex that is pending
			// first go through each pending request's cycleIndex and use that index to get the 4 unsigned integers to see if the pending request can be granted
			// if granted use the pending cIndex to access list to grant request.
			// remove pending index and cycleIndex to make sure that task-number does not perform two activity in one cycle
			// skip to make sure task-number does not go to the non pending activity and is granted another activity
			if(!pending.isEmpty() && !deadlock) {
				for( int j=0; j<pending.size(); j++) {
					if(Integer.parseInt(lists[(int) pending.get(j)].get(4))<= eachNumOfUnitRType[Integer.parseInt(lists[(int) pending.get(j)].get(3))]) {
						eachNumOfUnitRType[Integer.parseInt(lists[(int) pending.get(j)].get(3))]-= Integer.parseInt(lists[(int) pending.get(j)].get(4));
						resourceGiven[(int) pending.get(j)][Integer.parseInt(lists[(int) pending.get(j)].get(3))] += Integer.parseInt(lists[(int) pending.get(j)].get(4));
						lists[(int) pending.get(j)].remove(0);
						lists[(int) pending.get(j)].remove(0);
						lists[(int) pending.get(j)].remove(0);
						lists[(int) pending.get(j)].remove(0);
						lists[(int) pending.get(j)].remove(0);
						cycleIndex.remove(new Integer((int) pending.get(j)));
						pending.remove(new Integer((int) pending.get(j)));
						skip=true;
					}
					else {
						cycleIndex.remove(pending.get(j));
					}
				}
			}
			
			
			// deadlock
			// if dealock find the lowest index from pending and abort it.
			// remove the aborted index from cycleIndex and from running so that it will never again be added to cycleIndex.
			if(deadlock) {
				int remove = (int) Collections.min(pending);

				for( int i=1; i<numOfResourceType+1; i++) {
					eachNumOfUnitRType[i] += resourceGiven[remove][i];
					resourceGiven[remove][i]=0;
				}
					terminated.add(remove);
					aborted.add(remove);
					pending.remove(new Integer(remove));
					running.remove(new Integer(remove));
					cycleIndex.remove(0);
					skip = true;
				}
			
			// if cycleIndex is empty that means we went through all task-number at least once in the pending and deadlock case so refresh the cycleIndex with the running indexes and increment the waitResult of indexes in the pending.
			// increment cycle since we went through all task at least once
			if(cycleIndex.isEmpty()) {
			skip  = true;
			cycle++;
			for( int i=0; i<pending.size(); i++) {
				waitResult[(int) pending.get(i)]++;
			}
			for( int i=0; i< running.size(); i++) {
				cycleIndex.add(running.get(i));
				}
			}
			
			// if all cycleIndex happened to have been in pending than skip the next part since its for non pending tasks
			if( skip) {
				continue;
			}
			
			
			//request;
			//if task-number in the cycleIndex is non pending go through this part of code.
			// lists[cycleIndex] is the task-number  arraylist[task-number].get(0) is the activity 1-4 are the 4 unsigned integer.
			// if remaining resource is greater than requested amount by cycleIndex task-number; subtract the amount of resource asked by this task-number from the resource remaining kept track by eachNumOfUnitRType[task-number].
			// and increment the amount given to this task kept track by resourceGiven[task-number][resource-type].
			// if remaining resource is less than requested amount by cycleIndex, put the cycleIndex into pending.
			// remove the 5 object from lists that represents the line/activity granted now to see the next line of that task.
			if(lists[(int) cycleIndex.get(0)].get(0).contains("request")) {
				if(Integer.parseInt(lists[(int) cycleIndex.get(0)].get(4))<= eachNumOfUnitRType[Integer.parseInt(lists[(int) cycleIndex.get(0)].get(3))]) {
					eachNumOfUnitRType[Integer.parseInt(lists[(int) cycleIndex.get(0)].get(3))]-= Integer.parseInt(lists[(int) cycleIndex.get(0)].get(4));
					resourceGiven[(int)cycleIndex.get(0)][Integer.parseInt(lists[(int) cycleIndex.get(0)].get(3))] += Integer.parseInt(lists[(int) cycleIndex.get(0)].get(4));
					lists[(int) cycleIndex.get(0)].remove(0);
					lists[(int) cycleIndex.get(0)].remove(0);
					lists[(int) cycleIndex.get(0)].remove(0);
					lists[(int) cycleIndex.get(0)].remove(0);
					lists[(int) cycleIndex.get(0)].remove(0);
					cycleIndex.remove(0);
				}
				else {
					pending.add((int) cycleIndex.get(0));
					cycleIndex.remove(0);
				}
			}
			
			// release
			// put the line of release into releaseWaiting and save relwait as next cycle to be used in the next cycle to actually release the resource
			// if after removing the line of release and we see a terminate right after, add the cycle index in terminate and remove it from running to not access it anymore; once not in running, the index will not be added into cycle index to be checked.
			// release all resource the task held
			else if(lists[(int) cycleIndex.get(0)].get(0).contains("release")) {
				relWait =cycle+1;
				releaseWaiting.add(lists[(int) cycleIndex.get(0)].get(0));
				releaseWaiting.add(lists[(int) cycleIndex.get(0)].get(1));
				releaseWaiting.add(lists[(int) cycleIndex.get(0)].get(2));
				releaseWaiting.add(lists[(int) cycleIndex.get(0)].get(3));
				releaseWaiting.add(lists[(int) cycleIndex.get(0)].get(4));
				if(Integer.parseInt(lists[(int) cycleIndex.get(0)].get(2))!=0) {
				}
				lists[(int) cycleIndex.get(0)].remove(0);
				lists[(int) cycleIndex.get(0)].remove(0);
				lists[(int) cycleIndex.get(0)].remove(0);
				lists[(int) cycleIndex.get(0)].remove(0);
				lists[(int) cycleIndex.get(0)].remove(0);
				if(lists[(int) cycleIndex.get(0)].get(0).contains("terminate")) {
					if(Integer.parseInt(lists[(int) cycleIndex.get(0)].get(2))!=0) {
						cycleCount[(int)cycleIndex.get(0)] += Integer.parseInt(lists[(int) cycleIndex.get(0)].get(2));
					}
					for( int i=0; i<numOfResourceType;i++) {
						eachNumOfUnitRType[i] += resourceGiven[(int) cycleIndex.get(0)][i];
						resourceGiven[(int)cycleIndex.get(0)][i] =0;
					}
					terminated.add(cycleIndex.get(0));
					cycleCount[(int)cycleIndex.get(0)] += cycle+1;
					running.remove(new Integer((int) cycleIndex.get(0)));
				}
				cycleIndex.remove(0);
				relWait =cycle+1;
			}
			
			// terminate
			//add the cycle index in terminate and remove it from running to not access it anymore; once not in running, the index will not be added into cycle index to be checked.
			// release all resource the task held
			else if(lists[(int) cycleIndex.get(0)].get(0).contains("terminate")) {
				terminated.add(cycleIndex.get(0));
				for( int i=0; i<numOfResourceType;i++) {
					eachNumOfUnitRType[i] += resourceGiven[(int) cycleIndex.get(0)][i];
					resourceGiven[(int)cycleIndex.get(0)][i] =0;
				}
				running.remove(new Integer((int) cycleIndex.get(0)));
			}
			
			// if there was a delay this "computing is visited to waste cycle and not do anything except remove the computing part from the list. 
			else if(lists[(int) cycleIndex.get(0)].get(0).contains("computing")) {
				lists[(int) cycleIndex.get(0)].remove(0);
				lists[(int) cycleIndex.get(0)].remove(0);
				lists[(int) cycleIndex.get(0)].remove(0);
				lists[(int) cycleIndex.get(0)].remove(0);
				lists[(int) cycleIndex.get(0)].remove(0);
				cycleIndex.remove(0);
			}
			// if cycleIndex is empty that means we went through all task-number at least once in the pending and deadlock case so refresh the cycleIndex with the running indexes and increment the waitResult of indexes in the pending.
			// increment cycle since we went through all task at least once
			if(cycleIndex.isEmpty()) {
			cycle++;
			if(!pending.isEmpty()) {
				for( int i=0; i<pending.size(); i++) {
					waitResult[(int) pending.get(i)]++;
				}
			}
			for( int i=0; i< running.size(); i++) {
				cycleIndex.add(running.get(i));
				}
			}
		}// end of while loop 
		
		
		

		
		
		// Banker
		// clear these arrayList used in fifo to be used  again for Banker
		running.clear();
		cycleIndex.clear();
		pending.clear();
		releaseWaiting.clear();
		computing.clear();
		for( int i=1; i< numOfTask+1; i++) {
			running.add(i);
			cycleIndex.add(i);
		}
		
		// use sequenceOfInitiate to make the array table for max and need
		// max[task-number][resource-type]= maximum amount a task can request for this resource type need[task-number][resource-type] = remaining resource amount this task can ask for this particular resource type
		// sequenceOfInitiate has all the activity + 4unsigned integer saved I use sequenceOfInitiate.get(0-4) to get the task-number, resource-type, and number claimed to make max, need.
		// if number claimed is larger than the initial(initiated) amount of resource-type, then abort the task by putting it in aborted,terminated, and removing it from cycleIndex and running so it wont be visited.
		//  max,need built for one task, remove first  5 obj of sequenceOfInitiate to move on to the next  one 
 		for( int i=0; i< (numOfResourceType*numOfTask); i++) {
			if(Integer.parseInt((String) sequenceOfInitiate.get(4))<= eachNumOfUnitRTypeBanker[Integer.parseInt((String) (sequenceOfInitiate.get(3)))]) {
				max[Integer.parseInt((String) (sequenceOfInitiate.get(1)))][Integer.parseInt((String) (sequenceOfInitiate.get(3)))] = Integer.parseInt((String) sequenceOfInitiate.get(4));
				need[Integer.parseInt((String) (sequenceOfInitiate.get(1)))][Integer.parseInt((String) (sequenceOfInitiate.get(3)))] = Integer.parseInt((String) sequenceOfInitiate.get(4));
			}
			else {
				running.remove(new Integer( Integer.parseInt((String) sequenceOfInitiate.get(1))));
				terminated.add( Integer.parseInt((String) sequenceOfInitiate.get(1)));
				abortedBanker.add( Integer.parseInt((String) sequenceOfInitiate.get(1)));
				abortedBankerOutput.add(sequenceOfInitiate.get(0));
				abortedBankerOutput.add(Integer.parseInt((String) sequenceOfInitiate.get(1)));
				abortedBankerOutput.add(Integer.parseInt((String) sequenceOfInitiate.get(2)));
				abortedBankerOutput.add(Integer.parseInt((String) sequenceOfInitiate.get(3)));
				abortedBankerOutput.add(Integer.parseInt((String) sequenceOfInitiate.get(4)));
				cycleIndex.remove(new Integer( Integer.parseInt((String) sequenceOfInitiate.get(1))));
			}
			sequenceOfInitiate.remove(0);
			sequenceOfInitiate.remove(0);
			sequenceOfInitiate.remove(0);
			sequenceOfInitiate.remove(0);
			sequenceOfInitiate.remove(0);
		}
		
 		// variable initiate
		int cycleBanker=numOfResourceType;
		int relWaitBanker = 0;

		// keep looping while we still have a task not terminated 
		while(!running.isEmpty()) {
			
			// safe array to indicate if any of the task will create a safe state
			for( int i=1; i<numOfTask+1; i++) {
				safe[i] = "true";
				safe[0] = "false";
			}

			boolean skip =false;
			// same release manner as fifo
			if(relWaitBanker==cycleBanker) {
				for(int i=0; i<releaseWaiting.size(); i++) {
					if( releaseWaiting.get(0).equals("release")||releaseWaiting.get(0).equals("request")) {
						eachNumOfUnitRTypeBanker[Integer.parseInt((String) releaseWaiting.get(3))]+=Integer.parseInt((String) releaseWaiting.get(4));
						resourceGivenBanker[Integer.parseInt((String) releaseWaiting.get(1))][Integer.parseInt((String) releaseWaiting.get(3))]-= Integer.parseInt((String) releaseWaiting.get(4));
						releaseWaiting.remove(0);
						releaseWaiting.remove(0);
						releaseWaiting.remove(0);
						releaseWaiting.remove(0);
						releaseWaiting.remove(0);
					}
				}
				relWaitBanker= 0;
			}
			

			
			// delay
			//same delay manner as fifo
			if(Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(2))!=0) {
				computing.add(cycleIndex.get(0));
				waitingTimeBanker[(int) cycleIndex.get(0)] += Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(2));
				int iteration = (Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(2)));
				listsBanker[(int) cycleIndex.get(0)].set(2, "0");
				for( int i=0; i<iteration; i++){
					listsBanker[(int)cycleIndex.get(0)].add(0, "0");
					listsBanker[(int)cycleIndex.get(0)].add(0, "0");
					listsBanker[(int)cycleIndex.get(0)].add(0, "0");
					listsBanker[(int)cycleIndex.get(0)].add(0, "0");
					listsBanker[(int)cycleIndex.get(0)].add(0, "computing");
				}
			}
			
			
			// pending requests
			// first grant the request and increment the  decrement the resource amount remaining and the resource amount need(amount remaining it can ask) by that task
			// second check safety by going through all the running task and its resources.
			// safety check is done with  a double for loop that goes through each need per task and a safe array. The safe array is initially true, but is turned false if any of the task's need exceed's the available resource
			// if any of the task does not change the safe array's value to false that means there is a task whose need for each resource type is less or equal to the available and  the state is safe.
			// if safe grant the pending request and increment the resourceGiven array by how much it asked for but dont touch the need or resource  remaining since it was done already and remove the index from pending and cycleIndex.
			// if not safe give back the need and resource amount remaining that was decremented and just remove the cycleIndex.
			// loop this process for all pending index
		int min=100;
		boolean isSafe =false;
			if(!pending.isEmpty()) {
				for(int i=0; i<pending.size(); i++) {
					eachNumOfUnitRTypeBanker[Integer.parseInt(listsBanker[(int) pending.get(i)].get(3))]-= Integer.parseInt(listsBanker[(int) pending.get(i)].get(4));
					need[Integer.parseInt(listsBanker[(int) pending.get(i)].get(1))][Integer.parseInt(listsBanker[(int) pending.get(i)].get(3))] -= Integer.parseInt(listsBanker[(int) pending.get(i)].get(4));
					for ( int z=0; z<running.size();z++) {
						for ( int j=1; j< numOfResourceType+1; j++) {
							if(need[(int) running.get(z)][j]> eachNumOfUnitRTypeBanker[j]) {
								safe[(int) running.get(z)] = "false";
							}	
							}
						}
					for(int k=0; k< running.size();k++) {
						if(safe[(int) running.get(k)].contains("true")) {
							isSafe = true;
						}
					}
					if(isSafe) {
						resourceGivenBanker[(int)pending.get(i)][Integer.parseInt(listsBanker[(int) pending.get(i)].get(3))] += Integer.parseInt(listsBanker[(int) pending.get(i)].get(4));
						listsBanker[(int) pending.get(i)].remove(0);
						listsBanker[(int) pending.get(i)].remove(0);
						listsBanker[(int) pending.get(i)].remove(0);
						listsBanker[(int) pending.get(i)].remove(0);
						listsBanker[(int) pending.get(i)].remove(0);
						cycleIndex.remove(new Integer((int) pending.get(i)));
						pending.remove(new Integer((int) pending.get(i)));
					}
					else {
						eachNumOfUnitRTypeBanker[Integer.parseInt(listsBanker[(int) pending.get(i)].get(3))]+= Integer.parseInt(listsBanker[(int) pending.get(i)].get(4));
						need[Integer.parseInt(listsBanker[(int) pending.get(i)].get(1))][Integer.parseInt(listsBanker[(int) pending.get(i)].get(3))] += Integer.parseInt(listsBanker[(int) pending.get(i)].get(4));
						cycleIndex.remove(new Integer((int) pending.get(i)));
					}
				}	
			} // end of taking care of pending request

			// if cycleIndex is empty that means we went through all task-number at least once in the pending and deadlock case so refresh the cycleIndex with the running indexes and increment the waitResult of indexes in the pending.
			// increment cycle since we went through all task at least once
			if(cycleIndex.isEmpty()) {
			skip = true;
			cycleBanker++;
			for( int i=0; i<pending.size(); i++) {
				waitResultBanker[(int) pending.get(i)]++;
			}
			for( int i=0; i< running.size(); i++) {
				cycleIndex.add(running.get(i));
				}
			}
			
			// skip the non pending part if all cycleIndex is in pending
			if(skip) {
			continue;
		}
			
			
			
			
			
			//request;
			// same request and safety check as pending request
			// if not safe put the cycleIndex in pending
			min=100;
			for( int i=1; i<numOfTask+1; i++) {
				safe[i] = "true";
				safe[0] = "false";
			}
			isSafe =false;

			if(listsBanker[(int) cycleIndex.get(0)].get(0).contains("request")) {
				eachNumOfUnitRTypeBanker[Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(3))]-= Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(4));
				need[Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(1))][Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(3))] -= Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(4));
				for( int i=0; i< running.size();i++) {
					for ( int j=1; j< numOfResourceType+1; j++) {
						if(need[(int) running.get(i)][j]> eachNumOfUnitRTypeBanker[j]) {
							safe[(int) running.get(i)] = "false";

						}
						}
					}
				
				
				for(int i=0; i< running.size();i++) {
					if(safe[(int) running.get(i)].contains("true")) {
						isSafe = true;
					}
				}
				
				if(isSafe) {
					resourceGivenBanker[(int)cycleIndex.get(0)][Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(3))] += Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(4));
					if(resourceGivenBanker[(int)cycleIndex.get(0)][Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(3))] > max[(int)cycleIndex.get(0)][Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(3))]) {
						relWaitBanker = cycleBanker+1;
						resourceGivenBanker[(int)cycleIndex.get(0)][Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(3))] -= Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(4));
						eachNumOfUnitRTypeBanker[Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(3))]+= Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(4));
						releaseWaiting.add(listsBanker[(int) cycleIndex.get(0)].get(0));
						releaseWaiting.add(listsBanker[(int) cycleIndex.get(0)].get(1));
						releaseWaiting.add(listsBanker[(int) cycleIndex.get(0)].get(2));
						releaseWaiting.add(listsBanker[(int) cycleIndex.get(0)].get(3));
						releaseWaiting.add(Integer.toString(resourceGivenBanker[(int)cycleIndex.get(0)][Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(3))]));
						terminatedBanker.add(Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(1)));
						abortedBanker.add(Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(1)));		
						int remainder = resourceGivenBanker[(int) cycleIndex.get(0)][Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(3))];
						for( int i=0; i<numOfResourceType;i++) {
							eachNumOfUnitRTypeBanker[i] += resourceGivenBanker[(int) cycleIndex.get(0)][i];
							resourceGivenBanker[(int)cycleIndex.get(0)][i] =0;
						}
						abortedBankerOutput.add(cycleBanker);
						abortedBankerOutput.add(Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(1)));
						abortedBankerOutput.add(Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(2)));
						abortedBankerOutput.add(Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(3)));
						abortedBankerOutput.add(Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(4)));
						abortedBankerOutput.add(remainder);
						running.remove(new Integer(Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(1))));
					}
					listsBanker[(int) cycleIndex.get(0)].remove(0);
					listsBanker[(int) cycleIndex.get(0)].remove(0);
					listsBanker[(int) cycleIndex.get(0)].remove(0);
					listsBanker[(int) cycleIndex.get(0)].remove(0);
					listsBanker[(int) cycleIndex.get(0)].remove(0);
					cycleIndex.remove(0);
				}
				else {
					if(!pending.contains(new Integer((int) cycleIndex.get(0)))) {
					pending.add((int) cycleIndex.get(0));
					}
					eachNumOfUnitRTypeBanker[Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(3))]+= Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(4));
					need[Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(1))][Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(3))] += Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(4));
					cycleIndex.remove(0);
				}
			}
			
			// release
			// same release as the one at fifo
			else if(listsBanker[(int) cycleIndex.get(0)].get(0).contains("release")) {
				relWaitBanker =cycle+1;
				need[Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(1))][Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(3))] +=Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(4));
				releaseWaiting.add(listsBanker[(int) cycleIndex.get(0)].get(0));
				releaseWaiting.add(listsBanker[(int) cycleIndex.get(0)].get(1));
				releaseWaiting.add(listsBanker[(int) cycleIndex.get(0)].get(2));
				releaseWaiting.add(listsBanker[(int) cycleIndex.get(0)].get(3));
				releaseWaiting.add(listsBanker[(int) cycleIndex.get(0)].get(4));
				if(Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(2))!=0) {
				}
				listsBanker[(int) cycleIndex.get(0)].remove(0);
				listsBanker[(int) cycleIndex.get(0)].remove(0);
				listsBanker[(int) cycleIndex.get(0)].remove(0);
				listsBanker[(int) cycleIndex.get(0)].remove(0);
				listsBanker[(int) cycleIndex.get(0)].remove(0);
				if(listsBanker[(int) cycleIndex.get(0)].get(0).contains("terminate")) {
					if(Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(2))!=0) {
						cycleCountBanker[(int)cycleIndex.get(0)] += Integer.parseInt(listsBanker[(int) cycleIndex.get(0)].get(2));
					}
					for( int i=0; i<numOfResourceType;i++) {
						eachNumOfUnitRTypeBanker[i] += resourceGivenBanker[(int) cycleIndex.get(0)][i];
						resourceGivenBanker[(int)cycleIndex.get(0)][i] =0;
					}
					terminatedBanker.add(cycleIndex.get(0));
					cycleCountBanker[(int)cycleIndex.get(0)] += cycleBanker+1;
					running.remove(new Integer((int) cycleIndex.get(0)));
				}
				cycleIndex.remove(0);
				relWaitBanker =cycleBanker+1;
			}
			
			// terminate
			// same terminate as fifo
			else if(listsBanker[(int) cycleIndex.get(0)].get(0).contains("terminate")) {
				terminatedBanker.add(cycleIndex.get(0));
				for( int i=0; i<numOfResourceType;i++) {
					eachNumOfUnitRTypeBanker[i] += resourceGivenBanker[(int) cycleIndex.get(0)][i];
					resourceGivenBanker[(int)cycleIndex.get(0)][i] =0;
				}
				running.remove(new Integer((int) cycleIndex.get(0)));
			}
			
			else if(listsBanker[(int) cycleIndex.get(0)].get(0).contains("computing")) {
				listsBanker[(int) cycleIndex.get(0)].remove(0);
				listsBanker[(int) cycleIndex.get(0)].remove(0);
				listsBanker[(int) cycleIndex.get(0)].remove(0);
				listsBanker[(int) cycleIndex.get(0)].remove(0);
				listsBanker[(int) cycleIndex.get(0)].remove(0);
				cycleIndex.remove(0);
			}
			
			// cycleIndex is empty, we went through each task at least once
			// increment cycle and refresh cycleIndex
			// if pending is not empty, increment wait time
			if(cycleIndex.isEmpty()) {
			cycleBanker++;
			if(!pending.isEmpty()) {
				for( int i=0; i<pending.size(); i++) {
					waitResultBanker[(int) pending.get(i)]++;
				}
			}
			for( int i=0; i< running.size(); i++) {
				cycleIndex.add(running.get(i));
				}
			}
		}// end of while loop 
		
		
		

		
		
		
		
		
		
		//output format
		int  totalCycle = 0;
		int totalWait = 0;
		int  totalCycleBanker = 0;
		int totalWaitBanker = 0;
		int size=abortedBanker.size();
		if(!abortedBanker.isEmpty()) {
			for( int i=0; i<size;i++) {
				if( abortedBankerOutput.get(0).toString().contains("initiate")) {
			System.out.println("Banker aborts Task " + abortedBanker.get(i) + " before run begins:");
			System.out.println(" claim for resource " + abortedBankerOutput.get(3)+ " (" +abortedBankerOutput.get(4) + ") exceeds number of units present (" + allData.get(1+(int) abortedBankerOutput.get(3)) + ")");
				}
				else {
					System.out.println("During cycle "+ abortedBankerOutput.get(0) + "-" + ((int)abortedBankerOutput.get(0)+1) + " of Banker's algorithms");
					System.out.println(" Task " + abortedBankerOutput.get(1) + "'s request exceeds its claim; aborted;" +abortedBankerOutput.get(5) + " units available next cycle");
				}
			}
		}
		System.out.println("	FIFO		BANKER'S");
		for( int i=1; i<numOfTask+1; i++) {
		String output = "Task" + i;
		if(aborted.contains(new Integer(i))) {
			output += "	 aborted    ";
		}
		else {
			output += "   " + cycleCount[i] +"  " + waitResult[i] + "  " +(String.format("%.02f",(float)waitResult[i]/(float)cycleCount[i]*100)) + "%"; 

			totalCycle+= cycleCount[i];
			totalWait += waitResult[i];

		}
		
		if( abortedBanker.contains(new Integer(i))) {
			output+= " Task" +i +"  " + "aborted";
		}
		else {
		output += "  Task" + i + " " + cycleCountBanker[i] +"  " + waitResultBanker[i] + "  " +(String.format("%.02f",(float)waitResultBanker[i]/(float)cycleCountBanker[i]*100)) + "%"; 
		totalCycleBanker += cycleCountBanker[i];
		totalWaitBanker += waitResultBanker[i];
		}
		System.out.println(output);
		}
		System.out.println("total   " + totalCycle + "  " + totalWait+ "  " + String.format("%.02f",((float)totalWait/(float)totalCycle*100)) + "%" + "  total " + totalCycleBanker + "  " + totalWaitBanker+ "  " + String.format("%.02f",((float)totalWaitBanker/(float)totalCycleBanker*100)) + "%");
	}// end of resourceManager function
	

	

	
	
	
	
	
	// main method
	public static void main(String[] args) {

		String filename;
		filename = args[0];
		String val = "";
		 Scanner input = newScanner(filename);
		
		
		
		 resourceManager(input);
	}

}
