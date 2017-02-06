package schemmer.hexagon.addition;

import schemmer.hexagon.utils.Ressource;

public abstract class SalesAddition implements Addition{
	public static class SalesAdditionStore extends SalesAddition {
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
			return "Store";
		}

		@Override
		public int getCost() {
			return 2;
		}
	}
	
	public static class SalesAdditionManagement extends SalesAddition {
		
		@Override
		public Ressource roundAddition() {
			return new Ressource(0,0);
		}

		@Override
		public Ressource positionAddition() {
			return new Ressource(-2, 3);
		}
		
		@Override
		public String getName() {
			return "Sales Man.";
		}
		
		@Override
		public int getCost() {
			return 7;
		}
	}
	
	public static class SalesAdditionMall extends SalesAddition {
		@Override
		public Ressource roundAddition() {
			return new Ressource(0, 0);
		}

		@Override
		public Ressource positionAddition() {
			return new Ressource(2, 1);
		}

		@Override
		public String getName() {
			return "Mall";
		}
		
		@Override
		public int getCost() {
			return 7;
		}
	}


}