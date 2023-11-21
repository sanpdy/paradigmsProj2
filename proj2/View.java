import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import javax.swing.JButton;
import java.awt.Color;

class View extends JPanel {
	JButton b1;
	JButton b2;
	int scroll_x = 0;
	int scroll_y = 0;
	BufferedImage[] images;
	Model model;
	static int time = 0;

	View(Controller c, Model m, Game game) {
		// Make a button
		b1 = new JButton("SAVE");
		b1.addActionListener(c);
		b1.setActionCommand("SAVE");
		b1.setFocusable(false); // Disable focus for SAVE button
		this.add(b1);

		b2 = new JButton("LOAD");
		b2.addActionListener(c);
		b2.setActionCommand("LOAD");
		b2.setFocusable(false); // Disable focus for LOAD button
		this.add(b2);

		// Link up to other objects
		c.setView(this);
		model = m;

		// Send mouse events to the controller
		this.addMouseListener(c);

		// Load the images
		this.images = new BufferedImage[Game.THINGS.length];

		for (int i = 0; i < Game.THINGS.length; i++) {
			try {

				String filename = "images/" + Game.THINGS[i] + ".png";
				this.images[i] = ImageIO.read(new File(filename));

			} catch (Exception e) {
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
	}

	int getScrollX() {
		return scroll_x;
	}

	int getScrollY() {
		return scroll_y;
	}

	public void paintComponent(Graphics g) {
		time++;
		// Clear the background
		g.setColor(new Color(55, 128, 47));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		int w = this.images[model.selectedThing].getWidth();
		int h = this.images[model.selectedThing].getHeight();

		int imageW = (200 - w) / 2;
		int imageH = (200 - h) / 2;

		// Draw all the things from the ArrayList
		for (Thing thing : model.things) {
			int thingImageIndex = thing.kind;
			int thingImageWidth = this.images[thingImageIndex].getWidth();
			int thingImageHeight = this.images[thingImageIndex].getHeight();
			g.drawImage(images[thingImageIndex], (thing.getX() - thingImageWidth / 2) - scroll_x,
					thing.getY(time) - thingImageHeight - scroll_y, null);
		}

		// Draw purple square at the top right
		g.setColor(new Color(150, 30, 120));
		g.fillRect(0, 0, 200, 200);

		// Draw currently selected image centered in the purple square
		g.drawImage(images[model.selectedThing], imageW, imageH, null);
	}

	void removeButton() {
		this.remove(this.b1);
		this.repaint();
	}
}
