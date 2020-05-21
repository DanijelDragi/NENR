package lab3;

import java.util.ArrayList;

import lab1.DomainElement;
import lab1.IDomain;
import lab2.IFuzzySet;
import lab2.MutableFuzzySet;

public class RulesBase {
	
	private ArrayList<Rule> rules = new ArrayList<Rule>();
	private IDomain outputDomain;
	
	public RulesBase(IDomain outputDomain) {
		this.outputDomain = outputDomain;
	}
	
	public void addRule(Rule rule) {
		rules.add(rule);
	}
	
	public boolean deleteRule(Rule rule) {
		return rules.remove(rule);
	}
	
	public Rule deleteRule(int index) {
		return rules.remove(index);
	}
	
	public IFuzzySet singleRuleConclusion(int[] inputs, int index) {
		return rules.get(index).conclusion(inputs);
	}
	
	public IFuzzySet jointConclusion(int[] inputs) {
		
		MutableFuzzySet conclusion = new MutableFuzzySet(outputDomain);
		
		IFuzzySet[] localConclusions = new IFuzzySet[rules.size()];
		for(int i = 0; i < rules.size(); i++) {
			localConclusions[i] = rules.get(i).conclusion(inputs);
		}
		
		double max;
		for(DomainElement element : outputDomain) {
			max = 0;
			for(IFuzzySet local : localConclusions) {
				if(local.getValueAt(element) > max) max = local.getValueAt(element);
			}
			conclusion.set(element, max);
		}
		
		return conclusion;
	}
}
