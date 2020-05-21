package lab3;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

import lab1.DomainElement;
import lab1.IDomain;
import lab1.SimpleDomain;
import lab2.IBinaryFunction;
import lab2.IFuzzySet;
import lab2.MutableFuzzySet;
import lab2.Operations;

public class Main3 {

	public static void main(String[] args) throws IOException {
		
		
		BoatFuzzyController accelerationController = accelereationController();
		BoatFuzzyController rudderController = rudderController();
		
		//Initialize read/write
		Scanner scanner = new Scanner(System.in);
		Writer writer = new OutputStreamWriter(System.out);
		int[] inputs = new int[6];		//L, D, LK, DK, V, S
		
		//Main loop
		while(true) {
			String line = scanner.nextLine();
			String[] parts;
			if(line.equals("KRAJ")) {
				break;
			}
			else {
				parts = line.split(" ");
				for(int i = 0; i < parts.length; i++) {
					inputs[i] = Integer.parseInt(parts[i]);
				}
			}
			
			String out = String.valueOf(accelerationController.makeDecision(inputs)) + " " + String.valueOf(rudderController.makeDecision(inputs)) + "\n";
			writer.write(out);
			writer.flush();
			
			/*
			rudderController.singleRuleTest(inputs, 4);
			rudderController.rulesBaseTest(inputs);
			*/
		}
		scanner.close();
		return;
	}
	
	@SuppressWarnings("unused")
	public static BoatFuzzyController accelereationController() {
		
		double[] accelerationValues = new double[] {-35, -20, -10, 0, 10, 20, 35};	//A
		IDomain outputDomain = new SimpleDomain(0, 7);									//0,1,2,3,4,5,6
		IDomain distanceRulesDomain = new SimpleDomain(0,5);							//0,1,2,3,4
		IDomain speedRulesDomain = new SimpleDomain(0,5);								//0,1,2,3,4
		IDomain directionRulesDomain = new SimpleDomain(0,2);							//0,1
		
		Defuzzyficator accelerationDefuzzy = new CenterOfAreaDefuzzyficator(accelerationValues);
		
		RulesBase accelerationRules = new RulesBase(outputDomain);
		
		//Choose implication function
		//IBinaryFunction implication = Operations.zadehAnd();
		IBinaryFunction implication = Operations.algebraicProduct();
		
		//Prepare sets representing conditions
		IFuzzySet criticalDistance = new MutableFuzzySet(distanceRulesDomain)
				.set(DomainElement.of(new int[] {0}), 1)
				.set(DomainElement.of(new int[] {1}), 0.3);
		IFuzzySet closeDistance = new MutableFuzzySet(distanceRulesDomain)
				.set(DomainElement.of(new int[] {0}), 0.3)
				.set(DomainElement.of(new int[] {1}), 1)
				.set(DomainElement.of(new int[] {2}), 0.2);
		IFuzzySet mediumDistance = new MutableFuzzySet(distanceRulesDomain)
				.set(DomainElement.of(new int[] {1}), 0.2)
				.set(DomainElement.of(new int[] {2}), 1)
				.set(DomainElement.of(new int[] {3}), 0.2);
		IFuzzySet farDistance = new MutableFuzzySet(distanceRulesDomain)
				.set(DomainElement.of(new int[] {2}), 0.2)
				.set(DomainElement.of(new int[] {3}), 1)
				.set(DomainElement.of(new int[] {4}), 0.2);
		IFuzzySet veryFarDistance = new MutableFuzzySet(distanceRulesDomain)
				.set(DomainElement.of(new int[] {3}), 0.2)
				.set(DomainElement.of(new int[] {4}), 1);
		IFuzzySet anyDistance = new MutableFuzzySet(distanceRulesDomain)
				.set(DomainElement.of(new int[] {0}), 1)
				.set(DomainElement.of(new int[] {1}), 1)
				.set(DomainElement.of(new int[] {2}), 1)
				.set(DomainElement.of(new int[] {3}), 1)
				.set(DomainElement.of(new int[] {4}), 1);
		IFuzzySet notCriticalDistance = SetOperations.union(veryFarDistance, farDistance, mediumDistance);
		
		IFuzzySet backwardsSpeed = new MutableFuzzySet(speedRulesDomain)
				.set(DomainElement.of(new int[] {0}), 1)
				.set(DomainElement.of(new int[] {1}), 0.1);
		IFuzzySet slowSpeed = new MutableFuzzySet(speedRulesDomain)
				.set(DomainElement.of(new int[] {0}), 0.1)
				.set(DomainElement.of(new int[] {1}), 1)
				.set(DomainElement.of(new int[] {2}), 0.2);
		IFuzzySet mediumSpeed = new MutableFuzzySet(speedRulesDomain)
				.set(DomainElement.of(new int[] {1}), 0.2)
				.set(DomainElement.of(new int[] {2}), 1)
				.set(DomainElement.of(new int[] {3}), 0.2);
		IFuzzySet fastSpeed = new MutableFuzzySet(speedRulesDomain)
				.set(DomainElement.of(new int[] {2}), 0.2)
				.set(DomainElement.of(new int[] {3}), 1)
				.set(DomainElement.of(new int[] {4}), 0.1);
		IFuzzySet veryFastSpeed = new MutableFuzzySet(speedRulesDomain)
				.set(DomainElement.of(new int[] {3}), 0.1)
				.set(DomainElement.of(new int[] {4}), 1);
		IFuzzySet anySpeed = new MutableFuzzySet(speedRulesDomain)
				.set(DomainElement.of(new int[] {0}), 1)
				.set(DomainElement.of(new int[] {1}), 1)
				.set(DomainElement.of(new int[] {2}), 1)
				.set(DomainElement.of(new int[] {3}), 1)
				.set(DomainElement.of(new int[] {4}), 1);
		IFuzzySet notSlowSpeed = SetOperations.union(veryFastSpeed, fastSpeed, mediumSpeed);
		
		IFuzzySet goodDirection = new MutableFuzzySet(directionRulesDomain)
				.set(DomainElement.of(new int[] {0}), 0)
				.set(DomainElement.of(new int[] {1}), 1);
		IFuzzySet badDirection = new MutableFuzzySet(directionRulesDomain)
				.set(DomainElement.of(new int[] {0}), 1)
				.set(DomainElement.of(new int[] {1}), 0);
		IFuzzySet anyDirection = new MutableFuzzySet(directionRulesDomain)
				.set(DomainElement.of(new int[] {0}), 1)
				.set(DomainElement.of(new int[] {1}), 1);
		
		IFuzzySet maxBackwardsAccelereation = new MutableFuzzySet(outputDomain)
				.set(DomainElement.of(new int[] {0}), 1);
		IFuzzySet hardBackwardsAccelereation = new MutableFuzzySet(outputDomain)
				.set(DomainElement.of(new int[] {1}), 1);
		IFuzzySet normalBackwardsAccelereation = new MutableFuzzySet(outputDomain)
				.set(DomainElement.of(new int[] {2}), 1);
		IFuzzySet noAccelereation = new MutableFuzzySet(outputDomain)
				.set(DomainElement.of(new int[] {3}), 1);
		IFuzzySet normalForwardsAccelereation = new MutableFuzzySet(outputDomain)
				.set(DomainElement.of(new int[] {4}), 1);
		IFuzzySet hardForwardsAccelereation = new MutableFuzzySet(outputDomain)
				.set(DomainElement.of(new int[] {5}), 1);
		IFuzzySet maxForwardsAccelereation = new MutableFuzzySet(outputDomain)
				.set(DomainElement.of(new int[] {6}), 1);
		
		//F acceleration rules
		accelerationRules.addRule(new Rule(new IFuzzySet[] {anyDistance, anyDistance, anyDistance, anyDistance, backwardsSpeed, anyDirection}, maxForwardsAccelereation, implication));
		accelerationRules.addRule(new Rule(new IFuzzySet[] {SetOperations.union(notCriticalDistance, closeDistance), SetOperations.union(notCriticalDistance, closeDistance), SetOperations.union(notCriticalDistance, closeDistance), SetOperations.union(notCriticalDistance, closeDistance), slowSpeed, goodDirection}, hardForwardsAccelereation, implication));
		accelerationRules.addRule(new Rule(new IFuzzySet[] {notCriticalDistance, notCriticalDistance, notCriticalDistance, notCriticalDistance, mediumSpeed, goodDirection}, normalForwardsAccelereation, implication));
		
		//B acceleration rules
		accelerationRules.addRule(new Rule(new IFuzzySet[] {anyDistance, anyDistance, criticalDistance, criticalDistance, notSlowSpeed, goodDirection}, normalBackwardsAccelereation, implication));
		accelerationRules.addRule(new Rule(new IFuzzySet[] {anyDistance, anyDistance, anyDistance, anyDistance, veryFastSpeed, anyDirection}, hardBackwardsAccelereation, implication));
		
		return new BoatFuzzyController(accelerationRules, accelerationDefuzzy);
	}
	
	@SuppressWarnings("unused")
	public static BoatFuzzyController rudderController() {
		
		IDomain outputDomain = new SimpleDomain(0, 7);									//0,1,2,3,4,5,6
		IDomain distanceRulesDomain = new SimpleDomain(0,5);							//0,1,2,3,4
		IDomain speedRulesDomain = new SimpleDomain(0,5);								//0,1,2,3,4
		IDomain directionRulesDomain = new SimpleDomain(0,2);							//0,1
		double[] rudderValues = new double[] {90, 45, 20, 0, -20, -45, -90};			//K
		
		RulesBase rudderRules = new RulesBase(outputDomain);
		Defuzzyficator rudderDefuzzy = new CenterOfAreaDefuzzyficator(rudderValues);
		
		//Choose implication function
		//IBinaryFunction implication = Operations.zadehAnd();
		IBinaryFunction implication = Operations.algebraicProduct();

		//Prepare sets representing conditions
		IFuzzySet criticalDistance = new MutableFuzzySet(distanceRulesDomain)
				.set(DomainElement.of(new int[] {0}), 1)
				.set(DomainElement.of(new int[] {1}), 0.3);
		IFuzzySet closeDistance = new MutableFuzzySet(distanceRulesDomain)
				.set(DomainElement.of(new int[] {0}), 0.3)
				.set(DomainElement.of(new int[] {1}), 1)
				.set(DomainElement.of(new int[] {2}), 0.2);
		IFuzzySet mediumDistance = new MutableFuzzySet(distanceRulesDomain)
				.set(DomainElement.of(new int[] {1}), 0.2)
				.set(DomainElement.of(new int[] {2}), 1)
				.set(DomainElement.of(new int[] {3}), 0.2);
		IFuzzySet farDistance = new MutableFuzzySet(distanceRulesDomain)
				.set(DomainElement.of(new int[] {2}), 0.2)
				.set(DomainElement.of(new int[] {3}), 1)
				.set(DomainElement.of(new int[] {4}), 0.2);
		IFuzzySet veryFarDistance = new MutableFuzzySet(distanceRulesDomain)
				.set(DomainElement.of(new int[] {3}), 0.2)
				.set(DomainElement.of(new int[] {4}), 1);
		IFuzzySet anyDistance = new MutableFuzzySet(distanceRulesDomain)
				.set(DomainElement.of(new int[] {0}), 1)
				.set(DomainElement.of(new int[] {1}), 1)
				.set(DomainElement.of(new int[] {2}), 1)
				.set(DomainElement.of(new int[] {3}), 1)
				.set(DomainElement.of(new int[] {4}), 1);
		IFuzzySet notCriticalDistance = SetOperations.union(veryFarDistance, farDistance, mediumDistance);
		
		IFuzzySet backwardsSpeed = new MutableFuzzySet(speedRulesDomain)
				.set(DomainElement.of(new int[] {0}), 1)
				.set(DomainElement.of(new int[] {1}), 0.1);
		IFuzzySet slowSpeed = new MutableFuzzySet(speedRulesDomain)
				.set(DomainElement.of(new int[] {0}), 0.1)
				.set(DomainElement.of(new int[] {1}), 1)
				.set(DomainElement.of(new int[] {2}), 0.2);
		IFuzzySet mediumSpeed = new MutableFuzzySet(speedRulesDomain)
				.set(DomainElement.of(new int[] {1}), 0.2)
				.set(DomainElement.of(new int[] {2}), 1)
				.set(DomainElement.of(new int[] {3}), 0.2);
		IFuzzySet fastSpeed = new MutableFuzzySet(speedRulesDomain)
				.set(DomainElement.of(new int[] {2}), 0.2)
				.set(DomainElement.of(new int[] {3}), 1)
				.set(DomainElement.of(new int[] {4}), 0.1);
		IFuzzySet veryFastSpeed = new MutableFuzzySet(speedRulesDomain)
				.set(DomainElement.of(new int[] {3}), 0.1)
				.set(DomainElement.of(new int[] {4}), 1);
		IFuzzySet anySpeed = new MutableFuzzySet(speedRulesDomain)
				.set(DomainElement.of(new int[] {0}), 1)
				.set(DomainElement.of(new int[] {1}), 1)
				.set(DomainElement.of(new int[] {2}), 1)
				.set(DomainElement.of(new int[] {3}), 1)
				.set(DomainElement.of(new int[] {4}), 1);
		IFuzzySet notSlowSpeed = SetOperations.union(veryFastSpeed, fastSpeed, mediumSpeed);
		
		IFuzzySet goodDirection = new MutableFuzzySet(directionRulesDomain)
				.set(DomainElement.of(new int[] {0}), 0)
				.set(DomainElement.of(new int[] {1}), 1);
		IFuzzySet badDirection = new MutableFuzzySet(directionRulesDomain)
				.set(DomainElement.of(new int[] {0}), 1)
				.set(DomainElement.of(new int[] {1}), 0);
		IFuzzySet anyDirection = new MutableFuzzySet(directionRulesDomain)
				.set(DomainElement.of(new int[] {0}), 1)
				.set(DomainElement.of(new int[] {1}), 1);
		
		
		IFuzzySet sharpL = new MutableFuzzySet(outputDomain)
				.set(DomainElement.of(new int[] {0}), 1);
		IFuzzySet hardL = new MutableFuzzySet(outputDomain)
				.set(DomainElement.of(new int[] {1}), 1);
		IFuzzySet mediumL = new MutableFuzzySet(outputDomain)
				.set(DomainElement.of(new int[] {2}), 1);
		IFuzzySet noRudder = new MutableFuzzySet(outputDomain)
				.set(DomainElement.of(new int[] {3}), 1);
		IFuzzySet mediumR = new MutableFuzzySet(outputDomain)
				.set(DomainElement.of(new int[] {4}), 1);
		IFuzzySet hardR = new MutableFuzzySet(outputDomain)
				.set(DomainElement.of(new int[] {5}), 1);
		IFuzzySet sharpR = new MutableFuzzySet(outputDomain)
				.set(DomainElement.of(new int[] {6}), 1);
		
		//Direction correction rules
		rudderRules.addRule(new Rule(new IFuzzySet[] {SetOperations.union(notCriticalDistance, closeDistance), anyDistance, SetOperations.union(notCriticalDistance, closeDistance), anyDistance, anySpeed, badDirection}, sharpL, implication));
		rudderRules.addRule(new Rule(new IFuzzySet[] {criticalDistance, SetOperations.union(notCriticalDistance, closeDistance), criticalDistance, SetOperations.union(notCriticalDistance, closeDistance), anySpeed, badDirection}, sharpR, implication));
		
		//L rudder rules

		//SharpL
		rudderRules.addRule(new Rule(new IFuzzySet[] {notCriticalDistance, criticalDistance, notCriticalDistance, anyDistance, anySpeed, goodDirection}, sharpL, implication));
		rudderRules.addRule(new Rule(new IFuzzySet[] {notCriticalDistance, anyDistance, notCriticalDistance, criticalDistance, anySpeed, goodDirection}, sharpL, implication));
		//HardL
		rudderRules.addRule(new Rule(new IFuzzySet[] {notCriticalDistance, closeDistance, notCriticalDistance, anyDistance, anySpeed, goodDirection}, hardL, implication));
		rudderRules.addRule(new Rule(new IFuzzySet[] {notCriticalDistance, anyDistance, notCriticalDistance, closeDistance, anySpeed, goodDirection}, hardL, implication));
		
		//R rudder rules
		//SharpR
		rudderRules.addRule(new Rule(new IFuzzySet[] {criticalDistance, notCriticalDistance, anyDistance, notCriticalDistance, anySpeed, goodDirection}, sharpR, implication));
		rudderRules.addRule(new Rule(new IFuzzySet[] {anyDistance, notCriticalDistance, criticalDistance, notCriticalDistance, anySpeed, goodDirection}, sharpR, implication));
		//HardR
		rudderRules.addRule(new Rule(new IFuzzySet[] {closeDistance, notCriticalDistance, anyDistance, notCriticalDistance, anySpeed, goodDirection}, hardR, implication));
		rudderRules.addRule(new Rule(new IFuzzySet[] {anyDistance, notCriticalDistance, closeDistance, notCriticalDistance, anySpeed, goodDirection}, hardR, implication));
		
		return new BoatFuzzyController(rudderRules, rudderDefuzzy);
	}
}
