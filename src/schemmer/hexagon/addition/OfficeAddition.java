package schemmer.hexagon.addition;

import schemmer.hexagon.utils.Ressource;

public abstract class OfficeAddition implements Addition{
	public static class OfficeAdditionCentre extends OfficeAddition {
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
			return "Centre";
		}
	}
	
	public static class OfficeAdditionEmployeeHousing extends OfficeAddition {
		
		@Override
		public Ressource roundAddition() {
			return new Ressource(-2, 3);
		}

		@Override
		public Ressource positionAddition() {
			return new Ressource(0,0);
		}
		
		@Override
		public String getName() {
			return "Empl. Hous.";
		}
	}
	
	public static class OfficeAdditionCubicle extends OfficeAddition {
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
			return "Cubicle";
		}
	}
}