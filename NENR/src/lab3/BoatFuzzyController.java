package lab3;

import lab1.Main;
import lab2.IFuzzySet;

public class BoatFuzzyController implements FuzzyControler{

	private RulesBase rulesBase;
	private Defuzzyficator defuzzyficator;
	
	public BoatFuzzyController(RulesBase rulesBase, Defuzzyficator defuzzyficator){
		this.rulesBase = rulesBase;
		this.defuzzyficator = defuzzyficator;
	}

	@Override
	public int makeDecision(int[] inputs) {
		int[] singletons = parseInputs(inputs);
		
		//System.out.println(Arrays.toString(singletons));
		return defuzzyficator.defuzzyfy(rulesBase.jointConclusion(singletons));
	}
	
	public void singleRuleTest(int[] inputs, int index) {
		int[] singletons = parseInputs(inputs);
		
		IFuzzySet conclusion = rulesBase.singleRuleConclusion(singletons, index);
		Main.printSet(conclusion, "Rule conclusion: ");
		System.out.println("Defuzzy value: " + defuzzyficator.defuzzyfy(conclusion));
	}
	
	public void rulesBaseTest(int[] inputs) {
		int[] singletons = parseInputs(inputs);
		
		IFuzzySet conclusion = rulesBase.jointConclusion(singletons);
		Main.printSet(conclusion, "Rules joint conclusion: ");
		System.out.println("Defuzzy value: " + defuzzyficator.defuzzyfy(conclusion) + "\n");
	}
	
	private int[] parseInputs(int[] inputs) {
		int[] singletons = new int[inputs.length];
		for(int i = 0; i < 4; i++) {
			if(inputs[i] < Distance.Critical.distance) singletons[i] = 0;
			else if(inputs[i] < Distance.Close.distance) singletons[i] = 1;
			else if(inputs[i] < Distance.Medium.distance) singletons[i] = 2;
			else if(inputs[i] < Distance.Far.distance) singletons[i] = 3;
			else if(inputs[i] < Distance.VeryFar.distance) singletons[i] = 4;
			else singletons[i] = -1;	//Error
		}
		if(inputs[4] < Speed.Backworads.speed) singletons[4] = 0;
		else if(inputs[4] < Speed.Slow.speed) singletons[4] = 1;
		else if(inputs[4] < Speed.Medium.speed) singletons[4] = 2;
		else if(inputs[4] < Speed.Fast.speed) singletons[4] = 3;
		else if(inputs[4] < Speed.VeryFast.speed) singletons[4] = 4;
		else singletons[4] = -1;	//Error
		
		singletons[5] = inputs[5];
		return singletons;
	}
}
