package OwnGameHost;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Wall extends JLabel {
	Ball ball;
	WallColor color;
	
	public Wall(int maxWidth, int maxHeight) {
		ImageIcon ii = new ImageIcon(WallColor.GRAY.path);
		setIcon(ii);
		setSize(ii.getIconWidth(), ii.getIconHeight());
	}
	
	public void scoring() {
		Bar.score += color.point;
	}
}
