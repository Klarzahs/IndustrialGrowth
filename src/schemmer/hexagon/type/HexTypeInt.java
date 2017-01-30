package schemmer.hexagon.type;

public enum HexTypeInt {
	TYPE_NONE(0),
	TYPE_SALES(1),
	TYPE_PRODUCTION(2),
	TYPE_MEDIA(3),
	TYPE_OFFICE(4),
    ;
	
	private final int value;
    private HexTypeInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
