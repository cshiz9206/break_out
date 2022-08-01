package OwnGameHost;

public enum WallColor {
	GRAY(1, "..\\BreakOut_figure\\wallGRAY.png", 50, "..\\BreakOut_figure\\fragGRAY.png"),
	YELLOW(2, "..\\BreakOut_figure\\wallYELLOW.png", 100, "..\\BreakOut_figure\\fragYELLOW.png"),
	GREEN(3, "..\\BreakOut_figure\\wallGREEN.png", 150, "..\\BreakOut_figure\\fragGREEN.png"),
	ORANGE(4, "..\\BreakOut_figure\\wallORANGE.png", 200, "..\\BreakOut_figure\\fragORANGE.png"),
	RED(5, "..\\BreakOut_figure\\wallRED.png", 250, "..\\BreakOut_figure\\fragRED.png");
	
	int floorNum;
	String path;
	int point;
	String fragPath;
	
	private WallColor(int floorNum, String path, int point, String fragPath) {
		this.floorNum = floorNum;
		this.path = path;
		this.point = point;
		this.fragPath = fragPath;
	}
}
