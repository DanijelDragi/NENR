package lab2;
import java.util.InputMismatchException;

import lab1.CompositeDomain;
import lab1.Domain;
import lab1.DomainElement;
import lab1.IDomain;
import lab1.SimpleDomain;

public class Relations {

	private Relations() {};
	
	public static boolean isUTimesURelation(IFuzzySet set) {
		
		IDomain domain = set.getDomain();
		int numComponents = domain.getNumberOfComponents();
		if(numComponents < 2 || numComponents % 2 != 0) return false;
		else {
			int half = numComponents / 2;
			for(int i = 0; i < half; i++) {
				if(!domain.getComponent(i).equals(domain.getComponent(i + half))) return false;
			}
		}
		return true;
	}
	
	public static boolean isSymmetric(IFuzzySet set) {
		
		if(!isUTimesURelation(set)) return false;
		IDomain domain = set.getDomain();
		int half = domain.getNumberOfComponents() / 2;
		int[] values = new int[domain.getNumberOfComponents()];
		for(int i = 0; i < domain.getCardinality(); i++) {
			DomainElement temp = domain.elementForIndex(i);
			for(int j = 0; j < half; j++) {
				values[j] = temp.getComponentValue(j + half);
				values[j + half] = temp.getComponentValue(j);
			}
			DomainElement tempInvers = new DomainElement(values);
			if(set.getValueAt(temp) != set.getValueAt(tempInvers)) return false;
		}
		return true;
	}
	
	public static boolean isReflexive(IFuzzySet set) {
		
		if(!isUTimesURelation(set)) return false;
		IDomain domain = set.getDomain();
		int half = domain.getNumberOfComponents() / 2;
		SimpleDomain[] uDomains = new SimpleDomain[half];
		for(int i = 0; i < half; i++) {
			uDomains[i] = (SimpleDomain) domain.getComponent(i);
		}
		CompositeDomain u = new CompositeDomain(uDomains);
		
		int[] temp = new int[half * 2];
		for(DomainElement element : u) {
			for(int j = 0; j < half; j++) {
				temp[j] = element.getComponentValue(j);
				temp[j + half] = element.getComponentValue(j);
			}
			if(set.getValueAt(DomainElement.of(temp)) != 1) return false;
		}
		return true;
	}
	
	public static boolean isMaxMinTransitive(IFuzzySet set) {
		
		if(!isUTimesURelation(set)) return false;
		IDomain domain = set.getDomain();
		int half = domain.getNumberOfComponents() / 2;
		SimpleDomain[] uDomains = new SimpleDomain[half];	
		for(int i = 0; i < half; i++) {
			uDomains[i] = (SimpleDomain) domain.getComponent(i);
		}
		CompositeDomain u = new CompositeDomain(uDomains);
		
		int[] tempXZ = new int[half * 2], tempXY = new int[half * 2], tempYZ = new int[half * 2], tempY;
		double xRz, xRy, yRz, min;
		for(DomainElement XZ : domain) {
			xRz = set.getValueAt(XZ);
			tempXZ = XZ.getAllComponentValues();
			for(int j = 0; j < half; j++) {
				tempXY[j] = tempXZ[j];
				tempYZ[j + half] = tempXZ[j + half];
			}			
			for(DomainElement y : u) {
				min = 0;
				tempY = y.getAllComponentValues();
				for(int k = 0; k < half; k++) {
					tempXY[k + half] = tempY[k];
					tempYZ[k] = tempY[k];
				}
				xRy = set.getValueAt(DomainElement.of(tempXY));
				yRz = set.getValueAt(DomainElement.of(tempYZ));
				min = xRy < yRz ? xRy : yRz;
				if(min > xRz) return false;
			}
		}
		return true;
	}
	
	public static IFuzzySet compositionOfBinaryRelations(IFuzzySet set1, IFuzzySet set2) {
		return compositionOfBinaryRelations(set1, set2, 1, 1);
	}
	
	public static IFuzzySet compositionOfBinaryRelations(IFuzzySet set1, IFuzzySet set2, int numComponentsU, int numComponentsW) {
		
		int numComponentsV = set1.getDomain().getNumberOfComponents() - numComponentsU;
		if(set2.getDomain().getNumberOfComponents() - numComponentsW != numComponentsV) throw new InputMismatchException();		
		IDomain domain1 = set1.getDomain();
		IDomain domain2 = set2.getDomain();
		for(int i = 0; i < numComponentsV; i++) {
			if(!domain1.getComponent(i + numComponentsU).equals(domain2.getComponent(i))) throw new InputMismatchException();
		}
		SimpleDomain[] uDomains = new SimpleDomain[numComponentsU];
		SimpleDomain[] vDomains = new SimpleDomain[numComponentsV];
		SimpleDomain[] wDomains = new SimpleDomain[numComponentsW];
		for(int i = 0; i < numComponentsV; i++) {
			vDomains[i] = (SimpleDomain) domain1.getComponent(numComponentsU + i); 
		}
		for(int i = 0; i < numComponentsU; i++) {
			uDomains[i] = (SimpleDomain) domain1.getComponent(i); 
		}
		for(int i = 0; i < numComponentsW; i++) {
			wDomains[i] = (SimpleDomain) domain2.getComponent(numComponentsV + i); 
		}
		
		CompositeDomain v = new CompositeDomain(vDomains);
		CompositeDomain u = new CompositeDomain(uDomains);
		CompositeDomain w = new CompositeDomain(wDomains);
		IDomain uw = Domain.combine(u, w);
		MutableFuzzySet composition = new MutableFuzzySet(Domain.combine(u, w));
		
		int[] tempUW, tempUV = new int[numComponentsU + numComponentsV], tempVW = new int[numComponentsV + numComponentsW];
		double min, max, uRv, vRw;
		for(DomainElement e : uw) {
			min = 0; max = 0;
			tempUW = e.getAllComponentValues();
			for(int i = 0; i < tempUW.length; i++) {
				if (i < numComponentsU) tempUV[i] = tempUW[i];
				else tempVW[i - numComponentsU + numComponentsV] = tempUW[i];
			}
			
			for(DomainElement f : v) {
				for(int i = 0; i < numComponentsV; i++) {
					tempUV[i + numComponentsU] = f.getComponentValue(i);
					tempVW[i] = f.getComponentValue(i);
				}
				uRv = set1.getValueAt(DomainElement.of(tempUV));
				vRw = set2.getValueAt(DomainElement.of(tempVW));
				min = uRv < vRw ? uRv : vRw;
				if(min > max) max = min;
			}
			composition.set(e, max);
		}
		return composition;
	}
	
	public static boolean isFuzzyEquivalence(IFuzzySet set) {
		if(isReflexive(set) && isSymmetric(set) && isMaxMinTransitive(set)) return true;
		return false;
	}
}
