package schemmer.hexagon.addition;

import schemmer.hexagon.utils.Ressource;

public abstract class MediaAddition implements Addition{
	public static class MediaAdditionInternet extends MediaAddition {
		@Override
		public Ressource roundAddition() {
			return new Ressource(1, 1);
		}

		@Override
		public Ressource positionAddition() {
			return new Ressource(0, 0);
		}

		@Override
		public String getName() {
			return "Internet";
		}
		
		@Override
		public int getCost() {
			return 5;
		}
	}
	
	public static class MediaAdditionPaper extends MediaAddition {
		
		@Override
		public Ressource roundAddition() {
			return new Ressource(0, 0);
		}

		@Override
		public Ressource positionAddition() {
			return new Ressource(1, 1);
		}
		
		@Override
		public String getName() {
			return "Paper";
		}
		
		@Override
		public int getCost() {
			return 5;
		}
	}


}
