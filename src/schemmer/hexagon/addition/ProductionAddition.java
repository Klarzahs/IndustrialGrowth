package schemmer.hexagon.addition;

import schemmer.hexagon.utils.Ressource;

public abstract class ProductionAddition implements Addition {
	public static class ProductionAdditionChemistryPlant extends ProductionAddition {
		@Override
		public Ressource roundAddition() {
			return new Ressource(1, 0);
		}

		@Override
		public Ressource positionAddition() {
			return new Ressource(0, 0);
		}

		@Override
		public String getName() {
			return "Chem. Pl.";
		}
		
		@Override
		public int getCost() {
			return 2;
		}
	}
	
	public static class ProductionAdditionPowerPlant extends ProductionAddition {
		
		@Override
		public Ressource roundAddition() {
			return new Ressource(0, 0);
		}

		@Override
		public Ressource positionAddition() {
			return new Ressource(1, 0);
		}
		
		@Override
		public String getName() {
			return "Pow. Pl.";
		}
		
		@Override
		public int getCost() {
			return 2;
		}
	}


	public static class ProductionAdditionSweatShop extends ProductionAddition {
	
		@Override
		public Ressource roundAddition() {
			return new Ressource(2, -1);
		}
	
		@Override
		public Ressource positionAddition() {
			return new Ressource(0, 0);
		}
		
		@Override
		public String getName() {
			return "Chem. Pl.";
		}
		
		@Override
		public int getCost() {
			return 3;
		}
	}
}
