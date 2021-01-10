import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class US_elections {
	
	/**
	 * This method Uses a greedy algorithm and outputs the minimum number of Votes that Biden requires to win the US Election. 
	 * @param num_states
	 * @param delegates
	 * @param votes_Biden
	 * @param votes_Trump
	 * @param votes_Undecided
	 * @return minimum votes required for Biden to win the election or -1 if there is no chance of Biden to win.
	 */
	public static int solution(int num_states, int[] delegates, int[] votes_Biden, int[] votes_Trump, int[] votes_Undecided){

		if(num_states<=0) {
			return -1;
		}
		if(delegates ==null || votes_Biden ==null || votes_Trump == null || votes_Undecided == null) {
			throw new IllegalArgumentException("InValid Input");
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//setting up the data structures
		int index= 0;
		ArrayList<LinkedList<Integer>> sortedByDelegatesArray = new ArrayList<>();
		int trumpDelegates=0;
		int bidenDelegates=0;
		int state_delegates=0;
		for (int i=0; i<num_states; i++) {
			int delegate= delegates[i];
			int votesBiden= votes_Biden[i];
			int votesTrump= votes_Trump[i];
			int votesUndecided= votes_Undecided[i];
			if(votesUndecided==0.0 || votesUndecided==0) {
				if(votesBiden > votesTrump) { 	//Biden has more votes and 0 undecided votes
					bidenDelegates += delegate;					
				}
				else {	//Trump has more or equal votes and 0 undecided votes
					trumpDelegates += delegate;					
				}
			}
			else { //for guarenteed wins i.e sample input 2
				if(votesBiden - votesTrump > votesUndecided) { //Biden win is guaranteed 
					bidenDelegates += delegate;
				}
				else if (votesTrump - votesBiden >= votesUndecided){	//Trump win is guaranteed
					trumpDelegates += delegate;
				}

				else {	//when there is a chance for biden to win

					LinkedList<Integer> stateData = new LinkedList<>();
					stateData.add(delegate);
					stateData.add(votesBiden);
					stateData.add(votesTrump);
					stateData.add(votesUndecided);
					sortedByDelegatesArray.add(index, stateData);
					index++;		
				}

			}	
		}
		///////////////////////////////////////////////////////////////////SORTING THE DATA OF THE STATES////////////////////////////////////////////////////////////////////////

		int totalDelegates = 0;
		//calculation total delegates
		for(int i=0; i < delegates.length; i++) {
			totalDelegates += delegates[i];
		}
		if(totalDelegates > 2016) {
			throw new IllegalArgumentException("Total delegates of all the states cannot be more than 2016");
		}
		if(!sortedByDelegatesArray.isEmpty()) {
			sortedByDelegatesArray = mergesortByDecendingDelegates(sortedByDelegatesArray);
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		int minDelegatesNeededToWin = (totalDelegates/2) +1;
		int minVotesByDecendingDelegates=0;


		for (int i = 0; i < index; i++ ) {

			state_delegates = sortedByDelegatesArray.get(i).get(0); 	//delegates
			int bidenVotes = sortedByDelegatesArray.get(i).get(1);		//votes for Biden
			int trumpVotes = sortedByDelegatesArray.get(i).get(2);		//votes for Trump
			int undecidedVotes = sortedByDelegatesArray.get(i).get(3);	//undecided people/votes

			////////////////////////////////////////////////////////////////MAIN LOGIC///////////////////////////////////////////////////////////////////////////

			if(bidenDelegates >= minDelegatesNeededToWin) {
				break;
			}
			if (undecidedVotes != 0) {

				int votesRemaining = undecidedVotes;
				int votes=0;

				for(int j=0; j < undecidedVotes; j++) {

					votesRemaining--;
					bidenVotes++;
					votes+=1;

					if(bidenVotes  > (trumpVotes + votesRemaining)) {

						bidenDelegates += state_delegates;
						minVotesByDecendingDelegates += votes;

						if(bidenDelegates >= minDelegatesNeededToWin) {
							break;
						}	
						break;
					}

				}

			}

		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		int minVotesByAcendingVotes = solutionByVotes(num_states, delegates, votes_Biden, votes_Trump, votes_Undecided,totalDelegates);
		int minVotesByDecendingRatios = solutionByRatio(num_states, delegates, votes_Biden, votes_Trump, votes_Undecided, totalDelegates);
		//		System.out.println("BY DELEGATES: "+ minVotesByDecendingDelegates);
		//		System.out.println("BY VOTES: "+ minVotesByAcendingVotes);
		//		System.out.println("BY RATIOS: "+ minVotesByDecendingRatios);

		if(bidenDelegates >= minDelegatesNeededToWin) {

			return minVotes(minVotesByDecendingDelegates, minVotesByAcendingVotes, minVotesByDecendingRatios);

		}
		return -1;
	}
	/**
	 * This helper method sorts the Data (US states) in an descending order of Delegates
	 * @param list1
	 * @param list2
	 * @return sorted list of states by Delegates
	 */
	private static ArrayList<LinkedList<Integer>> mergeByDecendingDelegates(ArrayList<LinkedList<Integer>> list1, ArrayList<LinkedList<Integer>> list2){
		ArrayList<LinkedList<Integer>> list= new ArrayList<LinkedList<Integer>>();

		while (!list1.isEmpty() && !list2.isEmpty()) {
			LinkedList<Integer> element= list1.get(0);
			LinkedList<Integer> toCompare = list2.get(0);

			if(element.get(0).compareTo(toCompare.get(0)) > 0){
				list.add(list1.remove(0));
			}
			else {
				if((element.get(0) == (toCompare.get(0)) && element.get(3).compareTo(toCompare.get(3)) < 0)) {
					list.add(list1.remove(0));
				}
				else {
					list.add(list2.remove(0));					
				}
			}	
		} 	
		while(!list1.isEmpty()) {
			list.add(list1.remove(0));
		}
		while(!list2.isEmpty()) {
			list.add(list2.remove(0));
		}
		return list;
	}


	private static ArrayList<LinkedList<Integer>> mergesortByDecendingDelegates(ArrayList<LinkedList<Integer>> list){
		if(list.size()==1) {
			return  list;
		}
		else {
			int mid= (list.size())/2;
			ArrayList<LinkedList<Integer>> list1=  new ArrayList<LinkedList<Integer>>();
			for(int i=0; i < (mid); i++) {
				list1.add(list.get(i));
			}
			ArrayList<LinkedList<Integer>> list2=  new ArrayList<LinkedList<Integer>>();
			for(int i=mid; i < list.size(); i++) {
				list2.add(list.get(i));
			}
			list1= mergesortByDecendingDelegates(list1);
			list2= mergesortByDecendingDelegates(list2);
			return (ArrayList<LinkedList<Integer>>) mergeByDecendingDelegates(list1, list2);
		}
	}
	///////////////////////////////////////////////////////////METHODS FOR VOTES APPROACH////////////////////////////////////////////////////////////////////////////////
	/**
	 * This helper method sorts the Data (US States) in an ascending order of votes
	 * @param list1
	 * @param list2
	 * @return sorted list of states by votes
	 */
	private static ArrayList<LinkedList<Integer>> mergeByAcendingVotes(ArrayList<LinkedList<Integer>> list1, ArrayList<LinkedList<Integer>> list2){
		ArrayList<LinkedList<Integer>> list= new ArrayList<LinkedList<Integer>>();

		while (!list1.isEmpty() && !list2.isEmpty()) {
			LinkedList<Integer> element= list1.get(0);
			LinkedList<Integer> toCompare = list2.get(0);

			if(element.get(3).compareTo(toCompare.get(3)) < 0){
				list.add(list1.remove(0));
			}
			else {
				if((element.get(3) == (toCompare.get(3)) && element.get(0).compareTo(toCompare.get(0)) > 0)) {
					list.add(list1.remove(0));
				}
				else {
					list.add(list2.remove(0));					
				}
			}	
		} 	
		while(!list1.isEmpty()) {
			list.add(list1.remove(0));
		}
		while(!list2.isEmpty()) {
			list.add(list2.remove(0));
		}
		return list;
	}


	private static ArrayList<LinkedList<Integer>> mergesortByAcendingVotes(ArrayList<LinkedList<Integer>> list){
		if(list.size()==1) {
			return  list;
		}
		else {
			int mid= (list.size())/2;
			ArrayList<LinkedList<Integer>> list1=  new ArrayList<LinkedList<Integer>>();
			for(int i=0; i < (mid); i++) {
				list1.add(list.get(i));
			}
			ArrayList<LinkedList<Integer>> list2=  new ArrayList<LinkedList<Integer>>();
			for(int i=mid; i < list.size(); i++) {
				list2.add(list.get(i));
			}
			list1= mergesortByAcendingVotes(list1);
			list2= mergesortByAcendingVotes(list2);
			return (ArrayList<LinkedList<Integer>>) mergeByAcendingVotes(list1, list2);
		}
	}
	/**
	 * This method uses Greedy algorithm to output the minimum votes required for 
	 * Biden to win the election in regards to the states being sorted by an ascending order of votes.
	 * @param num_states
	 * @param delegates
	 * @param votes_Biden
	 * @param votes_Trump
	 * @param votes_Undecided
	 * @param totalDelegates
	 * @return minimum votes required for Biden to win the US election or -1 if there is no chance of Biden to win. 
	 */
	public static int solutionByVotes(int num_states, int[] delegates, int[] votes_Biden, int[] votes_Trump, int[] votes_Undecided, int totalDelegates){

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//setting up the data structures
		int index= 0;
		ArrayList<LinkedList<Integer>> sortedByVotesArray = new ArrayList<>();
		int trumpDelegates=0;
		int bidenDelegates=0;
		int state_delegates=0;
		for (int i=0; i<num_states; i++) {
			int delegate= delegates[i];
			int votesBiden= votes_Biden[i];
			int votesTrump= votes_Trump[i];
			int votesUndecided= votes_Undecided[i];

			if(votesUndecided==0.0 || votesUndecided==0) {
				if(votesBiden > votesTrump) { 	//Biden has more votes and 0 undecided votes
					bidenDelegates += delegate;					
				}
				else {	//Trump has more or equal votes and 0 undecided votes
					trumpDelegates += delegate;					
				}
			}
			else { //for guarenteed wins i.e sample input 2
				if(votesBiden - votesTrump > votesUndecided) { //Biden win is guaranteed 
					bidenDelegates += delegate;
				}
				else if (votesTrump - votesBiden >= votesUndecided){	//Trump win is guaranteed
					trumpDelegates += delegate;
				}

				else {	//when there is a chance for biden to win

					LinkedList<Integer> stateData = new LinkedList<>();
					stateData.add(delegate);
					stateData.add(votesBiden);
					stateData.add(votesTrump);
					stateData.add(votesUndecided);
					sortedByVotesArray.add(index, stateData);
					index++;		
				}

			}
		}
		///////////////////////////////////////////////////////////////////SORTING THE DATA OF THE STATES////////////////////////////////////////////////////////////////////////
		if(!sortedByVotesArray.isEmpty()) {
			sortedByVotesArray = mergesortByAcendingVotes(sortedByVotesArray);
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		int minDelegatesNeededToWin = (totalDelegates/2) +1;
		int minVotes=0;

		for (int i = 0; i < index; i++ ) {

			state_delegates = sortedByVotesArray.get(i).get(0); 	//delegates
			int bidenVotes = sortedByVotesArray.get(i).get(1);		//votes for Biden
			int trumpVotes = sortedByVotesArray.get(i).get(2);		//votes for Trump
			int undecidedVotes = sortedByVotesArray.get(i).get(3);	//undecided people/votes

			////////////////////////////////////////////////////////////////MAIN LOGIC///////////////////////////////////////////////////////////////////////////
			if(bidenDelegates >= minDelegatesNeededToWin) {
				return minVotes;
			}
			if (undecidedVotes != 0) {

				int votesRemaining = undecidedVotes;
				int votes=0;


				for(int j=0; j < undecidedVotes; j++) {

					votesRemaining--;
					bidenVotes++;
					votes+=1;
					if(bidenVotes  > (trumpVotes + votesRemaining)) {

						bidenDelegates += state_delegates;
						minVotes += votes;

						if(bidenDelegates >= minDelegatesNeededToWin) {
							return minVotes;
						}	
						break;
					}
				}

			}

		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if(bidenDelegates >= minDelegatesNeededToWin) {
			return minVotes;
		}
		return -1;
	}

	///////////////////////////////////////////////////////////METHODS FOR RATIO APPROACH////////////////////////////////////////////////////////////////////////////////
	/**
	 * This helper method sorts the Data (US states) in an descending order of ratio of (Delegates/minVotesToWin) which corresponds to how valuable a vote is
	 * in regards to the delegates per state.
	 * @param list1
	 * @param list2
	 * @return sorted list of states by Descending Ratio 
	 */
	private static ArrayList<LinkedList<Double>> mergeByDecendingRatios(ArrayList<LinkedList<Double>> list1, ArrayList<LinkedList<Double>> list2){
		ArrayList<LinkedList<Double>> list= new ArrayList<LinkedList<Double>>();

		while (!list1.isEmpty() && !list2.isEmpty()) {
			LinkedList<Double> element= list1.get(0);
			LinkedList<Double> toCompare = list2.get(0);

			if(element.get(4).compareTo(toCompare.get(4)) > 0){
				list.add(list1.remove(0));
			}
			else {
				if((element.get(4) == (toCompare.get(4)) && element.get(0).compareTo(toCompare.get(0)) < 0)) {
					list.add(list1.remove(0));
				}
				else {
					list.add(list2.remove(0));					
				}
			}	
		} 	
		while(!list1.isEmpty()) {
			list.add(list1.remove(0));
		}
		while(!list2.isEmpty()) {
			list.add(list2.remove(0));
		}
		return list;
	}


	private static ArrayList<LinkedList<Double>> mergesortByDecendingRatios(ArrayList<LinkedList<Double>> list){
		if(list.size()==1) {
			return  list;
		}
		else {
			int mid= (list.size())/2;
			ArrayList<LinkedList<Double>> list1=  new ArrayList<LinkedList<Double>>();
			for(int i=0; i < (mid); i++) {
				list1.add(list.get(i));
			}
			ArrayList<LinkedList<Double>> list2=  new ArrayList<LinkedList<Double>>();
			for(int i=mid; i < list.size(); i++) {
				list2.add(list.get(i));
			}
			list1= mergesortByDecendingRatios(list1);
			list2= mergesortByDecendingRatios(list2);
			return (ArrayList<LinkedList<Double>>) mergeByDecendingRatios(list1, list2);
		}
	}


	/**
	 * This method uses Greedy algorithm to output the minimum votes required for 
	 * Biden to win the election in regards to the states being sorted by an Descending order of Ratio of (Delegates/minVotesToWin).
	 * @param num_states
	 * @param delegates
	 * @param votes_Biden
	 * @param votes_Trump
	 * @param votes_Undecided
	 * @param totalDelegates
	 * @return minimum votes required for Biden to win the US election or -1 if there is no chance of Biden to win.
	 */
	public static int solutionByRatio(int num_states, int[] delegates, int[] votes_Biden, int[] votes_Trump, int[] votes_Undecided, int totalDelegates){

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//setting up the data structures
		int index= 0;
		ArrayList<LinkedList<Double>> sortedByRatioArray = new ArrayList<>();
		int trumpDelegates=0;
		int bidenDelegates=0;
		double state_delegates=0.0;

		for (int i=0; i<num_states; i++) {
			double delegate= delegates[i];
			double votesBiden= votes_Biden[i];
			double votesTrump= votes_Trump[i];
			double votesUndecided= votes_Undecided[i];

			if(votesUndecided==0.0 || votesUndecided==0) {
				if(votesBiden > votesTrump) { 	//Biden has more votes and 0 undecided votes
					bidenDelegates += delegate;					
				}
				else {	//Trump has more or equal votes and 0 undecided votes
					trumpDelegates += delegate;					
				}
			}

			else { //for guarenteed wins i.e sample input 2
				if(votesBiden - votesTrump > votesUndecided) { //Biden win is guaranteed 
					bidenDelegates += delegate;
				}
				else if (votesTrump - votesBiden >= votesUndecided){	//Trump win is guaranteed
					trumpDelegates += delegate;
				}

				else {	//when there is a chance for biden to win

					LinkedList<Double> stateData = new LinkedList<>();
					stateData.add(delegate);
					stateData.add(votesBiden);
					stateData.add(votesTrump);
					stateData.add(votesUndecided);

					double minVotesNeededToWin =  ((((int)votesBiden + (int)votesTrump + (int)votesUndecided ) / 2) + 1) - votesBiden;
					double ratio = (double)delegate / minVotesNeededToWin;
					stateData.add(ratio);
					sortedByRatioArray.add(index, stateData);
					index++;		
				}

			}

		}
		///////////////////////////////////////////////////////////////////SORTING THE DATA OF THE STATES////////////////////////////////////////////////////////////////////////
		if(!sortedByRatioArray.isEmpty()) {
			sortedByRatioArray = mergesortByDecendingRatios(sortedByRatioArray);			
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		int minDelegatesNeededToWin = (totalDelegates/2) +1;
		int minVotes=0;

		if(bidenDelegates >= minDelegatesNeededToWin) {
			return minVotes;
		}

		for (int i = 0; i <index; i++ ) {

			state_delegates = sortedByRatioArray.get(i).get(0); 		//delegates
			double bidenVotes = sortedByRatioArray.get(i).get(1);		//votes for Biden
			double trumpVotes = sortedByRatioArray.get(i).get(2);		//votes for Trump
			double undecidedVotes = sortedByRatioArray.get(i).get(3);	//undecided people/votes

			////////////////////////////////////////////////////////////////MAIN LOGIC///////////////////////////////////////////////////////////////////////////
			if(bidenDelegates >= minDelegatesNeededToWin) {
				return minVotes;
			}
			if (undecidedVotes != 0) {

				int votesRemaining = (int) undecidedVotes;
				int votes=0;

				for(int j=0; j < undecidedVotes; j++) {

					votesRemaining--;
					bidenVotes++;
					votes+=1;
					if(bidenVotes  > (trumpVotes + votesRemaining)) {

						bidenDelegates += state_delegates;
						minVotes += votes;

						if(bidenDelegates >= minDelegatesNeededToWin) {
							return minVotes;
						}	
						break;
					}
				}

			}

		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if(bidenDelegates >= minDelegatesNeededToWin) {
			return minVotes;
		}
		return -1;
	}
	/**
	 * Helper method that compares all the minimum votes corresponding to each of the three algorithms 
	 * @param a
	 * @param b
	 * @param c
	 * @return the lowest votes required to win the US election out of the three algorithms.
	 */
	private static int minVotes(int a, int b, int c) {
		ArrayList<Integer> votesByDifferentMethods = new ArrayList<>();
		votesByDifferentMethods.add(a);
		votesByDifferentMethods.add(b);
		votesByDifferentMethods.add(c);
		return Collections.min(votesByDifferentMethods);
	}

	public static void main(String[] args) {
		 
		try {
			final long startTime = System.currentTimeMillis();
			String path = "Testing.txt";//args[0];
			File myFile = new File(path);
			Scanner sc = new Scanner(myFile);
			int num_states = sc.nextInt();
			int[] delegates = new int[num_states];
			int[] votes_Biden = new int[num_states];
			int[] votes_Trump = new int[num_states];
			int[] votes_Undecided = new int[num_states];	
			for (int state = 0; state<num_states; state++){
				delegates[state] =sc.nextInt();
				votes_Biden[state] = sc.nextInt();
				votes_Trump[state] = sc.nextInt();
				votes_Undecided[state] = sc.nextInt();
			}
			sc.close();
			int answer = solution(num_states, delegates, votes_Biden, votes_Trump, votes_Undecided);
			System.out.println(answer);
			final long endTime = System.currentTimeMillis();
			System.out.println("Total execution time: " + (endTime - startTime) + "ms");
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

}

