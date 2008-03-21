package crisp.planner.lazygp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crisp.planningproblem.Problem;
import de.saar.chorus.term.Compound;
import de.saar.chorus.term.Term;

public class AtomTable {
	private List<Compound> indexToInstance;
	//private Map<Compound,Integer> instanceToIndex;
	
	private Map<String,Map<Compound,Integer>> labelToInstanceToIndex;
	
	private int nextIndex;
	
	
	public AtomTable(Problem problem) {
		indexToInstance = new ArrayList<Compound>();
		//instanceToIndex = new HashMap<Compound, Integer>();
		labelToInstanceToIndex = new HashMap<String, Map<Compound,Integer>>();
		nextIndex = 0;
		//this.problem = problem;
		
		
		/*
		// translate problem goal state into goalState array
		List<Literal> goals = problem.getGoal().getGoalList(problem);
		goalState = new boolean[goals.size()];
		for( Literal goal : goals ) {
			goalState[nextIndex] = goal.getPolarity();
			add((Compound) goal.getAtom());
		}
		*/
		
		
		//System.err.println("init: " + nextIndex + " atoms.");
	}

	
	int getIndexForAtom(Compound a) {
		Map<Compound,Integer> instanceToInt = labelToInstanceToIndex.get(a.getLabel());
		
		if( instanceToInt == null ) {
			return -1;
		} else {
			Integer ret = instanceToInt.get(a);

			if( ret == null ) {
				return -1;
			} else {
				return ret;
			}
		}
	}
	
	
	public Compound get(int i) {
		return indexToInstance.get(i);
	}
	
	public void add(Compound a) {
		String label = a.getLabel();
		
		Map<Compound,Integer> instanceToInt = labelToInstanceToIndex.get(label);
		if( instanceToInt == null ) {
			instanceToInt = new HashMap<Compound, Integer>();
			labelToInstanceToIndex.put(label, instanceToInt);
		}
		
		
		indexToInstance.add(a);
		instanceToInt.put(a, nextIndex);
		
		// System.err.println("Atom table: add " + a + " with index " + nextIndex);
		
		nextIndex++;
	}
	
	public int size() {
		return nextIndex;
	}


	public byte[] setTrueLiterals(List<Term> list) {
		for( Term c : list ) {
			ensureAtomKnown((Compound) c);
		}
		
		byte[] ret = new byte[nextIndex];
		for( Term c : list ) {
			ret[getIndexForAtom((Compound) c)] = State.LIT_POSITIVE;
		}
		
		return ret;
	}
	
	private void ensureAtomKnown(Compound c) {
		if( getIndexForAtom(c) == -1 ) {
			add(c);
		}
	}
	
	public int countGoalMismatches(boolean[] trueAtoms, String pred) {
		int ret = 0;
		
		for( int i = 0; i < trueAtoms.length; i++ ) {
			if( trueAtoms[i]) {
				if( get(i).getLabel().equals(pred)) {
					ret++;
				}
			}
		}
		
		return ret;
	}



}