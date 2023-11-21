import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

// USE ARROW KEYS OR WASD TO MOVE THE "CAMERA" AROUND
class Controller implements ActionListener, MouseListener, KeyListener {
	View view;
	Model model;
	boolean keyLeft;
	boolean keyRight;
	boolean keyUp;
	boolean keyDown;

	Controller(Model m) {
		model = m;
	}

	void setView(View v) {
		view = v;
	}
	/*
	 * private void updateModelFromJson(Json jsonData) {
	 * model.things.clear(); // Clear existing things
	 * 
	 * Json thingsList = jsonData.get("things");
	 * for (int i = 0; i < thingsList.size(); i++) {
	 * Json thingObj = thingsList.get(i);
	 * int x = (int) thingObj.getLong("x");
	 * int y = (int) thingObj.getLong("y");
	 * int kind = (int) thingObj.getLong("kind");
	 * 
	 * model.things.add(new Thing(x, y, kind));
	 * }
	 * }
	 */

	// Writes the map.json file to store the correct coordinates for the things
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("SAVE")) {
			try {
				FileWriter writer = new FileWriter("map.json");
				writer.write(this.model.marshal().toString());
				writer.close();
			} catch (IOException exception) {
				exception.printStackTrace();
				System.exit(1);
			}
		} else if (e.getActionCommand().equals("LOAD")) {
			Json loadedData = Json.load("map.json");
			model.unmarshal(loadedData);
		}
	}

	public void mousePressed(MouseEvent e) {
		if (e.getX() <= 200 && e.getY() <= 200) {
			model.selectedThing++;
			if (model.selectedThing >= Game.THINGS.length) {
				model.selectedThing = 0;
			}
		}

		else if (e.getButton() == 1 && (e.getX() > 200 || e.getY() > 200)) {
			// Pass the adjusted coordinates to addThing
			model.addThing(view, e.getX(), e.getY());
			model.setDestination(e.getX(), e.getY());
		}

		else if (e.getButton() == 3 && (e.getX() > 200 || e.getY() > 200)) {
			model.removeThing(e.getX(), e.getY());
		}
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	// Movement of the "camera" with WASD or arrow keys (Scroll)
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				view.scroll_x += 20;
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				view.scroll_x -= 20;
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				view.scroll_y -= 20;
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				view.scroll_y += 20;
				break;
		}
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				keyRight = false;
				break;
			case KeyEvent.VK_LEFT:
				keyLeft = false;
				break;
			case KeyEvent.VK_UP:
				keyUp = false;
				break;
			case KeyEvent.VK_DOWN:
				keyDown = false;
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
		}
		char c = Character.toLowerCase(e.getKeyChar());
		if (c == 'q')
			System.exit(0);
		if (c == 'r')
			model.reset();
	}

	// Look for Save key "S" or "s"
	public void keyTyped(KeyEvent e) {
	}

	void update() {
		if (keyRight)
			model.dest_x += Model.speed;
		if (keyLeft)
			model.dest_x -= Model.speed;
		if (keyDown)
			model.dest_y += Model.speed;
		if (keyUp)
			model.dest_y -= Model.speed;
	}

}
